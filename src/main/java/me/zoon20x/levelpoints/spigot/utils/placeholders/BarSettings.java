package me.zoon20x.levelpoints.spigot.utils.placeholders;

public class BarSettings {
    private int stepMin;
    private int stepMax;


    private String visualBorder;
    private String visualCompletedStep;
    private String visualUncompletedStep;

    public BarSettings() {
        stepMin = 1;
        stepMax = 5;

        visualBorder = "&8┃";
        visualCompletedStep = "&a░";
        visualUncompletedStep = "&7░";
    }

    public int getStepMin() {
        return stepMin;
    }

    public int getStepMax() {
        return stepMax;
    }

    public String getVisualBorder() {
        return visualBorder;
    }

    public String getVisualCompletedStep() {
        return visualCompletedStep;
    }

    public String getVisualUncompletedStep() {
        return visualUncompletedStep;
    }
}
