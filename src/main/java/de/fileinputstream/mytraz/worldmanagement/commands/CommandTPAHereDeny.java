package de.fileinputstream.mytraz.worldmanagement.commands;

import java.util.List;
import java.util.UUID;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandTPAHereDeny implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length == 0)
                player.sendMessage("§bMC-Survival.de §7» §7Bitte verwende /tpaheredeny <Name>");
            else if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer == null) {
                    player.sendMessage("§bMC-Survival.de §7»  §3" + args[0] + " §7ist nicht §3online§7.");
                    return false;
                }

                if (!Bootstrap.getInstance().getTpaHereRequests().containsKey(player.getUniqueId())) {
                    player.sendMessage("§bMC-Survival.de §7»  §7Du hast keine §3TPA-Here-Anfragen §7erhalten.");
                    return false;
                }

                List<UUID> senders = Bootstrap.getInstance().getTpaHereRequests().get(player.getUniqueId());

                if (!senders.contains(targetPlayer.getUniqueId())) {
                    player.sendMessage(
                            "§bMC-Survival.de §7»  §3" + targetPlayer.getName() + " §7hat dir keine §3TPA-Here-Anfragen §7gesendet");
                    return false;
                }

                senders.remove(targetPlayer.getUniqueId());

                Bootstrap.getInstance().getTpaHereRequests().replace(player.getUniqueId(), senders);

                player.sendMessage(
                        "§bMC-Survival.de §7»  §7Du hast die §3TPA-Here-Anfrage §7von §3" + targetPlayer.getName() + " §7abgelehnt.");
                targetPlayer
                        .sendMessage("§bMC-Survival.de §7»  §3" + player.getName() + " §7hat deine §3TPA-Here-Anfrage §7abgelehnt.");
            } else
                player.sendMessage("§bMC-Survival.de §7»  §c/tpaheredeny (Spieler)");
        }
        return false;
    }
}
