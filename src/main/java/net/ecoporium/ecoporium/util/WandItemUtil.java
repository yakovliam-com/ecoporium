package net.ecoporium.ecoporium.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

public class WandItemUtil {

    /**
     * Returns a wand item
     *
     * @return item
     */
    public static ItemStack createWandItem() {
        ItemStack itemStack = new ItemStack(Material.STICK, 1);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName("Ecoporium Wand");
        meta.setLore(Collections.emptyList());

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
