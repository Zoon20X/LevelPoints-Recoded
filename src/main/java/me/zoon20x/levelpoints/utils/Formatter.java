package me.zoon20x.levelpoints.utils;

import lombok.Data;
import me.zoon20x.levelpoints.containers.Player.ActiveBooster;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Data
public class Formatter {

    private final String player;
    private final int level;
    private final double exp;
    private final double requiredExp;
    private final int prestige;


    private final int requiredLevel;
    private final double progress;
    private ActiveBooster booster;


    public Formatter(String player, int level, double exp, double requiredExp, int prestige, int requiredLevel, double progress){
        this.player = player;
        this.level = level;
        this.exp = exp;
        this.requiredExp = requiredExp;
        this.prestige = prestige;
        this.requiredLevel = requiredLevel;
        this.progress = progress;
    }
    public Formatter(String player, int level, double exp, double requiredExp, int prestige, int requiredLevel, double progress, ActiveBooster booster){
        this.player = player;
        this.level = level;
        this.exp = exp;
        this.requiredExp = requiredExp;
        this.prestige = prestige;
        this.requiredLevel = requiredLevel;
        this.progress = progress;
        this.booster = booster;
    }

}
