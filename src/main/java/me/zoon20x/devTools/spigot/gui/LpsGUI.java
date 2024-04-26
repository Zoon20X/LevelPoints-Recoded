package me.zoon20x.devTools.spigot.gui;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.guis.GUIData;
import me.zoon20x.levelpoints.spigot.utils.Items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class LpsGUI implements InventoryHolder {

    private String title;
    private int size;
    private Inventory inventory;

    private GUIData guiData;

    public LpsGUI(GUIData guiData){
        this.guiData = guiData;
        title = guiData.getName();
        size = guiData.getSize();
    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(this, size, LevelPoints.getInstance().getMessagesUtil().getColor(title));
        setGUI();
        return inventory;
    }

    private void setGUI(){
        setEmpty();
        setBaseItems();
    }



    private void setEmpty(){
        for(int i=0; i<inventory.getSize(); i++){
            inventory.setItem(i, guiData.getBlankSpace());
        }
    }

    private void setBaseItems() {
        guiData.getGuiItemDataList().forEach(item -> {
            inventory.setItem(item.getSlot(), item.getItem());
        });
    }


}
