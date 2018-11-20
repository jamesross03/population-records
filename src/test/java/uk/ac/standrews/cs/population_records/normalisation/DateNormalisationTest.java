package uk.ac.standrews.cs.population_records.normalisation;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class DateNormalisationTest {

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

        assertEquals("---", DateNormalisation.normaliseDayOfWeek("february"));
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

        assertEquals("--", DateNormalisation.normaliseMonth("monday"));
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
    }

    @Test
    public void extractFromDate() {

        assertEquals("3", DateNormalisation.extractDay("3/05/1866"));
        assertEquals("03", DateNormalisation.extractDay("03/05/1866"));
        assertEquals("ng", DateNormalisation.extractDay("ng/05/1866"));

        assertEquals("05", DateNormalisation.extractMonth("03/05/1866"));
        assertEquals("May", DateNormalisation.extractMonth("3/May/1866"));

        assertEquals("1866", DateNormalisation.extractYear("03/05/1866"));
        assertEquals("66", DateNormalisation.extractYear("03/05/66"));
    }

    private void assertDayNormalisedTo(String expected, String actual) {

        assertEquals(expected, DateNormalisation.normaliseDayOfWeek(actual));
    }

    private void assertMonthNormalisedTo(String expected, String actual) {

        assertEquals(expected, DateNormalisation.normaliseMonth(actual));
    }

    private void assertDateCleanedTo(String expected, String day, String month, String year) {

        assertEquals(expected, DateNormalisation.makeDate(DateNormalisation.cleanDay(day), DateNormalisation.normaliseMonth(DateNormalisation.cleanMonth(month)), DateNormalisation.cleanYear(year)));
    }
}
