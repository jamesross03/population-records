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

    private static final List<String> NORMALISED_DAY_NAMES = Arrays.asList("mon", "tue", "wed", "thu", "fri", "sat", "sun");

    private static final List<Set<String>> DAY_NAMES = Arrays.asList(
            makeSet("monday"),
            makeSet("tuesday"),
            makeSet("wednesday"),
            makeSet("thursday", "thur", "thurs"),
            makeSet("friday"),
            makeSet("saturday"),
            makeSet("sunday"));

    private static final List<String> NORMALISED_MONTH_NAMES = Arrays.asList("jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec");

    private static final List<Set<String>> MONTH_NAMES = Arrays.asList(
            makeSet("january"),
            makeSet("february"),
            makeSet("march"),
            makeSet("april"),
            makeSet("may"),
            makeSet("june"),
            makeSet("july"),
            makeSet("august"),
            makeSet("september", "sept"),
            makeSet("october"),
            makeSet("november"),
            makeSet("december"));

    static {

        // Add the normalised names and indices to the recognised names.
        setUp(NORMALISED_MONTH_NAMES, MONTH_NAMES);
        setUp(NORMALISED_DAY_NAMES, DAY_NAMES);
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

    /**
     * @param input the text to normalise
     * @return that text representation of the month in a standard form
     */
    public static String normaliseMonth(String input) {

        return normalise(input, NORMALISED_MONTH_NAMES, MONTH_NAMES);
    }

    private static String normalise(String input, List<String> normalised_names, List<Set<String>> alternative_names) {

        input = stripRubbish(input).toLowerCase();

        for (int i = 0; i < normalised_names.size(); i++) {

            if (alternative_names.get(i).contains(input)) {
                return normalised_names.get(i);
            }
        }

        throw new RuntimeException("Unrecognized: " + input);
    }

    /**
     * @param input the text to normalise
     * @return that text representation of the day of week in a standard form
     */
    public static String normaliseDayOfWeek(String input) {

        return normalise(input, NORMALISED_DAY_NAMES, DAY_NAMES);
    }

    public static String cleanDate(String day, String month, String year) {

        return cleanDay(day) + DATE_SEPARATOR + cleanMonth(month) + DATE_SEPARATOR + cleanYear(year);
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
        return input;
    }

    private static String cleanDay(final String day) {

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

    private static String cleanMonth(final String month) {

        if (notGiven(month)) {
            return BLANK_MONTH;
        }

        if (month.length() > 3) {
            return month.substring(0, 3);
        }
        return month;
    }

    private static String cleanYear(final String year) {

        if (notGiven(year)) {
            return BLANK_YEAR;
        }

        try {
            int i = Integer.parseInt(year);
            if (i > 10) {
                i += 1800;
            } else {
                i += 1900;
            }
            return String.valueOf(i);

        } catch (NumberFormatException e) {
            return BLANK_YEAR;
        }
    }

    private static boolean notGiven(final String field) {

        return field.equals("") || field.equals("na") || field.equals("ng");
    }

    private static Set<String> makeSet(String... strings) {

        return new HashSet<>(Arrays.asList(strings));
    }

    private static final String DATE_SEPARATOR = "/";

    private static final String BLANK_DAY = "--";
    private static final String BLANK_MONTH = "---";
    private static final String BLANK_YEAR = "----";
}
