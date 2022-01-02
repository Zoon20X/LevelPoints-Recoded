package me.zoon20x.levelpoints.utils;

import org.jetbrains.annotations.NotNull;

public class FormatStatics {

    public static String userDefinedNumberFormat = "%02d";
    public static String userDefinedDateFormat   = "%02d";


    /**
     * Formats number
     * @param number Number to format
     * @return Formatted number
     */
    public static @NotNull String format(@NotNull Number number) {
        return String.format(userDefinedNumberFormat, number);
    }

}
