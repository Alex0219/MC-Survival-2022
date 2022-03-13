package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by Alexander on 09.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandSetSpawn implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (player.isOp()) {
                try {
                    setSpawnLocation(player.getLocation());
                    player.sendMessage("§aDer Lobby-Spawn wurde erfolgreich gesetzt.");
                } catch (IOException e) {
                    player.sendMessage("§cEin Fehler ist aufgetreten.");
                    e.printStackTrace();
                }
            } else {
                player.sendMessage("§cYou do not have permission to execute this command!");
                return true;

            }
        } else {
            return true;
        }

        return false;
    }

    private void setSpawnLocation(Location loc) throws IOException {

        File f = new File("plugins//WorldManagement", "spawn.yml");

        if (!f.exists()) {
            f.createNewFile();
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        cfg.set("X", loc.getX());
        cfg.set("Y", loc.getY());
        cfg.set("Z", loc.getZ());
        cfg.set("Yaw", loc.getYaw());
        cfg.set("Pitch", loc.getPitch());
        cfg.set("World", loc.getWorld().getName());
        cfg.save(f);

    }
}
