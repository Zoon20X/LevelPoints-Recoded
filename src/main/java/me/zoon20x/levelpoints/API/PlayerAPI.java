package me.zoon20x.levelpoints.API;

public interface PlayerAPI {
    void setLevel(int level);

    void addExp(double exp);

    void addLevel();

    void addLevel(int level);

    void addPrestige();

    void addPrestige(int prestige);

    int getLevel();

    double getExp();

    double getRequiredEXP();

    int getPrestige();

    void setPrestige(int prestige);
}
