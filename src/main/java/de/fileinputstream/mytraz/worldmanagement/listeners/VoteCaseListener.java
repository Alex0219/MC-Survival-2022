package de.fileinputstream.mytraz.worldmanagement.listeners;

import de.fileinputstream.mytraz.worldmanagement.votegamble.CaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Alexander on 12.08.2020 01:36
 * © 2020 Alexander Fiedler
 */
public class VoteCaseListener implements Listener {

    @EventHandler
    public void on1(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.TRIPWIRE_HOOK && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6CaseOpening §8| §eNormalCase!")) {
                if (!CaseManager.caseopening.contains(e.getClickedBlock().getLocation())) {
                    CaseManager.caseopening.add(e.getClickedBlock().getLocation());
                    CaseManager.saveCaseOpeningData();
                    CaseManager.loadCaseOpeningData();
                    p.sendMessage("§bMC-Survival.de §7» §cVoteCase Chest §7wurde gesetzt.");
                    e.setCancelled(true);
                } else {
                    p.sendMessage("§bMC-Survival.de §7» §cVoteCase Chest existiert bereits!");
                }
            }
            if (CaseManager.caseopening.contains(e.getClickedBlock().getLocation())) {
                if (p.getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                    if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6Vote-Key")) {
                        if (!CaseManager.caseopeningspieler.containsKey(p)) {
                            CaseManager.caseopeningspieler.put(p, System.currentTimeMillis() + 6000L);
                            p.openInventory(CaseManager.caseopeninginv);
                            final ItemStack is = p.getInventory().getItemInHand();
                            is.setAmount(is.getAmount() - 1);
                            p.setItemInHand(is);
                            e.setCancelled(true);
                        } else {
                            e.setCancelled(true);
                            p.sendMessage("§bMC-Survival.de §7» §cDu bist bereits in der VoteCase Chest!");
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                    p.sendMessage("§bMC-Survival.de §7» §cDu kannst die Votekiste nur mit einem Votekey öffnen.");
                    p.sendMessage("§bMC-Survival.de §7» §cBrauchst du einen Schlüssel? §6§l/vote");
                }
            }
        }
    }

    @EventHandler
    public void on(final InventoryClickEvent e) {
        if (e.getWhoClicked().getWorld().getName().equalsIgnoreCase("world")) {
            if (e.getWhoClicked().getOpenInventory().getType() == InventoryType.CHEST)
                e.setCancelled(true);
        }

    }
}
