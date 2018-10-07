package uk.ac.standrews.cs.population_records.importer;

import uk.ac.standrews.cs.population_records.record_types.Birth;
import uk.ac.standrews.cs.storr.impl.exceptions.BucketException;
import uk.ac.standrews.cs.storr.interfaces.IBucket;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

import java.util.List;

/**
 * Utility classes for importing records in digitising scotland format
 * Created by al on 8/11/2016.
 *
 * @author Alan Dearle (alan.dearle@st-andrews.ac.uk)
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public abstract class BirthRecordImporter extends RecordImporter {

    public abstract void addAvailableCompoundFields(final DataSet data, final List<String> record, final Birth birth);

    public abstract void addAvailableNormalisedFields(DataSet data, List<String> record, Birth birth);

    public int importBirthRecords(IBucket<Birth> births, DataSet data) throws BucketException {

        int count = 0;

        for (List<String> record : data.getRecords()) {

            births.makePersistent(importBirthRecord(data, record));
            count++;
        }

        return count;
    }

    private Birth importBirthRecord(DataSet data, List<String> record) {

        Birth birth = new Birth();

        addAvailableSingleFields(data, record, birth, getRecordMap());
        addAvailableNormalisedFields(data, record, birth);
        addAvailableCompoundFields(data, record, birth);
        addUnavailableFields(birth, getUnavailableRecords());

        return birth;
    }
}
