package levelpoints.Containers;

public class BoostersContainer {


    private double multiplier;
    private String time;
    private int id;
    private int amount;

    public BoostersContainer(double multiplier, String time, int id, int amount){
        this.multiplier = multiplier;
        this.time = time;
        this.id = id;
        this.amount = amount;
    }
    public double getMultiplier(){
        return multiplier;
    }
    public int getId(){
        return id;
    }
    public int getAmount(){
        return amount;
    }
    public void setAmount(int amount){
        System.out.println(amount);
        this.amount = amount;
    }


    public String getTime() {
        return time;
    }
}
