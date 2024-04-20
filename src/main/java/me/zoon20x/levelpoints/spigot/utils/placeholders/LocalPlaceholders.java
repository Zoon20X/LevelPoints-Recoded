package me.zoon20x.levelpoints.spigot.utils.placeholders;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.Bukkit;

public class LocalPlaceholders {

    public static String parse(String message, PlayerData data){
        return replaceText(message, data);
    }
    public static String parse(String message,String reward ,PlayerData data){
        return replaceText(message,reward, data);
    }
    public static String parse(String message, int requiredLevel, PlayerData data){
        return replaceText(message,requiredLevel, data);
    }
    public static String parse(String message, int level, String name, int topPosition, int topPage){
        return replaceText(message,level, name, topPosition, topPage);
    }

    public static String parse(String message, String blank, String name, int topPosition, int topPage){
        return replaceText(message,blank, name, topPosition, topPage);
    }

    private static String replaceText(String message, PlayerData data){
        return message
                .replace("{lps_level}", String.valueOf(data.getLevel()))
                .replace("{lps_exp}", String.valueOf(data.getExp()))
                .replace("{lps_prestige}", String.valueOf(data.getPrestige()))
                .replace("{lps_required_exp}", String.valueOf(data.getRequiredEXP()))
                .replace("{lps_max}", String.valueOf(data.isMax()))
                .replace("{lps_progress_bar}", ProgressStatics.makeProgressBar(LevelPoints.getInstance().getLang().getBarSettings(), data.getExp(), data.getRequiredEXP()))
                .replace("{player}", data.getName());
    }
    private static String replaceText(String message, String reward, PlayerData data){
        return replaceText(message, data)
                .replace("{reward}", reward);
    }
    private static String replaceText(String message, int requiredLevel, PlayerData data){
        return replaceText(message, data)
                .replace("{lps_required_level}", String.valueOf(requiredLevel));
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
