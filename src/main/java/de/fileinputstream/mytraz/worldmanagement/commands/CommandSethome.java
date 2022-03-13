package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 08.06.2020 21:16
 * © 2020 Alexander Fiedler
 */
public class CommandSethome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());

            if (!Bootstrap.getInstance().getWorldManager().hasWorld(dbUser.getUuid())) {
                return true;
            }
            if (args.length == 0) {
                String world = Bootstrap.getInstance().getWorldManager().getWorld(dbUser.getUuid());
                ;

                if (!Bootstrap.getInstance().getWorldManager().worldExists(player.getWorld().getName())) {
                    player.sendMessage("§bMC-Survival.de §7» Du darfst dir in dieser Welt kein Home setzen!");
                    return true;
                }
                if (!player.getWorld().getName().equalsIgnoreCase(world) && !Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), world)) {
                    player.sendMessage("§bMC-Survival.de §7» Du darfst dir in dieser Welt kein Home setzen!");
                    return true;

                } else if (Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), world)) {

                    Location location = player.getLocation();

                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.x", String.valueOf(location.getX()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.y", String.valueOf(location.getY()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.z", String.valueOf(location.getZ()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.pitch", String.valueOf(location.getPitch()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.yaw", String.valueOf(location.getYaw()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.world", String.valueOf(location.getWorld().getName()));

                    player.sendMessage("§bMC-Survival.de §7» Du hast Home §c#1 §7gesetzt!");
                    return true;
                } else if (player.getWorld().getName().equalsIgnoreCase(world)) {
                    Location location = player.getLocation();

                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.x", String.valueOf(location.getX()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.y", String.valueOf(location.getY()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.z", String.valueOf(location.getZ()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.pitch", String.valueOf(location.getPitch()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.yaw", String.valueOf(location.getYaw()));
                    Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.1.world", String.valueOf(location.getWorld().getName()));

                    player.sendMessage("§bMC-Survival.de §7» Du hast Home §c#1 §7gesetzt!");
                    return true;
                }

            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("2")) {
                    if (dbUser.getRank() == RankEnum.STAMMSPIELER || dbUser.getRank() == RankEnum.ADMIN || dbUser.getRank() == RankEnum.MOD || dbUser.getRank() == RankEnum.VIP) {
                        String world = Bootstrap.getInstance().getWorldManager().getWorld(dbUser.getUuid());
                        ;

                        if (!Bootstrap.getInstance().getWorldManager().worldExists(player.getWorld().getName())) {
                            player.sendMessage("§bMC-Survival.de §7» Du darfst dir in dieser Welt kein Home setzen!");
                            return true;
                        }
                        if (!player.getWorld().getName().equalsIgnoreCase(world) && !Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), world)) {
                            player.sendMessage("§bMC-Survival.de §7» Du darfst dir in dieser Welt kein Home setzen!");
                            return true;

                        } else if (Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), world)) {

                            Location location = player.getLocation();

                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.x", String.valueOf(location.getX()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.y", String.valueOf(location.getY()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.z", String.valueOf(location.getZ()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.pitch", String.valueOf(location.getPitch()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.yaw", String.valueOf(location.getYaw()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.world", String.valueOf(location.getWorld().getName()));

                            player.sendMessage("§bMC-Survival.de §7» Du hast Home §c#2 §7gesetzt!");
                            return true;
                        } else if (player.getWorld().getName().equalsIgnoreCase(world)) {
                            Location location = player.getLocation();

                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.x", String.valueOf(location.getX()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.y", String.valueOf(location.getY()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.z", String.valueOf(location.getZ()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.pitch", String.valueOf(location.getPitch()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.yaw", String.valueOf(location.getYaw()));
                            Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "home.2.world", String.valueOf(location.getWorld().getName()));

                            player.sendMessage("§bMC-Survival.de §7» Du hast Home §c#2 §7gesetzt!");
                            return true;
                        }
                    } else {
                        player.sendMessage("§bMC-Survival.de §7» §cDu darfst dir keinen zweiten Homepunkt setzen!");
                        return true;
                    }
                } else {
                    player.sendMessage("§bMC-Survival.de §7» §cUngültiges Argument!");
                    return true;
                }
            }
        }
        return false;
    }
}
