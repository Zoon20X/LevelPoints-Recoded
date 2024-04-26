package me.zoon20x.levelpoints.spigot.guis;

import org.bukkit.inventory.ItemStack;

public class GUIItemData {

    private ItemStack item;
    private int slot;

    public GUIItemData(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
