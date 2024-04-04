package me.zoon20x.devTools.spigot.player;

import me.zoon20x.levelpoints.API.PlayerAPI;
import me.zoon20x.levelpoints.LevelPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerInfo implements PlayerAPI {

    private UUID uuid;
    private int level;
    private double requiredEXP;
    private double exp;


    public PlayerInfo(UUID uuid){
        this.uuid = uuid;
        this.level = 1;
        this.exp = 0.0;
        this.requiredEXP = calculateRequiredEXP(this.level);
    }


    public void addEXP(double exp){
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
