package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 18.12.2018 at 16:32
 */
public class CommandBackupList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin")) {
                if (args.length == 1) {
                    String worldID = args[0];
                    player.sendMessage("§cLade Daten.. Könnte etwas Zeit in Anspruch nehmen.");
                    Bootstrap.getInstance().getBackupManager().getBackupsFromWorld(worldID, (Consumer<List<String>>) worldBackups -> {
                        if (worldBackups.size() < 1) {
                            player.sendMessage("§7MC-Survival.de §7» §cFür diese Welt wurde noch kein Backup erstellt!");
                            return;
                        }
                        List<List<String>> pages = CommandBackupList.getPages(worldBackups, 5);
                        player.sendMessage("§bMC-Survival.de §7» Es gibt §c" + pages.size() + " §7Seiten.");
                        player.sendMessage("§bMC-Survival.de §7» Seite: 0");
                        for (int i = 0; i < pages.size(); i++) {
                            if (i == 0) {

                                List<String> sortedList = pages.get(i);
                                Collections.sort(sortedList, Collections.reverseOrder());
                                for (String singleListItem : sortedList) {
                                    String[] splitted = singleListItem.split("_");
                                    String millisString = splitted[1].replace(".zip", "");
                                    millisString = millisString.replace(" ", "");
                                    Long millis = Long.valueOf(millisString);
                                    Instant instant = Instant.ofEpochMilli(millis);
                                    ZonedDateTime z = instant.atZone(ZoneId.of("Europe/Berlin"));

                                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern(" dd.MM.yyyy kk:mm ");
                                    player.sendMessage("§bMC-Survival.de §7» ID:§b" + millis + " §7Von:§b" + fmt.format(z));

                                }
                            }

                        }

                    });

                } else if (args.length == 2) {
                    String worldID = args[0];
                    player.sendMessage("§cLade Daten.. Könnte etwas Zeit in Anspruch nehmen.");
                    Bootstrap.getInstance().getBackupManager().getBackupsFromWorld(worldID, (Consumer<List<String>>) worldBackups -> {
                        int pageNumber = Integer.valueOf(args[1]);
                        if (worldBackups.size() < 1) {
                            player.sendMessage("§7MC-Survival.de §7» §cFür diese Welt wurde noch kein Backup erstellt!");
                            return;
                        }
                        List<List<String>> pages = CommandBackupList.getPages(worldBackups, 5);
                        for (int i = 0; i < pages.size(); i++) {
                            System.out.println(i);
                            if (i == pageNumber) {
                                System.out.println("List: " + i);
                                List<String> sortedList = pages.get(i);
                                Collections.sort(sortedList, Collections.reverseOrder());
                                for (String singleListItem : sortedList) {
                                    String[] splitted = singleListItem.split("_");
                                    String millisString = splitted[1].replace(".zip", "");
                                    millisString = millisString.replace(" ", "");
                                    Long millis = Long.valueOf(millisString);
                                    Instant instant = Instant.ofEpochMilli(millis);
                                    ZonedDateTime z = instant.atZone(ZoneId.of("Europe/Berlin"));

                                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern(" dd.MM.yyyy kk:mm ");
                                    player.sendMessage("§bMC-Survival.de §7» ID:§b" + millis + " §7Von:§b" + fmt.format(z));

                                }
                            }
                        }
                    });

                } else {
                    player.sendMessage("§bMC-Survival.de §7» Verwende /listbackups <ID> oder /listbackups <ID> <Seitenzahl>");
                    return true;
                }
            } else {

            }
        } else {
            sender.sendMessage("§bMC-Survival.de §7» Nur Spieler können diesen Befehl ausführen.");
        }
        return false;
    }

    public static <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
        if (c == null)
            return Collections.emptyList();
        List<T> list = new ArrayList<T>(c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size())
            pageSize = list.size();
        int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
        List<List<T>> pages = new ArrayList<List<T>>(numPages);
        for (int pageNum = 0; pageNum < numPages; )
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        return pages;
    }


}
