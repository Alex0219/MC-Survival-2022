package de.fileinputstream.mytraz.worldmanagement.listeners;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

/**
 * Created by Alexander on 14.08.2020 03:21
 * © 2020 Alexander Fiedler
 */
public class ScoreboardEventListener implements Listener {

    private int taskID = 7231633;
    private HashMap<Player, Integer> task = new HashMap<Player, Integer>();
    private HashMap<Player, Integer> coins = new HashMap<Player, Integer>();
    private HashMap<Player, Scoreboard> scoreboard = new HashMap<Player, Scoreboard>();


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        System.out.println("OK");
        if(!task.containsKey(e.getPlayer())){
            scoreboard.put(e.getPlayer(), Bukkit.getServer().getScoreboardManager().getNewScoreboard());
            boardStart(e.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(task.containsKey(e.getPlayer())){
            scoreboard.remove(e.getPlayer());
            Bukkit.getScheduler().cancelTask(task.get(e.getPlayer()));
            task.remove(e.getPlayer());
            e.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            coins.remove(e.getPlayer());
        }
    }

    public void boardStats(Player p){
        Scoreboard board = scoreboard.get(p);
        Objective objectiveSidebar = board.getObjective("sidebar");
        if(objectiveSidebar == null){
            objectiveSidebar = board.registerNewObjective("sidebar", "abc");
            objectiveSidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            objectiveSidebar.setDisplayName("§b§lMC-Survival.de");

            objectiveSidebar.getScore("§5§lDeine Welt:").setScore(7);
            if(Bootstrap.getInstance().getWorldManager().hasWorld(p.getUniqueId().toString())) {
                objectiveSidebar.getScore("§f" + (Bootstrap.getInstance().getWorldManager().getWorld(p.getUniqueId().toString()))).setScore(6);
            } else {
                objectiveSidebar.getScore("§fKeine Welt").setScore(6);
            }

            objectiveSidebar.getScore("§1").setScore(5);

            objectiveSidebar.getScore("§e§lCoins").setScore(4);
            objectiveSidebar.getScore("§f").setScore(3);

            objectiveSidebar.getScore("§2").setScore(2);
        }

        Team money = board.getTeam("money");

        try {
            if (money == null) {
                money = board.registerNewTeam("money");
                money.addEntry("§f");
            }
        } finally {
            money.setSuffix(Integer.toString(1));
        }

        Objective objectiveTablist = board.getObjective("tablist");
        if(objectiveTablist == null) {
            objectiveTablist = board.registerNewObjective("tablist", "def");
        }

        for(Player all : Bukkit.getOnlinePlayers()) {
            String prefix = null;
            String height = null;    //"0001" is highest rank and "0011" is lowest
            RankEnum rank =  Bootstrap.getInstance().getRankManager().getDBUser(all.getName()).getRank();
            if (rank == RankEnum.SUPERADMIN) {
                height = "001SuperAdmin";
            } else if (rank == RankEnum.ADMIN) {
                all.setDisplayName("§4Admin §7| §4" + all.getName());
                all.setPlayerListName("§4Admin §7| §4" + all.getName());
                height = "002Admin";
            } else if (rank == RankEnum.MOD) {
                height = "003Mod";
                all.setDisplayName("§cMod §7| §c" + all.getName());
                all.setPlayerListName("§cMod §7| §c" + all.getName());
            } else if (rank == RankEnum.VIP) {
                height = "004VIP";
                all.setDisplayName("§bVIP §7| §b" + all.getName());
                all.setPlayerListName("§bVIP §7| §b" + all.getName());
            } else if (rank == RankEnum.STAMMSPIELER) {
                height = "005Stammspieler";
                all.setDisplayName("§6Stammspieler §7| §6" + all.getName());
                all.setPlayerListName("§6Stammspieler §7| §6" + all.getName());
            } else {
                height = "006Spieler";
                all.setDisplayName("§a" + all.getName());
                all.setPlayerListName("§a" + all.getName());
            }


            Team team = null;
            for(Team team2 : board.getTeams()) {
                if(team2.getName().contains(all.getUniqueId().toString().replace("-", "").substring(0, 10))) {
                    team = team2;
                }
            }
            if(team == null || !team.getPrefix().equalsIgnoreCase(prefix)) {
                if(team != null) team.unregister();
                team = board.registerNewTeam(height+ "-" + all.getUniqueId().toString().replace("-", "").substring(0, 10));
                team.setPrefix(prefix);
                team.addEntry(all.getName());
                team.addPlayer(all);

                all.setScoreboard(scoreboard.get(all));
                return;
            }
        }
    }

    public void boardStart(final Player p){
        taskID = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Bootstrap.getInstance(), new Runnable() {
            public void run() {
                if(p.isOnline()){
                    boardStats(p);

                }else{
                    p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    scoreboard.remove(p);
                    Bukkit.getScheduler().cancelTask(task.get(p));
                    task.remove(p);
                    coins.remove(p);
                }
            }
        }, 0, 20);
        task.put(p, taskID);
    }


}

