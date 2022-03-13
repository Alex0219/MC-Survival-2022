package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.BackupPerformData;
import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 19:51
 */
public class CommandRestoreBackup implements CommandExecutor {

    public static HashMap<Player, BackupPerformData> backupList = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
            if (dbUser.getRank() == RankEnum.ADMIN) {
                if (args.length == 2) {
                    String worldID = args[0];
                    String backupID = args[1];

                    if (!Bootstrap.getInstance().getCurrentDownloadingWorlds().contains(worldID)) {
                        if (backupList.containsKey(player)) {
                            player.sendMessage("§7MC-Survival.de §7» §cDu hast bereits eine Backup Aktion am Laufen!");
                            player.sendMessage("§7MC-Survival.de §7» §cBitte führe diese zuerst durch oder breche diese ab.");
                            return true;
                        }
                        Bootstrap.getInstance().getBackupManager().getBackupsFromWorld(args[0], (Consumer<List<String>>) worldBackups -> {
                            if (worldBackups.size() < 1) {
                                player.sendMessage("§7MC-Survival.de §7» §cFür diese Welt wurde noch kein Backup erstellt!");
                                return;
                            }
                            if (!new File("/mnt/onedrive-backup/minecraft-backups/" + worldID + "_" + backupID + ".zip").exists()) {
                                player.sendMessage("§7MC-Survival.de §7» §cDie eingegebende Backup-ID ist ungültig!");
                                return;
                            }

                            player.sendMessage("§bMC-Survival.de §7» §4Die folgende Welt wird mit dem folgenden Backup ersetzt: §a" + backupID);
                            Long millis = Long.valueOf(backupID);
                            Instant instant = Instant.ofEpochMilli(millis);
                            ZonedDateTime z = instant.atZone(ZoneId.of("Europe/Berlin"));

                            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(" dd.MM.yyyy kk:mm ");
                            player.sendMessage("§bMC-Survival.de §7» ID: §b" + millis + " §7Von: §b" + fmt.format(z));
                            player.sendMessage("§bMC-Survival.de §7» §cDie Welt wird auf den Zeitpunkt: §a" + fmt.format(z) + " §czurückgesetzt.");
                            player.sendMessage("§bMC-Survival.de §7» §aAktion durchführen: /dobackup, §eAktion §cnicht §edurchführen: /denybackup");
                            backupList.put(player, new BackupPerformData(worldID, "/mnt/onedrive-backup/minecraft-backups/" + worldID + "_" + backupID + ".zip"));
                            return;
                        });
                    } else {
                        player.sendMessage("§bMC-Survival.de §7» §cDiese Welt ist momentan nicht verfügbar, da ein Backup geladen wird!");
                        return true;
                    }


                } else {
                    player.sendMessage("§bMC-Survival.de §7» Verwende /restorebackup <Welten-ID> <Backupid>");
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
