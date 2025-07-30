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

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.neo4j.harness.*;

import uk.ac.standrews.cs.utilities.dataset.DataSet;

import org.neo4j.graphdb.*;
import org.neo4j.driver.*;

import static org.neo4j.driver.GraphDatabase.driver;
import static org.neo4j.driver.Values.parameters;

public class RecordRepositoryIntegrationTest {
    private final static String REPOSITORY_NAME = "umea";

    private static Neo4j embeddedDatabaseServer;
    private static Driver driver;

    @BeforeAll
    static void startServer() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder().build();
        driver = driver(embeddedDatabaseServer.boltURI(), Config.defaultConfig());
    }

    @AfterAll
    static void stopServer() {
        driver.close();
        embeddedDatabaseServer.close();
    }
}

