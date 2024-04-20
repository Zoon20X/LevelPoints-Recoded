package me.zoon20x.levelpoints.spigot.containers.Player;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.API.PlayerAPI;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Levels.LevelSettings;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class PlayerData implements PlayerAPI {
    private YamlDocument config;

    private String name;
    private UUID uuid;
    private int level;
    private int prestige;
    private double requiredEXP;
    private double exp;
    private boolean isMax;

    public PlayerData(UUID uuid,String name, YamlDocument config){
        this.config = config;
        this.name = name;
        this.uuid = uuid;
        this.level = 1;
        this.prestige = 0;
        this.exp = 0.0;
        this.requiredEXP = calculateRequiredEXP(this.level);
        this.isMax = false;
    }


    public void setExp(double exp){
        this.exp = exp;
    }
    @Override
    public void setLevel(int level){
        this.exp = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getExp();
        this.level = level;
        calculateRequiredEXP(level);
    }
    @Override
    public void addExp(double exp){
        this.exp+=exp;
        if(this.exp >= this.requiredEXP) {
            double remain = this.exp - this.requiredEXP;

            addLevel();
            if (!isMax()) {
                addExp(remain);
            }
        }
    }

    public boolean removeExp(double exp){
        if(this.exp <= exp){
            return false;
        }
        this.exp -= exp;
        return true;
    }
    @Override
    public void addLevel(){
        addLevel(1);
    }
    @Override
    public void addLevel(int level){
        int temp = this.level +level;
        LevelSettings levelSettings = LevelPoints.getInstance().getLpsSettings().getLevelSettings();
        if(temp >= levelSettings.getMaxData().getLevel()){
            if(levelSettings.getMaxData().usePrestige()){
                addPrestige();
            }
            this.exp = 0;
            return;
        }
        this.level = temp;
        this.exp = 0;
        this.requiredEXP = calculateRequiredEXP(this.level);
        if(Bukkit.getPlayer(uuid) == null){
            return;
        }
        Player player = Bukkit.getPlayer(uuid);
        LevelPoints.getInstance().getEventUtils().triggerLevelUpEvent(player, this);
    }
    public boolean removeLevel(int level){
        if(this.level <= level){
            return false;
        }
        this.level -= level;
        return true;
    }

    @Override
    public void addPrestige(){
        addPrestige(1);

    }
    public boolean removePrestige(int prestige){
        if(this.prestige < prestige){
            return false;
        }
        this.prestige -= prestige;
        return true;
    }
    @Override
    public void addPrestige(int prestige){
        int temp = this.prestige + prestige;
        if(temp >= LevelPoints.getInstance().getLpsSettings().getLevelSettings().getMaxData().getPrestige()){
            this.prestige = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getMaxData().getPrestige();
            return;
        }
        this.prestige = temp;
        this.level = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getLevel();
        this.exp = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getExp();
        if(Bukkit.getPlayer(uuid) == null){
            return;
        }
        Player player = Bukkit.getPlayer(uuid);
        LevelPoints.getInstance().getEventUtils().triggerPrestigeEvent(player, this);

    }

    private double calculateRequiredEXP(int level){
        this.requiredEXP = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getExperienceData().getExpression().setVariable("level", level).evaluate();
        return this.requiredEXP;
    }


    public void save() throws IOException {
        config.set("Level", this.level);
        config.set("Prestige", this.prestige);
        config.set("Exp.Amount", this.exp);

        config.save();
    }

    public YamlDocument getConfig(){
        return config;
    }

    public UUID getUUID(){
        return uuid;
    }
    @Override
    public int getLevel(){
        return level;
    }
    @Override
    public double getExp(){
        return exp;
    }
    @Override
    public double getRequiredEXP(){
        return requiredEXP;
    }

    @Override
    public int getPrestige() {
        return prestige;
    }
    @Override
    public void setPrestige(int prestige, boolean resetLevel) {
        this.prestige = prestige;
        if (resetLevel) {
            setLevel(LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getLevel());
        }
    }


    @Override
    public boolean isMax() {
        LevelSettings levelSettings = LevelPoints.getInstance().getLpsSettings().getLevelSettings();
        isMax = this.level >= levelSettings.getMaxData().getLevel() && this.prestige >= levelSettings.getMaxData().getPrestige();
        return isMax;
    }

    public String getName() {
        return name;
    }
}
