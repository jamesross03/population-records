/*
 * Copyright 2017 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
 *
 * This file is part of the module linkage-java.
 *
 * linkage-java is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * linkage-java is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with linkage-java. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.population_records;

import uk.ac.standrews.cs.population_records.record_types.Birth;
import uk.ac.standrews.cs.population_records.record_types.Death;
import uk.ac.standrews.cs.population_records.record_types.Marriage;
import uk.ac.standrews.cs.storr.impl.BucketKind;
import uk.ac.standrews.cs.storr.impl.LXP;
import uk.ac.standrews.cs.storr.impl.Store;
import uk.ac.standrews.cs.storr.impl.exceptions.BucketException;
import uk.ac.standrews.cs.storr.impl.exceptions.RepositoryException;
import uk.ac.standrews.cs.storr.interfaces.IBucket;
import uk.ac.standrews.cs.storr.interfaces.IRepository;
import uk.ac.standrews.cs.storr.interfaces.IStore;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class RecordRepository {

    private static final String BIRTHS_BUCKET_NAME = "birth_records";
    private static final String DEATHS_BUCKET_NAME = "death_records";
    private static final String MARRIAGES_BUCKET_NAME = "marriage_records";

    private IStore store;

    private IBucket<Birth> births;
    private IBucket<Marriage> marriages;
    private IBucket<Death> deaths;

    public RecordRepository(Path store_path, String repository_name) {

        store = new Store(store_path);
        try {
            initialiseBuckets(repository_name);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

    }

    public Iterable<Birth> getBirths() {
        return getRecords(births);
    }

    public Iterable<Death> getDeaths() {
        return getRecords(deaths);
    }

    public Iterable<Marriage> getMarriages() {
        return getRecords(marriages);
    }

    public void addBirth(Birth birth) throws BucketException {
        births.makePersistent(birth);
    }

    public void addDeath(Death death) throws BucketException {
        deaths.makePersistent(death);
    }

    public void addMarriage(Marriage marriage) throws BucketException {
        marriages.makePersistent(marriage);
    }

    public void importBirthRecords(DataSet birth_records) throws BucketException {

        for (Birth birth : Birth.convertToRecords(birth_records)) {
            addBirth(birth);
        }
    }

    public void importDeathRecords(DataSet death_records) throws BucketException {

        for (Death death : Death.convertToRecords(death_records)) {
            addDeath(death);
        }
    }

    public void importMarriageRecords(DataSet marriage_records) throws BucketException {

        for (Marriage marriage : Marriage.convertToRecords(marriage_records)) {
            addMarriage(marriage);
        }
    }

    private <T extends LXP> Iterable<T> getRecords(IBucket<T> bucket) {

        return () -> new Iterator<T>() {

            final List<Long> object_ids = bucket.getOids();
            final int bucket_size = object_ids.size();
            int next_index = 0;

            @Override
            public boolean hasNext() {
                return next_index < bucket_size;
            }

            @Override
            public T next() {
                try {
                    return bucket.getObjectById(object_ids.get(next_index++));

                } catch (BucketException e) {
                    return null;
                }
            }
        };
    }

    private void initialiseBuckets(String repository_name) throws RepositoryException {

        try {
            IRepository input_repository = store.getRepository(repository_name);

            births = input_repository.getBucket(BIRTHS_BUCKET_NAME, Birth.class);
            deaths = input_repository.getBucket(DEATHS_BUCKET_NAME, Death.class);
            marriages = input_repository.getBucket(MARRIAGES_BUCKET_NAME, Marriage.class);

        } catch (RepositoryException e) {

            // The repository hasn't previously been initialised.

            IRepository input_repository = store.makeRepository(repository_name);

            births = input_repository.makeBucket(BIRTHS_BUCKET_NAME, BucketKind.DIRECTORYBACKED, Birth.class);
            deaths = input_repository.makeBucket(DEATHS_BUCKET_NAME, BucketKind.DIRECTORYBACKED, Death.class);
            marriages = input_repository.makeBucket(MARRIAGES_BUCKET_NAME, BucketKind.DIRECTORYBACKED, Marriage.class);
        }
    }

    public void stopStoreWatcher() {
        store.getWatcher().stopService();
    }
}
