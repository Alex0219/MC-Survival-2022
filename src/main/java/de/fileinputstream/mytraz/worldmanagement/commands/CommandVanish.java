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

import java.util.ArrayList;

public class CommandVanish implements CommandExecutor {

    private static ArrayList<Player> vanishedPlayers = new ArrayList<Player>();

    public static ArrayList<Player> getVanishedPlayers() {
        return vanishedPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
            if (dbUser.getRank() == RankEnum.ADMIN || dbUser.getRank() == RankEnum.MOD) {
                if (args.length == 0) {
                    if (!getVanishedPlayers().contains(player)) {
                        player.sendMessage("§cDu bist nun im Vanish.");
                        Bukkit.broadcastMessage("§c" + player.getDisplayName() + " §7hat den Server verlassen.");
                        getVanishedPlayers().add(player);
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            if (!RankManager.getRank(players.getUniqueId().toString()).equalsIgnoreCase("admin")) {
                                players.hidePlayer(player);
                            }
                        });
                    } else {
                        player.sendMessage("§cDu bist nun nicht mehr im Vanish.");
                        Bukkit.broadcastMessage("§c" + player.getDisplayName() + " §7hat den Server betreten.");
                        getVanishedPlayers().remove(player);
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.showPlayer(player);
                        });
                        return true;
                    }
                } else {
                    player.sendMessage("§cVerwende /v");
                    return true;
                }
            } else {
                player.sendMessage("§cYou do not have permission to execute this command!");
                return true;
            }
        } else {
            return true;
        }

        return false;
    }
}
