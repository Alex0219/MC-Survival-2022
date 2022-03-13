package de.fileinputstream.mytraz.worldmanagement;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.fileinputstream.mytraz.worldmanagement.anticrash.AntiCrash;
import de.fileinputstream.mytraz.worldmanagement.backup.BackupManager;
import de.fileinputstream.mytraz.worldmanagement.chatlog.ChatLogManager;
import de.fileinputstream.mytraz.worldmanagement.commands.*;
import de.fileinputstream.mytraz.worldmanagement.crafting.RecipeRegistry;
import de.fileinputstream.mytraz.worldmanagement.db.RedisConnector;
import de.fileinputstream.mytraz.worldmanagement.listeners.*;
import de.fileinputstream.mytraz.worldmanagement.npc.NPCManager;
import de.fileinputstream.mytraz.worldmanagement.npc.NPCPlayerInjector;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.tracker.OntimeTracker;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.votegamble.CaseManager;
import de.fileinputstream.mytraz.worldmanagement.votegamble.CaseTimer;
import de.fileinputstream.mytraz.worldmanagement.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.*;


/**
 * User: Alexander<br/>
 * Date: 10.02.2018<br/>
 * Time: 19:33<br/>
 * MIT License
 * <p>
 * Copyright (c) 2017 Alexander Fiedler
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use and modify without distributing the software to anybody else,
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * <p>
 * MIT Lizenz
 * Copyright (c) 2017 Alexander Fiedler
 * Hiermit wird unentgeltlich jeder Person, die eine Kopie der Software und der zugehörigen Dokumentationen (die "Software") erhält, die Erlaubnis erteilt, sie uneingeschränkt zu nutzen, inklusive und ohne Ausnahme mit dem Recht, sie zu verwenden, zu verändern und Personen, denen diese Software überlassen wird, diese Rechte zu verschaffen, außer sie zu verteilen unter den folgenden Bedingungen:
 * <p>
 * Der obige Urheberrechtsvermerk und dieser Erlaubnisvermerk sind in allen Kopien oder Teilkopien der Software beizulegen.
 * <p>
 * DIE SOFTWARE WIRD OHNE JEDE AUSDRÜCKLICHE ODER IMPLIZIERTE GARANTIE BEREITGESTELLT, EINSCHLIEßLICH DER GARANTIE ZUR BENUTZUNG FÜR DEN VORGESEHENEN ODER EINEM BESTIMMTEN ZWECK SOWIE JEGLICHER RECHTSVERLETZUNG, JEDOCH NICHT DARAUF BESCHRÄNKT. IN KEINEM FALL SIND DIE AUTOREN ODER COPYRIGHTINHABER FÜR JEGLICHEN SCHADEN ODER SONSTIGE ANSPRÜCHE HAFTBAR ZU MACHEN, OB INFOLGE DER ERFÜLLUNG EINES VERTRAGES, EINES DELIKTES ODER ANDERS IM ZUSAMMENHANG MIT DER SOFTWARE ODER SONSTIGER VERWENDUNG DER SOFTWARE ENTSTANDEN.
 */
public class Bootstrap extends JavaPlugin {

    static Bootstrap instance;
    RedisConnector redisConnector;
    public Jedis jedis;
    WorldManager worldManager;
    String spawnWorld;
    ChatLogManager chatLogManager;
    BackupManager backupManager;
    String prefix = "§bMC-Survival.de §7»";
    Scoreboard mainScoreboard;
    Location spawnLocation;
    ArrayList<String> permittedBuilders = new ArrayList<>();
    HashMap<String, String> tpaInvites = new HashMap<>();
    public ProtocolManager protocolManager;
    public ArrayList<String> currentDownloadingWorlds = new ArrayList<>();
    RankManager rankManager;
    NPCManager npcManager;
    ArrayList<Player> afkPlayers = new ArrayList<>();
    NPCPlayerInjector npcPlayerInjector;
    RecipeRegistry recipeRegistry;
    private Map<UUID, List<UUID>> tpaRequests = new HashMap<>(), tpaHereRequests = new HashMap<>(),
            tradeRequests = new HashMap<>();


    public static Bootstrap getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;

        worldManager = new WorldManager();
        backupManager = new BackupManager();
        Bukkit.getPluginManager().registerEvents(new ListenerConnect(), this);
        Bukkit.getPluginManager().registerEvents(new ListenerChat(), this);
        Bukkit.getPluginManager().registerEvents(new VoteEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new VoteCaseListener(), this);
        Bukkit.getPluginManager().registerEvents(new AntiCrashListener(), this);
        Bukkit.getPluginManager().registerEvents(new NPCPlayerInjector(), this);
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("DB", "1");
        getConfig().addDefault("SpawnWorld", "world");
        saveConfig();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        redisConnector = new RedisConnector();
        redisConnector.connectToRedis("127.0.0.1", 6379);
        jedis = redisConnector.getJedis();
        System.out.println("Connected to redis!");
        if (!getJedis().exists("survivalWorldIDCount")) {
            getJedis().set("survivalWorldIDCount", "1");
            System.out.println("Backend -> Warning! Key 'survivalWorldIDCount' was missing! Created it.");
        }

        mainScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        new OntimeTracker().startCounter();
        chatLogManager = new ChatLogManager();
        spawnWorld = getConfig().getString("SpawnWorld");

        NameTags.createScoreboardTeam();

        rankManager = new RankManager();
        npcManager = new NPCManager();
        npcPlayerInjector = new NPCPlayerInjector();

        loadCommands();

        recipeRegistry = new RecipeRegistry();
        recipeRegistry.registerRecipes();

        this.protocolManager = ProtocolLibrary.getProtocolManager();
        new AntiCrash(Bootstrap.getInstance());

        runEntityDestroyScheduler();

