package de.fileinputstream.mytraz.worldmanagement.anticrash;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Bukkit;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alexander on 12.08.2020
 * © 2020 Alexander Fiedler
 **/
public class AntiCrash {

    Bootstrap main;
    ConcurrentHashMap<String, Double> lastY;
    ConcurrentHashMap<String, Double> lastZ;
    String lastSlotName;
    long lastSlotTime;

    public AntiCrash(final Bootstrap main) {
        this.lastY = new ConcurrentHashMap<String, Double>();
        this.lastZ = new ConcurrentHashMap<String, Double>();
        this.lastSlotName = "";
        this.lastSlotTime = System.currentTimeMillis();
        this.main = main;
        main.protocolManager.addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.POSITION}) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.POSITION) {
                    AntiCrash.this.listenPos(event);
                }
            }
        });
        main.protocolManager.addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.POSITION_LOOK}) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
                    AntiCrash.this.listenPos(event);
                }
            }
        });
        main.protocolManager.addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.CUSTOM_PAYLOAD}) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.CUSTOM_PAYLOAD) {

                }
            }
        });
    }

    private void listenPos(final PacketEvent ev) {
        final double x = ev.getPacket().getDoubles().read(0);
        final double y = ev.getPacket().getDoubles().read(1);
        final double z = ev.getPacket().getDoubles().read(2);
        if (!this.lastY.containsKey(ev.getPlayer().getName())) {
            this.lastY.put(ev.getPlayer().getName(), y);
        }
        if (!this.lastZ.containsKey(ev.getPlayer().getName())) {
            this.lastZ.put(ev.getPlayer().getName(), z);
        }
        if (y - this.lastY.get(ev.getPlayer().getName()) == 9.0) {
            ev.setCancelled(true);
            this.lastY.remove(ev.getPlayer().getName());
        } else if (z - this.lastZ.get(ev.getPlayer().getName()) == 9.0) {
            ev.setCancelled(true);
            this.lastZ.remove(ev.getPlayer().getName());
        }
        if (x == Double.MIN_VALUE || x == Double.MAX_VALUE || z == Double.MIN_VALUE || z == Double.MAX_VALUE) {
            ev.setCancelled(true);
            ev.getPlayer().kickPlayer("§cNope :)");
            System.out.println("Alert: " + ev.getPlayer().getName() + " tried to crash the server!");
            Bukkit.getOnlinePlayers().forEach(players -> {
                if (RankManager.getRank(players.getUniqueId().toString()).equalsIgnoreCase("admin") || RankManager.getRank(players.getUniqueId().toString()).equalsIgnoreCase("mod")) {
                    players.sendMessage("§c" + ev.getPlayer().getDisplayName() + " §7hat versucht, den Server zu crashen!");
                }
            });
        }
    }

}
