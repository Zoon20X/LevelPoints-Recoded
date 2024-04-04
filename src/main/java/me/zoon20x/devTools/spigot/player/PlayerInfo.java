package me.zoon20x.devTools.spigot.player;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.API.PlayerAPI;
import me.zoon20x.levelpoints.LevelPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class PlayerInfo implements PlayerAPI {

    private UUID uuid;
    private int level;
    private int prestige;
    private double requiredEXP;
    private double exp;


    public PlayerInfo(UUID uuid){
        this.uuid = uuid;
        this.level = 1;
        this.exp = 0.0;
        this.requiredEXP = calculateRequiredEXP(this.level);
    }

    public void setExp(double exp){
        this.exp = exp;
    }
    public void setLevel(int level){
        this.level = level;
        calculateRequiredEXP(level);
    }

    public void addExp(double exp){
        this.exp+=exp;
        if(this.exp >= this.requiredEXP){
            this.exp-=this.requiredEXP;
            addLevel();
        }
    }
    public void addLevel(){
        addLevel(1);
    }
    public void addLevel(int level){
        this.level +=level;
        this.requiredEXP = calculateRequiredEXP(this.level);
        if(Bukkit.getPlayer(uuid) == null){
            return;
        }
        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage(String.valueOf(this.level));
    }

    private double calculateRequiredEXP(int level){
        this.requiredEXP = LevelPoints.getInstance().getExpression().setVariable("level", level).evaluate();
        return this.requiredEXP;
    }

    public void save(YamlDocument config) throws IOException {
        config.set("Level", this.level);
        config.set("Exp", this.exp);
        config.save();
    }



    public UUID getUUID(){
        return uuid;
    }
    public int getLevel(){
        return level;
    }
    public double getExp(){
        return exp;
    }
    public double getRequiredEXP(){
        return requiredEXP;
    }
}
