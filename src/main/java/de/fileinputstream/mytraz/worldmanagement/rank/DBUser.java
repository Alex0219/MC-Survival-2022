package de.fileinputstream.mytraz.worldmanagement.rank;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Alexander<br/>
 * Date: 04.02.2018<br/>
 * Time: 17:36<br/>
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
public class DBUser {
    /**
     * Diese Klasse verwaltet den @DBUser, auch bekannt als Spieler.
     * Sämtliche Abfragen werden durch diese Klasse verwaltet.
     * Auch der Spieler wird in dieser Klasse erstellt.
     * Auch der {@link Player} wird in dieser Klasse instanziert.
     */

    String uuid;
    String name;
    RankEnum rank = null;
    Player player;

    public DBUser(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.rank = RankEnum.getRankByName(Bootstrap.getInstance().getJedis().hget("uuid:" + getUuid(), "rank"));
        if (Bukkit.getPlayer(name) != null) {
            this.player = Bukkit.getPlayer(uuid);
        }
        String language = Bootstrap.getInstance().getJedis().hget("uuid:" + getUuid(), "language");


    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean userExists() {
        try {
            return Bootstrap.getInstance().getJedis().exists("uuid:" + getUuid());
        } catch (java.lang.ClassCastException exception) {
            Bootstrap.getInstance().getRedisConnector().connectToRedis("127.0.0.1", 6379);
            Bootstrap.getInstance().jedis = Bootstrap.getInstance().getRedisConnector().getJedis();
        } catch (Exception exception) {
            Bootstrap.getInstance().getRedisConnector().connectToRedis("127.0.0.1", 6379);
            Bootstrap.getInstance().jedis = Bootstrap.getInstance().getRedisConnector().getJedis();
        }
        return true;
    }

    public void createUser() {

        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "name", getName());
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "registertimestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "logins", "1");
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "rank", "spieler");
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "banned", "false");
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "muted", "false");
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "hasworld", "false");
        System.out.println("Backend -> Created user with uuid:" + uuid);

    }

    /**
     * If the player object is not null(which means that the player is online) this method will return the player object.
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Updates the online time of a user by 1L(1 minute)
     *
     * @param time
     */
    public void updateOntime(long time) {
        final long currentOntime = Long.parseLong(Bootstrap.getInstance().getJedis().hget("uuid:" + getUuid(), "ontime"));
        final long ontimeNow = currentOntime + 1L;
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "ontime", String.valueOf(ontimeNow));
    }

    public Integer getVotes() {
        return Integer.valueOf(Bootstrap.getInstance().getJedis().hget("uuid:" + getUuid(), "votes"));
    }

    public Integer getStoredEXP() {
        return Integer.valueOf(Bootstrap.getInstance().getJedis().hget("uuid:" + getUuid(), "storedexp"));
    }

    public void setEXP(int exp) {
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "storedexp", String.valueOf(exp));
    }

    public void addVote() {
        int currentVotes = getVotes();
        int newVotes = currentVotes + 1;
        Bootstrap.getInstance().getJedis().hset("uuid:" + getUuid(), "votes", String.valueOf(newVotes));

    }


    public String getOnlinetime() {
        int minutes = Integer.parseInt(Bootstrap.getInstance().getJedis().hget("uuid:" + getUuid(), "ontime"));

        long hours = 0L;
        long days = 0L;
        long weeks = 0L;

        while (minutes > 60L) {
            minutes -= 60L;
            ++hours;
        }
        while (hours > 23L) {
            hours -= 23L;
            ++days;
        }
        while (days > 6L) {
            days -= 6L;
            ++weeks;
        }
        while (weeks > 7L) {
            days -= 7L;
        }

        return "§c" + days + " §7Tag(e) §c" + hours + " §7Stunde(n)";
    }

    public RankEnum getRank() {
        return rank;
    }

    public void setRank(RankEnum rank) {
        this.rank = rank;
    }

}

