package levelpoints.utils.utils;

import levelpoints.levelpoints.LevelPoints;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class API{
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
    private java.util.Date cDate = new java.util.Date();
    private String cDateS = format.format(cDate);
    private java.util.Date until = null;
    private Date current = null;
    private long daytime = (1000 * 60 * 60);
    private LevelPoints lps = LevelPoints.getPlugin(LevelPoints.class);


    private Player player;
    private Player target;
    private String msg;

    public API(Player player, String msg){
        this.msg = msg;
        this.player = player;
    }
    public API(){

    }

    public API(Player target, Player player, String msg){
        this.msg = msg;
        this.player = player;
        this.target = target;
    }

    public static String format(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public String formatTags(){
        UtilCollector uc = new UtilCollector();
        double percentage = uc.getCurrentEXP(player) * 100;
        File userdata = new File(lps.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        String Percentage = Math.round(percentage / uc.getRequiredEXP(player)) + "%";
        Date cDate = new Date();
        String cDateS = format.format(cDate);
        try {
            until = format.parse(UsersConfig.getString("BoosterOff"));
            current = format.parse(cDateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!current.after(until)) {
            long diff = until.getTime() - current.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (daytime);

            String timeleft = format(uc.getLangConfig().getString("lpsTimeFormat").replace("{DAYS}", String.valueOf(diffDays)).replace("{HOURS}", String.valueOf(diffHours)).replace("{MINUTES}", String.valueOf(diffMinutes)).replace("{SECONDS}", String.valueOf(diffSeconds)));

            msg = msg.replace("{lp_Booster_Active}", timeleft);

        }else{
            msg = msg.replace("{lp_Booster_Active}", uc.getLangConfig().getString("lpBoosterNone"));
        }
        msg = format(msg.replace("{lp_player}", player.getName())
                .replace("{lp_level}", String.valueOf(uc.getCurrentLevel(player)))
                .replace("{lp_exp}", String.valueOf(uc.getCurrentEXP(player)))
                .replace("{lp_Required_EXP}", String.valueOf(uc.getRequiredEXP(player)))
                .replace("{lp_progress}", Percentage)
                .replace("{lp_prestige}", String.valueOf(uc.getCurrentPrestige(player))))
                .replace("{lp_Progress_Bar}", uc.getProgressBar(player))
                .replace("{lp_Booster_Multiplier}", String.valueOf(UsersConfig.getInt("ActiveBooster")))
                .replace("{lp_Kills}", String.valueOf(UsersConfig.getInt("Kills")))
                .replace("{lp_Deaths}", String.valueOf(UsersConfig.getInt("Deaths")))
                .replace("{lp_Placed}", String.valueOf(UsersConfig.getInt("BlocksPlaced")))
                .replace("{lp_Broken}", String.valueOf(UsersConfig.getInt("BlocksBroken")));


        return msg;
    }



}
