package de.akra.coronatestmanagement.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestDate {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * An attempt to sort of leniently but safely parse dates. Example CSV sometimes mixed up
     * "/" and "." delimiters.
     *
     * TODO: Properly work with optionals here
     */
    public static LocalDate parse(String date) {
        if (date.isBlank()) {
            return null;
        } else {
            return LocalDate.from(DATE_TIME_FORMATTER.parse(date.replace(".", "-")));
        }
    }
}
