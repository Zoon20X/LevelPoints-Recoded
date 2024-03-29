package me.zoon20x.levelpoints.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String getServerVersion(){
        String reformat = Bukkit.getBukkitVersion().replace("-R0.1-SNAPSHOT", "");
        reformat = reformat.replace("-R0.2-SNAPSHOT", "");

        return reformat;
    }


    public static String levelPlaceholder = "{level}";
    public static String prestigePlaceholder = "{prestige}";
    public static String playerPlaceholder = "{player}";
    public static String requiredExpPlaceholder = "{required_exp}";
    public static String requiredLevelPlaceholder = "{required_level}";
    public static String expPlaceholder = "{exp}";
    public static String previousLevelPlaceholder = "{previous_level}";
    public static String progressPlaceholder = "{progress}";
    public static String rewardPlaceholder = "{reward}";
    public static String boosterPlaceholderId = "{booster_id}";
    public static String boosterPlaceholderMultiplier = "{booster_multiplier}";
    public static String boosterPlaceholderDate = "{booster_date}";
    public static String boosterPlaceholderDateTime = "{booster_date_time}";


    public static void sendActionBar(Player player, String msgs){
        final String[] msg = {msgs};
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
                Formatter formatter = new Formatter(data.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), 0, data.getProgress());
                if (LevelPoints.getInstance().getConfig().getBoolean("Actionbar.PlaceholderAPI")) {
                    msg[0] = PlaceholderAPI.setPlaceholders(player, msg[0]);
                }
                if (!Bukkit.getVersion().contains("1.8")) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(msg[0], formatter)));
                } else {
                    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

                    try {
                        Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                        Object p = c1.cast(player);
                        Object ppoc;
                        Class<?> c4 = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
                        Class<?> c5 = Class.forName("net.minecraft.server." + version + ".Packet");

                        Class<?> c2 = Class.forName("net.minecraft.server." + version + ".ChatComponentText");
                        Class<?> c3 = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");


                        Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(format(msg[0], formatter));
                        ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
                        Method getHandle = c1.getDeclaredMethod("getHandle");
                        Object handle = getHandle.invoke(p);

                        Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
                        Object playerConnection = fieldConnection.get(handle);

                        Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
                        sendPacket.invoke(playerConnection, ppoc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());
    }
    public static void sendTitle(Player player, String Title, String Subtitle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Bukkit.getVersion().contains("1.8")) {
                    player.sendTitle(Title, Subtitle, 10, 30, 10);
                }
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());

    }

    public static String getLevelColor(UUID uuid){
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(uuid);
        if(!LevelPoints.getInstance().getConfigSettings().isPlaceholderColorLevelEnabled()) {
            return FormatStatics.format(data.getLevel());
        }
        if(data.getLevelColor().equalsIgnoreCase("none")){
            return FormatStatics.format(data.getLevel());
        }

        return getColor(LevelPoints.getInstance().getLevelColorSettings().getColorData(data.getLevelColor()).getColor() + "" + FormatStatics.format(data.getLevel()));
    }

    public static List<String> formatRewardMessages(String x){
        return Arrays.asList(x.split(";"));
    }

    public static String format(String x, Formatter formatter){
        if(formatter.getBooster() !=null){
            Date boosterDate = formatter.getBooster().getDateExpire();
            Date current = new Date();
            String boosterExpire;
            String boosterExpireTime;

            if(current.after(boosterDate)){
                boosterExpire = "&cExpired&r";
                boosterExpireTime = "&cExpired&r";
            }else{
                boosterExpire = valueOf(boosterDate);
                boosterExpireTime = valueOf(formatDate(boosterDate));

            }

            x = x.replace(boosterPlaceholderId, valueOf(formatter.getBooster().getID()))
                    .replace(boosterPlaceholderMultiplier, valueOf(formatter.getBooster().getMultiplier()))
                    .replace(boosterPlaceholderDate, boosterExpire)
                    .replace(boosterPlaceholderDateTime, boosterExpireTime);
        }

        if(formatter.getPlayer() !=null){
            x = x.replace(levelPlaceholder, valueOf(formatter.getLevel()))
                    .replace(expPlaceholder, valueOf(formatter.getExp()))
                    .replace(requiredExpPlaceholder, valueOf(formatter.getRequiredExp()))
                    .replace(previousLevelPlaceholder, valueOf(formatter.getLevel() - 1))
                    .replace(requiredLevelPlaceholder, valueOf(formatter.getRequiredLevel()))
                    .replace(requiredExpPlaceholder, valueOf(formatter.getRequiredExp()))
                    .replace(prestigePlaceholder, valueOf(formatter.getPrestige()))
                    .replace(progressPlaceholder, valueOf(formatter.getProgress()) + "%")
                    .replace(playerPlaceholder, valueOf(formatter.getPlayer()));
            if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") !=null){
                OfflinePlayer player = Bukkit.getOfflinePlayer(formatter.getPlayer());
                x = PlaceholderAPI.setPlaceholders(player, x);
            }
            return x;
        }
        x = x.replace(levelPlaceholder, valueOf(formatter.getLevel()))
                .replace(expPlaceholder, valueOf(formatter.getExp()))
                .replace(previousLevelPlaceholder, valueOf(formatter.getLevel() - 1))
                .replace(requiredLevelPlaceholder, valueOf(formatter.getRequiredLevel()))
                .replace(requiredExpPlaceholder, valueOf(formatter.getRequiredExp()))
                .replace(prestigePlaceholder, valueOf(formatter.getPrestige()))
                .replace(progressPlaceholder, valueOf(formatter.getProgress()) + "%");

        return x;
    }

    public static String getColor(String msg){
        if(getServerVersion().contains("1.16") || getServerVersion().contains("1.17")){
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
    public static String valueOf(Object x){
        return String.valueOf(x);
    }

    public static String formatDate(Date date){


        long difference = date.getTime() - new Date().getTime();

        long differenceSeconds = difference / 1000 % 60;
        long differenceMinutes = difference / (60 * 1000) % 60;
        long differenceHours = difference / (60 * 60 * 1000) % 24;
        long differenceDays = difference / (24 * 60 * 60 * 1000);

        if(differenceDays >= 1){
            return differenceDays + " Day(s)";
        }
        if(differenceHours >= 1){
            return differenceHours + " Hour(s)";
        }
        if(differenceMinutes >= 1){
            return differenceMinutes + " Minute(s)";
        }
        return differenceSeconds + " Second(s)";
    }
}
