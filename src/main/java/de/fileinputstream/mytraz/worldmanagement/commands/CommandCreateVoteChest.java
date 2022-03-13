package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Alexander on 12.08.2020 02:45
 * © 2020 Alexander Fiedler
 */
public class CommandCreateVoteChest implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
                final Player p = (Player)sender;

                String uuid = p.getUniqueId().toString();
                String rank = RankManager.getRank(uuid);
                if (rank.equalsIgnoreCase("admin") ) {

                    final ItemStack schlüssel = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                    final ItemMeta schlüssel_im = schlüssel.getItemMeta();
                    schlüssel_im.setDisplayName("§6Vote-Key");
                    schlüssel.setItemMeta(schlüssel_im);
                    final ItemStack kiste = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                    final ItemMeta kiste_im = kiste.getItemMeta();
                    kiste_im.setDisplayName("§6CaseOpening §8| §eNormalCase!");
                    kiste.setItemMeta(kiste_im);
                    p.getInventory().addItem(new ItemStack[] { schlüssel });
                    p.getInventory().addItem(new ItemStack[] { kiste });
                    p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 10.0f, 1.0f);
                }
                else {
                    p.sendMessage("§cYou do not have permission to execute this command!");
                }

        }
        else {
            return true;
        }
        return false;
    }
}
