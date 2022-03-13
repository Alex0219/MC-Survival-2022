package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTPAToggle implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());

            if (!Bootstrap.getInstance().getJedis().hget("uuid:" + dbUser.getUuid(), "tpaallow").equalsIgnoreCase("true")) {
                player.sendMessage("§bMC-Survival.de §7» §7Andere §3Spieler §7können dir jetzt wieder eine §3TPA-Anfrage §7senden.");

                Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "tpaallow", "true");
            } else {
                player.sendMessage("§bMC-Survival.de §7» §7Andere §3Spieler §7können dir jetzt keine §3TPA-Anfrage §7mehr senden.");

                Bootstrap.getInstance().getJedis().hset("uuid:" + dbUser.getUuid(), "tpaallow", "false");
            }
        }
        return false;
    }
}
