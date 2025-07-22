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

import uk.ac.standrews.cs.neoStorr.impl.LXP;
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
public class Death extends StaticLXP {

    public static final String ROLE_DECEASED = "ROLE_DECEASED";
    public static final String ROLE_SPOUSE = "ROLE_SPOUSE";
    public static final String ROLE_MOTHER = "ROLE_MOTHER";
    public static final String ROLE_FATHER = "ROLE_FATHER";
    public static final String ROLE_PARENTS = "ROLE_PARENTS"; // When both parents are matched

    private static LXPMetaData static_metadata;
    static {

        try {
            static_metadata = new LXPMetaData(Death.class, "Death");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static LXPMetaData getStaticMetaData() {
        return static_metadata;
    }

    public Death() {

        super();
    }

    public Death(long persistent_Object_id, Map<String, Object> properties, IBucket<Death> bucket) throws PersistentObjectException {

        super(persistent_Object_id, properties, bucket);
    }

    public Death(DataSet data, List<String> record) {

        this();
        Utilities.addFieldsToLXP(this, getLabels(), record, data.getColumnLabels());
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Death && ((((Death) o).getId()) == getId());
    }

    @Override
    public int hashCode() {
        return Long.valueOf(this.getId()).hashCode();
    }

    @Override
    public LXPMetaData getMetaData() {
        return static_metadata;
    }

    public static List<String> getLabels() {

        return static_metadata.getFieldNamesInSlotOrder();
    }

    public static Iterable<Death> convertToRecords(final DataSet data_set) {

        return () -> new Iterator<>() {

            final Iterator<LXP> iterator = convertToUntypedRecords(data_set).iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Death next() {
                return (Death) iterator.next();
            }
        };
    }

    public static Iterable<LXP> convertToUntypedRecords(final DataSet data_set) {

        return () -> new Iterator<>() {

            int row = 0;

            final List<List<String>> records = data_set.getRecords();
            final int number_of_rows = records.size();

            @Override
            public boolean hasNext() {
                return row < number_of_rows;
            }

            @Override
            public LXP next() {
                return new Death(data_set, records.get(row++));
            }
        };
    }

    public static DataSet convertToDataSet(Iterable<Death> records) {

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
    public static int CHANGED_MOTHERS_MAIDEN_SURNAME;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_OCCUPATION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_OCCUPATION;

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
    public static int COD_A;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int COD_B;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int COD_C;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PLACE_OF_DEATH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DATE_OF_BIRTH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DEATH_DAY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DEATH_MONTH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DEATH_YEAR;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int AGE_AT_DEATH;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CHANGED_DEATH_AGE;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int OCCUPATION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MARITAL_STATUS;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SPOUSE_NAMES;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SPOUSE_OCCUPATION;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_DECEASED;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_DECEASED;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int CERTIFYING_DOCTOR;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int DECEASED_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SPOUSE_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int BIRTH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int PARENT_MARRIAGE_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int FATHER_BIRTH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int MOTHER_BIRTH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SPOUSE_MARRIAGE_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int SPOUSE_BIRTH_RECORD_IDENTITY;

    @LXP_SCALAR(type = LXPBaseType.STRING)
    public static int IMMIGRATION_GENERATION;
}
