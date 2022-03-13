package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 15.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandAFK implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Bootstrap.getInstance().getAfkPlayers().contains(player)) {
                player.sendMessage("§bMC-Survival.de §7» §cDu bist nun AFK.");
                Bootstrap.getInstance().getAfkPlayers().add(player);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    NameTags.setTags(players);
                });
            } else {
                player.sendMessage("§bMC-Survival.de §7» §cDu bist nun nicht mehr AFK.");
                Bootstrap.getInstance().getAfkPlayers().remove(player);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    NameTags.setTags(players);
                });
            }

        } else {
            sender.sendMessage("§bMC-Survival.de §7» Nur Spieler können diesen Befehl ausführen.");
        }
        return false;
    }
}
