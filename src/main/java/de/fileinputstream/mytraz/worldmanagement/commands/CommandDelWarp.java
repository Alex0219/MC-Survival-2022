package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class CommandDelWarp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.DARK_RED + "Die Konsole kann diesen Befehl nicht ausführen.");
            return true;
        }

        if (args.length == 1) {
            Player player = (Player) sender;
            if (player.hasPermission("lobby.warp")) {
                if (args.length == 1) {
                    File file = new File("plugins//WorldManagement//Warps//" + args[0]);
                    if (file.exists()) {
                        file.delete();
                        player.sendMessage("§cDer Warp §a " + args[0] + "§7wurde erfolgreich gelöscht.");
                        return true;
                    }
                    player.sendMessage("§4Dieser Warp existiert nicht!");
                    return true;
                }


            }

        }

        return false;
    }
}
