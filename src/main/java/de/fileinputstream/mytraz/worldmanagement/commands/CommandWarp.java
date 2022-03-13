package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandWarp implements CommandExecutor {

    private static ArrayList<String> warps = new ArrayList<>();
    private static File path = new File("plugins//WorldManagement//Warps");
    private static File[] files = path.listFiles();


    public final ExecutorService service = Executors.newCachedThreadPool();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) {

        } else {
            Player player = (Player) sender;
            if (args.length == 0) {

                service.execute(() -> {

                    List<String> results = new ArrayList<String>();


                    File[] files = new File("plugins//WorldManagement//Warps").listFiles();
                    //If this pathname does not denote a directory, then listFiles() returns null.

                    for (File file : files) {
                        if (file.isFile()) {
                            results.add(file.getName());
                        }
                    }


                    String separator = "§7,"; // separator here is your ","
                    player.sendMessage("§cWarp §7Es gibt Momentan folgende Warppunkte: : §6" + Stream.of(results.toString()).collect(Collectors.joining(", ")).replace("[", "").replace("]", ""));
                });


            } else if (args.length == 1) {
                File path = new File("plugins//WorldManagement//Warps//" + args[0]);
                if (path.exists()) {
                    FileConfiguration cfg = YamlConfiguration.loadConfiguration(path);

                    double x = cfg.getDouble("X");
                    double y = cfg.getDouble("Y");
                    double z = cfg.getDouble("Z");
                    float yaw = (float) cfg.getDouble("Yaw");
                    float pitch = (float) cfg.getDouble("Pitch");
                    World world = Bukkit.getWorld(cfg.getString("World"));

                    Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage("§cWarp §4Dieser Warp existiert nicht!");
                }
            }
        }
        return false;
    }
}