package me.zoon20x.levelpoints.containers.Player;

import java.util.Date;

public class ActiveBooster {

    private String id;
    private Date dateExpire;
    private double multiplier;

    public ActiveBooster(String id, Date date, double multiplier){
        this.id = id;
        this.dateExpire = date;
        this.multiplier = multiplier;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public double getMultiplier() {
        Date date = new Date();
        if(date.after(dateExpire)){
            return 1.0;
        }
        return multiplier;
    }

    public String getID() {
        return id;
    }
}
