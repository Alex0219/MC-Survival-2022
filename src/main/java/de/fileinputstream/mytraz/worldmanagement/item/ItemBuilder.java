package de.fileinputstream.mytraz.worldmanagement.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by Alexander on 15.08.2020
 * © 2020 Alexander Fiedler
 **/
public class ItemBuilder {

    Material material;
    String displayName;
    List<String> lores;

    public ItemBuilder(Material material, String displayName) {
        this.material = material;
        this.displayName = displayName;
    }

    public ItemBuilder(Material material, String displayName, List<String> lores) {
        this.material = material;
        this.displayName = displayName;
        this.lores = lores;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(this.material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(this.displayName);
        if (lores != null) {
            itemMeta.setLore(this.lores);
        }
        item.setItemMeta(itemMeta);

        this.displayName = null;
        this.material = null;
        this.lores = null;
        return item;
    }
}
