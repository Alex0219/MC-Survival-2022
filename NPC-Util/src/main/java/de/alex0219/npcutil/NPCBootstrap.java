package de.alex0219.npcutil;

import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Alexander on 13.08.2020
 * © 2020 Alexander Fiedler
 **/
public class NPCBootstrap extends JavaPlugin implements Listener {

    /*
        This is an example class, it spawns an NPC with some text at the player's location when un-sneaking.
        It will also send an interactor a message if it's the first NPC spawned.
     */

    // To keep track of NPC IDs.
    private final SortedSet<String> ids = new TreeSet<>();
    private NPCLib npclib;

    @Override
    public void onEnable() {
        this.npclib = new NPCLib(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        NPC npc = npclib.createNPC(Arrays.asList(ChatColor.WHITE + "Hi there (#2)", ChatColor.YELLOW + "Click on me!"));
        ids.add(npc.getId());
        npclib.setAutoHideDistance(200.0);
        double x = 138.607;
        double y = 69;
        double z = -234.673;
        float pitch = event.getPlayer().getLocation().getPitch();
        float yaw = event.getPlayer().getLocation().getYaw();
        Location location = new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
        npc.setLocation(location);
        npc.create();
        npc.show(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        String firstId = ids.first();
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        String firstId = ids.first();
        if (event.getNPC().getId().equals(firstId)) {
            event.getWhoClicked().sendMessage(ChatColor.GREEN + "I'm the first NPC spawned since boot!");
        }
    }
}
