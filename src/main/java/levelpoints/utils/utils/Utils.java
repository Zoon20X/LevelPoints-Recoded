package levelpoints.utils.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public interface Utils {


    void ActionBar(Player player, String Message);

    void PlayerAdd(UUID uuid, String Name);
    void PlayerDataLoad(Player player) throws IOException;
    void wait(int seconds, Player player);

    void RunSQLUpdate(Player player);
    void RunSQLDownload(Player player);
    void SQLReconnect();
    void SQLDisconnect(int seconds);
    void LevelUpEventTrigger(Player player, int level, boolean EXPOverlap, int EXPOverlapAmount);

    void FarmEventTrigger(Player player, String FarmedItem, int expAmount, String Task);

    void RunModuels();
    void SaveLoadFiles(File file, FileConfiguration config, String Location, String secLoc, String Name);
    void MySQL();
    void versionChecker();

    void Rewards(Player player, int Level, int Prestige);
    void getRewards(String cmd, Player player);
    void Title(Player player, String Title, String Subtitle);
    void GainEXP(Player player, int amount);
    int getRequiredEXP(Player player);
    int getMaxLevel();
    int getCurrentLevel(Player player);
    int getCurrentPrestige(Player player);
    int getCurrentEXP(Player player);
    int getCurrentBoosters(Player player, int Multipler);
    void boosteruseclick(Player player, int multiplier) throws IOException;
    void RunFiles();
    void TimedEXP();
    double getMaxLevelEXP(Player player);
    FileConfiguration getLevelsConfig();
    FileConfiguration getEXPConfig();
    FileConfiguration getRewardsConfig();
    FileConfiguration getFormatsConfig();
    FileConfiguration getLangConfig();
    FileConfiguration getTopListConfig();






}
