package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * User: Alexander<br/>
 * Date: 10.02.2018<br/>
 * Time: 13:33<br/>
 * MIT License
 * <p>
 * Copyright (c) 2017 Alexander Fiedler
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use and modify without distributing the software to anybody else,
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * <p>
 * MIT Lizenz
 * Copyright (c) 2017 Alexander Fiedler
 * Hiermit wird unentgeltlich jeder Person, die eine Kopie der Software und der zugehörigen Dokumentationen (die "Software") erhält, die Erlaubnis erteilt, sie uneingeschränkt zu nutzen, inklusive und ohne Ausnahme mit dem Recht, sie zu verwenden, zu verändern und Personen, denen diese Software überlassen wird, diese Rechte zu verschaffen, außer sie zu verteilen unter den folgenden Bedingungen:
 * <p>
 * Der obige Urheberrechtsvermerk und dieser Erlaubnisvermerk sind in allen Kopien oder Teilkopien der Software beizulegen.
 * <p>
 * DIE SOFTWARE WIRD OHNE JEDE AUSDRÜCKLICHE ODER IMPLIZIERTE GARANTIE BEREITGESTELLT, EINSCHLIEßLICH DER GARANTIE ZUR BENUTZUNG FÜR DEN VORGESEHENEN ODER EINEM BESTIMMTEN ZWECK SOWIE JEGLICHER RECHTSVERLETZUNG, JEDOCH NICHT DARAUF BESCHRÄNKT. IN KEINEM FALL SIND DIE AUTOREN ODER COPYRIGHTINHABER FÜR JEGLICHEN SCHADEN ODER SONSTIGE ANSPRÜCHE HAFTBAR ZU MACHEN, OB INFOLGE DER ERFÜLLUNG EINES VERTRAGES, EINES DELIKTES ODER ANDERS IM ZUSAMMENHANG MIT DER SOFTWARE ODER SONSTIGER VERWENDUNG DER SOFTWARE ENTSTANDEN.
 */
public class CommandAddResident implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {

                String uuid = player.getUniqueId().toString();
                String targetUUID = UUIDFetcher.getUUID(args[0]).toString();

                if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
                    String worldID = Bootstrap.getInstance().getWorldManager().getWorld(uuid);
                    if (!Bootstrap.getInstance().getWorldManager().isResidentInWorld(targetUUID, worldID)) {
                        if (!Bootstrap.getInstance().getWorldManager().worldInvites.containsKey(targetUUID)) {
                            if (Bukkit.getPlayer(args[0]) != null) {
                                if (!Bootstrap.getInstance().getWorldManager().worldInvites.containsKey(targetUUID)) {
                                    player.sendMessage("§bMC-Survival.de §7» Du hast §a" + args[0] + " §cin deine Welt eingeladen.");
                                    player.sendMessage("§bMC-Survival.de §7» Diese Einladung läuft automatisch nach 5 Minuten ab.");
                                    Bukkit.getPlayer(args[0]).sendMessage("§bMC-Survival.de §7» §aDu wurdest von §a" + player.getName() + " §cin seine Welt eingeladen.");
                                    Bukkit.getPlayer(args[0]).sendMessage("§bMC-Survival.de §7» Du kannst diese Einladung mit /acceptinvite akzeptieren.");
                                    Bukkit.getPlayer(args[0]).sendMessage("§bMC-Survival.de §7» Diese Einladung läuft automatisch nach 5 Minuten ab.");
                                    Bootstrap.getInstance().getWorldManager().worldInvites.put(targetUUID, worldID);
                                    scheduleInviteTask(args[0], targetUUID);
                                } else {
                                    player.sendMessage("§bMC-Survival.de §7» Dieser Spieler wurde bereits in eine Welt eingeladen.");
                                    player.sendMessage("§bMC-Survival.de §7» Bitte warte noch, bis der Spieler die Einladung der anderen Welt angenommen hat.");
                                    return true;
                                }

                            } else {
                                player.sendMessage("§bMC-Survival.de §7» Dieser Spieler ist nicht online!");
                                return true;
                            }
                        } else {
                            player.sendMessage("§bMC-Survival.de §7» Du hast diesen Spieler bereits in deine Welt eingeladen.");
                            return true;
                        }

                    } else {
                        player.sendMessage("§bMC-Survival.de §7» Dieser Spieler ist bereits Mitbewohner deiner Welt.");
                        return true;
                    }
                } else {
                    player.sendMessage("§bMC-Survival.de §7» Du besitzt noch keine Welt. Erstelle dir eine mit: §a/createworld§c!");
                    return true;
                }


            } else {
                player.sendMessage("§bMC-Survival.de §7» Verwende /addresident <Spielername>");
                return true;
            }
        } else {

        }
        return false;
    }

    public void scheduleInviteTask(String name, String uuid) {

        new BukkitRunnable() {
            @Override
            public void run() {
                Bootstrap.getInstance().getWorldManager().worldInvites.remove(uuid);
                if (Bukkit.getPlayer(name) != null) {
                    Bukkit.getPlayer(name).sendMessage("§bMC-Survival.de §7» Deine Einladung ist abgelaufen.");
                }
            }
        }.runTaskLater(Bootstrap.getInstance(), 300000);
    }
}