        CaseManager.loadCaseOpeningData();
        CaseManager.loadCaseOpening();
        CaseTimer caseTimer = new CaseTimer();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bootstrap.getInstance(), new Runnable() {
            @Override
            public void run() {
                caseTimer.run();
            }
        }, 10L, 10L);

        File f = new File("plugins//WorldManagement", "spawn.yml");

        if (!f.exists()) {
            return;
        }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        double x = cfg.getDouble("X");
        double y = cfg.getDouble("Y");
        double z = cfg.getDouble("Z");
        float yaw = (float) cfg.getDouble("Yaw");
        float pitch = (float) cfg.getDouble("Pitch");
        String world = cfg.getString("World");
        spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                getBackupManager().runCronJob();
            }
        }, 20 * 60 * 60 * 12, 20 * 60 * 60 * 12);

    }

    @Override
    public void onDisable() {
        CaseManager.saveCaseOpeningData();
        instance = null;
    }

    public void loadCommands() {
        getCommand("tpworld").setExecutor(new CommandTPWorld());
        getCommand("createworld").setExecutor(new CommandCreateWorld());
        getCommand("addresident").setExecutor(new CommandAddResident());
        getCommand("removeresident").setExecutor(new CommandRemoveResident());
        getCommand("acceptinvite").setExecutor(new CommandAcceptinvite());
        getCommand("newworldspawn").setExecutor(new CommandNewWorldSpawn());
        getCommand("worlds").setExecutor(new CommandWorlds());
        getCommand("setpvp").setExecutor(new CommandSetPvP());
        getCommand("tutorial").setExecutor(new CommandTutorial());
        getCommand("gm").setExecutor(new CommandGM());
        getCommand("v").setExecutor(new CommandVanish());
        getCommand("vanish").setExecutor(new CommandVanish());
        getCommand("ontime").setExecutor(new CommandOnTime());
        getCommand("warp").setExecutor(new CommandWarp());
        getCommand("setwarp").setExecutor(new CommandSetWarp());
        getCommand("delwarp").setExecutor(new CommandDelWarp());
        getCommand("chatlog").setExecutor(new CommandChatLog());
        getCommand("listbackups").setExecutor(new CommandBackupList());
        getCommand("restorebackup").setExecutor(new CommandRestoreBackup());
        getCommand("dobackup").setExecutor(new CommandDoBackup());
        getCommand("ec").setExecutor(new CommandEnderChest());
        getCommand("vote").setExecutor(new CommandVote());
        getCommand("build").setExecutor(new CommandBuild());
        getCommand("setspawn").setExecutor(new CommandSetSpawn());
        getCommand("residents").setExecutor(new CommandResidents());
        getCommand("spawn").setExecutor(new CommandSpawn());
        getCommand("invsee").setExecutor(new CommandInvsee());
        getCommand("fly").setExecutor(new CommandFly());
        getCommand("cc").setExecutor(new CommandCC());
        getCommand("denybackup").setExecutor(new CommandDenyBackup());
        getCommand("createvotechest").setExecutor(new CommandCreateVoteChest());
        getCommand("home").setExecutor(new CommandHome());
        getCommand("sethome").setExecutor(new CommandSethome());
        getCommand("discord").setExecutor(new CommandDiscord());
        getCommand("tp").setExecutor(new CommandTeleport());
        getCommand("tpaaccept").setExecutor(new CommandTPAAccept());
        getCommand("tpa").setExecutor(new CommandTPA());
        getCommand("tpadeny").setExecutor(new CommandTPADeny());
        getCommand("tpahere").setExecutor(new CommandTPAHere());
        getCommand("tpahereaccept").setExecutor(new CommandTPAHereAccept());
        getCommand("tpahereall").setExecutor(new CommandTPAHereAll());
        getCommand("tpaheredeny").setExecutor(new CommandTPAHereDeny());
        getCommand("tpatoggle").setExecutor(new CommandTPAToggle());
        getCommand("lockworld").setExecutor(new CommandLockWorld());
        getCommand("afk").setExecutor(new CommandAFK());
        getCommand("whereami").setExecutor(new CommandWhereami());

    }

    private void runEntityDestroyScheduler() {

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Bukkit.getWorld("world").getEntities()) {
                    if (!(entity instanceof Player) && !(entity instanceof Item)) {
                        entity.remove();
                    }
                }
            }
        }.runTaskTimer(this, 200L, 200L);
    }


    public Jedis getJedis() {
        try {
            return jedis;
        } catch (Exception exception) {
            Bootstrap.getInstance().getRedisConnector().connectToRedis("127.0.0.1", 6379);
            Bootstrap.getInstance().jedis = Bootstrap.getInstance().getRedisConnector().getJedis();
        }
        return jedis;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public String getSpawnWorld() {
        return spawnWorld;
    }

    public String getPrefix() {
        return prefix;
    }

    public RedisConnector getRedisConnector() {
        return redisConnector;
    }

    public Scoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    public ChatLogManager getChatLogManager() {
        return chatLogManager;
    }

    public BackupManager getBackupManager() {
        return backupManager;
    }

    public ArrayList<String> getPermittedBuilders() {
        return permittedBuilders;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public HashMap<String, String> getTpaInvites() {
        return tpaInvites;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public Map<UUID, List<UUID>> getTpaRequests() {
        return tpaRequests;
    }

    public Map<UUID, List<UUID>> getTpaHereRequests() {
        return tpaHereRequests;
    }

    public ArrayList<Player> getAfkPlayers() {
        return afkPlayers;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public NPCPlayerInjector getNpcPlayerInjector() {
        return npcPlayerInjector;
    }

    public ArrayList<String> getCurrentDownloadingWorlds() {
        return currentDownloadingWorlds;
    }
}