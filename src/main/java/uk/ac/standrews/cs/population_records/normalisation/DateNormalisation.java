/*
 * Copyright 2017 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
 *
 * This file is part of the module linkage-java.
 *
 * linkage-java is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * linkage-java is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with linkage-java. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.population_records.normalisation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateNormalisation {

    private static final String DATE_SEPARATOR = "/";

    private static final String BLANK_DAY = "--";
    private static final String BLANK_DAY_OF_WEEK = "---";
    private static final String BLANK_MONTH = "--";
    private static final String BLANK_YEAR = "----";

    private static final List<String> NORMALISED_DAY_NAMES = Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");

    private static final List<Set<String>> DAY_NAMES = Arrays.asList(
            makeSet("monday"),
            makeSet("tuesday"),
            makeSet("wednesday"),
            makeSet("thursday", "thur", "thurs"),
            makeSet("friday"),
            makeSet("saturday"),
            makeSet("sunday"));

    private static final List<String> NORMALISED_MONTH_NAMES = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

    private static final List<Set<String>> MONTH_NAMES = Arrays.asList(
            makeSet("jan", "january"),
            makeSet("feb", "february"),
            makeSet("mar", "march"),
            makeSet("apr", "april"),
            makeSet("may"),
            makeSet("jun", "june"),
            makeSet("jul", "july"),
            makeSet("aug", "august"),
            makeSet("sep", "september", "sept"),
            makeSet("oct", "october"),
            makeSet("nov", "november"),
            makeSet("dec", "december"));

    static {

        // Add the normalised names and indices to the recognised names.
        setUp(NORMALISED_MONTH_NAMES, MONTH_NAMES);
        setUp(NORMALISED_DAY_NAMES, DAY_NAMES);
    }

    public static String makeDate(String day, String month, String year) {

        return day + DATE_SEPARATOR + month + DATE_SEPARATOR + year;
    }

    public static String extractDay(String date) {

        return date.split(DATE_SEPARATOR)[0];
    }

    public static String extractMonth(String date) {

        return date.split(DATE_SEPARATOR)[1];
    }

    public static String extractYear(String date) {

        return date.split(DATE_SEPARATOR)[2];
    }

    public static String cleanDay(String day) {

        day = stripRubbish(day);

        if (notGiven(day)) {
            return BLANK_DAY;
        }

        try {
            String d = String.valueOf(Integer.parseInt(day));

            if (d.length() == 1) {
                return "0" + d;
            }
            return d;
        } catch (NumberFormatException e) {
            return BLANK_DAY;
        }
    }

    public static String cleanMonth(String month) {

        month = stripRubbish(month);

        if (notGiven(month)) {
            return BLANK_MONTH;
        }

        if (month.length() == 1) {
            return "0" + month;
        }

        if (month.length() > 3) {
            return month.substring(0, 3);
        }
        return month;
    }

    public static String cleanYear(String year) {

        year = stripRubbish(year);

        if (notGiven(year)) {
            return BLANK_YEAR;
        }

        try {
            int i = Integer.parseInt(year);

            if (i <= 10) {
                i += 1900;
            }
            if (i > 10 && i < 100) {
                i += 1800;
            }

            return String.valueOf(i);

        } catch (NumberFormatException e) {
            return BLANK_YEAR;
        }
    }

    /**
     * @param day the text to normalise
     * @return that text representation of the day of week in a standard form
     */
    public static String normaliseDayOfWeek(String day) {

        try {
            return normalise(day, NORMALISED_DAY_NAMES, DAY_NAMES);

        } catch (Exception e) {
            return BLANK_DAY_OF_WEEK;
        }
    }

    /**
     * @param month the text to normalise
     * @return that text representation of the month in a standard form
     */
    public static String normaliseMonth(String month) {

        try {
            return normalise(month, NORMALISED_MONTH_NAMES, MONTH_NAMES);

        } catch (Exception e) {
            return BLANK_MONTH;
        }
    }

    private static String stripRubbish(String input) {

        input = input.trim();
        if (input.contains(" ")) {
            input = input.substring(0, input.indexOf(" "));
        }
        if (input.contains("[")) {
            input = input.substring(0, input.indexOf("["));
        }
        if (input.contains("(")) {
            input = input.substring(0, input.indexOf("("));
        }
        return input.toLowerCase();
    }

    private static boolean notGiven(final String field) {

        return field.equals("") || field.equals("na") || field.equals("ng");
    }

    private static Set<String> makeSet(String... strings) {

        return new HashSet<>(Arrays.asList(strings));
    }

    private static void setUp(final List<String> normalised_names, final List<Set<String>> alternative_names) {

        for (int i = 0; i < normalised_names.size(); i++) {

            final Set<String> names = alternative_names.get(i);
            final String index_as_string = String.valueOf(i + 1);

            names.add(normalised_names.get(i));
            names.add(index_as_string);

            if (index_as_string.length() == 1) {
                names.add("0" + index_as_string);
            }
        }
    }

    private static String normalise(String input, List<String> normalised_names, List<Set<String>> alternative_names) {

        input = stripRubbish(input);

        for (int i = 0; i < normalised_names.size(); i++) {

            if (alternative_names.get(i).contains(input)) {
                return normalised_names.get(i);
            }
        }

        throw new RuntimeException("Unrecognized: " + input);
    }
}
