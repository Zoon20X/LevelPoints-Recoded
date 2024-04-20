package me.zoon20x.devTools.spigot.gui;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.Items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class LpsGUI implements InventoryHolder {

    private Inventory inventory;

    public LpsGUI(){

    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(this, 9*3, LevelPoints.getInstance().getMessagesUtil().getColor("&3&lLevelPoints Settings"));

        return inventory;
    }

    private void setGUI(String name){
        if(name.equalsIgnoreCase("Exp-Settings")){
            setEmpty();


            return;
        }
        setEmpty();
        setBaseItems();
    }



    private void setEmpty(){
        for(int i=0; i<inventory.getSize(); i++){
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
    }

    private void setBaseItems(){
        inventory.setItem(10, getExpSettings());
        inventory.setItem(13, new ItemStack(Material.EMERALD_BLOCK));
        inventory.setItem(16, new ItemStack(Material.MAP));
    }

    private void setExpItems(){
        inventory.setItem(1, getExpSettings());
        inventory.setItem(13, new ItemStack(Material.EMERALD_BLOCK));
        inventory.setItem(16, new ItemStack(Material.MAP));
    }


    public ItemStack getExpSettings(){
        ItemStack item = new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("&2&lExp-Based Settings").build();
        return item;
    }


}
