package me.zoon20x.levelpoints.containers.Settings.Boosters;

public class BoosterData {

    private String id;
    private double multiplier;
    private String time;


    public BoosterData(String id, double multiplier, String time){
        this.id = id;
        this.multiplier = multiplier;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getTime() {
        return time;
    }
}
