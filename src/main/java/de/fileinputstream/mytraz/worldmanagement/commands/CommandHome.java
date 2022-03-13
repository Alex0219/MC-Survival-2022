package de.fileinputstream.mytraz.worldmanagement.commands;


import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.world.AsyncWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 08.06.2020 21:16
 * © 2020 Alexander Fiedler
 */
public class CommandHome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());

            if (args.length == 0) {
                if (Bootstrap.getInstance().getJedis().hexists("uuid:" + dbUser.getUuid(), "home.1.x")) {
                    AsyncWorld worldLoader = AsyncWorld.create(new WorldCreator(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world")));
                    String world = Bootstrap.getInstance().getWorldManager().getWorld(dbUser.getUuid());
                    if (Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world") != null && Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world").equalsIgnoreCase(world)) {

                        double x = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.x"));
                        double y = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.y"));
                        double z = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.z"));
                        double pitch = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.pitch"));
                        double yaw = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.yaw"));
                        String parsedWorld = Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world");

                        Location teleportLocation = new Location(Bukkit.getWorld(parsedWorld), x, y, z, (float) yaw, (float) pitch);
                        player.teleport(teleportLocation);
                    } else if (Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world"))) {

                        double x = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.x"));
                        double y = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.y"));
                        double z = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.z"));
                        double pitch = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.pitch"));
                        double yaw = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.yaw"));
                        String parsedWorld = Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world");

                        Location teleportLocation = new Location(Bukkit.getWorld(parsedWorld), x, y, z, (float) yaw, (float) pitch);
                        player.teleport(teleportLocation);

                    } else {
                        player.sendMessage("§bMC-Survival.de §7» §cUngültiger Homepunkt.");
                        return true;
                    }

                } else {
                    player.sendMessage("§bMC-Survival.de §7» §7Du hast den Home §c#1 §7noch nicht gesetzt!");
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("2")) {
                    if (Bootstrap.getInstance().getJedis().hexists("uuid:" + dbUser.getUuid(), "home.2.x")) {
                        AsyncWorld worldLoader = AsyncWorld.create(new WorldCreator(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.world")));
                        String world = Bootstrap.getInstance().getWorldManager().getWorld(dbUser.getUuid());
                        if (Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world") != null && Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.1.world").equalsIgnoreCase(world)) {

                            double x = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.x"));
                            double y = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.y"));
                            double z = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.z"));
                            double pitch = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.pitch"));
                            double yaw = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.yaw"));
                            String parsedWorld = Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.world");

                            Location teleportLocation = new Location(Bukkit.getWorld(parsedWorld), x, y, z, (float) yaw, (float) pitch);
                            player.teleport(teleportLocation);
                        } else if (Bootstrap.getInstance().getWorldManager().isResidentInWorld(dbUser.getUuid(), Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.world"))) {

                            double x = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.x"));
                            double y = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.y"));
                            double z = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.z"));
                            double pitch = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.pitch"));
                            double yaw = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.yaw"));
                            String parsedWorld = Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "home.2.world");

                            Location teleportLocation = new Location(Bukkit.getWorld(parsedWorld), x, y, z, (float) yaw, (float) pitch);
                            player.teleport(teleportLocation);

                        } else {
                            player.sendMessage("§bMC-Survival.de §7» §cUngültiger Homepunkt.");
                            return true;
                        }

                    } else {
                        if (dbUser.getRank() == RankEnum.STAMMSPIELER || dbUser.getRank() == RankEnum.ADMIN || dbUser.getRank() == RankEnum.MOD || dbUser.getRank() == RankEnum.VIP) {
                            player.sendMessage("§bMC-Survival.de §7» §7Du hast den Home §c#1 §7noch nicht gesetzt!");
                        } else {
                            player.sendMessage("§bMC-Survival.de §7» §cEin zweiter Homepunkt kann gar nicht existieren, da die Berechtigung dazu fehlt!");
                            return true;
                        }
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
