package levelpoints.levelpoints;


import levelpoints.Utils.AsyncEvents;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class LevelPointsExpansion extends PlaceholderExpansion {

    private int posTop = 0;


    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("LevelPoints") != null;
    }

    @Override
    public boolean register() {
        // Make sure "SomePlugin" is on the server
        if (!canRegister()) {
            return false;
        }


        if (LevelPoints.getInstance() == null) {
            return false;
        }

        /*
         * Since we override the register method, we need to call the super method to actually
         * register this hook
         */
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
        return "0.1.6";
    }

    @Override
    public String onRequest(OfflinePlayer players, String identifier) {
        if (players instanceof Player) {
            Player player = players.getPlayer();
            if (identifier.equals("player_level")) {
                return String.valueOf(AsyncEvents.getPlayerContainer(player).getLevel());

            }
            if (identifier.contains("Top")) {


                ArrayList<String> name = new ArrayList<>();
                ArrayList<Integer> level = new ArrayList<>();
                File TopFile = new File(LevelPoints.getInstance().getDataFolder(), "TopList.yml");
                FileConfiguration con = YamlConfiguration.loadConfiguration(TopFile);
                ConfigurationSection cf = con.getConfigurationSection("");
                cf.getValues(false)
                        .entrySet()
                        .stream()
                        .sorted((a1, a2) -> {
                            int points1 = ((MemorySection) a1.getValue()).getInt("Level");
                            int points2 = ((MemorySection) a2.getValue()).getInt("Level");
                            return points2 - points1;
                        }).limit(10).forEach(f -> {
                    posTop += 1;

                    int points = ((MemorySection) f.getValue()).getInt("Level");
                    String playername = ((MemorySection) f.getValue()).getString("Name");

                    name.add(playername);
                    level.add(points);


                });

                if (identifier.contains("Top_Name")) {

                    String value = identifier.replace("Top_Name_", "");
                    if (name.size() < Integer.parseInt(value)) {
                        return "";
                    } else {
                        return name.get(Integer.parseInt(value) - 1);
                    }
                }
                if (identifier.contains("Top_Level")) {
                    String value = identifier.replace("Top_Level_", "");
                    if (level.size() < Integer.parseInt(value)) {
                        return "";
                    } else {
                        return String.valueOf(level.get(Integer.parseInt(value) - 1));
                    }
                }


            }
            if (identifier.equals("exp_amount")) {
                return String.valueOf(AsyncEvents.getPlayerContainer(player).getEXP());
            }
            if (identifier.equals("exp_required")) {

                return String.valueOf(AsyncEvents.getPlayerContainer(player).getRequiredEXP());
            }

            if (identifier.equals("progress_bar")) {
                return AsyncEvents.getProgressBar(player);
            }
            if (identifier.equals("exp_progress")) {
                float percentage = (float) AsyncEvents.getPlayerContainer(player).getEXP();
                return String.valueOf(Math.round((percentage / AsyncEvents.getPlayerContainer(player).getRequiredEXP()) * 100));
            }
            if (identifier.equals("booster_active")) {

                return String.valueOf(AsyncEvents.getPlayerContainer(player).getMultiplier());
            }

            if (player == null) {
                return "";
            }

            if (identifier.equals("rank_multiplier")) {
                return String.valueOf(AsyncEvents.getRankMultiplier(player));
            }


            if (identifier.equals("prestige")) {
                return String.valueOf(AsyncEvents.getPlayerContainer(player).getPrestige());
            }

        }
        return null;
    }
}