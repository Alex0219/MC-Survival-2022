package de.fileinputstream.mytraz.worldmanagement.listeners;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.chatlog.entry.ChatEntry;
import de.fileinputstream.mytraz.worldmanagement.commands.CommandVanish;
import de.fileinputstream.mytraz.worldmanagement.item.ItemBuilder;
import de.fileinputstream.mytraz.worldmanagement.npc.CustomNPCPlayer;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import de.fileinputstream.mytraz.worldmanagement.xp.ExperienceManager;

import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;

import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;


import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * User: Alexander<br/>
 * Date: 22.02.2018<br/>
 * Time: 20:49<br/>
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
public class ListenerConnect implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Bukkit.broadcastMessage("§c" + event.getPlayer().getDisplayName() + " §7ist §cgestorben!");
        String uuid = UUIDFetcher.getUUID(event.getPlayer().getName()).toString();
        if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
            new WorldCreator(Bootstrap.getInstance().getWorldManager().getWorld(uuid)).createWorld();
            event.setRespawnLocation(Bukkit.getWorld(Bootstrap.getInstance().getWorldManager().getWorld(uuid)).getSpawnLocation());
        } else {
            event.setRespawnLocation(Bukkit.getWorld(Bootstrap.getInstance().getConfig().getString("SpawnWorld")).getSpawnLocation());
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = event.getEntity();
            if(Bootstrap.getInstance().getRankManager().getDBUser(player.getName()).getRank() == RankEnum.VIP) {
                event.setKeepInventory(true);
                event.getDrops().clear();
            }
            event.setDeathMessage(null);
        }

    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
        if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("1 EXP auslagern")) {
                payoutEXP(1,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("10 EXP auslagern")) {
                payoutEXP(10,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("100 EXP auslagern")) {
                payoutEXP(100,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("500 EXP auslagern")) {
                payoutEXP(500,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Alles auslagern")) {
                payoutEXP(dbUser.getStoredEXP(),player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("1 EXP einlagern")) {
                payin(1,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("10 EXP einlagern")) {
                payin(10,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("100 EXP einlagern")) {
                payin(100,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("500 EXP einlagern")) {
                payin(500,player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Alles einlagern")) {
                payin(getPlayerExp(player),player);
                event.getInventory().clear();
                resetEXPInventory(player,event.getClickedInventory());
            }



        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(Bootstrap.getInstance().getRankManager().getDBUser(event.getPlayer().getName()) == null) {
            DBUser dbUser = new DBUser(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
            Bootstrap.getInstance().getRankManager().getDbusers().add(dbUser);
            long millisNow = System.currentTimeMillis();

            if (!dbUser.userExists()) {
                dbUser.createUser();
                String millis = String.valueOf(System.currentTimeMillis() - millisNow);
                System.out.println("Backend -> Player Join took " + millis + " milliseconds");

            } else {
                System.out.println("Backend -> User already exists!");
                String millis = String.valueOf(System.currentTimeMillis() - millisNow);
                System.out.println("Backend -> Player Join took " + millis + " milliseconds");
            }
            Player player = event.getPlayer();

            if(!Bootstrap.getInstance().getJedis().hexists("uuid:" + dbUser.getUuid() , "tpaallow")) {
                Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid() , "tpaallow","true");
            }
            if(!Bootstrap.getInstance().getJedis().hexists("uuid:" + dbUser.getUuid() , "storedexp")) {
                Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid() , "storedexp","0");
            }




            NameTags.setTags(player);

            CommandVanish.getVanishedPlayers().forEach(vanishedPlayer -> {
                if(!RankManager.getRank(player.getUniqueId().toString()).equalsIgnoreCase("admin")) {
                    player.hidePlayer(vanishedPlayer);
                }
            });

            if(!Bootstrap.getInstance().getChatLogManager().chatLogs.containsKey(dbUser.getUuid())) {
                Bootstrap.getInstance().getChatLogManager().chatLogs.put(dbUser.getUuid(), new ArrayList<ChatEntry>());
            }
            event.setJoinMessage(event.getPlayer().getDisplayName() + " §7ist dem Server beigetreten.");
            //Remove cooldown that was added in minecraft 1.9
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);

            if (player.getWorld().getName().equalsIgnoreCase("world")) {
                player.setHealthScale(20.0);
                player.setFoodLevel(20);
            }
            if(!player.hasPlayedBefore()) {
                player.teleport(Bootstrap.getInstance().getSpawnLocation());
            }



            Bootstrap.getInstance().getNpcManager().spawnNPCs();
            Bootstrap.getInstance().getNpcManager().show(player);



            player.sendMessage("           §7*---*---*         ");
            player.sendMessage(" ");
            player.sendMessage("     §7Willkommen auf §a§lMC-Survival.de");
            player.sendMessage("     §7Gebe §c/tutorial §7ein, um das Hilfebuch aufzurufen.");
            player.sendMessage("     §7Vergiss nicht zu voten! /vote");
            player.sendMessage(" ");
            player.sendMessage("           §7*---*---*           ");
        }



        }



    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§c" + event.getPlayer().getDisplayName() + " §7hat den Server verlassen.");
        Player player = event.getPlayer();
        String uuid = event.getPlayer().getUniqueId().toString();
        if(Bootstrap.getInstance().getRankManager().getDBUser(event.getPlayer().getName()) != null) {
            Bootstrap.getInstance().getRankManager().getDbusers().remove(Bootstrap.getInstance().getRankManager().getDBUser(player.getName()));
        }
        if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
            String world = Bootstrap.getInstance().getWorldManager().getWorld(uuid);
            Bukkit.unloadWorld(world, true);
            System.out.println("Backend -> Unloading world from player: " + player.getName() + "(uuid:" + uuid + ").");
        }
    }

    @EventHandler
    public void onPerfomCommand(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage();
        if ((msg.equalsIgnoreCase("/plugins")) || (msg.equalsIgnoreCase("/pl")) || (msg.startsWith("/bukkit:plugins")) || (msg.startsWith("/bukkit:pl")) || ((msg.startsWith("/bukkit:ver") | msg.startsWith("/bukkit:version"))) || (msg.startsWith("/ver")) || (msg.startsWith("/version")) || (msg.startsWith("/?")) || (msg.equalsIgnoreCase("/help")) || (msg.startsWith("/bukkit:?")) || (msg.equalsIgnoreCase("/bukkit:?")) || (msg.equalsIgnoreCase("/bukkit:help")) || (msg.equalsIgnoreCase("/icanhasbukkit")) || (msg.equalsIgnoreCase("/me")) || (msg.startsWith("/minecraft:me")) || (msg.startsWith("/rg")) || (msg.startsWith("/worldguard")) || (msg.startsWith("//")) || (msg.startsWith("/tell")) || (msg.startsWith("/minecraft:tell"))   ) {
            if (!e.getPlayer().hasPermission("api.bypass")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
            } else {
            }

        }
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if(event.getFrom().getWorld().getName().equalsIgnoreCase("world")) {
            CustomNPCPlayer npc = Bootstrap.getInstance().getNpcManager().getNpcList().get(event.getPlayer());
            if(npc !=null) {
                Location location = new Location(Bukkit.getServer().getWorld("world"), -242.656, 68, -55.455, (float) 0.4, (float) 2.5);
                location.setDirection(event.getPlayer().getLocation().subtract(location).toVector());
                PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().b;
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                connection.a(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
                connection.a(new PacketPlayOutEntityHeadRotation(npc.getEntityPlayer(), (byte) ((yaw%360.)*256/360)));
            }
        }
    }


    @EventHandler(priority= EventPriority.HIGHEST)
    public void onWorldInit(final WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
            //keep the current food level
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.SPAWNER) {
             
        }
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            if(Bootstrap.getInstance().getPermittedBuilders().contains(event.getPlayer().getName())) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            if(Bootstrap.getInstance().getPermittedBuilders().contains(event.getPlayer().getName())) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }




        @EventHandler
    public void onPlayerEnterPortal(PlayerPortalEvent event) {
        if(event.getFrom().getWorld().getName().equalsIgnoreCase("world_nether")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(final PlayerCommandPreprocessEvent e) {
        final String m = e.getMessage().toLowerCase().split(" ")[0];
        if ((m.equals("/me") || m.equals("/minecraft:me")) || m.equalsIgnoreCase("/tell") || m.equalsIgnoreCase("/minecraft:tell")) {
            e.getPlayer().sendMessage("§cYou do not have permission to execute this command!");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            if(Bootstrap.getInstance().getPermittedBuilders().contains(event.getPlayer().getName())) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBabyPiglinPickupItem(final EntityPickupItemEvent event) {
        if(event.getEntityType().equals(EntityType.PIGLIN)) {
            final Ageable entity = (Ageable)event.getEntity();
            if(!entity.isAdult()) {
                event.setCancelled(true);
            }
        }
    }
    public void payoutEXP(final int exp, final Player player) {
        DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
        if(dbUser.getStoredEXP() >= exp) {
            if(dbUser.getStoredEXP() == 0) {
                player.sendMessage("§bMC-Survival.de §7» §cDu hast nicht genug EXP!");
                return;
            }
            changePlayerExp(player, +exp);
            player.sendMessage("§bMC-Survival.de §7» §7Du hast §c" + exp + " §7EXP ausgezahlt.");

            int currentEXP = dbUser.getStoredEXP();
            int newEXP = currentEXP-exp;
            dbUser.setEXP(newEXP);
        } else {
            player.sendMessage("§bMC-Survival.de §7» §cDu hast nicht genug EXP!");
        }
    }

    public void payin(final int exp, final Player player) {
        DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
        if(getPlayerExp(player) >= exp) {
            if(exp == 0 || getPlayerExp(player) == 0) {
                player.sendMessage("§bMC-Survival.de §7» §cDu hast nicht genug EXP!");
                return;
            }
            ExperienceManager experienceManager = new ExperienceManager(player);
            if(getPlayerExp(player) < 1) {
                experienceManager.setExp(0);
                player.sendMessage("§bMC-Survival.de §7» §cDu hast nicht genug EXP!");
                return;
            }
            changePlayerExp(player,-exp);
            player.sendMessage("§bMC-Survival.de §7» §7Du hast §c" + exp + " §7EXP eingezahlt.");

            int currentEXP = dbUser.getStoredEXP();
            int newEXP = currentEXP+exp;
            dbUser.setEXP(newEXP);
        } else {
            player.sendMessage("§bMC-Survival.de §7» §cDu hast nicht genug EXP!");
        }
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent event) {
       // if(event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
         //   Bootstrap.getInstance().getNpcManager().spawnNPCs();
           Bootstrap.getInstance().getNpcManager().show(event.getPlayer());
        }


    public int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }

    // Calculate total experience up to a level
    public int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    // Calculate player's current EXP amount
    public int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }

    // Give or take EXP
    public int changePlayerExp(Player player, int exp){
        // Get player's current exp
        int currentExp = getPlayerExp(player);

        // Reset player's current exp to 0
        player.setExp(0);
        player.setLevel(0);
        ExperienceManager experienceManager = new ExperienceManager(player);
        // Give the player their exp back, with the difference
        int newExp = currentExp + exp;
        player.giveExp(newExp);

        // Return the player's new exp amount
        return newExp;
    }


    public void resetEXPInventory(final Player player,final Inventory inventory) {

        DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bootstrap.getInstance(), () -> {
            org.bukkit.inventory.ItemStack greenPanepayin = new ItemBuilder(org.bukkit.Material.LIME_STAINED_GLASS_PANE,"EXP einlagern",null).build();
            org.bukkit.inventory.ItemStack redPanepayin = new ItemBuilder(org.bukkit.Material.RED_STAINED_GLASS_PANE,"EXP einlagern",null).build();
            org.bukkit.inventory.ItemStack greenPanepayout= new ItemBuilder(org.bukkit.Material.LIME_STAINED_GLASS_PANE,"EXP auslagern",null).build();
            org.bukkit.inventory.ItemStack redPanepayout = new ItemBuilder(org.bukkit.Material.RED_STAINED_GLASS_PANE,"EXP auslagern",null).build();
            org.bukkit.inventory.ItemStack onexp = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"1 EXP einlagern",null).build();
            org.bukkit.inventory.ItemStack tenxp = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"10 EXP einlagern",null).build();
            org.bukkit.inventory.ItemStack hundredxp = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"100 EXP einlagern",null).build();
            org.bukkit.inventory.ItemStack fivehundred = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"500 EXP einlagern",null).build();
            org.bukkit.inventory.ItemStack all = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"Alles einlagern",null).build();

            org.bukkit.inventory.ItemStack onexppayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"1 EXP auslagern",null).build();
            org.bukkit.inventory.ItemStack tenxppayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"10 EXP auslagern",null).build();
            org.bukkit.inventory.ItemStack hundredxppayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"100 EXP auslagern",null).build();
            org.bukkit.inventory.ItemStack fivehundredpayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"500 EXP auslagern",null).build();
            org.bukkit.inventory.ItemStack allpayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE,"Alles auslagern",null).build();

            ItemStack xpdisplay = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE,"Eingelagert: " + dbUser.getStoredEXP() + " EXP",null).build();

            inventory.setItem(0,greenPanepayin);
            inventory.setItem(1,greenPanepayin);
            inventory.setItem(2,onexp);
            inventory.setItem(3,tenxp);
            inventory.setItem(4,hundredxp);
            inventory.setItem(5,fivehundred);
            inventory.setItem(6,all);
            inventory.setItem(7,greenPanepayin);
            inventory.setItem(8,greenPanepayin);
            inventory.setItem(9,xpdisplay);
            inventory.setItem(10,xpdisplay);
            inventory.setItem(11,xpdisplay);
            inventory.setItem(12,xpdisplay);
            inventory.setItem(13,xpdisplay);
            inventory.setItem(14,xpdisplay);
            inventory.setItem(15,xpdisplay);
            inventory.setItem(16,xpdisplay);
            inventory.setItem(17,xpdisplay);
            inventory.setItem(18,redPanepayout);
            inventory.setItem(19,redPanepayout);
            inventory.setItem(20,onexppayout);
            inventory.setItem(21,tenxppayout);
            inventory.setItem(22,hundredxppayout);
            inventory.setItem(23,fivehundredpayout);
            inventory.setItem(24,allpayout);
            inventory.setItem(25,redPanepayout);
            inventory.setItem(26,redPanepayout);


            player.openInventory(inventory);
        });
    }

    @EventHandler
    public void onCreatureSpawn(SpawnerSpawnEvent event) {

        if(event.getSpawner().getLocation().getY() >= 20) {

            Block block = event.getSpawner().getLocation().getBlock();

            if(block.getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {

                event.setCancelled(false);
            } else {

                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }


        }


    }



