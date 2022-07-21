package me.zoon20x.levelpoints.utils;

import me.zoon20x.levelpoints.LevelPoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Contains static methods providing progress bar functionality
 */
public class ProgressStatics {

    public static BarSettings userConfiguredStyle
            = new BarSettings();

    /**
     * Generates progress bar
     *
     * @param current Current progress
     * @param target  Targeted progress
     * @return Coloured progress bar
     */
    public static @NotNull String makeProgressBar(@Nullable BarSettings settings,
                                                  double current, double target) {
        settings = userConfiguredStyle;
        int currentStep = 0;

        if(current != 0 || target != 0)
            currentStep = (int) Math.round(settings.stepMax * (current / target));

        StringBuilder bar = new StringBuilder(settings.visualBorder);
        for(int step = settings.stepMin; step < settings.stepMax; step++) {
            if(step <= currentStep) {
                bar.append(settings.visualCompletedStep);
            } else
                bar.append(settings.visualUncompletedStep);
        }
        bar.append(settings.visualBorder);

        return bar.toString();
    }


    public static class BarSettings {
        public int stepMin = 1;
        public int stepMax = 5;

        public String visualBorder             = "&8┃";
        public String visualCompletedStep      = "&a░";
        public String visualUncompletedStep    = "&7░";
    }
}
