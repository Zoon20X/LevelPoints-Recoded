package me.zoon20x.devTools.spigot.gui;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.Items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ExpGUI implements InventoryHolder {

    private Inventory inventory;



    public ExpGUI(){

    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(this, 9*3, LevelPoints.getInstance().getMessagesUtil().getColor("&2LevelPoints Exp"));
        setGUI();
        return inventory;
    }

    private void setGUI(){
        setEmpty();
        setBaseItems();
    }



    private void setEmpty(){
        for(int i=0; i<inventory.getSize(); i++){
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
    }

    private void setBaseItems(){
        inventory.setItem(1, getBlockSettings());
        inventory.setItem(4, getMobSettings());
        inventory.setItem(7, getFarmSettings());
        inventory.setItem(19, getCraftSettings());
    }


    public ItemStack getBlockSettings(){
        ItemStack item = new ItemBuilder(Material.NETHERITE_PICKAXE).setName("&7&lBlock Settings").build();
        return item;
    }
    public ItemStack getMobSettings(){
        ItemStack item = new ItemBuilder(Material.PARROT_SPAWN_EGG).setName("&c&lMob Settings").build();
        return item;
    }
    public ItemStack getFarmSettings(){
        ItemStack item = new ItemBuilder(Material.NETHERITE_HOE).setName("&b&lFarm Settings").build();
        return item;
    }
    public ItemStack getCraftSettings(){
        ItemStack item = new ItemBuilder(Material.CRAFTING_TABLE).setName("&e&lCraft Settings").build();
        return item;
    }
}
