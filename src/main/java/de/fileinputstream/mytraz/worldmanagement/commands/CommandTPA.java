package de.fileinputstream.mytraz.worldmanagement.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandTPA implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0)
                player.sendMessage("§bMC-Survival.de §7» §7Bitte verwende /tpa <Name>");
            else if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer == null) {
                    player.sendMessage("§bMC-Survival.de §7» §3" + args[0] + " §7ist nicht §3online§7.");
                    return false;
                }
                DBUser targetdbuser = Bootstrap.getInstance().getRankManager().getDBUser(args[0]);

                if (targetPlayer.equals(player)) {
                    player.sendMessage("§bMC-Survival.de §7» §7Du kannst dir §3selbst §7keine §3TPA-Anfrage §7schicken.");
                    return false;
                }

                if (Bootstrap.getInstance().getTpaRequests().containsKey(targetPlayer.getUniqueId())) {
                    List<UUID> senders = Bootstrap.getInstance().getTpaRequests().get(targetPlayer.getUniqueId());

                    if (senders.contains(player.getUniqueId())) {
                        player.sendMessage("§bMC-Survival.de §7» §7Du hast §3" + targetPlayer.getName()
                                + " §7bereits eine §3TPA-Anfrage §7gesendet.");
                        return false;
                    }


                    if (!Bootstrap.getInstance().getJedis().hget("uuid:" + targetdbuser.getUuid(), "tpaallow").equalsIgnoreCase("true")) {
                        player.sendMessage("§bMC-Survival.de §7» §3" + targetPlayer.getName() + " §7möchte keine §3TPA-Anfragen§7.");
                        return false;
                    }

                    senders.add(player.getUniqueId());
                    Bootstrap.getInstance().getTpaRequests().replace(targetPlayer.getUniqueId(), senders);

                    sendTpa(player, targetPlayer);
                } else {
                    if (!Bootstrap.getInstance().getJedis().hget("uuid:" + targetdbuser.getUuid(), "tpaallow").equalsIgnoreCase("true")) {
                        player.sendMessage("§bMC-Survival.de §7» §3" + targetPlayer.getName() + " §7möchte keine §3TPA-Anfragen§7.");
                        return false;
                    }

                    List<UUID> senders = new ArrayList<>();
                    senders.add(player.getUniqueId());

                    Bootstrap.getInstance().getTpaRequests().put(targetPlayer.getUniqueId(), senders);

                    sendTpa(player, targetPlayer);
                }
            } else
                player.sendMessage("§bMC-Survival.de §7» §7Bitte verwende /tpa <Spielername>");
        }
        return false;
    }

    public void sendTpa(Player player, Player targetPlayer) {
        String playerName = player.getName();
        TextComponent textComponent1 = new TextComponent("§bMC-Survival.de §7» §7Der Spieler §3" + player.getName()
                + " §7hat §7dir §7eine §3TPA-Anfrage §7geschickt.\n");
        TextComponent textComponent2 = new TextComponent("                  §a§lAnnehmen");
        textComponent2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + player.getName()));
        textComponent2.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§oJa ich will").create()));
        TextComponent textComponent3 = new TextComponent("§c§lAblehnen");
        textComponent3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + player.getName()));
        textComponent3.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§3§oNein ich will nicht").create()));
        textComponent1.addExtra(textComponent2);
        textComponent1.addExtra(" ");
        textComponent1.addExtra(textComponent3);
        targetPlayer.spigot().sendMessage(textComponent1);

        player.sendMessage("§bMC-Survival.de §7» §3TPA-Anfrage §7an §3" + targetPlayer.getName() + " §7wurde verschickt.");

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Bootstrap.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Bootstrap.getInstance().getTpaRequests().containsKey(targetPlayer.getUniqueId())) {
                    List<UUID> senders = Bootstrap.getInstance().getTpaRequests().get(targetPlayer.getUniqueId());
                    if (senders.contains(player.getUniqueId())) {
                        senders.remove(player.getUniqueId());
                        if (Bukkit.getPlayer(playerName) != null)
                            player.sendMessage("§bMC-Survival.de §7» §3TPA-Anfrage §7an §3" + targetPlayer.getName()
                                    + " §7wurde §3gelöscht§7.");
                        if (Bukkit.getPlayer(targetPlayer.getUniqueId()) != null)
                            targetPlayer.sendMessage(
                                    "§bMC-Survival.de §7» §3TPA-Anfrage §7von §3" + playerName + " §7wurde §3gelöscht§7.");
                        Bootstrap.getInstance().getTpaRequests().replace(targetPlayer.getUniqueId(), senders);
                    }
                }
            }
        }, 2400L);
    }

}


