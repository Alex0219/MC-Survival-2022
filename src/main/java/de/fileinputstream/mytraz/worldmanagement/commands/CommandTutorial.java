package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alexander<br/>
 * Date: 23.02.2018<br/>
 * Time: 23:54<br/>
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
public class CommandTutorial implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;


            ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
            bookMeta.setTitle("MC-Survival.de Hilfe");
            bookMeta.setAuthor("MC-Survival.de");

            List<String> pages = new ArrayList<String>();
            pages.add("Willkommen auf MC-Survival.de!\n \nSchön, dass du unseren Server gefunden hast und dir nun das Tutorial ansehen möchtest.\nBei uns kannst du dir deine eigene Welt erstellen. Wie das geht erfährst du auf den folgenden Seiten");
            pages.add("§c/createworld \n \n§rHiermit kannst du dir deine eigene Welt erstellen. Allerdings kannst keine zweite Welt erstellen.");
            pages.add("§c/addresident <Name> \n \n§rSchickt dem angegebenen Spieler eine Einladung zu deiner Welt.");
            pages.add("§c/acceptinvite \n \n§rNimmt die aktuelle Welteneinladung eines Spielers an.");
            pages.add("§c/removeresident <Name> \n \n§rEntfernt einen Spieler aus deiner Welt.");
            pages.add("§c/newworldspawn \n \n§rSetzt den Spawn deiner Welt auf die aktuelle Position");
            pages.add("§c/lockworld\n \n§rWirft alle Nicht-Bewohner aus deiner Welt.");
            pages.add("§c/sethome \n \n§rSetzt dein Home auf die aktuelle Position.");
            pages.add("§c/home \n \n§rTeleportiert dich zu deinem Home.");
            pages.add("§c/tpworld oder /tpworld <Weltenname> \n§rTeleportiert dich in eine Welt. Mit einer ID als zweites Argument kannst du in Welten gelangen, in denen du Bewohner bist.");
            pages.add("§c/setpvp \n \n§rAktiviert oder deaktiviert PvP in deiner Welt.");
            pages.add("§c/worlds \n \n§rZeigt dir alle Welten an, in denen du bereits Mitbewohner bist.");
            pages.add("§c/ec \n \n§rÖffnet deine Enderchest.");
            pages.add("§c/ontime \n \n§rZeigt dir deine aktuelle Ontime an.");
            pages.add("§c/vote \n \n§rZeigt dir deine aktuellen Votes an.");
            pages.add("§c/warp \n \n§rZeigt dir alle verfügbaren Warps an.");
            pages.add("§c/chatlog <Name> \n \n§rErstellt einen Chatlog über einen Spieler.");
            pages.add("Weitere Informationen: \n \nAb vier Tagen Onlinezeit erhälst du den Stammspieler Rang.");
            pages.add("Votes: \n \n§rMit jedem Vote bekommst du einen Vote-Key, den du am Spawn mit einem Rechtsklick auf die Votekiste einlösen kannst.");
            pages.add("Vielen Dank für das Lesen des Tutorials. Nun solltest du alle Befehle kennen und bist somit startklar!");

            bookMeta.setPages(pages);
            writtenBook.setItemMeta(bookMeta);
            player.openBook(writtenBook);
        }
        return false;
    }
}
