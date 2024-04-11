package me.zoon20x.spigot.levelpoints.containers.Levels;


import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.spigot.levelpoints.LevelPoints;
import net.objecthunter.exp4j.Expression;

import java.io.IOException;

public class LevelSettings {
    private StartingData startingData;
    private MaxData maxData;
    private ExperienceData experienceData;

    public LevelSettings(){
        load();
    }
    public void reload() throws IOException {
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLevelSettings();
        config.reload();
        load();
    }
    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLevelSettings();
        startingData = new StartingData(config.getInt("Starting.Level"), config.getInt("Starting.Prestige"), config.getDouble("Starting.Experience"));
        maxData = new MaxData(config.getInt("Max.Level"), config.getInt("Max.Prestige.Value"), config.getBoolean("Max.Prestige.Enabled"));
        experienceData = new ExperienceData(config.getString("Experience.Formula").replace("{lps_level}", "level"));
    }
    public StartingData getStartingData() {
        return startingData;
    }

    public MaxData getMaxData() {
        return maxData;
    }

    public ExperienceData getExperienceData() {
        return experienceData;
    }
}
