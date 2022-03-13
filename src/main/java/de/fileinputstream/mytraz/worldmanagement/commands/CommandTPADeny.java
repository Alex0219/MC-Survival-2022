package de.fileinputstream.mytraz.worldmanagement.commands;

import java.util.List;
import java.util.UUID;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandTPADeny implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length == 0)
                player.sendMessage("§bMC-Survival.de §7» §7Bitte verwende /tpadeny <Name>");
            else if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer == null) {
                    player.sendMessage("§bMC-Survival.de §7» §3" + args[0] + " §7ist nicht §3online§7.");
                    return false;
                }

                if (!Bootstrap.getInstance().getTpaRequests().containsKey(player.getUniqueId())) {
                    player.sendMessage("§bMC-Survival.de §7» §7Du hast keine §3TPA-Anfragen §7erhalten.");
                    return false;
                }

                List<UUID> senders = Bootstrap.getInstance().getTpaRequests().get(player.getUniqueId());

                if (!senders.contains(targetPlayer.getUniqueId())) {
                    player.sendMessage(
                            "§bMC-Survival.de §7» §3" + targetPlayer.getName() + " §7hat dir keine §3TPA-Anfrage §7gesendet.");
                    return false;
                }

                senders.remove(targetPlayer.getUniqueId());

                Bootstrap.getInstance().getTpaRequests().replace(player.getUniqueId(), senders);

                player.sendMessage("§bMC-Survival.de §7» §7Du hast die §3TPA-Anfrage §7von §3" + targetPlayer.getName() + " §7abgelehnt.");
                targetPlayer.sendMessage("§bMC-Survival.de §7» §3" + player.getName() + " §7hat deine §3TPA-Anfrage §7abgelehnt.");
            } else
                player.sendMessage("§bMC-Survival.de §7» §7Bitte verwende /tpadeny <Name>");
        }
        return false;
    }
}
