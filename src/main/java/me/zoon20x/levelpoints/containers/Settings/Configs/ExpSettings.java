package me.zoon20x.levelpoints.containers.Settings.Configs;

import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockUtils;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobData;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobUtils;
import me.zoon20x.levelpoints.files.FilesStorage;
import me.zoon20x.levelpoints.utils.DataLocation;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

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
        if(BlockUtils.hasBlockData(x)){
            return "Block";
        }
        if(MobUtils.hasMob(x)){
            return "Mob";
        }
        return "none";
    }


    public Double getMobEXP(EntityType type){

        if(!MobUtils.hasMob(type)){
            return 0.0;
        }

        return MobUtils.getMobData(type).getExp();
    }
    public Boolean isBlockExpEnabled(){
        return (Boolean) values.get("BlockEnabled");
    }
    public Boolean isMobExpEnabled(){
        return (Boolean) values.get("MobEnabled");
    }



    public void generateBlocks(){
        FileConfiguration config = FilesStorage.getConfig("expConfig");
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
                minXp = config.getDouble(DataLocation.getMinEXP(x));
                maxXp = config.getDouble(DataLocation.getMaxEXP(x));
                BlockUtils.addBlock(Material.getMaterial(x), new BlockData(Material.getMaterial(x), (byte) 0, minXp, maxXp, RequiredBreak, RequiredPlace));
            }else {
                for (String xx : config.getStringList(DataLocation.BlockSettings + "." + x + ".Blocks")) {
                    RequiredBreak = config.getInt(DataLocation.getRequiredBreak(x));
                    RequiredPlace = config.getInt(DataLocation.getRequiredPlace(x));
                    minXp = config.getDouble(DataLocation.getMinEXP(x));
                    maxXp = config.getDouble(DataLocation.getMaxEXP(x));
                    BlockUtils.addBlock(Material.getMaterial(xx), new BlockData(Material.getMaterial(xx), (byte) 0, minXp, maxXp, RequiredBreak, RequiredPlace));
                }
            }

        }

    }
    public void generateMobs(){
        FileConfiguration config = FilesStorage.getConfig("expConfig");
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
                minXp = config.getDouble(DataLocation.getMinEXP(x));
                maxXp = config.getDouble(DataLocation.getMaxEXP(x));
                MobUtils.addMob(new MobData(EntityType.valueOf(x), minXp, maxXp, RequiredDamage));
            }else {
                for (String xx : config.getStringList(DataLocation.MobSettings + "." + x + ".Mobs")) {
                    RequiredDamage = config.getInt(DataLocation.getRequiredDamage(x));
                    minXp = config.getDouble(DataLocation.getMinEXP(x));
                    maxXp = config.getDouble(DataLocation.getMaxEXP(x));
                    MobUtils.addMob(new MobData(EntityType.valueOf(xx), minXp, maxXp, RequiredDamage));
                }
            }
        }
    }

}
