package de.fileinputstream.mytraz.worldmanagement.votegamble;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Alexander on 12.08.2020 01:39
 * © 2020 Alexander Fiedler
 */
public class CaseManager {

    public static File datafile = new File("plugins/WorldManagement", "cases.yml");
    public static FileConfiguration caseopeningdata = YamlConfiguration.loadConfiguration(datafile);
    public static ArrayList<Location> caseopening = new ArrayList();
    public static HashMap<Player, Long> caseopeningspieler = new HashMap();
    public static Queue<ItemStack> caseopeningitems = new LinkedList();
    public static Inventory caseopeninginv = null;

    public static void loadCaseOpeningData() {
        caseopening.clear();
        List chest = caseopeningdata.getStringList(".data");
        if (!chest.isEmpty()) {
            for (Object o : chest) {
                String id = o.toString();
                String[] locs = id.split(",");
                Location loc = new Location(Bukkit.getWorld(locs[0]), Double.valueOf(locs[1]).doubleValue(),
                        Double.valueOf(locs[2]).doubleValue(), Double.valueOf(locs[3]).doubleValue());
                if (loc != null) {
                    caseopening.add(loc);

                }
            }
        }
    }

    public static void saveCaseOpeningData() {
        List chest = caseopeningdata.getStringList(".data");
        chest.clear();
        for (Location loc : caseopening) {
            chest.add(loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ());
        }
        caseopeningdata.set(".data", chest);
        try {
            caseopeningdata.save(datafile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadCaseOpening() {


        ItemStack repairBook = addBookEnchantment(new ItemStack(Material.ENCHANTED_BOOK, 1), Enchantment.MENDING, 1);
        ItemMeta repairBook_im = repairBook.getItemMeta();
        repairBook_im.setDisplayName("Repair-Book x" + repairBook.getAmount());
        repairBook.setItemMeta(repairBook_im);

        ItemStack dyingTotem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta dyingTotem_meta = dyingTotem.getItemMeta();
        dyingTotem_meta.setDisplayName("Unsterblichkeitstotem  x" + dyingTotem.getAmount());
        dyingTotem.setItemMeta(dyingTotem_meta);

        ItemStack diamond = new ItemStack(Material.DIAMOND, 4);
        ItemMeta diamond_meta = diamond.getItemMeta();
        diamond_meta.setDisplayName("Diamant x" + diamond.getAmount());
        diamond.setItemMeta(diamond_meta);

        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 2);
        ItemMeta goldenApple_meta = goldenApple.getItemMeta();
        goldenApple_meta.setDisplayName("Goldapfel x" + diamond.getAmount());
        goldenApple.setItemMeta(goldenApple_meta);

        ItemStack bread = new ItemStack(Material.BREAD, 16);
        ItemMeta bread_im = bread.getItemMeta();
        bread_im.setDisplayName("Brot x" + bread.getAmount());
        bread.setItemMeta(bread_im);

        ItemStack diamond_chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemMeta diamond_chestplate_im = diamond_chestplate.getItemMeta();
        diamond_chestplate_im.setDisplayName("§aDiamantrüstung x" + diamond_chestplate.getAmount());
        diamond_chestplate.setItemMeta(diamond_chestplate_im);

        ItemStack diamond_helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemMeta diamond_helmet_im = diamond_helmet.getItemMeta();
        diamond_helmet_im.setDisplayName("§aDiamantrüstung x" + diamond_helmet.getAmount());
        diamond_helmet.setItemMeta(diamond_helmet_im);

        ItemStack diamond_leggings = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemMeta diamond_leggings_im = diamond_leggings.getItemMeta();
        diamond_leggings_im.setDisplayName("§aDiamantrüstung x" + diamond_leggings.getAmount());
        diamond_leggings.setItemMeta(diamond_leggings_im);

        ItemStack diamond_boots = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemMeta diamond_boots_im = diamond_boots.getItemMeta();
        diamond_boots_im.setDisplayName("§aDiamantrüstung x" + diamond_boots.getAmount());
        diamond_boots.setItemMeta(diamond_chestplate_im);

        ItemStack oakWood = new ItemStack(Material.DARK_OAK_WOOD, 32);
        ItemMeta oakWood_meta = oakWood.getItemMeta();
        oakWood_meta.setDisplayName("§cDunkles Eichenholz x" + oakWood.getAmount());
        oakWood.setItemMeta(oakWood_meta);

        ItemStack blaze = new ItemStack(Material.BLAZE_POWDER, 32);
        ItemMeta blaze_im = blaze.getItemMeta();
        blaze_im.setDisplayName("§bBlaze Powder x" + blaze.getAmount());
        blaze.setItemMeta(blaze_im);

        ItemStack beef = new ItemStack(Material.COOKED_BEEF, 32);
        ItemMeta beef_im = beef.getItemMeta();
        beef_im.setDisplayName("§aRindfleisch x" + beef.getAmount());

        ItemStack blaze1 = new ItemStack(Material.BLAZE_POWDER, 45);
        ItemMeta blaze1_im = blaze.getItemMeta();
        blaze1_im.setDisplayName("§bBlaze Powder x" + blaze1.getAmount());
        blaze1.setItemMeta(blaze1_im);

        ItemStack xp_bottle = new ItemStack(Material.EXPERIENCE_BOTTLE, 64);
        ItemMeta xp_bottle_im = xp_bottle.getItemMeta();
        xp_bottle_im.setDisplayName("§cXP Flaschen x" + xp_bottle.getAmount());
        xp_bottle.setItemMeta(xp_bottle_im);

        ItemStack golden_apple = new ItemStack(Material.GOLDEN_APPLE, 16);
        ItemMeta golden_apple_im = golden_apple.getItemMeta();
        golden_apple_im.setDisplayName("§eGoldener Apfel x" + golden_apple.getAmount());
        golden_apple.setItemMeta(golden_apple_im);

        ItemStack diamond_sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta diamond_sword_im = diamond_sword.getItemMeta();
        diamond_sword_im.setDisplayName("§cDiamantschwert x" + diamond_sword.getAmount());
        diamond_sword.setItemMeta(diamond_sword_im);


        ItemStack sapling = new ItemStack(Material.OAK_SAPLING, 16);
        ItemMeta sapling_im = sapling.getItemMeta();
        sapling_im.setDisplayName("§eEichensetzling x" + sapling.getAmount());
        sapling.setItemMeta(sapling_im);

        caseopeningitems.add(diamond_chestplate);
        caseopeningitems.add(sapling);
        caseopeningitems.add(repairBook);
        caseopeningitems.add(sapling);
        caseopeningitems.add(diamond_helmet);
        caseopeningitems.add(xp_bottle);
        caseopeningitems.add(blaze);
        caseopeningitems.add(sapling);
        caseopeningitems.add(diamond_leggings);
        caseopeningitems.add(beef);
        caseopeningitems.add(diamond_boots);
        caseopeningitems.add(xp_bottle);
        caseopeningitems.add(bread);
        caseopeningitems.add(sapling);
        caseopeningitems.add(blaze);
        caseopeningitems.add(diamond_leggings);
        caseopeningitems.add(oakWood);
        caseopeningitems.add(sapling);
        caseopeningitems.add(dyingTotem);
        caseopeningitems.add(bread);
        caseopeningitems.add(diamond_boots);
        caseopeningitems.add(xp_bottle);
        caseopeningitems.add(oakWood);
        caseopeningitems.add(sapling);
        caseopeningitems.add(repairBook);
        caseopeningitems.add(oakWood);
        caseopeningitems.add(golden_apple);
        caseopeningitems.add(xp_bottle);
        caseopeningitems.add(sapling);
        caseopeningitems.add(bread);
        caseopeningitems.add(blaze);
        caseopeningitems.add(diamond_leggings);
        caseopeningitems.add(oakWood);
        caseopeningitems.add(diamond_boots);
        caseopeningitems.add(xp_bottle);
        caseopeningitems.add(diamond_sword);
        caseopeningitems.add(blaze);
        caseopeningitems.add(blaze1);
        caseopeningitems.add(sapling);
        caseopeningitems.add(bread);
        caseopeningitems.add(sapling);
        caseopeninginv = Bukkit.createInventory(null, 45, "§cVote-Case");
        ItemStack platzhalter1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta platzhalter1_im = platzhalter1.getItemMeta();
        platzhalter1_im.setDisplayName("  ");
        platzhalter1.setItemMeta(platzhalter1_im);

        ItemStack platzhalter2 = new ItemStack(Material.HOPPER, 1);
        ItemMeta platzhalter2_im = platzhalter2.getItemMeta();
        platzhalter2_im.setDisplayName("§aGEWINN");
        platzhalter2.setItemMeta(platzhalter2_im);

        ItemStack platzhalter3 = new ItemStack(Material.IRON_BARS, 1);
        ItemMeta platzhalter3_im = platzhalter3.getItemMeta();
        platzhalter3_im.setDisplayName(" ");
        platzhalter3.setItemMeta(platzhalter3_im);

        ItemStack platzhalter5 = new ItemStack(Material.LEVER, 1);
        ItemMeta platzhalter5_im = platzhalter5.getItemMeta();
        platzhalter5_im.setDisplayName("§7Gewinn");
        platzhalter5.setItemMeta(platzhalter5_im);

        caseopeninginv.setItem(0, platzhalter1);
        caseopeninginv.setItem(1, platzhalter1);
        caseopeninginv.setItem(2, platzhalter1);
        caseopeninginv.setItem(3, platzhalter1);
        caseopeninginv.setItem(4, platzhalter1);
        caseopeninginv.setItem(5, platzhalter1);
        caseopeninginv.setItem(6, platzhalter1);
        caseopeninginv.setItem(7, platzhalter1);
        caseopeninginv.setItem(8, platzhalter1);

        caseopeninginv.setItem(9, platzhalter3);
        caseopeninginv.setItem(10, platzhalter3);
        caseopeninginv.setItem(11, platzhalter3);
        caseopeninginv.setItem(12, platzhalter3);
        caseopeninginv.setItem(13, platzhalter2);
        caseopeninginv.setItem(14, platzhalter3);
        caseopeninginv.setItem(15, platzhalter3);
        caseopeninginv.setItem(16, platzhalter3);
        caseopeninginv.setItem(17, platzhalter3);

        caseopeninginv.setItem(27, platzhalter3);
        caseopeninginv.setItem(28, platzhalter3);
        caseopeninginv.setItem(29, platzhalter3);
        caseopeninginv.setItem(30, platzhalter3);
        caseopeninginv.setItem(31, platzhalter5);
        caseopeninginv.setItem(32, platzhalter3);
        caseopeninginv.setItem(33, platzhalter3);
        caseopeninginv.setItem(34, platzhalter3);
        caseopeninginv.setItem(35, platzhalter3);

        caseopeninginv.setItem(36, platzhalter1);
        caseopeninginv.setItem(37, platzhalter1);
        caseopeninginv.setItem(38, platzhalter1);
        caseopeninginv.setItem(39, platzhalter1);
        caseopeninginv.setItem(40, platzhalter1);
        caseopeninginv.setItem(41, platzhalter1);
        caseopeninginv.setItem(42, platzhalter1);
        caseopeninginv.setItem(43, platzhalter1);
        caseopeninginv.setItem(44, platzhalter1);
    }

    public static ItemStack addBookEnchantment(ItemStack item, Enchantment enchantment, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return item;
    }
}
