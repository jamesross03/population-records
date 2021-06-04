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
import uk.ac.standrews.cs.utilities.dataset.DataSet;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static void printSampleRecords(DataSet data_set, String record_type, int number_to_print) {

        printRow(data_set.getColumnLabels());
        List<List<String>> records = data_set.getRecords();

        for (int i = 0; i < number_to_print; i++) {
            printRow(records.get(i));
        }

        System.out.println("Printed " + number_to_print + " of " + records.size() + " " + record_type + " records");
    }

    static <T extends LXP> DataSet toDataSet(Iterable<T> records) {

        DataSet data_set = null;
        List<String> headings = null;

        for (LXP record : records) {
            if (data_set == null) {
                headings = getHeadings(record);
                data_set = new DataSet(headings);
            }

            data_set.addRow(getRecordValues(record, headings));
        }
        return data_set;
    }

    static void addFieldsToLXP(LXP lxp_record, List<String> lxp_record_labels, List<String> data_set_record, List<String> data_set_record_labels) {

        for (int i = 0; i < data_set_record.size(); i++) {

            final int slot = lxp_record_labels.indexOf(data_set_record_labels.get(i));

            String field_value = data_set_record.get(i);
            if (field_value == null) field_value = "";

            lxp_record.put(slot, field_value);
        }
    }

    private static void printRow(List<String> row) {

        boolean first = true;
        for (String element : row) {
            if (!first) {
                System.out.print(",");
            }
            first = false;
            if (element != null) {
                System.out.print(element);
            }
        }
        System.out.println();
    }

    private static List<String> getHeadings(LXP record) {

        List<String> result = new ArrayList<>();

        int count = record.getMetaData().getFieldCount();
        for (int i = 0; i < count; i++) {
            result.add(record.getMetaData().getFieldName(i));
        }

        return result;
    }

    private static List<String> getRecordValues(LXP record, List<String> headings) {

        List<String> values = new ArrayList<>();
        for (String heading : headings) {
            values.add((String) record.get(heading));
        }
        return values;
    }
}
