package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.BackupPerformData;
import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 19:51
 */
public class CommandDenyBackup implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin")) {
                if (args.length == 0) {
                    if (CommandRestoreBackup.backupList.containsKey(player)) {
                        player.sendMessage("§bMC-Survival.de §7» §cDie aktuelle Backupaktion wurde abgebrochen!");
                        CommandRestoreBackup.backupList.remove(player);
                        return true;
                    } else {
                        player.sendMessage("§7MC-Survival.de §7» §cDu hast keine Backup Aktion am Laufen!");
                        return true;
                    }
                } else {
                    player.sendMessage("§bMC-Survival.de §7» Verwende /denybackup");
                    return true;
                }

            } else {
                sender.sendMessage("§cYou do not have permission to execute this command!");
                return true;
            }
        } else {
            sender.sendMessage("§bMC-Survival.de §7» Nur Spieler können diesen Befehl ausführen.");
        }
        return false;
    }
}
