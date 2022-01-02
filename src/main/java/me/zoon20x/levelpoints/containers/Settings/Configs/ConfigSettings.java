package me.zoon20x.levelpoints.containers.Settings.Configs;

import lombok.Getter;
import me.zoon20x.levelpoints.LevelPoints;

public class ConfigSettings {

    private Boolean sql;

    @Getter
    private boolean onExpEnabled;
    @Getter
    private String onExpMessage;

    private boolean placeholderColorLevelEnabled;


    public ConfigSettings(){
        this.onExpEnabled = LevelPoints.getInstance().getConfig().getBoolean("Actionbar.OnExp.Enabled");
        this.onExpMessage = LevelPoints.getInstance().getConfig().getString("Actionbar.OnExp.Message");
        this.placeholderColorLevelEnabled = LevelPoints.getInstance().getConfig().getBoolean("PlaceholderSettings.LevelColors.Enabled");
    }

    public boolean isPlaceholderColorLevelEnabled() {
        return placeholderColorLevelEnabled;
    }
}
