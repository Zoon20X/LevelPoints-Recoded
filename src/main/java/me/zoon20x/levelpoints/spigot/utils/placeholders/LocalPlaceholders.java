package me.zoon20x.levelpoints.spigot.utils.placeholders;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.Bukkit;

public class LocalPlaceholders {

    public static String parse(String message, PlayerData data){
        return replaceText(message,0, data, 0, 0);
    }
    public static String parse(String message, int requiredLevel, PlayerData data){
        return replaceText(message,requiredLevel, data, 0,0);
    }
    public static String parse(String message, int level, String name, int topPosition, int topPage){
        return replaceText(message,level, name, topPosition, topPage);
    }

    public static String parse(String message, String blank, String name, int topPosition, int topPage){
        return replaceText(message,blank, name, topPosition, topPage);
    }

    private static String replaceText(String message, int requiredLevel, PlayerData data, int topPosition, int topPage){
        return message
                .replace("{lps_level}", String.valueOf(data.getLevel()))
                .replace("{lps_exp}", String.valueOf(data.getExp()))
                .replace("{lps_prestige}", String.valueOf(data.getPrestige()))
                .replace("{lps_required_exp}", String.valueOf(data.getRequiredEXP()))
                .replace("{lps_max}", String.valueOf(data.isMax()))
                .replace("{lps_required_level}", String.valueOf(requiredLevel))
                .replace("{lps_top_position}", String.valueOf(topPosition))
                .replace("{lps_top_page}", String.valueOf(topPage))
                .replace("{lps_top_page_max}", String.valueOf(LevelPoints.getInstance().getTopSettings().getMaxPages()))
                .replace("{player}", Bukkit.getPlayer(data.getUUID()).getName());
    }
    private static String replaceText(String message, int level, String name, int topPosition, int topPage){
            return message
                    .replace("{lps_level}", String.valueOf(level))
                    .replace("{lps_top_position}", String.valueOf(topPosition))
                    .replace("{lps_top_page}", String.valueOf(topPage))
                    .replace("{lps_top_page_max}", String.valueOf(LevelPoints.getInstance().getTopSettings().getMaxPages()))
                    .replace("{player}", name);
        }

    private static String replaceText(String message, String blank, String name, int topPosition, int topPage){
        return message
                .replace("{lps_level}", blank)
                .replace("{lps_top_position}", String.valueOf(topPosition))
                .replace("{lps_top_page}", String.valueOf(topPage))
                .replace("{lps_top_page_max}", String.valueOf(LevelPoints.getInstance().getTopSettings().getMaxPages()))
                .replace("{player}", name);
    }

}
