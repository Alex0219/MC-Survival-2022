package de.fileinputstream.mytraz.worldmanagement.tracker;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * User: Alexander<br/>
 * Date: 26.02.2018<br/>
 * Time: 19:51<br/>
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
public class OntimeTracker {

    public OntimeTracker() {

    }

    public void startCounter() {
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Bootstrap.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap.getInstance().getJedis().set("timeoutkey", "yes");
                } catch (JedisConnectionException exception) {
                    Bootstrap.getInstance().getRedisConnector().connectToRedis("127.0.0.1", 6379);
                    Bootstrap.getInstance().jedis = Bootstrap.getInstance().getRedisConnector().getJedis();
                    System.out.println("Reconnected to redis!");
                } catch (java.lang.ClassCastException exception) {
                    Bootstrap.getInstance().getRedisConnector().connectToRedis("127.0.0.1", 6379);
                    Bootstrap.getInstance().jedis = Bootstrap.getInstance().getRedisConnector().getJedis();
                } catch (Exception exception) {
                    Bootstrap.getInstance().getRedisConnector().connectToRedis("127.0.0.1", 6379);
                    Bootstrap.getInstance().jedis = Bootstrap.getInstance().getRedisConnector().getJedis();
                }
                for (final Player all : Bukkit.getOnlinePlayers()) {
                    if (!Bootstrap.getInstance().getAfkPlayers().contains(all)) {
                        try {
                            DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(all.getName());
                            final long currentOntime = Long.parseLong(Bootstrap.getInstance().getJedis().hget("uuid:" + all.getUniqueId().toString(), "ontime"));
                            if (dbUser.getRank().getId() == 0 && currentOntime > 2864) {
                                //set new rank if ontime is more than 4 days
                                RankManager.setRank(all.getUniqueId().toString(), "stammspieler");
                                dbUser.setRank(RankEnum.STAMMSPIELER);
                                //update tablist

                                Bukkit.getOnlinePlayers().forEach(players -> NameTags.setTags(players));
                            }

                            final long ontimeNow = currentOntime + 1;
                            if (!Bootstrap.getInstance().getJedis().isConnected()) {
                                Bootstrap.getInstance().getJedis().connect();
                            }
                            Bootstrap.getInstance().getJedis().hset("uuid:" + UUIDFetcher.getUUID(all.getName()), "ontime", String.valueOf(ontimeNow));
                        } catch (final Exception ex) {
                        }
                    }
                }
            }
        }, 60 * 20, 60 * 20);
    }


}
