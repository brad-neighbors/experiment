package com.incandescent.lean.experiment.db.jdbc;

import com.incandescent.lean.experiment.Experiment;
import com.incandescent.lean.experiment.ExperimentName;
import com.incandescent.lean.experiment.MultiOutcomeExperiment;
import com.incandescent.lean.experiment.Option;
import com.incandescent.lean.experiment.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Brad Neighbors
 */
@ContextConfiguration(locations = {"classpath:com/incandescent/lean/experiment/db/testDb.xml"})
public class JdbcExperimentPersistenceTest extends AbstractTransactionalTestNGSpringContextTests {

    private ExperimentName homePageExperimentName = new ExperimentName("Home Page");
    private MultiOutcomeExperiment experiment;

    @Autowired
    private JdbcExperimentRepository persistence;

    @BeforeMethod
    public void prepare() {
        assertThat(persistence, is(notNullValue()));
        experiment = new MultiOutcomeExperiment(homePageExperimentName);
    }

    @Test
    public void canStoreExperimentsWithNoOptions() {
        persistence.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));

        final Experiment persistedExperiment = persistence.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.getName(), is(homePageExperimentName));
        assertThat(persistedExperiment.startedOn(), is(nullValue()));
        assertThat(persistedExperiment.endedOn(), is(nullValue()));
    }

    @Test
    public void storesExperimentsWithTheirLifecyclePreserved_started() {
        experiment.start();
        persistence.store(experiment);

        final Experiment persistedExperiment = persistence.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.startedOn(), is(notNullValue()));
        assertThat(persistedExperiment.endedOn(), is(nullValue()));
    }

    @Test
    public void storesExperimentsWithTheirLifecyclePreserved_ended() {
        experiment.start();
        experiment.end();

        persistence.store(experiment);

        final Experiment persistedExperiment = persistence.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.startedOn(), is(notNullValue()));
        assertThat(persistedExperiment.endedOn(), is(notNullValue()));
    }

    @Test
    public void storesExperimentsWithOptions() {
        experiment.addOption(new Option("index1.html"));
        experiment.addOption(new Option("index2.html"));

        persistence.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));
        assertThat(countRowsInTable("experiment_option"), is(2));

        final Experiment persistedExperiment = persistence.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.countOptions(), is(2));
    }

    @Test
    public void storesExperimentsWithSubjectOutcomes() {
        final Option option1 = new Option("index1.html");
        final Option option2 = new Option("index2.html");

        experiment.addOption(option1);
        experiment.addOption(option2);

        experiment.specifySubjectOutcome(new Subject("John Doe"), option1);
        experiment.specifySubjectOutcome(new Subject("Jane Doe"), option2);

        experiment.start();

        experiment.evaluateOutcomeFor(new Subject("John Doe"));
        experiment.evaluateOutcomeFor(new Subject("John Doe"));
        experiment.evaluateOutcomeFor(new Subject("Jane Doe"));

        persistence.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));
        assertThat(countRowsInTable("experiment_option"), is(2));

        final Experiment persistedExperiment = persistence.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.countOptions(), is(2));
        assertThat(persistedExperiment.countSubjectOutcomes(), is(2));
        assertThat(persistedExperiment.countOutcomesFor(option1), is(2));
        assertThat(persistedExperiment.countOutcomesFor(option2), is(1));
    }

    @Test
    public void updatesExperimentsLifecycleData() {
        experiment.addOption(new Option("index1.html"));
        experiment.addOption(new Option("index2.html"));

        persistence.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));
        assertThat(experiment.startedOn(), is(nullValue()));
        assertThat(experiment.endedOn(), is(nullValue()));

        experiment.start();
        experiment.end();

        persistence.store(experiment);

        final Experiment retrievedExperiment = persistence.findExperimentBy(homePageExperimentName);
        assertThat(retrievedExperiment.startedOn(), is(notNullValue()));
        assertThat(retrievedExperiment.endedOn(), is(notNullValue()));
    }
}
