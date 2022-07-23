package me.zoon20x.levelpoints.containers.Player;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Settings.Boosters.BoosterData;
import me.zoon20x.levelpoints.containers.Settings.Configs.PvpBracketData;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PlayerData implements LpsPlayer{

    private final UUID uuid;
    private String name;
    private int level;
    private Double exp;
    private int prestige;
    private Double requiredExp;
    private int previousLevel;
    private boolean updateSQL;
    private PvpBracketData bracketData;
    private ActiveBooster activeBooster;
    private String levelColor;

    private HashMap<String, Integer> boosterStorage = new HashMap<>();



    public PlayerData(UUID uuid, String name, Integer level, Double exp, Double requiredExp, Integer prestige, Integer previousLevel, ActiveBooster activeBooster, String levelColor) {
        this.uuid = uuid;
        this.name = name;
        this.level = level;
        this.exp = exp;
        this.prestige = prestige;
        this.previousLevel = previousLevel;
        this.activeBooster = activeBooster;
        this.requiredExp = requiredExp;
        this.updateSQL = false;
        this.levelColor = levelColor;

        updateBrackets();
    }


    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getLevel() {
        return level;
    }
    @Override
    public void setLevel(int level) {
        this.level = level;
        setRequiredExp(LevelPoints.getInstance().getLevelSettings().getRequireExp(level));
        if(this.requiredExp <= this.exp){
            this.exp = 0.0;
        }
        updateBrackets();
        setLevelColor(LevelPoints.getInstance().getLevelColorSettings().getLevelColor(level));

    }
    @Override
    public void addLevel(int value, boolean removeEXP) {
        LevelPoints.getDebug(DebugSeverity.SEVER, requiredExp);
        if (LevelPoints.getInstance().getLevelSettings().getMaxLevel() == getLevel()) {
            exp = requiredExp;
            return;
        }
        if (removeEXP) {
            removeEXP(requiredExp);
        }
        previousLevel = level;
        level += value;
        requiredExp = LevelPoints.getInstance().getLevelSettings().getRequireExp(level);
        EventUtils.triggerLevelUpEvent(this.level, this);
        if (!canLevelUp()) {
            updateBrackets();
            return;
        } else {
            addLevel(1, removeEXP);
        }

    }
    @Override
    public boolean removeLevel(int level) {
        if(level > this.level){
            return false;
        }

        this.level -= level;
        setRequiredExp(LevelPoints.getInstance().getLevelSettings().getRequireExp(this.level));
        updateBrackets();
        setLevelColor(LevelPoints.getInstance().getLevelColorSettings().getLevelColor(level));

        return true;
    }
    @Override
    public double getExp() {
        return LevelPoints.round(exp, 2);
    }

    public void setExp(double exp) {
        this.exp = exp;
    }
    @Override
    public void addEXP(double exp) {
        Double xp = this.exp + exp;

        if(level >= LevelPoints.getInstance().getLevelSettings().getMaxLevel()){
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
    @Override
    public boolean removeEXP(double xp) {
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
    @Override
    public int getPrestige() {

        return prestige;
    }
    public void prestige(){
        this.addPrestige(1);
        this.setLevel(LevelPoints.getInstance().getLevelSettings().getStartingLevel());
        this.setExp(LevelPoints.getInstance().getLevelSettings().getStartingExp());
        return;
    }

    @Override
    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }
    @Override
    public void addPrestige(int value) {
        if(LevelPoints.getInstance().getLevelSettings().getMaxPrestige() <= value + this.prestige){
            return;
        }
        this.prestige+= value;
        EventUtils.triggerPrestigeUpEvent(this.prestige, this);
        return;
    }
    @Override
    public boolean canPrestige(){
        if(this.prestige == LevelPoints.getInstance().getLevelSettings().getMaxPrestige()){
            return false;
        }
        if(this.level != LevelPoints.getInstance().getLevelSettings().getMaxLevel()){
            return false;
        }
        double max = LevelPoints.getInstance().getLevelSettings().getRequireExp(LevelPoints.getInstance().getLevelSettings().getMaxLevel());

        if(this.exp != max){
            return false;
        }
        return true;
    }
    public boolean removePrestige(int prestige) {
        if(prestige > this.prestige){
            return false;
        }

        this.prestige -= prestige;
        setRequiredExp(LevelPoints.getInstance().getLevelSettings().getRequireExp(this.level));
        return true;
    }

    public void updateBrackets(){
        if(!LevelPoints.getInstance().getPvpSettings().isPvpBracketsEnabled()){
            return;
        }
        bracketData = null;
        LevelPoints.getInstance().getPvpSettings().getAllPvpBrackets().forEach(x->{
            String pl = String.valueOf(level);
            if(x.getLevelsIncluded().contains(pl)){
                bracketData = x;
                return;
            }
        });
    }


    @Override
    public Set<String> getAllBoosters(){
        return boosterStorage.keySet();
    }
    public HashMap<String, Integer> getBoosterStorage(){
        return boosterStorage;
    }
    @Override
    public void setActiveBooster(BoosterData data){
        activeBooster = new ActiveBooster(
                data.getId(),
                LevelPoints.getInstance().getPlayerGenerator().formatBoosterTime(data.getTime()),
                data.getMultiplier());
    }
    @Override
    public void addBooster(BoosterData data, int amount){
        boosterStorage.put(data.getId(), amount);
    }
    @Override
    public void removeBooster(BoosterData data){
        boosterStorage.remove(data.getId());
    }
    @Override
    public boolean hasBooster(String value){
        return boosterStorage.containsKey(value);
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public void updatePreviousLevel(Integer previousLevel) {
        this.previousLevel = previousLevel;
    }
    @Override
    public ActiveBooster getActiveBooster() {
        return activeBooster;
    }

    public double getRequiredExp() {
        return LevelPoints.round(requiredExp, 2);
    }
    @Override
    public double getRemainingExp() {
        double remain = getRequiredExp() - getExp();
        return LevelPoints.round(remain, 2);
    }
    public void setRequiredExp(double requiredExp) {
        this.requiredExp = requiredExp;
    }
    @Override
    public boolean canLevelUp(){
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

    @Nullable
    public PvpBracketData getBracketData() {
        return bracketData;
    }

    public String getLevelColor() {
        return levelColor;
    }
    public void setLevelColor(String color) {
        levelColor = color;
    }
}
