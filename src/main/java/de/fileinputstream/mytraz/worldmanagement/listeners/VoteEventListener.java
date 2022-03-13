package de.fileinputstream.mytraz.worldmanagement.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankEnum;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Alexander on 08.08.2020
 * © 2020 Alexander Fiedler
 **/
public class VoteEventListener implements Listener {

    @EventHandler
    public void onVote(final VotifierEvent event) {
        final Vote vote = event.getVote();
        DBUser dbuser = Bootstrap.getInstance().getRankManager().getDBUser(event.getVote().getUsername());
        System.out.println(vote.getUsername());
        System.out.println(vote.getAddress());
        if (dbuser != null) {
            Bukkit.broadcastMessage("§bMC-Survival.de §7» §c" + vote.getUsername() + " §7hat gevotet! Vote jetzt auch und erhalte einen §cVote-Key§7! §7/vote");
            dbuser.addVote();
        }
        if (Bukkit.getPlayer(vote.getUsername()) != null) {
            final Player player = Bukkit.getPlayer(vote.getUsername());
            if (dbuser.getRank() == RankEnum.STAMMSPIELER || dbuser.getRank() == RankEnum.VIP) {
                final ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK, 2);
                final ItemMeta keyim = key.getItemMeta();
                keyim.setDisplayName("§6Vote-Key");
                key.setItemMeta(keyim);
                Bukkit.getPlayer(vote.getUsername()).getInventory().addItem(key);
                Bukkit.getPlayer(vote.getUsername()).sendMessage("§bMC-Survival.de §7» §7Danke für deinen §cVote§7! Du hast §72 §cVote-Keys §7erhalten!");
            } else {
                final ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK, 1);
                final ItemMeta keyim = key.getItemMeta();
                keyim.setDisplayName("§6Vote-Key");
                key.setItemMeta(keyim);
                Bukkit.getPlayer(vote.getUsername()).getInventory().addItem(key);
                Bukkit.getPlayer(vote.getUsername()).sendMessage("§bMC-Survival.de §7» §7Danke für deinen §cVote§7! Du hast einen §cVote-Key §7erhalten!");
            }

        }

    }
}
