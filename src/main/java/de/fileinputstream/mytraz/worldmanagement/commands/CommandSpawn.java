package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Created by Alexander on 09.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandSpawn implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender commandSender,  Command command,  String s,  String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.teleport(Bootstrap.getInstance().getSpawnLocation());
        }
        return false;
    }
}
