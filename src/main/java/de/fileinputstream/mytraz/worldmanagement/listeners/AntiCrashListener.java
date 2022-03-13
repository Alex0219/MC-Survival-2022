package de.fileinputstream.mytraz.worldmanagement.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander on 12.08.2020
 * © 2020 Alexander Fiedler
 **/
public class AntiCrashListener implements Listener {

    private static final String KICK_MESSAGE = ChatColor.DARK_RED + "Nope :)";

    private Map<String, Integer> hitCount, switchCount;
    private Map<String, Long> lastHit, lastSwitch, hitAverage, switchAverage;

    public AntiCrashListener() {
        this.hitCount = new HashMap<>();
        this.switchCount = new HashMap<>();
        this.lastHit = new HashMap<>();
        this.lastSwitch = new HashMap<>();
        this.hitAverage = new HashMap<>();
        this.switchAverage = new HashMap<>();
    }

    public int getHitCount(String name) {
        return this.hitCount.get(name);
    }

    public int getSwitchCount(String name) {
        return this.switchCount.get(name);
    }

    public long getLastHit(String name) {
        return this.lastHit.get(name);
    }

    public long getLastSwitch(String name) {
        return this.lastSwitch.get(name);
    }

    public long getHitAverage(String name) {
        return this.hitAverage.get(name);
    }

    public long getSwitchAverage(String name) {
        return this.switchAverage.get(name);
    }

    private void updateHitCount(String name) {
        this.hitCount.put(name, getHitCount(name) + 1);
    }

    private void updateSwitchCount(String name) {
        this.switchCount.put(name, getSwitchCount(name) + 1);
    }

    private void updateLastHit(String name) {
        this.lastHit.put(name, System.nanoTime());
    }

    private void updateLastSwitch(String name) {
        this.lastSwitch.put(name, System.nanoTime());
    }

    private void updateHitAverage(String name) {
        this.hitAverage.put(name, this.getHitAverage(name) + (System.nanoTime() - this.getLastHit(name)));
    }

    private void updateSwitchAverage(String name) {
        this.switchAverage.put(name, this.getSwitchAverage(name) + (System.nanoTime() - this.getLastSwitch(name)));
    }

    private void punishSuspicious(Player target) {
        target.kickPlayer(KICK_MESSAGE);
    }

    private void handleConnect(String name) {
        this.hitCount.put(name, 0);
        this.switchCount.put(name, 0);
        this.lastHit.put(name, 0L);
        this.lastSwitch.put(name, 0L);
        this.hitAverage.put(name, 0L);
        this.switchAverage.put(name, 0L);
    }

    private void handleDisconnect(String name) {
        this.hitCount.remove(name);
        this.switchCount.remove(name);
        this.lastHit.remove(name);
        this.lastSwitch.remove(name);
        this.hitAverage.remove(name);
        this.switchAverage.remove(name);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.handleConnect(event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.handleDisconnect(event.getPlayer().getName());
    }

    @EventHandler
    public void onCheckItemHeld(PlayerItemHeldEvent event) {
        String name = event.getPlayer().getName();

        if (this.getSwitchCount(name) > 200) {
            if (this.getSwitchAverage(name) / 200L < 50000L) {
                this.punishSuspicious(event.getPlayer());
            }
        } else {
            if (this.getLastSwitch(name) != 0L) {
                this.updateSwitchAverage(name);
            }

            this.updateLastSwitch(name);
            this.updateSwitchCount(name);
        }
    }

    @EventHandler
    public void onCheckAnimation(PlayerAnimationEvent event) {
        String name = event.getPlayer().getName();

        if (this.getHitCount(name) > 200) {
            if (this.getHitAverage(name) / 200L < 50000L) {
                this.punishSuspicious(event.getPlayer());
            }
        } else {
            if (this.getLastHit(name) != 0L) {
                this.updateHitAverage(name);
            }

            this.updateLastHit(name);
            this.updateHitCount(name);
        }
    }
}
