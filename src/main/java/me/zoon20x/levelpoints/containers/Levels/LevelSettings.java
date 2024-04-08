package me.zoon20x.levelpoints.containers.Levels;


import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.LevelPoints;
import net.objecthunter.exp4j.Expression;

public class LevelSettings {
    private final StartingData startingData;
    private final MaxData maxData;
    private final ExperienceData experienceData;

    public LevelSettings(){
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
