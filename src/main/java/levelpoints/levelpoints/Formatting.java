package levelpoints.levelpoints;

import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatting {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String basicColor(String msg){
        if(LevelPoints.getInstance().getConfig().getBoolean("1.16")){
            msg = formatRGB(msg);
        }else {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        }
        return msg;
    }


    public static String formatRGB(String msg){

        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()){
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String stripColor(String msg){
        return ChatColor.stripColor(msg);
    }
    public static Date formatDate(String x){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        int ticks = 0;
        long daytime = (1000 * 60 * 60);
        switch (formatTime(x)){
            case "Minutes":
                x = x.replace("m", "");

                ticks = Integer.parseInt(x) * (1000*60);
                break;
            case "Hours":
                x = x.replace("h", "");
                ticks = Integer.parseInt(x) * (1000*60*60);
                break;
            case "Days":
                x = x.replace("d", "");
                ticks = Integer.parseInt(x) * (1000*60*60*24);
                break;
            case "Seconds":
                x = x.replace("s", "");
                ticks = Integer.parseInt(x) * (1000);
                break;
        }


        Date cDate = new Date();

        Date tomorrow = new Date(cDate.getTime() + ticks);
        return tomorrow;
    }
    public static String formatTime(String value){
        if(value.contains("m")){
            return "Minutes";
        }
        if(value.contains("h")){
            return "Hours";
        }
        if(value.contains("d")){
            return "Days";
        }
        if(value.contains("s")){
            return "Seconds";
        }
        return "";
    }

    public static String formatInfoTags(Player player, String msg) {
        PlayerContainer playerContainer = AsyncEvents.getPlayerContainer(player);
        if (LevelPoints.getInstance().getConfig().getBoolean("Lang.PlaceholderAPI")) {
            msg = PlaceholderAPI.setPlaceholders(player, msg);
        }

        msg = msg.replace("{lp_player}", player.getName())
                .replace("{lp_level}", valueOf(playerContainer.getLevel()))
                .replace("{lp_exp}", valueOf(playerContainer.getEXP()))
                .replace("{lp_Required_EXP}", valueOf(playerContainer.getRequiredEXP()))
                .replace("{lp_progress}", getPercentage(player))
                .replace("{lp_Progress_Bar}", AsyncEvents.getProgressBar(player))
                .replace("{lp_prestige}", valueOf(playerContainer.getPrestige()))
                .replace("{Booster_Active}", valueOf(playerContainer.getMultiplier()))
                .replace("{Booster_Date}", valueOf(playerContainer.getBoosterDate()));
        msg = basicColor(msg);
        return msg;


    }
    public static String formatTopTags(String player, String msg, int pos, int level){
        msg = msg.replace("{lp_player}", player)
                .replace("{lp_level}", valueOf(level))
                .replace("{lp_Top_Position}", valueOf(pos));
        msg = basicColor(msg);
        return msg;
    }

    public static String formatInfoTags(OfflinePlayer player, String msg){

        if(LevelPoints.getInstance().getConfig().getBoolean("Lang.PlaceholderAPI")){
            msg = PlaceholderAPI.setPlaceholders(player, msg);
        }

        msg = msg.replace("{lp_player}", player.getName())
                .replace("{lp_level}", valueOf(AsyncEvents.getOfflineLevel(player.getName())))
                .replace("{lp_exp}", valueOf(AsyncEvents.getOfflineEXP(player.getName())))
                .replace("{lp_Required_EXP}", valueOf(AsyncEvents.getOfflineRequiredEXP(player.getName())))
                .replace("{lp_progress}", getPercentage(player))
                .replace("{lp_prestige}", valueOf(AsyncEvents.getOfflinePrestige(player.getName())));
        msg = basicColor(msg);
        return msg;


    }
    public static String getPercentage(Player player){
        PlayerContainer container = AsyncEvents.getPlayerContainer(player);
        double value = container.getEXP();
        double valueRequired = container.getRequiredEXP();

        return Math.round((value / valueRequired) * 100) + "%";
    }
    public static String getPercentage(OfflinePlayer player){
        double value = AsyncEvents.getOfflineEXP(player.getName());
        double valueRequired = AsyncEvents.getOfflineRequiredEXP(player.getName());

        return Math.round((value / valueRequired) * 100) + "%";
    }
    public static String valueOf(Object x){
        return String.valueOf(x);
    }
}
