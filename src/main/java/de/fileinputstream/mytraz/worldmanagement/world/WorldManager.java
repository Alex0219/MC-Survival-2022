package de.fileinputstream.mytraz.worldmanagement.world;


import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Alexander<br/>
 * Date: 06.01.2018<br/>
 * Time: 14:37<br/>
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
public class WorldManager {

    public HashMap<String,
            String> worldInvites = new HashMap<String,
            String>();
    ExecutorService service = Executors.newCachedThreadPool();
    String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();

    public WorldManager() {

    }

    /**
     * Erstellt eine Welt für einen Benutzer. Hier wird überprüft, ob der Benutzer schon eine Welt hat.
     * Nach 30 Millisekunden wir der Spieler dann in seine Welt teleportiert.
     *
     * @param {@link DBUser} Der Benutzer in der Datenbank, der die Welt erhalten soll.
     */
    public void createWorld(String name, String uuid) {
        //WorldCreator wc = new WorldCreator()
        List<String> worlds = new ArrayList<String>();
        List<String> residentWorlds = new ArrayList<String>();

        String joinedResidentWorlds = Arrays.toString(residentWorlds.toArray());
        ArrayList<String> worldResidents = new ArrayList<String>();
        String worldID = Bootstrap.getInstance().getJedis().incr("survivalWorldIDCount").toString();
        if (!worlds.contains(worldID)) {
            worlds.add(worldID);
        }
        Bukkit.getPlayer(name).sendMessage("§aDeine Welt " + "§c" + worldID + " §awird nun erstellt. Wenn die Erstellung abgeschlossen ist, wirst du automatisch in deine Welt teleportiert.");
        String joinedWorld = Arrays.toString(worlds.toArray());
        AsyncWorld world = AsyncWorld.create(new WorldCreator(worldID));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "timestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "owner", name);
        Bootstrap.getInstance().getJedis().hset("uuid:" + uuid, "worlds", joinedWorld);
        Bootstrap.getInstance().getWorldManager().addResident(worldID, uuid);
        //Teleport the player to the spawn location
        Bukkit.getPlayer(name).teleport(Bukkit.getWorld(worldID).getSpawnLocation());
        Bukkit.getWorld(worldID).setAutoSave(true);
        Bukkit.getWorld(worldID).setSpawnLocation(Bukkit.getWorld(worldID).getSpawnLocation().getBlockX(), Bukkit.getWorld(worldID).getSpawnLocation().getBlockY() + 6, Bukkit.getWorld(worldID).getSpawnLocation().getBlockZ());
        Bukkit.getWorld(worldID).setKeepSpawnInMemory(false);
        Bukkit.getWorld(worldID).save();
        setWorldSpawnLocation(Bukkit.getWorld(worldID).getSpawnLocation(), worldID);
    }

    /**
     * @return {@link String} Gibt einen String zurück, der als WeltenID verwendet wird.
     */
    public String getNewWorldID() {
        return "#" + randomString(13);
    }

    //Random string for world id
    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    //Überprüft, ob die Welt existiert

    /**
     * @param worldID WeltenID
     * @return boolean
     */
    public boolean worldExists(String worldID) {

        return Bootstrap.getInstance().getJedis().exists("world:" + worldID);
    }

    //Weltenbewohner wird hinzugefügt

