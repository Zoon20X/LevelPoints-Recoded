package me.zoon20x.levelpoints.spigot.API;

import me.zoon20x.levelpoints.spigot.LevelPoints;

public class LevelPointsAPI {


    private final ConfigAPI configAPI;
    private final BlockSettingsAPI blockSettingsAPI;
    private final MobSettingsAPI mobSettingsAPI;

    public LevelPointsAPI(){
        configAPI = LevelPoints.getInstance().getConfigUtils();
        blockSettingsAPI = LevelPoints.getInstance().getLpsSettings().getBlockSettings();
        mobSettingsAPI = LevelPoints.getInstance().getLpsSettings().getMobSettings();

    }


    public ConfigAPI getConfigAPI(){
        return configAPI;
    }


    public BlockSettingsAPI getBlockSettingsAPI() {
        return blockSettingsAPI;
    }

    public MobSettingsAPI getMobSettingsAPI() {
        return mobSettingsAPI;
    }
}
