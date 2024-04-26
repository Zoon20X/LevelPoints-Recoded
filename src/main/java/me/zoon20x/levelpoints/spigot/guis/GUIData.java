package me.zoon20x.levelpoints.spigot.guis;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.utils.Items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GUIData {

    private String name;
    private int size;
    private ItemStack blankSpace;
    private List<GUIItemData> guiItemDataList = new ArrayList<>();


    public GUIData(String name, int size, ItemStack blankSpace) {
        this.name = name;
        this.size = size;
        this.blankSpace = blankSpace;
    }

    public void loadGUIItemList(YamlDocument config){
       config.getSection("Items").getRoutesAsStrings(false).forEach(key->{
           ItemStack itemStack = new ItemBuilder(
                   Material.getMaterial(config.getString("Items." + key + ".Material")))
                   .setName(config.getString("Items." + key + ".Name")).build();
           GUIItemData guiItemData = new GUIItemData(itemStack, config.getInt("Items." + key + ".Slot"));
           guiItemDataList.add(guiItemData);
       });
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public ItemStack getBlankSpace() {
        return blankSpace;
    }

    public List<GUIItemData> getGuiItemDataList() {
        return guiItemDataList;
    }
}
