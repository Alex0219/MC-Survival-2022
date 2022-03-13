package de.fileinputstream.mytraz.worldmanagement.uuid;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;


public class NameTags {

    public static void createScoreboardTeam() {
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("001SuperAdmin").setColor(ChatColor.DARK_RED);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("002Admin").setColor(ChatColor.DARK_RED);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("003Mod").setColor(ChatColor.RED);
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("004VIP").setColor(ChatColor.AQUA);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("005Stammspieler").setColor(ChatColor.GOLD);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("006Spieler").setColor(org.bukkit.ChatColor.GREEN);

        Bootstrap.getInstance().getMainScoreboard().getTeam("001SuperAdmin").setPrefix("§4Admin §7| §4");
        Bootstrap.getInstance().getMainScoreboard().getTeam("002Admin").setPrefix("§4Admin §7| §4");
        Bootstrap.getInstance().getMainScoreboard().getTeam("003Mod").setPrefix("§cMod §7| §c");
        Bootstrap.getInstance().getMainScoreboard().getTeam("004VIP").setPrefix("§bVIP §7| §b");
        Bootstrap.getInstance().getMainScoreboard().getTeam("005Stammspieler").setPrefix("§6Stammspieler §7| §6");
        Bootstrap.getInstance().getMainScoreboard().getTeam("006Spieler").setPrefix("§a");

    }

    public static void setTags(final Player player) {
        String userGroup;


        RankEnum rank = Bootstrap.getInstance().getRankManager().getDBUser(player.getName()).getRank();
        if (rank == RankEnum.SUPERADMIN) {
            userGroup = "001SuperAdmin";
        } else if (rank == RankEnum.ADMIN) {
            if (Bootstrap.getInstance().getAfkPlayers().contains(player)) {
                player.setPlayerListName("§4Admin §7| §4" + player.getName() + "§7[§cAFK§7]");
            } else {
                player.setPlayerListName("§4Admin §7| §4" + player.getName());
            }
            player.setDisplayName("§4Admin §7| §4" + player.getName());
            userGroup = "002Admin";
        } else if (rank == RankEnum.MOD) {
            userGroup = "003Mod";
            if (Bootstrap.getInstance().getAfkPlayers().contains(player)) {
                player.setPlayerListName("§cMod §7| §c" + player.getName() + "§7[§cAFK§7]");
            } else {
                player.setPlayerListName("§cMod §7| §c" + player.getName());
            }
            player.setDisplayName("§cMod §7| §c" + player.getName());
        } else if (rank == RankEnum.VIP) {
            userGroup = "004VIP";
            if (Bootstrap.getInstance().getAfkPlayers().contains(player)) {
                player.setPlayerListName("§bVIP §7| §b" + player.getName() + "§7[§cAFK§7]");
            } else {
                player.setPlayerListName("§bVIP §7| §b" + player.getName());
            }
            player.setDisplayName("§bVIP §7| §b" + player.getName());
        } else if (rank == RankEnum.STAMMSPIELER) {
            userGroup = "005Stammspieler";
            if (Bootstrap.getInstance().getAfkPlayers().contains(player)) {
                player.setPlayerListName("§6Stammspieler §7| §6" + player.getName() + "§7[§cAFK§7]");
            } else {
                player.setPlayerListName("§6Stammspieler §7| §6" + player.getName());
            }
            player.setDisplayName("§6Stammspieler §7| §6" + player.getName());
        } else {
            userGroup = "006Spieler";
            if (Bootstrap.getInstance().getAfkPlayers().contains(player)) {
                player.setPlayerListName("§a" + player.getName() + "§7[§cAFK§7]");
            } else {
                player.setPlayerListName("§a" + player.getName());
            }
            player.setDisplayName("§a" + player.getName());
        }
        Bootstrap.getInstance().getMainScoreboard().getTeam(userGroup).addPlayer(player);

        for (final Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(Bootstrap.getInstance().getMainScoreboard());
        }
    }
}

