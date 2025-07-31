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
package uk.ac.standrews.cs.population_records;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import uk.ac.standrews.cs.neoStorr.impl.Store;
import uk.ac.standrews.cs.neoStorr.impl.exceptions.BucketException;
import uk.ac.standrews.cs.neoStorr.impl.exceptions.RepositoryException;
import uk.ac.standrews.cs.population_records.record_types.*;

public class RecordRepositoryNeoTest {
    static final RecordTypeTest BIRTH_TEST = new BirthTest();
    static final RecordTypeTest MARRIAGE_TEST = new MarriageTest();
    static final RecordTypeTest DEATH_TEST = new DeathTest();
    static final String REPO_NAME_BASE = "test_";

    static Neo4j neo4jDb;
    String repoName;
    RecordRepository repo;

    @BeforeAll
    static void setUpNeo() {
        neo4jDb = Neo4jBuilders.newInProcessBuilder()
                .build();
        // Sets override for neo-storr.neoDbCypherBridge
        System.setProperty("NeoDBTestURL", neo4jDb.boltURI().toString());
    }

    @BeforeEach
    void setUpRepo() {
        repoName = REPO_NAME_BASE + System.nanoTime();
        repo = new RecordRepository(repoName);
    }
    
    @AfterEach
    void tearDown() throws RepositoryException {
        Store.getInstance().deleteRepository(repoName);
        assertFalse(Store.getInstance().repositoryExists(repoName));
        repo = null;
    }
    
    @AfterAll
    static void tearDownNeo() {
        Store.getInstance().close();
        System.clearProperty("NeoDBTestURL");

        if (neo4jDb!= null) neo4jDb.close();
        neo4jDb = null;
    }

    @Test
    public void testNeo4jConnection() {
        String testString = "Hello world";
        Driver driver = GraphDatabase.driver(neo4jDb.boltURI(), AuthTokens.none());

        try (Session session = driver.session()) {
            String response = session.run("RETURN '"+testString+"'").single().get(0).asString();
             assertEquals(testString, response);
        }
    }
    

    @Test
    void testAddAndGetBirthRecord() throws BucketException, RepositoryException, IOException {
        Birth birth = new Birth(BIRTH_TEST.datasetNoRecords, BIRTH_TEST.testRecords.get(0));
        repo.addBirth(birth);

        assertEquals(1, repo.getNumberOfBirths());

        Birth retrieved = repo.getBirths().iterator().next();
        assertEquals(birth, retrieved);
    }

    @Test
    void testAddAndGetMarriageRecord() throws BucketException, RepositoryException, IOException {
        Marriage marriage = new Marriage(MARRIAGE_TEST.datasetNoRecords, MARRIAGE_TEST.testRecords.get(0));
        repo.addMarriage(marriage);

        assertEquals(1, repo.getNumberOfMarriages());
        
        Marriage retrieved = repo.getMarriages().iterator().next();
        assertEquals(marriage, retrieved);
    }

    @Test
    void testAddAndGetDeathRecord() throws BucketException, RepositoryException, IOException {
        Death death = new Death(DEATH_TEST.datasetNoRecords, DEATH_TEST.testRecords.get(0));
        repo.addDeath(death);

        assertEquals(1, repo.getNumberOfDeaths());
        
        Death retrieved = repo.getDeaths().iterator().next();
        assertEquals(death, retrieved);
    }

    @Test
    void testAddAndGetRecordWithReopen() throws BucketException, RepositoryException {
        Birth birth = new Birth(BIRTH_TEST.datasetNoRecords, BIRTH_TEST.testRecords.get(0));
        repo.addBirth(birth);

        Store.getInstance().deleteRepository(repoName);
        repo = new RecordRepository(repoName);

        Birth retrieved = repo.getBirths().iterator().next();
        assertEquals(birth, retrieved);
    }

    @Test
    void testDeleteBuckets() {
    }

    @Test
    void testDeleteBucketsAlreadyDeleted() {
    }

    @Test
    void testImportRecords() {
    }
    
    @Test
    void testGetBuckets() {
    }

    @Test
    void testGetBucketsAlreadyDeleted() {
    }

    @Test
    void testTestIsolation() {
        
    }

}
