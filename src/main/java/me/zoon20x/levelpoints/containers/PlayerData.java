package me.zoon20x.levelpoints.containers;

import me.zoon20x.levelpoints.API.PlayerAPI;
import me.zoon20x.levelpoints.LevelPoints;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData implements PlayerAPI {

    private UUID uuid;
    private int level;
    private double requiredEXP;
    private double exp;





    public PlayerData(UUID uuid){
        this.uuid = uuid;
        this.level = 1;
        this.exp = 0.0;
    }


    public void addEXP(double exp){
        this.exp+=exp;
        if(this.exp >= this.requiredEXP){
            this.exp-=this.requiredEXP;
            addLevel(1);
        }
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
        JexlContext context = new MapContext();
        context.set("level", level);
        this.requiredEXP = (double) LevelPoints.getInstance().getExpression().evaluate(context);
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
