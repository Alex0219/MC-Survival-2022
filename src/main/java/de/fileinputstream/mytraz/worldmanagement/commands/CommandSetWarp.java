package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CommandSetWarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.DARK_RED + "Die Konsole kann diesen Befehl nicht ausführen.");
            return true;
        }
        Player player = (Player) sender;
        if (player.hasPermission("lobby.warp")) {
            if (args.length == 1) {
                File file = new File("plugins//WorldManagement//Warps//" + args[0]);

                if (file.exists()) {
                    player.sendMessage("§bMC-Survival.de §7» §4Dieser Warp existiert bereits!");
                    return true;
                } else {
                    try {
                        file.createNewFile();
                        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                        cfg.set("X", player.getLocation().getX());
                        cfg.set("Y", player.getLocation().getY());
                        cfg.set("Z", player.getLocation().getZ());
                        cfg.set("Yaw", player.getLocation().getYaw());
                        cfg.set("Pitch", player.getLocation().getPitch());
                        cfg.set("World", player.getLocation().getWorld().getName());
                        cfg.save(file);
                        player.sendMessage("§cWarp §7● §7Der Warp§a " + file.getName() + " §7wurde erfolgreich §aerstellt");
                    } catch (IOException e) {
                        player.sendMessage("§cWarp §7● §4Ein Fehler ist aufgetreten! Datei konnte nicht erstellt werden!");
                        return true;
                    }
                }
            } else {
                player.sendMessage("§cWarp §7● Verwende §c/setwarp <Name>");
                return true;
            }

        } else {
            sender.sendMessage("§cYou do not have permission to execute this command!");
        }


        return false;
    }
}
