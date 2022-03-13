package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGM implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
            if (dbUser.getRank() == RankEnum.ADMIN) {
                if (args.length == 1) {
                    switch (Integer.parseInt(args[0])) {

                        case 0:
                            player.setGameMode(GameMode.SURVIVAL);
                            break;
                        case 1:
                            player.setGameMode(GameMode.CREATIVE);
                            break;
                        case 2:
                            player.setGameMode(GameMode.ADVENTURE);
                            break;
                        case 3:
                            player.setGameMode(GameMode.SPECTATOR);
                            break;
                        default:
                            player.sendMessage("§cVerwende eine Zahl zwischen 1-3");
                            break;

                    }
                } else if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        switch (Integer.parseInt(args[0])) {

                            case 0:
                                target.setGameMode(GameMode.SURVIVAL);
                                break;
                            case 1:
                                target.setGameMode(GameMode.CREATIVE);
                                break;
                            case 2:
                                target.setGameMode(GameMode.ADVENTURE);
                                break;
                            case 3:
                                target.setGameMode(GameMode.SPECTATOR);
                                break;
                            default:
                                player.sendMessage("§cVerwende eine Zahl zwischen 1-3");
                                break;

                        }
                    } else {
                        player.sendMessage("§cDieser Spieler ist nicht online!");
                        return true;
                    }
                } else {
                    player.sendMessage("§cVerwende /gm 1,2 oder 3 oder /gm 1,2 oder 3 <Spieler>");
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
