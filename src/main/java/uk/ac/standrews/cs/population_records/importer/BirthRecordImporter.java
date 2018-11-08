package uk.ac.standrews.cs.population_records.importer;

/**
 * Utility classes for importing records in digitising scotland format
 * Created by al on 8/11/2016.
 *
 * @author Alan Dearle (alan.dearle@st-andrews.ac.uk)
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public abstract class BirthRecordImporter extends RecordImporter {

//    public abstract void addAvailableCompoundFields(final DataSet data, final List<String> record, final Birth birth);
//
//    public abstract void addAvailableNormalisedFields(DataSet data, List<String> record, Birth birth);
//
//    public void importBirthRecords(RecordRepository record_repository, DataSet data) throws BucketException {
//
//        for (List<String> record : data.getRecords()) {
//            record_repository.addBirth(importBirthRecord(data, record));
//        }
//    }
//
//    private Birth importBirthRecord(DataSet data, List<String> record) {
//
//        Birth birth = new Birth();
//
//        addAvailableSingleFields(data, record, birth, getRecordMap());
//        addAvailableNormalisedFields(data, record, birth);
//        addAvailableCompoundFields(data, record, birth);
//        addUnavailableFields(birth, getUnavailableRecords());
//
//        return birth;
//    }
}
