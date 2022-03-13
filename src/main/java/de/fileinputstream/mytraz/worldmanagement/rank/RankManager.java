package de.fileinputstream.mytraz.worldmanagement.rank;


import de.fileinputstream.mytraz.worldmanagement.Bootstrap;

import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

/**
 * User: Alexander<br/>
 * Date: 04.02.2018<br/>
 * Time: 20:16<br/>
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
public class RankManager {

    public ArrayList<DBUser> dbusers = new ArrayList<>();

    /**
     * @param uuid
     * @return @{@link String} Gibt einen String zurück, der den Rang des Spielers enthält.
     */
    public static String getRank(String uuid) {
        Bootstrap.getInstance().getJedis().select(0);
        DBUser user = new DBUser(uuid, UUIDFetcher.getName(uuid));
        if (user.userExists()) {
            return Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "rank");
        }
        return "";
    }

    /**
     * @param uuid ID des Spielers
     */
    public static void setRank(String uuid, String rank) {
        Bootstrap.getInstance().getJedis().select(0);
        DBUser user = new DBUser(uuid, UUIDFetcher.getName(uuid));
        if (user.userExists()) {
            Bootstrap.getInstance().getJedis().hset("uuid:" + uuid, "rank", rank);

        }
    }

    public DBUser getDBUser(final String name) {
        for (DBUser dbusers : Bootstrap.getInstance().getRankManager().getDbusers()) {
            if (dbusers.getName().equalsIgnoreCase(name)) {
                return dbusers;
            }
        }
        return null;
    }


    /**
     * Set's a players's playerlist name if no 1.8 tablist is available.
     *
     * @param player
     */
    public void setScoreboardAlternative(Player player) {
        String uuid = UUIDFetcher.getUUID(player.getName()).toString();

        String s = RankManager.getRank(uuid);

        if (s.equalsIgnoreCase("superadmin".toLowerCase())) {
            player.setPlayerListName("§4Admin §7❘ §4" + player.getName());
            player.setCustomName("§4" + player.getName());
            player.setCustomNameVisible(true);
            return;
        } else if (s.equalsIgnoreCase("admin".toLowerCase())) {
            player.setPlayerListName("§4" + player.getName());
            player.setCustomName("§4" + player.getName());
            player.setCustomNameVisible(true);
            return;
        } else if (s.equalsIgnoreCase("dev".toLowerCase())) {
            player.setPlayerListName("§3" + player.getName());
            player.setCustomName("§3" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("sup".toLowerCase())) {
            player.setPlayerListName("§1" + player.getName());
            player.setCustomName("§1" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("architekt".toLowerCase())) {
            player.setPlayerListName("§2" + player.getName());
            player.setCustomName("§2" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("youtuber".toLowerCase())) {
            player.setPlayerListName("§5" + player.getName());
            player.setCustomName("§5" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("premium".toLowerCase())) {
            player.setPlayerListName("§6" + player.getName());
            player.setCustomName("§6" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("spieler".toLowerCase())) {
            player.setPlayerListName("§7" + player.getName());
            player.setCustomName("§7" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("mod".toLowerCase())) {
            player.setPlayerListName("§cMod §7❘ §c" + player.getName());
            player.setCustomName("§c" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("bauleitung".toLowerCase())) {
            player.setPlayerListName("§2" + player.getName());
            player.setCustomName("§2" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("teamleitung".toLowerCase())) {
            player.setPlayerListName("§c" + player.getName());
            player.setCustomName("§c" + player.getName());
            return;

        } else if (s.equalsIgnoreCase("partner".toLowerCase())) {
            player.setPlayerListName("§6" + player.getName());
            player.setCustomName("§6" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("inhaber".toLowerCase())) {
            player.setPlayerListName("§4" + player.getName());
            player.setCustomName("§4" + player.getName());
            return;
        }
    }

    public ArrayList<DBUser> getDbusers() {
        return dbusers;
    }
}
