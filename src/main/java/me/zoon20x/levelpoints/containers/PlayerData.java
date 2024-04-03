package me.zoon20x.levelpoints.containers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private int level;
    private double exp;


    public PlayerData(UUID uuid){
        this.uuid = uuid;
        this.level = 1;
        this.exp = 0.0;
    }


    public void addEXP(double exp){
        this.exp+=exp;
        if(this.exp >= level*50){
            this.exp-=level*50;
            addLevel(1);
        }
    }
    public void addLevel(int level){
        this.level +=level;
        if(Bukkit.getPlayer(uuid) == null){
            return;
        }
        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage(String.valueOf(this.level));
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





}
