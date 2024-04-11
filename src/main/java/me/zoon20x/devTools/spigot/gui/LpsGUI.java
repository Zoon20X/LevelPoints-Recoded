package me.zoon20x.devTools.spigot.gui;

import me.zoon20x.spigot.levelpoints.LevelPoints;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class LpsGUI implements InventoryHolder {

    private Inventory inventory;


    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(this, InventoryType.HOPPER, LevelPoints.getInstance().getMessagesUtil().getColor("&3&lLevelPoints Settings"));
        inventory.setItem(0, new ItemStack(Material.EXPERIENCE_BOTTLE));
        inventory.setItem(1, new ItemStack(Material.ENDER_CHEST));
        inventory.setItem(2, new ItemStack(Material.EMERALD));
        inventory.setItem(3, new ItemStack(Material.CROSSBOW));
        inventory.setItem(4, new ItemStack(Material.NETHER_STAR));
        return inventory;
    }
}
