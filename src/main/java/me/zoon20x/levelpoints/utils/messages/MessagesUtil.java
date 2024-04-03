package me.zoon20x.levelpoints.utils.messages;

import me.zoon20x.levelpoints.LevelPoints;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesUtil {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");


    public String getServerVersion(){
        String reformat = Bukkit.getBukkitVersion().replace("-R0.1-SNAPSHOT", "");
        reformat = reformat.replace("-R0.2-SNAPSHOT", "");
        return reformat;
    }
    public String formatServerVersion(){
        String fsrv = getServerVersion();
        String result = Optional.ofNullable(fsrv)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 2))
                .orElse(fsrv);
        return result;
    }


    public String getColor(String msg){
        return formatRGB(msg);
    }
    public String formatRGB(String msg){

        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()){
            String color = msg.substring(matcher.start(), matcher.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }



    public void sendActionBar(Player player, String msg){
        new BukkitRunnable() {
            @Override
            public void run() {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getColor(msg)));
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());
    }
    public void sendTitle(Player player, String Title, String Subtitle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                    player.sendTitle(Title, Subtitle, 10, 30, 10);

            }
        }.runTaskAsynchronously(LevelPoints.getInstance());
    }

    public String centreText(String msg){

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : msg.toCharArray()) {
            if (c == '§') previousCode = true;
            else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontInfo dFI = FontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ?
                        dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }


        return sb + msg;
    }
    public void log(String... messages) {
        Arrays.asList(messages).forEach(m->{
            LevelPoints.getInstance().getLogger().info(getColor(m));
        });


    }
    public void log(Player player,boolean centre ,String... messages) {
        if(centre) {
            Arrays.asList(messages).forEach(m -> {
                player.sendMessage(getColor(centreText(m)));
            });
            return;
        }
        Arrays.asList(messages).forEach(m -> {
            player.sendMessage(getColor(m));
        });
    }
}