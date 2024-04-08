package me.zoon20x.levelpoints.containers.Player;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.API.PlayerAPI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Levels.LevelSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class PlayerData implements PlayerAPI {
    private YamlDocument config;


    private UUID uuid;
    private int level;
    private int prestige;
    private double requiredEXP;
    private double exp;


    public PlayerData(UUID uuid, YamlDocument config){
        this.config = config;
        this.uuid = uuid;
        this.level = 1;
        this.prestige = 0;
        this.exp = 0.0;
        this.requiredEXP = calculateRequiredEXP(this.level);
    }


    public void setExp(double exp){
        this.exp = exp;
    }
    @Override
    public void setLevel(int level){
        this.level = level;
        calculateRequiredEXP(level);
    }
    @Override
    public void addExp(double exp){
        this.exp+=exp;
        if(this.exp >= this.requiredEXP){
            double remain = this.exp-this.requiredEXP;

            addLevel();
            addExp(remain);
        }
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
            if(!levelSettings.getMaxData().usePrestige()){
                this.exp = 0;
                return;
            }
            this.level = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getMaxData().getLevel();
            addPrestige();
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
        player.sendMessage(String.valueOf(this.level));
    }

    @Override
    public void addPrestige(){
        addPrestige(1);

    }
    @Override
    public void addPrestige(int prestige){
        int temp = this.prestige + prestige;
        if(temp >= LevelPoints.getInstance().getLpsSettings().getLevelSettings().getMaxData().getPrestige()){
            this.prestige = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getMaxData().getPrestige();
            this.exp = 0;
            return;
        }
        this.prestige = temp;
        setLevel(LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getLevel());
        this.exp = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getExp();
    }

    private double calculateRequiredEXP(int level){
        this.requiredEXP = LevelPoints.getInstance().getLpsSettings().getLevelSettings().getExperienceData().getExpression().setVariable("level", level).evaluate();
        return this.requiredEXP;
    }


    public void save(YamlDocument config) throws IOException {
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
    public void setPrestige(int prestige) {
        this.prestige = prestige;
        setLevel(LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getLevel());
    }




}