    /**
     * Fügt einen Spieler zu einer Welt hinzu, sofern er noch nicht eingetragen ist.
     *
     * @param worldID WeltenID
     * @param uuid    ID des Spielers
     */
    public void addResident(String worldID, String uuid) {

        if (worldExists(worldID)) {
            ArrayList<String> residents = getWorldResidents(worldID);
            if (!residents.contains(uuid)) {
                residents.add(uuid);
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(getWorldResidentsFile(worldID));
                cfg.set("Residents", residents);

                try {
                    cfg.save(getWorldResidentsFile(worldID));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Backend -> Added resident:" + uuid + " to world id:" + worldID);
        }
    }

    /**
     * Entfernt einen Spieler aus der Welt sofern er eingetragen ist
     *
     * @param worldID WeltenID
     * @param uuid    ID des Spielers
     */
    public void removeResident(String worldID, String uuid) {
        if (worldExists(worldID)) {
            ArrayList<String> residents = getWorldResidents(worldID);
            residents.remove(uuid);
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(getWorldResidentsFile(worldID));
            cfg.set("Residents", residents);
            try {
                cfg.save(getWorldResidentsFile(worldID));
                System.out.println("Backend -> Removed resident:" + uuid + " from world id:" + worldID);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @param worldID
     * @return {@link ArrayList} Liste mit den Bewohnern von der angegebenen WorldID
     */
    public ArrayList<String> getWorldResidents(String worldID) {

        if (worldExists(worldID)) {
            File file = new File("plugins/WorldManagement/worlds", worldID + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    getWorldResidents(worldID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            return (ArrayList<String>) cfg.getStringList("Residents");
        } else {
            System.out.println("Backend -> Could not find user world: " + worldID);
            return null;
        }

    }

    public boolean isResidentInWorld(String uuid, String world) {
        return getWorldResidents(world).contains(uuid);
    }

    public File getWorldResidentsFile(String world) {
        File file = new File("plugins/WorldManagement/worlds", world + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public File getPlayerWorldFile(String uuid) {
        File file = new File("plugins/WorldManagement/worlds", uuid + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Gibt die Welten des angegeben Users zurück.
     *
     * @param uuid
     * @return List<String>
     */
    public List<String> getResidentWorlds(String uuid) {
        File folder = new File("plugins/WorldManagement/worlds/");
        File[] files = folder.listFiles();
        List<String> worldsResidents = new ArrayList<>();
        for (File file : files) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            List<String> worlds = cfg.getStringList("Residents");
            if (worlds.contains(uuid)) {
                worldsResidents.add(file.getName().replace(".yml", ""));
            } else {
                continue;
            }

        }
        return worldsResidents;

    }

    /**
     * Gibt den Weltenbesitzer
     *
     * @param worldID
     * @return
     */
    public String getOwnerFromWorld(String worldID) {
        if (worldExists(worldID)) {
            String owner = Bootstrap.getInstance().getJedis().hget("world:" + worldID, "owner");
            return owner;
        }
        return "";
    }


    /**
     * @param uuid
     * @return {@link boolean} Überprüft, ob ein Spieler eine Welt besitzt.
     * Dabei ist es egal, wieviele Welten der User hat.
     */

    public boolean hasWorld(String uuid) {

        return Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "hasworld").equalsIgnoreCase("true");
    }

    public String getWorld(String uuid) {

        if (hasWorld(uuid)) {
            String worldString = Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "worlds");
            System.out.println(worldString);
            ArrayList<String> playerWorlds = new ArrayList(Arrays.asList(worldString));
            String world = playerWorlds.get(0).replace("[", "").replace("]", "");
            return world;
        }
        return "";
    }

    public void setWorldSpawnLocation(Location location, String worldID) {
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "spawnX", String.valueOf(location.getX()));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "spawnY", String.valueOf(location.getY()));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "spawnZ", String.valueOf(location.getZ()));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "spawnPitch", String.valueOf(location.getPitch()));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "spawnYaw", String.valueOf(location.getYaw()));
    }

    public Location getSpawnLocation(String worldID) {
        double x = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("world:" + worldID, "spawnX"));
        double y = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("world:" + worldID, "spawnY"));
        double z = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("world:" + worldID, "spawnZ"));
        double pitch = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("world:" + worldID, "spawnPitch"));
        double yaw = Double.parseDouble(Bootstrap.getInstance().getJedis().hget("world:" + worldID, "spawnYaw"));

        return new Location(Bukkit.getWorld(worldID), x, y, z, (float) yaw, (float) pitch);

    }


}