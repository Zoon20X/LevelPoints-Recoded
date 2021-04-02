package me.zoon20x.levelpoints.containers.Player;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.utils.DebugSeverity;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String name;
    private Integer level;
    private Double exp;
    private Integer prestige;
    private Double requiredExp;
    private Integer previousLevel;
    private boolean updateSQL;


    private ActiveBooster activeBooster;


    private HashMap<UUID, HashMap<BoosterData, Integer>> boosters = new HashMap<>();


    public PlayerData(UUID uuid, String name, Integer level, Double exp, Double requiredExp, Integer prestige, Integer previousLevel, ActiveBooster activeBooster) {
        this.uuid = uuid;
        this.name = name;
        this.level = level;
        this.exp = exp;
        this.prestige = prestige;
        this.previousLevel = previousLevel;
        this.activeBooster = activeBooster;
        this.requiredExp = requiredExp;
        this.updateSQL = false;

    }


    public UUID getUUID() {
        return uuid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
        setRequiredExp(LevelPoints.getLevelSettings().getRequireExp(level));
    }
    public void addLevel(int value, boolean removeEXP) {
        LevelPoints.getDebug(DebugSeverity.SEVER, requiredExp);
        if (LevelPoints.getLevelSettings().getMaxLevel() == getLevel()) {
            exp = requiredExp;
            return;
        }
        if (removeEXP) {
            removeEXP(requiredExp);
        }
        previousLevel = level;
        level += value;
        requiredExp = LevelPoints.getLevelSettings().getRequireExp(level);
        EventUtils.triggerLevelUpEvent(this.level, this);
        if (!canLevelUp()) {
            return;
        } else {
            addLevel(1, removeEXP);
        }

    }
    public Boolean removeLevel(Integer level) {
        if(level > this.level){
            return false;
        }

        this.level -= level;
        setRequiredExp(LevelPoints.getLevelSettings().getRequireExp(this.level));
        return true;
    }

    public Double getExp() {
        return LevelPoints.round(exp, 2);
    }

    public void setExp(Double exp) {
        this.exp = exp;
    }
    public void addEXP(Double exp) {
        Double xp = this.exp + exp;

        if(level >= LevelPoints.getLevelSettings().getMaxLevel()){
            if(xp >= this.getRequiredExp()){
                this.exp = this.getRequiredExp();
                return;
            }
        }
        this.exp += exp;
        if(xp >= this.getRequiredExp()){

            addLevel(1, true);
            return;
        }
    }
    public Boolean removeEXP(Double xp) {
        if(xp > this.exp) {
            return false;
        }
        this.exp-=xp;
        return true;
    }
    public double getProgress(){
        double value = getExp();
        double valueRequired = getRequiredExp();

        return Math.round((value / valueRequired) * 100);
    }
    public Integer getPrestige() {

        return prestige;
    }
    public void prestige(){
        this.addPrestige(1);
        this.setLevel(1);
        this.setExp(0.0);
        return;
    }

    public void setPrestige(Integer prestige) {
        this.prestige = prestige;
    }
    public void addPrestige(int value) {
        if(LevelPoints.getLevelSettings().getMaxPrestige() <= value + this.prestige){
            return;
        }
        this.prestige+= value;
        EventUtils.triggerPrestigeUpEvent(this.prestige, this);
        return;
    }
    public boolean canPrestige(){
        if(this.prestige == LevelPoints.getLevelSettings().getMaxPrestige()){
            return false;
        }
        if(this.level != LevelPoints.getLevelSettings().getMaxLevel()){
            return false;
        }
        double max = LevelPoints.getLevelSettings().getRequireExp(LevelPoints.getLevelSettings().getMaxLevel());

        if(this.exp != max){
            return false;
        }
        return true;
    }
    public Boolean removePrestige(Integer prestige) {
        if(prestige > this.prestige){
            return false;
        }

        this.prestige -= prestige;
        setRequiredExp(LevelPoints.getLevelSettings().getRequireExp(this.level));
        return true;
    }
    public Integer getPreviousLevel() {
        return previousLevel;
    }

    public void updatePreviousLevel(Integer previousLevel) {
        this.previousLevel = previousLevel;
    }

    public ActiveBooster getActiveBooster() {
        return activeBooster;
    }

    public Double getRequiredExp() {
        return LevelPoints.round(requiredExp, 2);
    }

    public void setRequiredExp(Double requiredExp) {
        this.requiredExp = requiredExp;
    }
    public Boolean canLevelUp(){
        return getExp() >= getRequiredExp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isUpdateSQL() {
        return updateSQL;
    }

    public void setUpdateSQL(boolean updateSQL) {
        this.updateSQL = updateSQL;
    }
}
