package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 15.12.2018 at 22:40
 */
public class  CommandChatLog implements CommandExecutor {
    public ArrayList<String> cooldownList = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length == 1) {
            if(Bukkit.getPlayer(commandSender.getName()) !=null) {
                String targetName = args[0];
                if (commandSender.getName().equalsIgnoreCase(targetName)) {
                    commandSender.sendMessage("§bMC-Survival.de §7» §cDu kannst keinen ChatLog über dich selbst erstellen!");
                    return true;
                }
                if (!Bootstrap.getInstance().getChatLogManager().chatLogs.containsKey(UUIDFetcher.getUUID(targetName))) {
                    commandSender.sendMessage("§bMC-Survival.de §7» §cBitte gebe einen Spieler an, der online ist!");
                    return true;
                }


                String targetUUID = UUIDFetcher.getUUID(targetName).toString();
                String chatlogID = Bootstrap.getInstance().getChatLogManager().createChatLog(targetUUID, commandSender.getName(), targetName);
                if (chatlogID.equalsIgnoreCase("NOMESSAGES")) {
                    commandSender.sendMessage("§bMC-Survival.de §7» §cDieser Spieler hat noch keine Nachrichten geschrieben!");
                    return true;
                }
                if (cooldownList.contains(commandSender.getName())) {
                    commandSender.sendMessage("§bMC-Survival.de §7» §cBitte warte noch, bis du einen weiteren ChatLog erstellst!");
                    return true;
                }

                commandSender.sendMessage("§bMC-Survival.de §7» §aErstelle einen neuen ChatLog...");
                commandSender.sendMessage("§bMC-Survival.de §7» §eDu kannst den ChatLog hier einsehen: https://mc-survival.de/chatlog?chatlogid=" + chatlogID);
                cooldownList.add(commandSender.getName());
                executeCooldown(commandSender.getName());

            } else {
                commandSender.sendMessage("§bMC-Survival.de §7» §cBitte gebe einen Spieler an, der auf dem Server ist!");
            }
        } else {
            commandSender.sendMessage("§bMC-Survival.de §7» §cBitte verwende /chatlog <Spieler>");
        }

        return false;
    }

    public void executeCooldown(String playerName) {

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldownList.remove(playerName);
            }
        }.runTaskLaterAsynchronously(Bootstrap.getInstance(), 1200L);
    }

}
