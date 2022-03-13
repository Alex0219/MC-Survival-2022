package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 13.08.2020 00:06
 * © 2020 Alexander Fiedler
 */
public class CommandTeleport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender,  Command command,  String s, String[] args) {
        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;
            DBUser dbuser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
            if (dbuser.getRank() == RankEnum.ADMIN || dbuser.getRank() == RankEnum.MOD) {
                if (args.length == 1) {
                    final Player t = Bukkit.getPlayer(args[0]);
                    if (t != null) {
                        DBUser tDBUser = Bootstrap.getInstance().getRankManager().getDBUser(t.getName());
                        if (dbuser.getRank().getRankLevel() >= tDBUser.getRank().getRankLevel()) {
                            player.teleport(t);
                            player.sendMessage("§bMC-Survival.de §7» Du hast dich zu §c§o" + t.getName() + " §7teleportiert!");
                        } else {
                            player.sendMessage("§bMC-Survival.de §7» §cDu darfst dich nicht zu diesem Spieler teleportieren");
                        }
                    } else {
                        player.sendMessage("§bMC-Survival.de §7» Der Spieler ist §c§onicht §7online!");
                    }
                } else if (args.length == 2) {
                    final Player t2 = Bukkit.getPlayer(args[0]);
                    final Player t3 = Bukkit.getPlayer(args[1]);

                    if (t2 != null || t3 != null) {
                        DBUser dbUserT2 = Bootstrap.getInstance().getRankManager().getDBUser(args[0]);
                        DBUser dbUserT3 = Bootstrap.getInstance().getRankManager().getDBUser(args[1]);

                        if (dbuser.getRank() == RankEnum.ADMIN) {
                            t2.teleport(t3);
                            t2.sendMessage("§bMC-Survival.de §7» Du wurdest zu §c§o" + t3.getName() + " §7teleportiert!");
                            t3.sendMessage("§bMC-Survival.de §7» Der Spieler §c§o" + t2.getName() + " §7wurde zu dir teleportiert!");
                            player.sendMessage("§bMC-Survival.de §7» Du hast §c§o" + t2.getName() + " §7zu §c§o" + t3.getName() + " §7teleportiert!");
                        } else if (dbuser.getRank() == RankEnum.MOD) {
                            if (dbUserT2.getRank() == RankEnum.ADMIN || dbuser.getRank() == RankEnum.ADMIN || dbUserT3.getRank() == RankEnum.ADMIN) {
                                player.sendMessage("§bMC-Survival.de §7» §cFehler: Die aktuelle Aktion darf von dir nicht durchgeführt werden!");
                                return true;
                            } else {
                                t2.teleport(t3);
                                t2.sendMessage("§bMC-Survival.de §7» Du wurdest zu §c§o" + t3.getName() + " §7teleportiert!");
                                t3.sendMessage("§bMC-Survival.de §7» Der Spieler §c§o" + t2.getName() + " §7wurde zu dir teleportiert!");
                                player.sendMessage("§bMC-Survival.de §7» Du hast §c§o" + t2.getName() + " §7zu §c§o" + t3.getName() + " §7teleportiert!");
                            }
                        }
                    } else {
                        player.sendMessage("§bMC-Survival.de §7» Einer der angegebenen Spieler ist §c§onicht §7online!");
                    }
                } else {
                    player.sendMessage("§bMC-Survival.de §7» §7Bitte verwende /tp <Spieler1> <Spieler2>");
                }
            } else {
                player.sendMessage("§cYou do not have permission to execute this command!");
            }
        }
        return false;
    }
}
