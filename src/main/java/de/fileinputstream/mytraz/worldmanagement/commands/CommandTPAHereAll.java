package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTPAHereAll implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            DBUser dbuser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());

            if (dbuser.getRank() != RankEnum.ADMIN) {
                player.sendMessage("§cYou do not have permission to execute this command!");
                return false;
            }

            for (Player player2 : Bukkit.getOnlinePlayers()) {
                if (!player.equals(player2)) {
                    player.performCommand("tpahere " + player2.getName());
                }
            }
        }
        return false;
    }

}
