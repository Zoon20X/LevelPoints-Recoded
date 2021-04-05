package me.zoon20x.levelpoints.containers.Settings.Crafting;

import org.bukkit.Material;

public class CraftingData {

    private Material material;

    private Integer craftingRequired;
    private Double exp;
    private Double maxExp;

    public CraftingData(Material material, Double exp, Double maxExp, Integer craftingRequired){
        this.material = material;
        this.exp = exp;
        this.maxExp = maxExp;
        this.craftingRequired = craftingRequired;

    }

    public Material getMaterial() {
        return material;
    }

    public Integer getCraftingRequired() {
        return craftingRequired;
    }

    public Double getExp() {
        return exp;
    }

    public Double getMaxExp() {
        return maxExp;
    }
}
