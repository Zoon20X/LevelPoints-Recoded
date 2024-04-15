package me.zoon20x.levelpoints.spigot.utils;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Levels.LevelSettings;
import me.zoon20x.levelpoints.spigot.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.spigot.containers.Mobs.MobSettings;

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