package me.zoon20x.levelpoints.containers.Settings.Configs;

import me.zoon20x.levelpoints.LevelPoints;

public class ConfigSettings {

    private Boolean sql;

    private boolean onExpEnabled;
    private String onExpMessage;

    public ConfigSettings(){
        this.onExpEnabled = LevelPoints.getInstance().getConfig().getBoolean("Actionbar.OnExp.Enabled");
        this.onExpMessage = LevelPoints.getInstance().getConfig().getString("Actionbar.OnExp.Message");


    }


    public String getOnExpMessage(){
        return onExpMessage;
    }
    public boolean getOnExpEnabled(){
        return onExpEnabled;
    }



}
