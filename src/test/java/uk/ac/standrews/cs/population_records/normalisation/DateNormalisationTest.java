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

    @Test(expected = RuntimeException.class)
    public void unknownDayOfWeek() {

        DateNormalisation.normaliseDayOfWeek("february");
    }

    @Test
    public void normaliseMonth() {

        assertMonthNormalisedTo("jan", "jan");
        assertMonthNormalisedTo("jan", "january");
        assertMonthNormalisedTo("jan", " january ");
        assertMonthNormalisedTo("jan", "January");
        assertMonthNormalisedTo("jan", "JANUARY");
        assertMonthNormalisedTo("jan", "JAN");
        assertMonthNormalisedTo("jan", "1");
        assertMonthNormalisedTo("jan", "01");

        assertMonthNormalisedTo("sep", "sep");
        assertMonthNormalisedTo("sep", "september");
        assertMonthNormalisedTo("sep", " september ");
        assertMonthNormalisedTo("sep", "September");
        assertMonthNormalisedTo("sep", "SEPTEMBER");
        assertMonthNormalisedTo("sep", "SEP");
        assertMonthNormalisedTo("sep", "9");
        assertMonthNormalisedTo("sep", "09");

        assertMonthNormalisedTo("sep", "sept");
        assertMonthNormalisedTo("sep", " sept ");
        assertMonthNormalisedTo("sep", "Sept");
        assertMonthNormalisedTo("sep", "SEPT");
    }

    @Test(expected = RuntimeException.class)
    public void unknownMonth() {

        DateNormalisation.normaliseMonth("monday");
    }

    private void assertDayNormalisedTo(String expected, String actual) {

        assertEquals(expected, DateNormalisation.normaliseDayOfWeek(actual));
    }

    private void assertMonthNormalisedTo(String expected, String actual) {

        assertEquals(expected, DateNormalisation.normaliseMonth(actual));
    }
}
