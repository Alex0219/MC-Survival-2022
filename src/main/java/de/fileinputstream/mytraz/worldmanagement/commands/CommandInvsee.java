package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Created by Alexander on 10.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandInvsee implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(commandSender.getName());
            if (dbUser.getRank() == RankEnum.ADMIN || dbUser.getRank() == RankEnum.MOD) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        commandSender.sendMessage("§bMC-Survival.de §7» §cDer Spieler konnte nicht gefunden werden.");
                        return true;
                    }

                    DBUser targetDbUser = Bootstrap.getInstance().getRankManager().getDBUser(target.getName());

                    if (!(dbUser.getRank().getRankLevel() >= targetDbUser.getRank().getRankLevel())) {
                        commandSender.sendMessage("§bMC-Survival.de §7» §cDu darfst das Inventar dieses Spielers nicht öffnen!");
                        return true;
                    }

                    ((Player) commandSender).openInventory(target.getInventory());
                } else {
                    commandSender.sendMessage("§bMC-Survival.de §7» Verwende /invsee <Name>");
                    return true;
                }
            } else {
                commandSender.sendMessage("§cYou do not have permission to execute this command!");
                return true;
            }
        }
        return false;
    }
}
