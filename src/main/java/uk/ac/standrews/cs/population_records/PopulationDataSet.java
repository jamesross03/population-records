package uk.ac.standrews.cs.population_records;

import uk.ac.standrews.cs.utilities.crypto.CryptoException;
import uk.ac.standrews.cs.utilities.dataset.DataSet;
import uk.ac.standrews.cs.utilities.dataset.derived.DerivedDataSet;
import uk.ac.standrews.cs.utilities.dataset.derived.Mapper;
import uk.ac.standrews.cs.utilities.dataset.encrypted.EncryptedDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public abstract class PopulationDataSet extends DerivedDataSet {

    protected abstract InputStream getEncryptedKeys();

    protected abstract InputStream getRawData();

    protected abstract InputStream getFieldNameMap();

    protected abstract InputStream getCompositeFieldNameMap();

    protected abstract List<String> getConvertedHeadings();

    protected abstract void normaliseFieldValues(String[] record);

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

        Mapper mapper = new Mapper() {

            @Override
            public List<String> mapRecord(List<String> record, List<String> labels) {

                final String[] new_record = new String[getConvertedHeadings().size()];

                addFields(record, labels, new_record);
                addCompositeFields(record, labels, new_record);

                return Arrays.asList(new_record);
            }

            @Override
            public List<String> mapColumnLabels(List<String> labels) {
                return getConvertedHeadings();
            }
        };
        return source_data_set.map(mapper);
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

            new_record[new_record_index] = record.get(existing_record_index);
        }

        normaliseFieldValues(new_record);
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

            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(record.get(raw_record_labels.indexOf(source_field_label)));
        }

        return builder.toString();
    }
}
