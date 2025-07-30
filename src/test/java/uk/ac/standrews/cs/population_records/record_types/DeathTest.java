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

public class DeathTest extends RecordTypeTest {
    public DeathTest() {
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
            "CHANGED_MOTHERS_MAIDEN_SURNAME",
            "FATHER_OCCUPATION",
            "MOTHER_OCCUPATION",
            "YEAR_OF_REGISTRATION",
            "ENTRY",
            "REGISTRATION_DISTRICT_SUFFIX",
            "REGISTRATION_DISTRICT_NUMBER",
            "CORRECTED_ENTRY",
            "IMAGE_QUALITY",
            "COD_A",
            "COD_B",
            "COD_C",
            "PLACE_OF_DEATH",
            "DATE_OF_BIRTH",
            "DEATH_DAY",
            "DEATH_MONTH",
            "DEATH_YEAR",
            "AGE_AT_DEATH",
            "CHANGED_DEATH_AGE",
            "OCCUPATION",
            "MARITAL_STATUS",
            "SPOUSE_NAMES",
            "SPOUSE_OCCUPATION",
            "MOTHER_DECEASED",
            "FATHER_DECEASED",
            "CERTIFYING_DOCTOR",
            "DECEASED_IDENTITY",
            "MOTHER_IDENTITY",
            "FATHER_IDENTITY",
            "SPOUSE_IDENTITY",
            "BIRTH_RECORD_IDENTITY",
            "PARENT_MARRIAGE_RECORD_IDENTITY",
            "FATHER_BIRTH_RECORD_IDENTITY",
            "MOTHER_BIRTH_RECORD_IDENTITY",
            "SPOUSE_MARRIAGE_RECORD_IDENTITY",
            "SPOUSE_BIRTH_RECORD_IDENTITY",
            "IMMIGRATION_GENERATION"
        ));
    }

    @Override
    public void testGetLabelsNotNullAndContainsFields() {
        List<String> labels = Death.getLabels();
        assertNotNull(labels);

        assertEquals(expectedLabels.size(), labels.size());
        for (String expectedLabel : expectedLabels) {
            assertTrue(labels.contains(expectedLabel));
        } 
    }
    
    @Override
    public void testConstructorWithDataSet() {
        List<String> record = testRecords.get(0);
        Death death = new Death(datasetNoRecords, record);

        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)death.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testEqualsAndHashCodeWithIdenticalInstance() {
        Death death = new Death(datasetNoRecords, testRecords.get(0));

        assertEquals(death, death);
        assertEquals(death.hashCode(), death.hashCode());
    }

    @Override
    public void testEqualsAndHashCodeWithSameRecord() {
        Death death1 = new Death(datasetNoRecords, testRecords.get(0));
        Death death2 = new Death(datasetNoRecords, testRecords.get(0));

        assertNotEquals(death1, death2);
        assertNotEquals(death1.hashCode(), death2.hashCode());
    }

    @Override
    public void testEqualsAndHashCodeWithTwoRecords() {
        Death death1 = new Death(datasetNoRecords, testRecords.get(0));
        Death death2 = new Death(datasetNoRecords, testRecords.get(1));

        assertNotEquals(death1, death2);
        assertNotEquals(death1.hashCode(), death2.hashCode());
    }

    @Override
    public void testConvertToUntypedRecords() {
        DataSet dataset = new DataSet(expectedLabels);
        List<String> record = testRecords.get(0);
        dataset.addRow(record);
        
        LXP converted = Death.convertToUntypedRecords(dataset).iterator().next();
        assertTrue(converted instanceof Death);
        
        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)converted.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testConvertToRecords() {
        DataSet dataset = new DataSet(expectedLabels);
        List<String> record = testRecords.get(0);
        dataset.addRow(record);

        Death death = Death.convertToRecords(dataset).iterator().next();

        for (int i = 0; i < expectedLabels.size(); i++) {
            assertEquals(record.get(i), (String)death.get(expectedLabels.get(i)));
        }
    }

    @Override
    public void testConvertToDataSet() {
        List<String> record = testRecords.get(0);
        List<Death> deaths = List.of(new Death(datasetNoRecords, record));
        DataSet dataset = Death.convertToDataSet(deaths);

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
