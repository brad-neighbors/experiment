package com.incandescent.lean.experiment.db.jdbc;

import com.incandescent.lean.experiment.ExperimentName;
import com.incandescent.lean.experiment.MultiOutcomeExperiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Brad Neighbors
 */
@ContextConfiguration(locations = {"classpath:com/incandescent/lean/experiment/db/testDb.xml"})
public class JdbcExperimentPersistenceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private JdbcExperimentPersistence persistence;

    @BeforeMethod
    public void prepare() {
        assertThat(persistence, is(notNullValue()));
    }

    @Test
    public void canStoreExperimentsWithNoOptions() {
        assertThat(countRowsInTable("experiment"), is(0));

        final ExperimentName name = new ExperimentName("Home Page");
        persistence.store(new MultiOutcomeExperiment(name));
        assertThat(countRowsInTable("experiment"), is(1));

        final MultiOutcomeExperiment persistedExperiment = persistence.findMultiOutcomeExperimentBy(name);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.getName(), is(name));
    }
}
