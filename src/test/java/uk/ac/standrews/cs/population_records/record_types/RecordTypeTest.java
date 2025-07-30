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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.ac.standrews.cs.utilities.dataset.DataSet;

public abstract class RecordTypeTest {
    final List<String> expectedLabels;
    final List<List<String>> testRecords;
    final DataSet datasetNoRecords;

    public RecordTypeTest(List<String> expectedLabels) {
        this.expectedLabels = expectedLabels;

        this.testRecords = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<String> record = new ArrayList<>();
            for (String label : expectedLabels) {
                record.add("record" + i + "_" + label);
            }
            testRecords.add(record);
        }

        this.datasetNoRecords = new DataSet(expectedLabels);
    }

    @Test
    public abstract void testGetLabelsNotNullAndContainsFields();

    @Test
    public abstract void testConstructorWithDataSet();

    @Test
    public abstract void testEqualsAndHashCodeWithIdenticalInstance();

    @Test
    public abstract void testEqualsAndHashCodeWithSameRecord();

    @Test
    public abstract void testEqualsAndHashCodeWithTwoRecords();

    @Test
    public abstract void testConvertToUntypedRecords();

    @Test
    public abstract void testConvertToRecords();

    @Test
    public abstract void testConvertToDataSet();

        /*


    @Test
    void testConvertToUntypedRecords() {
        List<String> columnLabels = List.of("FORENAME", "SURNAME", "SEX");
        List<List<String>> rows = List.of(
                List.of("Alice", "Smith", "F"),
                List.of("Bob", "Jones", "M")
        );

        DataSet dataSet = new DataSet(columnLabels, rows);

        List<Birth> converted = new ArrayList<>();
        for (LXP lxp : Birth.convertToUntypedRecords(dataSet)) {
            assertTrue(lxp instanceof Birth);
            converted.add((Birth) lxp);
        }

        assertEquals(2, converted.size());
        assertEquals("Alice", converted.get(0).get(Birth.FORENAME));
        assertEquals("Bob", converted.get(1).get(Birth.FORENAME));
    }

    @Test
    void testConvertToDataSetRoundTrip() {
        List<String> columnLabels = List.of("FORENAME", "SURNAME", "SEX");
        List<String> values = List.of("Alice", "Smith", "F");
        DataSet dataSet = new DataSet(columnLabels, List.of(values));

        Iterable<Birth> births = Birth.convertToRecords(dataSet);
        DataSet result = Birth.convertToDataSet(births);

        assertEquals(1, result.getRecords().size());
        assertEquals("Alice", result.getRecords().get(0).get(0));
    }
        */
}
