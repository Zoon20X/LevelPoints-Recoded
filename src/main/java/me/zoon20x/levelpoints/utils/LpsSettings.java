package me.zoon20x.levelpoints.utils;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.containers.Levels.LevelSettings;
import me.zoon20x.levelpoints.containers.Mobs.MobSettings;

public class LpsSettings {
    private final LevelSettings levelSettings;

    private final BlockSettings blockSettings;
    private final MobSettings mobSettings;

    public LpsSettings(LevelPoints levelPoints){
        levelSettings = new LevelSettings();
        blockSettings = new BlockSettings(levelPoints.getConfigUtils().getBlockSettingsConfig().getBoolean("Blocks.Enabled"));
        mobSettings = new MobSettings(levelPoints.getConfigUtils().getMobSettingsConfig().getBoolean("Mobs.Enabled"));
    }


    public BlockSettings getBlockSettings() {
        return blockSettings;
    }

    public MobSettings getMobSettings() {
        return mobSettings;
    }

    public LevelSettings getLevelSettings() {
        return levelSettings;
    }
}
