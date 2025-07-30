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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import uk.ac.standrews.cs.neoStorr.impl.LXP;
import uk.ac.standrews.cs.utilities.dataset.DataSet;


public class MarriageTest extends RecordTypeTest {
    public MarriageTest() {
        super(List.of(
            "ORIGINAL_ID",
            "STANDARDISED_ID",
            "BRIDE_FORENAME",
            "BRIDE_SURNAME",
            "GROOM_FORENAME",
            "GROOM_SURNAME",
            "MARRIAGE_DAY",
            "MARRIAGE_MONTH",
            "MARRIAGE_YEAR",
            "PLACE_OF_MARRIAGE",
            "YEAR_OF_REGISTRATION",
            "BRIDE_MOTHER_FORENAME",
            "BRIDE_MOTHER_MAIDEN_SURNAME",
            "BRIDE_FATHER_FORENAME",
            "GROOM_MOTHER_FORENAME",
            "GROOM_MOTHER_MAIDEN_SURNAME",
            "GROOM_FATHER_FORENAME",
            "BRIDE_MARITAL_STATUS",
            "GROOM_MARITAL_STATUS",
            "BRIDE_FATHER_SURNAME",
            "GROOM_FATHER_SURNAME",
            "BRIDE_ADDRESS",
            "BRIDE_OCCUPATION",
            "GROOM_ADDRESS",
            "GROOM_OCCUPATION",
            "BRIDE_MOTHER_DECEASED",
            "BRIDE_FATHER_DECEASED",
            "GROOM_FATHER_DECEASED",
            "GROOM_MOTHER_DECEASED",
            "BRIDE_FATHER_OCCUPATION",
            "GROOM_FATHER_OCCUPATION",
            "CHANGED_GROOM_FORENAME",
            "IMAGE_QUALITY",
            "ENTRY",
            "GROOM_DID_NOT_SIGN",
            "DENOMINATION",
            "CHANGED_GROOM_SURNAME",
            "BRIDE_DID_NOT_SIGN",
            "REGISTRATION_DISTRICT_NUMBER",
            "REGISTRATION_DISTRICT_SUFFIX",
            "BRIDE_AGE_OR_DATE_OF_BIRTH",
            "CHANGED_BRIDE_SURNAME",
            "CORRECTED_ENTRY",
            "CHANGED_BRIDE_FORENAME",
            "GROOM_AGE_OR_DATE_OF_BIRTH",
            "GROOM_IDENTITY",
            "BRIDE_IDENTITY",
            "GROOM_MOTHER_IDENTITY",
            "GROOM_FATHER_IDENTITY",
            "BRIDE_MOTHER_IDENTITY",
            "BRIDE_FATHER_IDENTITY",
            "GROOM_BIRTH_RECORD_IDENTITY",
            "BRIDE_BIRTH_RECORD_IDENTITY",
            "GROOM_FATHER_BIRTH_RECORD_IDENTITY",
            "GROOM_MOTHER_BIRTH_RECORD_IDENTITY",
            "BRIDE_FATHER_BIRTH_RECORD_IDENTITY",
            "BRIDE_MOTHER_BIRTH_RECORD_IDENTITY",
            "BRIDE_IMMIGRATION_GENERATION",
            "GROOM_IMMIGRATION_GENERATION",
            "GROOM_FULL_NAME",
            "BRIDE_FULL_NAME"
        ));
    }

    @Override
    public void testGetLabelsNotNullAndContainsFields() {
        List<String> labels = Marriage.getLabels();
        assertNotNull(labels);

        for (String label : labels) { System.out.println(label);}
        assertEquals(expectedLabels.size(), labels.size());
        for (String expectedLabel : expectedLabels) {
            assertTrue(labels.contains(expectedLabel));
        } 
    }
    
    @Override
    public void testConstructorWithDataSet() {
        List<String> record = testRecords.get(0);
        Marriage marriage = new Marriage(datasetNoRecords, record);

        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)marriage.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testEqualsAndHashCodeWithIdenticalInstance() {
        Marriage marriage = new Marriage(datasetNoRecords, testRecords.get(0));

        assertEquals(marriage, marriage);
        assertEquals(marriage.hashCode(), marriage.hashCode());
    }

    @Override
    public void testEqualsAndHashCodeWithSameRecord() {
        Marriage marriage1 = new Marriage(datasetNoRecords, testRecords.get(0));
        Marriage marriage2 = new Marriage(datasetNoRecords, testRecords.get(0));

        assertNotEquals(marriage1, marriage2);
        assertNotEquals(marriage1.hashCode(), marriage2.hashCode());
    }

    @Override
    public void testEqualsAndHashCodeWithTwoRecords() {
        Marriage marriage1 = new Marriage(datasetNoRecords, testRecords.get(0));
        Marriage marriage2 = new Marriage(datasetNoRecords, testRecords.get(1));

        assertNotEquals(marriage1, marriage2);
        assertNotEquals(marriage1.hashCode(), marriage2.hashCode());
    }

    @Override
    public void testConvertToUntypedRecords() {
        DataSet dataset = new DataSet(expectedLabels);
        List<String> record = testRecords.get(0);
        dataset.addRow(record);
        
        LXP converted = Marriage.convertToUntypedRecords(dataset).iterator().next();
        assertTrue(converted instanceof Marriage);
        
        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)converted.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testConvertToRecords() {
        DataSet dataset = new DataSet(expectedLabels);
        List<String> record = testRecords.get(0);
        dataset.addRow(record);

        Marriage marriage = Marriage.convertToRecords(dataset).iterator().next();

        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)marriage.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testConvertToDataSet() {
        List<String> record = testRecords.get(0);
        List<Marriage> marriages = List.of(new Marriage(datasetNoRecords, record));
        DataSet dataset = Marriage.convertToDataSet(marriages);

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
