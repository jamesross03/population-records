/*
 * Copyright 2021 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
 *
 * This file is part of the module population-records.
 *
 * population-records is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * population-records is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with population-records. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.population_records;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class NormalisationTest {

    @Test
    public void normaliseDayOfWeek() {

        assertDayNormalisedTo("mon", "mon");
        assertDayNormalisedTo("mon", "monday");
        assertDayNormalisedTo("mon", " monday ");
        assertDayNormalisedTo("mon", "Monday");
        assertDayNormalisedTo("mon", "MONDAY");
        assertDayNormalisedTo("mon", "MON");
        assertDayNormalisedTo("mon", "1");
        assertDayNormalisedTo("mon", "01");

        assertDayNormalisedTo("thu", "thursday");
        assertDayNormalisedTo("thu", "thur");
        assertDayNormalisedTo("thu", " thursday ");
        assertDayNormalisedTo("thu", "Thursday");
        assertDayNormalisedTo("thu", "THURSDAY");
        assertDayNormalisedTo("thu", "THU");
        assertDayNormalisedTo("thu", "4");
        assertDayNormalisedTo("thu", "04");
    }

    @Test
    public void unknownDayOfWeek() {

        assertEquals("---", Normalisation.normaliseDayOfWeek("february"));
    }

    @Test
    public void normaliseMonth() {

        assertMonthNormalisedTo("01", "jan");
        assertMonthNormalisedTo("01", "january");
        assertMonthNormalisedTo("01", " january ");
        assertMonthNormalisedTo("01", "January");
        assertMonthNormalisedTo("01", "JANUARY");
        assertMonthNormalisedTo("01", "JAN");
        assertMonthNormalisedTo("01", "1");
        assertMonthNormalisedTo("01", "01");

        assertMonthNormalisedTo("09", "sep");
        assertMonthNormalisedTo("09", "september");
        assertMonthNormalisedTo("09", " september ");
        assertMonthNormalisedTo("09", "September");
        assertMonthNormalisedTo("09", "September(");
        assertMonthNormalisedTo("09", "September (");
        assertMonthNormalisedTo("09", "September (extra info)");
        assertMonthNormalisedTo("09", "SEPTEMBER");
        assertMonthNormalisedTo("09", "SEP");
        assertMonthNormalisedTo("09", "9");
        assertMonthNormalisedTo("09", "09");

        assertMonthNormalisedTo("09", "sept");
        assertMonthNormalisedTo("09", " sept ");
        assertMonthNormalisedTo("09", "Sept");
        assertMonthNormalisedTo("09", "SEPT");
    }

    @Test
    public void unknownMonth() {

        assertEquals("--", Normalisation.normaliseMonth("monday"));
    }

    @Test
    public void cleanDate() {

        assertDateCleanedTo("03/05/1866","3", "5", "66");
        assertDateCleanedTo("03/05/1866", "3", "5", "1866");
        assertDateCleanedTo("03/05/1966", "3", "5", "1966");
        assertDateCleanedTo("03/05/1966", "03", "05", "1966");

        assertDateCleanedTo("03/05/1966", "3", "May", "1966");
        assertDateCleanedTo("03/--/1966", "3", "JSND", "1966");
        assertDateCleanedTo("--/--/1966", "WFGDS", "JSND", "1966");
        assertDateCleanedTo("03/05/----", "3", "May", "LSHGD");

        assertDateCleanedTo("03/05/----", "3", "5", "QHAY");
        assertDateCleanedTo("--/05/----", "", "5", "QHAY");
        assertDateCleanedTo("--/05/----", "na", "5", "QHAY");
        assertDateCleanedTo("--/05/----", "ng", "5", "QHAY");
        assertDateCleanedTo("--/05/----", "NG", "5", "QHAY");
        assertDateCleanedTo("--/05/----", "0", "5", "QHAY");
    }

    @Test
    public void extractFromDate() {

        assertEquals("3", Normalisation.extractDay("3/05/1866"));
        assertEquals("03", Normalisation.extractDay("03/05/1866"));
        assertEquals("ng", Normalisation.extractDay("ng/05/1866"));

        assertEquals("05", Normalisation.extractMonth("03/05/1866"));
        assertEquals("May", Normalisation.extractMonth("3/May/1866"));

        assertEquals("1866", Normalisation.extractYear("03/05/1866"));
        assertEquals("66", Normalisation.extractYear("03/05/66"));
    }

    private void assertDayNormalisedTo(String expected, String actual) {

        assertEquals(expected, Normalisation.normaliseDayOfWeek(actual));
    }

    private void assertMonthNormalisedTo(String expected, String actual) {

        assertEquals(expected, Normalisation.normaliseMonth(actual));
    }

    private void assertDateCleanedTo(String expected, String day, String month, String year) {

        assertEquals(expected, Normalisation.cleanDate(day, month, year));
    }
}
