package app.project_fin_d_etude.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMAT_STANDARD =
            DateTimeFormatter.ofPattern("dd MMM yyyy à HH:mm");

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(FORMAT_STANDARD);
    }
}

