package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Alexander on 08.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandVote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(commandSender.getName());
            player.sendMessage("§bMC-Survival.de §7» §7Du hast bereits §c" + dbUser.getVotes() + " §7Votes");
            player.sendMessage("§bMC-Survival.de §7» §7Hier kannst du voten:");
            player.sendMessage("§7https://minecraft-server.eu/vote/index/21945");
            player.sendMessage("§7https://www.serverliste.net/vote/3628");
            player.sendMessage("§7https://www.minecraft-serverlist.net/vote/54438");
            return true;
        }
        return false;
    }
}
