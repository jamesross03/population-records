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
package uk.ac.standrews.cs.population_records.record_types;

import uk.ac.standrews.cs.neoStorr.impl.LXPMetaData;
import uk.ac.standrews.cs.neoStorr.impl.StaticLXP;
import uk.ac.standrews.cs.neoStorr.impl.exceptions.PersistentObjectException;
import uk.ac.standrews.cs.neoStorr.interfaces.IBucket;
import uk.ac.standrews.cs.neoStorr.types.LXPBaseType;
import uk.ac.standrews.cs.neoStorr.types.LXP_SCALAR;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Birth extends StaticLXP {

    public static final String ROLE_BABY = "ROLE_BABY";
    public static final String ROLE_MOTHER = "ROLE_MOTHER";
    public static final String ROLE_FATHER = "ROLE_FATHER";
    public static final String ROLE_PARENTS = "ROLE_PARENTS"; // When both parents are matched

    private static LXPMetaData static_metadata;
    static {

        try {
            static_metadata = new LXPMetaData(Birth.class, "Birth");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Birth() {

        super();
    }

    public Birth(long persistent_object_id, Map properties, IBucket bucket) throws PersistentObjectException {

        super(persistent_object_id, properties, bucket);
    }

    public Birth(DataSet data, List<String> record) {

        this();
        Utilities.addFieldsToLXP(this, getLabels(), record, data.getColumnLabels());
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Birth && ((((Birth) o).getId()) == getId());
    }

    @Override
    public int hashCode() {
        return new Long(getId()).hashCode();
    }

    @Override
    public LXPMetaData getMetaData() {
        return static_metadata;
    }

    public static List<String> getLabels() {

        return static_metadata.getFieldNamesInSlotOrder();
    }

    public static Iterable<Birth> convertToRecords(DataSet data_set) {

        return () -> new Iterator<>() {

            int row = 0;

            final List<List<String>> records = data_set.getRecords();
            final int number_of_rows = records.size();

            @Override
            public boolean hasNext() {
                return row < number_of_rows;
            }

            @Override
            public Birth next() {
                return new Birth(data_set, records.get(row++));
            }
        };
    }

    public static DataSet convertToDataSet(Iterable<Birth> records) {

        return Utilities.toDataSet(records);
    }

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int ORIGINAL_ID;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int STANDARDISED_ID;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FORENAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CHANGED_FORENAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CHANGED_SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SEX;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_FORENAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_FORENAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_MAIDEN_SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CHANGED_MOTHER_MAIDEN_SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_OCCUPATION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_OCCUPATION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int YEAR_OF_REGISTRATION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int ENTRY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int REGISTRATION_DISTRICT_SUFFIX;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int REGISTRATION_DISTRICT_NUMBER;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CORRECTED_ENTRY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int IMAGE_QUALITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int BIRTH_DAY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int BIRTH_MONTH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int BIRTH_YEAR;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int BIRTH_ADDRESS;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int ILLEGITIMATE_INDICATOR;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int ADOPTION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PARENTS_DAY_OF_MARRIAGE;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PARENTS_MONTH_OF_MARRIAGE;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PARENTS_YEAR_OF_MARRIAGE;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PARENTS_PLACE_OF_MARRIAGE;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PLACE_OF_BIRTH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int INFORMANT_DID_NOT_SIGN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int INFORMANT;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FAMILY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DEATH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FORENAME_CLEAN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SURNAME_CLEAN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_FORENAME_CLEAN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_SURNAME_CLEAN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_FORENAME_CLEAN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_SURNAME_CLEAN;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CHILD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DEATH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PARENT_MARRIAGE_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_BIRTH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_BIRTH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY1;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY2;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY3;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY4;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY5;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY6;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY7;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARRIAGE_RECORD_IDENTITY8;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int IMMIGRATION_GENERATION;
}
