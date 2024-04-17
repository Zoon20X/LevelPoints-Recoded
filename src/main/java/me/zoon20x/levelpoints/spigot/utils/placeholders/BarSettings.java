package me.zoon20x.levelpoints.spigot.utils.placeholders;

public class BarSettings {
    private int stepMin;
    private int stepMax;


    private String visualBorder;
    private String visualCompletedStep;
    private String visualUncompletedStep;

    public BarSettings(int stepMin, int stepMax, String visualBorder, String visualCompletedStep, String visualUncompletedStep) {
        this.stepMin = stepMin;
        this.stepMax = stepMax;

        this.visualBorder = visualBorder;
        this.visualCompletedStep = visualCompletedStep;
        this.visualUncompletedStep = visualUncompletedStep;
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
