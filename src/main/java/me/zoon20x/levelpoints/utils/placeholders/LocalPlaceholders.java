package me.zoon20x.levelpoints.utils.placeholders;

import me.zoon20x.levelpoints.containers.Player.PlayerData;

public class LocalPlaceholders {

    public static String parse(String message, PlayerData data){
        return replaceText(message,0, data);
    }
    public static String parse(String message, int requiredLevel, PlayerData data){
        return replaceText(message,requiredLevel, data);
    }

    private static String replaceText(String message, int requiredLevel, PlayerData data){
        return message
                .replace("{lps_level}", String.valueOf(data.getLevel()))
                .replace("{lps_exp}", String.valueOf(data.getExp()))
                .replace("{lps_prestige}", String.valueOf(data.getPrestige()))
                .replace("{lps_required_exp}", String.valueOf(data.getRequiredEXP()))
                .replace("{lps_max}", String.valueOf(data.isMax()))
                .replace("{lps_required_level}", String.valueOf(requiredLevel));
    }


}
