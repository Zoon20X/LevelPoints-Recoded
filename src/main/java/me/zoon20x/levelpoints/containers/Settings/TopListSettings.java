package me.zoon20x.levelpoints.containers.Settings;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class TopListSettings {

    private FileConfiguration config;
    private static HashMap<UUID, Integer> topCache = new HashMap<>();
    private int topChecked;

    public TopListSettings() {
        File userFile = new File(LevelPoints.getInstance().getDataFolder(), "TopList.yml");
        config = YamlConfiguration.loadConfiguration(userFile);
        generateTopCache(50);
    }

    public void modifyLevel(PlayerData data){
        File userFile = new File(LevelPoints.getInstance().getDataFolder(), "TopList.yml");
        config.set(data.getUUID() + ".Name", data.getName());
        config.set(data.getUUID() + ".Level", data.getLevel());

        try {
            config.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void generateTopCache(int topAmountSaved){
        topChecked = 0;
        topCache.clear();
        config.getConfigurationSection("").getValues(false)
                .entrySet()
                .stream().sorted((a1, a2)->{
            int points1 = ((MemorySection)a1.getValue()).getInt("Level");
            int points2 = ((MemorySection)a2.getValue()).getInt("Level");
            return points2 - points1;
        }).limit(topAmountSaved).forEach(f->{
            topChecked++;
            int level = ((MemorySection) f.getValue()).getInt("Level");
            topCache.put(UUID.fromString(f.getKey()), level);

        });
    }
    public void sendTopList(Player player){
        topChecked = 0;
        for(String x : LevelPoints.getLangSettings().getTopMessageTopText()){
            player.sendMessage(MessageUtils.getColor(x));
        }
        topCache.keySet().stream().sorted((a1, a2)->{
            int points1 = topCache.get(a1);
            int points2 = topCache.get(a2);
            return points2 - points1;
        }).limit(10).forEach(f->{
            topChecked++;
            for(String x : LevelPoints.getLangSettings().getTopMessageMiddleText()){
                Formatter formatter = new Formatter(Bukkit.getOfflinePlayer(f).getName(), topCache.get(f),0, 0,0,0,0);
                player.sendMessage(MessageUtils.getColor(MessageUtils.format(x.replace("{top_position}", String.valueOf(topChecked)), formatter)));
            }

        });
        for(String x : LevelPoints.getLangSettings().getTopMessageBottomText()){
            player.sendMessage(MessageUtils.getColor(x));
        }
    }


}
