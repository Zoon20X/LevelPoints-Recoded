package me.zoon20x.levelpoints.spigot.utils.placeholders;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains static methods providing progress bar functionality
 */
public class ProgressStatics {

    public static String makeProgressBar(BarSettings settings, double current, double target) {
        int currentStep = 0;

        if(current != 0 || target != 0)
            currentStep = (int) Math.round(settings.getStepMax() * (current / target));

        StringBuilder bar = new StringBuilder(settings.getVisualBorder());
        for(int step = settings.getStepMin(); step < settings.getStepMax(); step++) {
            if(step <= currentStep) {
                bar.append(settings.getVisualCompletedStep());
            } else
                bar.append(settings.getVisualUncompletedStep());
        }
        bar.append(settings.getVisualBorder());

        return LevelPoints.getInstance().getMessagesUtil().getColor(bar.toString());
    }
}
