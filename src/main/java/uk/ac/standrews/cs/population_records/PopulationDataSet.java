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
import java.util.List;

public abstract class PopulationDataSet extends DerivedDataSet {

    protected abstract InputStream getEncryptedKeys();

    protected abstract InputStream getRawData();

    protected abstract InputStream getFieldNameMap();

    protected abstract InputStream getCompositeFieldNameMap();

    protected abstract List<String> getConvertedHeadings();

    protected abstract void cleanFieldValues(String[] record);

    protected static final String RESOURCE_PATH_SEPARATOR = "/";

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

        record[Birth.YEAR_OF_REGISTRATION] = Normalisation.cleanYear(record[Birth.YEAR_OF_REGISTRATION]);

        record[Birth.BIRTH_ADDRESS] = Normalisation.cleanPlace(record[Birth.BIRTH_ADDRESS]);
        record[Birth.PARENTS_PLACE_OF_MARRIAGE] = Normalisation.cleanPlace(record[Birth.PARENTS_PLACE_OF_MARRIAGE]);
    }

    protected void cleanDeathFieldValues(String[] record) {

        record[Death.DEATH_DAY] = Normalisation.cleanDay(record[Death.DEATH_DAY]);
        record[Death.DEATH_MONTH] = Normalisation.cleanMonth(record[Death.DEATH_MONTH]);
        record[Death.DEATH_YEAR] = Normalisation.cleanYear(record[Death.DEATH_YEAR]);

        record[Death.DEATH_MONTH] = Normalisation.normaliseMonth(record[Death.DEATH_MONTH]);
        record[Death.YEAR_OF_REGISTRATION] = Normalisation.cleanYear(record[Death.YEAR_OF_REGISTRATION]);

        record[Death.PLACE_OF_DEATH] = Normalisation.cleanPlace(record[Death.PLACE_OF_DEATH]);
    }

    protected void cleanMarriageFieldValues(String[] record) {

        record[Marriage.MARRIAGE_DAY] = Normalisation.cleanDay(record[Marriage.MARRIAGE_DAY]);
        record[Marriage.MARRIAGE_MONTH] = Normalisation.cleanMonth(record[Marriage.MARRIAGE_MONTH]);
        record[Marriage.MARRIAGE_YEAR] = Normalisation.cleanYear(record[Marriage.MARRIAGE_YEAR]);

        record[Marriage.MARRIAGE_MONTH] = Normalisation.normaliseMonth(record[Marriage.MARRIAGE_MONTH]);
        record[Marriage.YEAR_OF_REGISTRATION] = Normalisation.cleanYear(record[Marriage.YEAR_OF_REGISTRATION]);

        record[Marriage.PLACE_OF_MARRIAGE] = Normalisation.cleanPlace(record[Marriage.PLACE_OF_MARRIAGE]);
        record[Marriage.BRIDE_ADDRESS] = Normalisation.cleanPlace(record[Marriage.BRIDE_ADDRESS]);
        record[Marriage.GROOM_ADDRESS] = Normalisation.cleanPlace(record[Marriage.GROOM_ADDRESS]);

        record[Marriage.BRIDE_OCCUPATION] = Normalisation.cleanOccupation(record[Marriage.BRIDE_OCCUPATION]);
        record[Marriage.GROOM_OCCUPATION] = Normalisation.cleanOccupation(record[Marriage.GROOM_OCCUPATION]);
        record[Marriage.BRIDE_FATHER_OCCUPATION] = Normalisation.cleanOccupation(record[Marriage.BRIDE_FATHER_OCCUPATION]);
        record[Marriage.GROOM_FATHER_OCCUPATION] = Normalisation.cleanOccupation(record[Marriage.GROOM_FATHER_OCCUPATION]);

        record[Marriage.BRIDE_MARITAL_STATUS] = Normalisation.cleanMaritalStatus(record[Marriage.BRIDE_MARITAL_STATUS]);
        record[Marriage.GROOM_MARITAL_STATUS] = Normalisation.cleanMaritalStatus(record[Marriage.GROOM_MARITAL_STATUS]);
    }

    protected InputStream getResourceStream(final String path) {

        return getClass().getResourceAsStream(path);
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
            if (Normalisation.notGiven(next_field_value)) next_field_value = Normalisation.NOT_GIVEN_PLACEHOLDER;

            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(next_field_value);
        }

        return builder.toString();
    }
}
