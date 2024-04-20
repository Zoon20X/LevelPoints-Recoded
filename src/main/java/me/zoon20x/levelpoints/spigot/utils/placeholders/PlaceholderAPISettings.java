package me.zoon20x.levelpoints.spigot.utils.placeholders;

import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Blocks.BlockData;
import me.zoon20x.levelpoints.spigot.containers.Farming.FarmData;
import me.zoon20x.levelpoints.spigot.containers.Mobs.MobData;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPISettings extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "levelpoints";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Zoon20X";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player,String params) {

        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())){
            return null;
        }
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId());
        if (params.equalsIgnoreCase("level")) {
            return String.valueOf(data.getLevel());
        }
        if (params.equalsIgnoreCase("exp")) {
            return String.valueOf(data.getExp());
        }
        if (params.equalsIgnoreCase("prestige")) {
            return String.valueOf(data.getPrestige());
        }
        if (params.equalsIgnoreCase("required_exp")) {
            return String.valueOf(data.getRequiredEXP());
        }
        if (params.equalsIgnoreCase("remaining_exp")) {
            return String.valueOf(data.getRequiredEXP() - data.getExp());
        }
        if (params.equalsIgnoreCase("remaining_level")) {
            return String.valueOf(LevelPoints.getInstance().getLpsSettings().getLevelSettings().getMaxData().getLevel() - data.getLevel());
        }

        if (params.contains("required_level_")) {
            String value = params.replace("required_level_", "");
            String[] requiredData = params.split("_");
            switch (requiredData[0]){
                case "farm":
                    String farmType = requiredData[1];
                    if(LevelPoints.getInstance().getLpsSettings().getFarmSettings().hasFarm(Material.getMaterial(farmType))){
                        FarmData farmData = LevelPoints.getInstance().getLpsSettings().getFarmSettings().getFarmData(Material.getMaterial(farmType));
                        return String.valueOf(farmData.getFarmRequired());
                    }
                    break;
                case "block":
                    String blockType = requiredData[1];
                    if(LevelPoints.getInstance().getLpsSettings().getBlockSettings().hasBlock(Material.getMaterial(blockType))){
                        BlockData blockData = LevelPoints.getInstance().getLpsSettings().getBlockSettings().getBlockData(Material.getMaterial(blockType));
                        switch (requiredData[2]){
                            case "break":
                                return String.valueOf(blockData.getBreakLevelRequired());
                            case "place":
                                return String.valueOf(blockData.getPlaceLevelRequired());
                        }
                    }
                    break;
                case "mob":
                    String mobType = requiredData[1];
                    if(LevelPoints.getInstance().getLpsSettings().getMobSettings().hasMob(mobType)){
                        MobData mobData = LevelPoints.getInstance().getLpsSettings().getMobSettings().getMobData(mobType);
                        return String.valueOf(mobData.getAttackRequired());
                    }
                    break;
            }
        }
        return null;
    }

}
