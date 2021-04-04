package me.zoon20x.levelpoints.utils;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class LpsExpansion extends PlaceholderExpansion {
    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("LevelPoints") != null;
    }
    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }
        if (LevelPoints.getInstance() == null) {
            return false;
        }
        return super.register();
    }
    @Override
    public String getAuthor() {
        return "Zoon20X";
    }
    @Override
    public String getIdentifier() {
        return "LevelPoints";
    }
    @Override
    public String getRequiredPlugin() {
        return "LevelPoints";
    }
    @Override
    public String getVersion() {
        return "2.0";
    }
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if(LevelPoints.getInstance().isReloading()){
            return "Reloading";
        }


        UUID uuid = player.getUniqueId();
        if(!LevelPoints.getPlayerStorage().hasPlayerFile(uuid)){
            return "";
        }
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(uuid);
        if(identifier.equals("player_level")){
            String color = MessageUtils.getLevelColor(data.getLevel());
            return MessageUtils.getColor(color + "" + data.getLevel());
        }
//        if(identifier.contains("required_place_")){
//            String id = identifier.replace("required_place_", "");
//            List<String> dat = Arrays.asList(id.split(":"));
//
//            if(!BlockUtils.hasBlockData(Material.getMaterial(dat.get(0)))){
//                return "0";
//            }
//            LevelPoints.getDebug(DebugSeverity.SEVER, dat.size());
//            if(dat.size() == 2){
//                return String.valueOf(BlockUtils.getBlockData(Material.getMaterial(dat.get(0)), Byte.valueOf(dat.get(1))).getPlaceRequired());
//
//            }
//            return String.valueOf(BlockUtils.getBlockData(Material.getMaterial(id), (byte) 0).getPlaceRequired());
//        }
        if(identifier.equals("player_exp")){
            return String.valueOf(data.getExp());
        }
        if(identifier.equals("player_required_exp")){
            return String.valueOf(data.getRequiredExp());
        }
        if(identifier.equals("player_prestige")){
            return String.valueOf(data.getPrestige());
        }
        return "";
    }
}