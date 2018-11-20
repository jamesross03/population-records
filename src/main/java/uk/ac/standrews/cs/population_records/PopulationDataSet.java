package uk.ac.standrews.cs.population_records;

import uk.ac.standrews.cs.population_records.record_types.Birth;
import uk.ac.standrews.cs.population_records.record_types.Death;
import uk.ac.standrews.cs.population_records.record_types.Marriage;
import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.dataset.DataSet;
import uk.ac.standrews.cs.utilities.dataset.derived.DerivedDataSet;
import uk.ac.standrews.cs.utilities.dataset.derived.Mapper;
import uk.ac.standrews.cs.utilities.dataset.encrypted.EncryptedDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PopulationDataSet extends DerivedDataSet {

    protected abstract InputStream getEncryptedKeys();

    protected abstract InputStream getRawData();

    protected abstract InputStream getFieldNameMap();

    protected abstract InputStream getCompositeFieldNameMap();

    protected abstract List<String> getConvertedHeadings();

    protected abstract void cleanFieldValues(String[] record);

    protected PopulationDataSet() throws IOException {
    }

    @Override
    public DataSet getSourceDataSet() throws IOException {

        try {
            return new EncryptedDataSet(getRawData(), getEncryptedKeys());

        } catch (CryptoException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected DataSet getDerivedDataSet(final DataSet source_data_set) {

        return source_data_set.map(new Mapper() {

            @Override
            public List<String> mapRecord(List<String> record, List<String> labels) {

                final String[] new_record = new String[getConvertedHeadings().size()];

                addFields(record, labels, new_record);
                addCompositeFields(record, labels, new_record);
                cleanFieldValues(new_record);

                return Arrays.asList(new_record);
            }

            @Override
            public List<String> mapColumnLabels(List<String> labels) {
                return getConvertedHeadings();
            }
        });
    }

    protected void cleanBirthFieldValues(String[] record) {

        record[Birth.BIRTH_DAY] = Normalisation.cleanDay(record[Birth.BIRTH_DAY]);
        record[Birth.BIRTH_MONTH] = Normalisation.cleanMonth(record[Birth.BIRTH_MONTH]);
        record[Birth.BIRTH_YEAR] = Normalisation.cleanYear(record[Birth.BIRTH_YEAR]);

        record[Birth.PARENTS_DAY_OF_MARRIAGE] = Normalisation.cleanDay(record[Birth.PARENTS_DAY_OF_MARRIAGE]);
        record[Birth.PARENTS_MONTH_OF_MARRIAGE] = Normalisation.cleanMonth(record[Birth.PARENTS_MONTH_OF_MARRIAGE]);
        record[Birth.PARENTS_YEAR_OF_MARRIAGE] = Normalisation.cleanYear(record[Birth.PARENTS_YEAR_OF_MARRIAGE]);

        record[Birth.BIRTH_MONTH] = Normalisation.normaliseMonth(record[Birth.BIRTH_MONTH]);
        record[Birth.PARENTS_MONTH_OF_MARRIAGE] = Normalisation.normaliseMonth(record[Birth.PARENTS_MONTH_OF_MARRIAGE]);

        record[Birth.PARENTS_PLACE_OF_MARRIAGE] = Normalisation.cleanPlace(record[Birth.PARENTS_PLACE_OF_MARRIAGE]);
    }

    protected void cleanDeathFieldValues(String[] record) {

        record[Death.DEATH_DAY] = Normalisation.cleanDay(record[Death.DEATH_DAY]);
        record[Death.DEATH_MONTH] = Normalisation.cleanMonth(record[Death.DEATH_MONTH]);
        record[Death.DEATH_YEAR] = Normalisation.cleanYear(record[Death.DEATH_YEAR]);

        record[Death.DEATH_MONTH] = Normalisation.normaliseMonth(record[Death.DEATH_MONTH]);
    }

    protected void cleanMarriageFieldValues(String[] record) {

        record[Marriage.MARRIAGE_DAY] = Normalisation.cleanDay(record[Marriage.MARRIAGE_DAY]);
        record[Marriage.MARRIAGE_MONTH] = Normalisation.cleanMonth(record[Marriage.MARRIAGE_MONTH]);
        record[Marriage.MARRIAGE_YEAR] = Normalisation.cleanYear(record[Marriage.MARRIAGE_YEAR]);

        record[Marriage.MARRIAGE_MONTH] = Normalisation.normaliseMonth(record[Marriage.MARRIAGE_MONTH]);
        record[Marriage.PLACE_OF_MARRIAGE] = Normalisation.cleanPlace(record[Marriage.PLACE_OF_MARRIAGE]);
    }

    protected InputStream getResourceStream(final Path path) {

        return getClass().getResourceAsStream(path.toString());
    }

    private void addFields(List<String> record, List<String> labels, String[] new_record) {

        for (List<String> mapping : new DataSet(getFieldNameMap()).getRecords()) {

            String converted_label = mapping.get(0);
            String raw_label = mapping.get(1);

            int existing_record_index = labels.indexOf(raw_label);
            int new_record_index = getConvertedHeadings().indexOf(converted_label);

            new_record[new_record_index] = record.get(existing_record_index).trim();
        }
    }

    private void addCompositeFields(List<String> record, List<String> labels, String[] new_record) {

        for (List<String> mapping : new DataSet(getCompositeFieldNameMap()).getRecords()) {

            String s = mapping.get(0);
            mapping.remove(0);
            new_record[getConvertedHeadings().indexOf(s)] = combineFields(record, labels, mapping);
        }
    }

    private static String combineFields(final List<String> record, List<String> raw_record_labels, List<String> source_field_labels) {

        StringBuilder builder = new StringBuilder();

        for (String source_field_label : source_field_labels) {

            String next_field_value = record.get(raw_record_labels.indexOf(source_field_label)).trim();

            if (builder.length() > 0 && next_field_value.length() > 0) {
                builder.append(" ");
            }
            builder.append(next_field_value);
        }

        return builder.toString();
    }    private static final String DATE_SEPARATOR = "/";

    private static final String BLANK_DAY = "--";
    private static final String BLANK_DAY_OF_WEEK = "---";
    private static final String BLANK_MONTH = "--";
    private static final String BLANK_YEAR = "----";

    private static final List<String> NOT_GIVEN_STRINGS = Arrays.asList("", "na", "ng", "0");

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

        day = clean(day);

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

        month = clean(month);

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

        year = clean(year);

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

    private static String clean(String input) {

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

        return NOT_GIVEN_STRINGS.contains(field);
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

        input = clean(input);

        for (int i = 0; i < normalised_names.size(); i++) {

            if (alternative_names.get(i).contains(input)) {
                return normalised_names.get(i);
            }
        }

        throw new RuntimeException("Unrecognized: " + input);
    }
}
