package me.zoon20x.levelpoints.containers.Player;

import me.zoon20x.levelpoints.containers.Settings.Boosters.BoosterData;

import java.util.Set;
import java.util.UUID;

public interface LpsPlayer {


    /*Gets the UUID of the player*/
    UUID getUUID();

    /*used to get the level of the player*/
    int getLevel();

    /*sets the players level*/
    void setLevel(int level);

    /*adds the level to the player, This will trigger
    * the LEVELUP event within lps*/
    void addLevel(int value, boolean removeEXP);

    /*method to check if the player can levelup*/
    boolean canLevelUp();

    /*Removes a specified amount of levels from a player*/
    boolean removeLevel(int level);

    /*gets the xp of the player*/
    double getExp();

    /*method to add exp to the player*/
    void addEXP(double exp);

    /*removes exp from a player, CAN GO BELOW 0*/
    boolean removeEXP(double xp);

    /*gets the remaining exp from the player*/
    double getRemainingExp();

    /*gets the players prestige*/
    int getPrestige();

    /*sets the players prestige*/
    void setPrestige(int prestige);

    /*adds a players prestige*/
    void addPrestige(int value);

    /*checks if the player cna prestige*/
    boolean canPrestige();

    /*gets the players boosters*/
    Set<String> getAllBoosters();

    /*sets the players active booster*/
    void setActiveBooster(BoosterData data);

    /*adds to the bank of boosters the players have*/
    void addBooster(BoosterData data, int amount);

    /*removes a booster from a player*/
    void removeBooster(BoosterData data);

    /*checks if the player has a booster*/
    boolean hasBooster(String value);

    /*gets the players active booster*/
    ActiveBooster getActiveBooster();




}
