package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Created by Alexander on 08.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandBuild implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = UUIDFetcher.getUUID(player.getName()).toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin")) {
                if (!Bootstrap.getInstance().getPermittedBuilders().contains(player.getName())) {
                    player.sendMessage("§bMC-Survival.de §7» §aDer Baumodus wurde aktiviert.");
                    Bootstrap.getInstance().getPermittedBuilders().add(player.getName());
                } else {
                    player.sendMessage("§bMC-Survival.de §7» §cDer Baumodus wurde deaktiviert.");
                    Bootstrap.getInstance().getPermittedBuilders().remove(player.getName());
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
