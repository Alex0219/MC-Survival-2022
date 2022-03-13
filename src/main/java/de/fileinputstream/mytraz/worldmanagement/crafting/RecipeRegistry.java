package de.fileinputstream.mytraz.worldmanagement.crafting;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Created by Alexander on 07.05.2021
 * © 2021 Alexander Fiedler
 **/
public class RecipeRegistry {

    public void registerRecipes() {

        final ItemStack opGoldenAppleItemStack = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

        ShapedRecipe opGoldenApple = new ShapedRecipe(opGoldenAppleItemStack);

        opGoldenApple.shape("***","*B*","***");

        opGoldenApple.setIngredient('*', Material.GOLD_BLOCK);
        opGoldenApple.setIngredient('B', Material.APPLE);

        Bootstrap.getInstance().getServer().addRecipe(opGoldenApple);
    }
}
