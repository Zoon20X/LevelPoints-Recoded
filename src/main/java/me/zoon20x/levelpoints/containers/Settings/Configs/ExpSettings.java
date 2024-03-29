package me.zoon20x.levelpoints.containers.Settings.Configs;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Breeding.BreedingData;
import me.zoon20x.levelpoints.containers.Breeding.BreedingUtils;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockUtils;
import me.zoon20x.levelpoints.containers.Settings.Crafting.CraftingData;
import me.zoon20x.levelpoints.containers.Settings.Crafting.CraftingUtils;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobData;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobUtils;
import me.zoon20x.levelpoints.utils.DataLocation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ExpSettings {

    private static HashMap<String, Object> values = new HashMap<>();

    public Double getBlockEXP(Material material, Byte x){

        if(!BlockUtils.hasBlockData(material)){
            return 0.0;
        }
        if(!BlockUtils.hasBlockExtraData(material, x)){
            return 0.0;
        }

        return BlockUtils.getBlockData(material, x).getExp();
    }

    public String expType(String x){
        if(BlockUtils.hasBlockData(x))
            return "Block";
        if(MobUtils.hasMob(x))
            return "Mob";
        if(CraftingUtils.hasItem(x))
            return "Crafting";

        return "none";
    }


    public Double getMobEXP(EntityType type){

        if(!MobUtils.hasMob(type)){
            return 0.0;
        }

        return MobUtils.getMobData(type).getExp();
    }
    public Double getBreedEXP(EntityType type){

        if(!BreedingUtils.hasBreed(type)){
            return 0.0;
        }

        return BreedingUtils.getBreedData(type).getExp();
    }
    public Boolean isBlockExpEnabled(){
        return (Boolean) values.get("BlockEnabled");
    }
    public Boolean isMobExpEnabled(){
        return (Boolean) values.get("MobEnabled");
    }
    public Boolean isCraftingExpEnabled(){
        return (Boolean) values.get("CraftingEnabled");
    }
    public Boolean isBreedingExpEnabled(){
        return (Boolean) values.get("BreedEnabled");
    }



    public void generateBlocks(){
        BlockUtils.clear();
        FileConfiguration config = LevelPoints.getInstance().getFilesGenerator().expSettings.getConfig();
        values.put("BlockEnabled", config.getBoolean(DataLocation.BlockEnabled));
        if (!config.getBoolean(DataLocation.BlockEnabled)) {
            return;
        }
        for(String x : config.getConfigurationSection(DataLocation.BlockSettings).getKeys(false)){
            boolean isGroup = config.getConfigurationSection(DataLocation.BlockSettings + "." + x).getKeys(false).contains("Blocks");
            Integer RequiredBreak;
            Integer RequiredPlace;
            Double minXp;
            Double maxXp;

            if(!isGroup){
                RequiredBreak = config.getInt(DataLocation.getRequiredBreak(x));
                RequiredPlace = config.getInt(DataLocation.getRequiredPlace(x));
                minXp = config.getDouble(DataLocation.getBlockMinEXP(x));
                maxXp = config.getDouble(DataLocation.getBlockMaxEXP(x));
                BlockUtils.addBlock(Material.getMaterial(x), new BlockData(Material.getMaterial(x), (byte) 0, minXp, maxXp, RequiredBreak, RequiredPlace));
            }else {
                for (String xx : config.getStringList(DataLocation.BlockSettings + "." + x + ".Blocks")) {
                    RequiredBreak = config.getInt(DataLocation.getRequiredBreak(x));
                    RequiredPlace = config.getInt(DataLocation.getRequiredPlace(x));
                    minXp = config.getDouble(DataLocation.getBlockMinEXP(x));
                    maxXp = config.getDouble(DataLocation.getBlockMaxEXP(x));
                    BlockUtils.addBlock(Material.getMaterial(xx), new BlockData(Material.getMaterial(xx), (byte) 0, minXp, maxXp, RequiredBreak, RequiredPlace));
                }
            }

        }

    }
    public void generateMobs(){
        MobUtils.clear();
        FileConfiguration config = LevelPoints.getInstance().getFilesGenerator().expSettings.getConfig();
        values.put("MobEnabled", config.getBoolean(DataLocation.MobEnabled));
        if (!config.getBoolean(DataLocation.MobEnabled)) {
            return;
        }
        for(String x : config.getConfigurationSection(DataLocation.MobSettings).getKeys(false)){
            boolean isGroup = config.getConfigurationSection(DataLocation.MobSettings + "." + x).getKeys(false).contains("Mobs");
            Integer RequiredDamage;
            Double minXp;
            Double maxXp;

            if(!isGroup){
                RequiredDamage = config.getInt(DataLocation.getRequiredDamage(x));
                minXp = config.getDouble(DataLocation.getMobsMinEXP(x));
                maxXp = config.getDouble(DataLocation.getMobsMaxEXP(x));
                MobUtils.addMob(new MobData(EntityType.valueOf(x), minXp, maxXp, RequiredDamage));
            }else {
                for (String xx : config.getStringList(DataLocation.MobSettings + "." + x + ".Mobs")) {
                    RequiredDamage = config.getInt(DataLocation.getRequiredDamage(x));
                    minXp = config.getDouble(DataLocation.getMobsMinEXP(x));
                    maxXp = config.getDouble(DataLocation.getMobsMaxEXP(x));
                    MobUtils.addMob(new MobData(EntityType.valueOf(xx), minXp, maxXp, RequiredDamage));
                }
            }
        }
    }
    public void generateBreeds(){
        BreedingUtils.clear();
        FileConfiguration config = LevelPoints.getInstance().getFilesGenerator().expSettings.getConfig();
        values.put("BreedEnabled", config.getBoolean(DataLocation.BreedEnabled));
        if (!config.getBoolean(DataLocation.BreedEnabled)) {
            return;
        }
        for(String x : config.getConfigurationSection(DataLocation.BreedSettings).getKeys(false)){
            boolean isGroup = config.getConfigurationSection(DataLocation.BreedSettings + "." + x).getKeys(false).contains("Mobs");
            Integer RequiredBreed;
            Double minXp;
            Double maxXp;

            if(!isGroup){
                RequiredBreed = config.getInt(DataLocation.getRequiredBreed(x));
                minXp = config.getDouble(DataLocation.getBreedMinEXP(x));
                maxXp = config.getDouble(DataLocation.getBreedMaxEXP(x));
                BreedingUtils.addBreed(new BreedingData(EntityType.valueOf(x), minXp, maxXp, RequiredBreed));
            }else {
                for (String xx : config.getStringList(DataLocation.BreedSettings + "." + x + ".Mobs")) {
                    RequiredBreed = config.getInt(DataLocation.getRequiredBreed(x));
                    minXp = config.getDouble(DataLocation.getBreedMinEXP(x));
                    maxXp = config.getDouble(DataLocation.getBreedMaxEXP(x));
                    BreedingUtils.addBreed(new BreedingData(EntityType.valueOf(xx), minXp, maxXp, RequiredBreed));
                }
            }
        }
    }
    public void generateCrafting(){
        CraftingUtils.clear();
        FileConfiguration config = LevelPoints.getInstance().getFilesGenerator().expSettings.getConfig();
        values.put("CraftingEnabled", config.getBoolean(DataLocation.CraftingEnabled));
        if (!config.getBoolean(DataLocation.CraftingEnabled)) {
            return;
        }
        for(String x : config.getConfigurationSection(DataLocation.CraftingSettings).getKeys(false)){
            boolean isGroup = config.getConfigurationSection(DataLocation.CraftingSettings + "." + x).getKeys(false).contains("Items");
            Integer requiredCraft;
            Double minXp;
            Double maxXp;

            if(!isGroup){
                requiredCraft = config.getInt(DataLocation.getRequiredCraft(x));
                minXp = config.getDouble(DataLocation.getCraftMinEXP(x));
                maxXp = config.getDouble(DataLocation.getCraftMaxEXP(x));
                CraftingUtils.addItem(new CraftingData(Material.getMaterial(x), minXp, maxXp, requiredCraft));
            }else {
                for (String xx : config.getStringList(DataLocation.CraftingSettings + "." + x + ".Items")) {
                    requiredCraft = config.getInt(DataLocation.getRequiredCraft(xx));
                    minXp = config.getDouble(DataLocation.getCraftMinEXP(xx));
                    maxXp = config.getDouble(DataLocation.getCraftMaxEXP(xx));
                    CraftingUtils.addItem(new CraftingData(Material.getMaterial(xx), minXp, maxXp, requiredCraft));
                }
            }
        }
    }


    public void startTimedEXP(){
        if(!LevelPoints.getInstance().getConfig().getBoolean("TimedEXP.Global.Enabled")){
            return;
        }
        double amount = LevelPoints.getInstance().getConfig().getDouble("TimedEXP.Global.Amount");
        int time = LevelPoints.getInstance().getConfig().getInt("TimedEXP.Global.Time");

        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player ->{
                    LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId()).addEXP(amount);
                });
            }
        }.runTaskTimerAsynchronously(LevelPoints.getInstance(), 0, time);
    }
}
