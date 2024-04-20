package me.zoon20x.levelpoints.spigot.utils.Items;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {


    private Material material;
    private String name;
    public ItemBuilder(Material material){
        this.material = material;
    }

    public ItemBuilder setName(String name){
        this.name = LevelPoints.getInstance().getMessagesUtil().getColor(name);
        return this;
    }


    public ItemStack build(){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


}
