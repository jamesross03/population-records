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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import uk.ac.standrews.cs.neoStorr.impl.LXP;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

public class BirthTest extends RecordTypeTest {
    public BirthTest() {
        super(List.of(
            "ORIGINAL_ID",
            "STANDARDISED_ID",
            "FORENAME",
            "CHANGED_FORENAME",
            "SURNAME",
            "CHANGED_SURNAME",
            "SEX",
            "FATHER_FORENAME",
            "FATHER_SURNAME",
            "MOTHER_FORENAME",
            "MOTHER_SURNAME",
            "MOTHER_MAIDEN_SURNAME",
            "CHANGED_MOTHER_MAIDEN_SURNAME",
            "MOTHER_OCCUPATION",
            "FATHER_OCCUPATION",
            "YEAR_OF_REGISTRATION",
            "ENTRY",
            "REGISTRATION_DISTRICT_SUFFIX",
            "REGISTRATION_DISTRICT_NUMBER",
            "CORRECTED_ENTRY",
            "IMAGE_QUALITY",
            "BIRTH_DAY",
            "BIRTH_MONTH",
            "BIRTH_YEAR",
            "BIRTH_ADDRESS",
            "ILLEGITIMATE_INDICATOR",
            "ADOPTION",
            "PARENTS_DAY_OF_MARRIAGE",
            "PARENTS_MONTH_OF_MARRIAGE",
            "PARENTS_YEAR_OF_MARRIAGE",
            "PARENTS_PLACE_OF_MARRIAGE",
            "PLACE_OF_BIRTH",
            "INFORMANT_DID_NOT_SIGN",
            "INFORMANT",
            "FAMILY",
            "DEATH",
            "FORENAME_CLEAN",
            "SURNAME_CLEAN",
            "FATHER_FORENAME_CLEAN",
            "FATHER_SURNAME_CLEAN",
            "MOTHER_FORENAME_CLEAN",
            "MOTHER_SURNAME_CLEAN",
            "CHILD_IDENTITY",
            "MOTHER_IDENTITY",
            "FATHER_IDENTITY",
            "DEATH_RECORD_IDENTITY",
            "PARENT_MARRIAGE_RECORD_IDENTITY",
            "FATHER_BIRTH_RECORD_IDENTITY",
            "MOTHER_BIRTH_RECORD_IDENTITY",
            "MARRIAGE_RECORD_IDENTITY1",
            "MARRIAGE_RECORD_IDENTITY2",
            "MARRIAGE_RECORD_IDENTITY3",
            "MARRIAGE_RECORD_IDENTITY4",
            "MARRIAGE_RECORD_IDENTITY5",
            "MARRIAGE_RECORD_IDENTITY6",
            "MARRIAGE_RECORD_IDENTITY7",
            "MARRIAGE_RECORD_IDENTITY8",
            "IMMIGRATION_GENERATION"
        ));
    }

    @Override
    public void testGetLabelsNotNullAndContainsFields() {
        List<String> labels = Birth.getLabels();
        assertNotNull(labels);

        assertEquals(expectedLabels.size(), labels.size());
        for (String expectedLabel : expectedLabels) {
            assertTrue(labels.contains(expectedLabel));
        } 
    }
    
    @Override
    public void testConstructorWithDataSet() {
        List<String> record = testRecords.get(0);
        Birth birth = new Birth(datasetNoRecords, record);

        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)birth.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testEqualsAndHashCodeWithIdenticalInstance() {
        Birth birth = new Birth(datasetNoRecords, testRecords.get(0));

        assertEquals(birth, birth);
        assertEquals(birth.hashCode(), birth.hashCode());
    }

    @Override
    public void testEqualsAndHashCodeWithSameRecord() {
        Birth birth1 = new Birth(datasetNoRecords, testRecords.get(0));
        Birth birth2 = new Birth(datasetNoRecords, testRecords.get(0));

        assertNotEquals(birth1, birth2);
        assertNotEquals(birth1.hashCode(), birth2.hashCode());
    }

    @Override
    public void testEqualsAndHashCodeWithTwoRecords() {
        Birth birth1 = new Birth(datasetNoRecords, testRecords.get(0));
        Birth birth2 = new Birth(datasetNoRecords, testRecords.get(1));

        assertNotEquals(birth1, birth2);
        assertNotEquals(birth1.hashCode(), birth2.hashCode());
    }

    @Override
    public void testConvertToUntypedRecords() {
        DataSet dataset = new DataSet(expectedLabels);
        List<String> record = testRecords.get(0);
        dataset.addRow(record);
        
        LXP converted = Birth.convertToUntypedRecords(dataset).iterator().next();
        assertTrue(converted instanceof Birth);
        
        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)converted.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testConvertToRecords() {
        DataSet dataset = new DataSet(expectedLabels);
        List<String> record = testRecords.get(0);
        dataset.addRow(record);

        Birth birth = Birth.convertToRecords(dataset).iterator().next();

        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)birth.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testConvertToDataSet() {
        List<String> record = testRecords.get(0);
        List<Birth> births = List.of(new Birth(datasetNoRecords, record));
        DataSet dataset = Birth.convertToDataSet(births);

        List<String> datasetLabels = dataset.getColumnLabels();
        assertEquals(expectedLabels.size(), datasetLabels.size());
        for (String label : expectedLabels) {
            assertTrue(datasetLabels.contains(label));
        }

        List<String> result = dataset.getRecords().get(0);
        for (int i = 0; i < record.size(); i++) {
            assertEquals(record.get(i), result.get(i));
        }
    }
}
