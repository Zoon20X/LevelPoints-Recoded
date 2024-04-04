package me.zoon20x.levelpoints.API;

import me.zoon20x.levelpoints.LevelPoints;

public class LevelPointsAPI {


    private ConfigAPI configAPI;

    public LevelPointsAPI(){
        configAPI = LevelPoints.getInstance().getConfigUtils();
    }


    public ConfigAPI getConfigAPI(){
        return configAPI;
    }



}
