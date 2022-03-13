package de.fileinputstream.mytraz.worldmanagement.votegamble;

/**
 * Created by Alexander on 12.08.2020 03:03
 * © 2020 Alexander Fiedler
 */

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.DyeColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CaseTimer {

    HashMap<Player, ItemStack> lastItem = new HashMap();

    public void run() {
        int startholder = 18;
        int endholder = 26;
        ItemStack is = CaseManager.caseopeningitems.element();
        CaseManager.caseopeningitems.remove(is);
        CaseManager.caseopeningitems.add(is);
        for (ItemStack items : CaseManager.caseopeningitems) {
            if (startholder <= endholder) {
                CaseManager.caseopeninginv.setItem(startholder, items);
                startholder++;
            }
        }
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (CaseManager.caseopeningspieler.containsKey(p)) {
                if ((CaseManager.caseopeningspieler.get(p)).longValue() - System.currentTimeMillis() > 0L) {
                    p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 10.0F, 1.0F);
                } else {
                    this.lastItem.put(p, CaseManager.caseopeninginv.getItem(22));
                    Inventory lastinv = Bukkit.createInventory(null, 45, "§cCaseOpening");
                    setLastInv(lastinv);
                    lastinv.setItem(22, this.lastItem.get(p));
                    p.closeInventory();
                    p.openInventory(lastinv);
                    CaseManager.caseopeningspieler.remove(p);
                    Bukkit.getScheduler().runTaskLater(Bootstrap.getInstance(), new Runnable() {
                        public void run() {
                            if ((p.getOpenInventory() != null) && (p.getOpenInventory().getTitle().equalsIgnoreCase("§cCaseOpening"))) {
                                p.closeInventory();
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0F, 1.0F);
                            CaseTimer.this.giveItems(p);
                        }
                    }, 40L);
                }
            }
        }
    }

    private void giveItems(Player p) {
        ItemStack gewinn = this.lastItem.get(p);
        ItemMeta gewinn_im = gewinn.getItemMeta();


        gewinn_im.setDisplayName(null);
        gewinn.setItemMeta(gewinn_im);
        p.sendMessage("§bMC-Survival.de §7» §7Du hast §c" + gewinn.getType().name() + " §7gewonnen!");
        p.getInventory().addItem(new ItemStack[]{gewinn});

    }

    public void setLastInv(Inventory caseopeninginv) {
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

        ItemStack platzhalter4 = new ItemStack(Material.LEGACY_GOLD_PLATE, 1);
        ItemMeta platzhalter4_im = platzhalter4.getItemMeta();
        platzhalter4_im.setDisplayName("§aGEWINN");
        platzhalter4.setItemMeta(platzhalter4_im);

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
        caseopeninginv.setItem(31, platzhalter4);
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
}
