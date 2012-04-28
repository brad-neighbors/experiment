package com.incandescent.lean.experiment.db.relational.jdbc;

import com.incandescent.lean.experiment.ABExperiment;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

/**
 * @author Brad Neighbors
 */
@ContextConfiguration(locations = {"classpath:com/incandescent/lean/experiment/db/testDb.xml"})
public class JdbcExperimentRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {

    private ExperimentName homePageExperimentName = new ExperimentName("Home Page");
    private MultiOutcomeExperiment experiment;

    @Autowired
    private JdbcExperimentRepository repository;

    @BeforeMethod
    public void prepare() {
        assertThat(repository, is(notNullValue()));
        experiment = new MultiOutcomeExperiment(homePageExperimentName);
    }

    @Test
    public void canStoreExperimentsWithNoOptions() {
        repository.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));

        final Experiment persistedExperiment = repository.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.getName(), is(homePageExperimentName));
        assertThat(persistedExperiment.startedOn(), is(nullValue()));
        assertThat(persistedExperiment.endedOn(), is(nullValue()));
    }

    @Test
    public void canFindAorBExperiments() {
        Option page1 = new Option("home_1.html");
        Option page2 = new Option("home_2.html");
        ABExperiment aorBExperiment = new ABExperiment(homePageExperimentName, page1, page2);
        aorBExperiment.start();

        repository.store(aorBExperiment);

        aorBExperiment = repository.findAorBExperimentBy(homePageExperimentName);
        assertThat(aorBExperiment, is(notNullValue()));
        assertThat(aorBExperiment.getName(), is(homePageExperimentName));
    }

    @Test
    public void canFindMultiOutcomeExperiments() {
        Option page1 = new Option("home_1.html");
        Option page2 = new Option("home_2.html");
        MultiOutcomeExperiment multiOutcomeExperiment = new MultiOutcomeExperiment(homePageExperimentName);
        multiOutcomeExperiment.addOption(page1);
        multiOutcomeExperiment.addOption(page2);

        multiOutcomeExperiment.start();

        repository.store(multiOutcomeExperiment);

        multiOutcomeExperiment = repository.findMultiOutcomeExperimentBy(homePageExperimentName);

        assertThat(multiOutcomeExperiment, is(notNullValue()));
        assertThat(multiOutcomeExperiment.getName(), is(homePageExperimentName));
    }

    @Test
    public void storesExperimentsWithTheirLifecyclePreserved_started() {
        experiment.start();
        repository.store(experiment);

        final Experiment persistedExperiment = repository.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.startedOn(), is(notNullValue()));
        assertThat(persistedExperiment.endedOn(), is(nullValue()));
    }

    @Test
    public void storesExperimentsWithTheirLifecyclePreserved_ended() {
        experiment.start();
        experiment.end();

        repository.store(experiment);

        final Experiment persistedExperiment = repository.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.startedOn(), is(notNullValue()));
        assertThat(persistedExperiment.endedOn(), is(notNullValue()));
    }

    @Test
    public void storesExperimentsWithOptions() {
        experiment.addOption(new Option("index1.html"));
        experiment.addOption(new Option("index2.html"));

        repository.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));
        assertThat(countRowsInTable("experiment_option"), is(2));

        final Experiment persistedExperiment = repository.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(((MultiOutcomeExperiment) persistedExperiment).countOptions(), is(2));
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

        repository.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));
        assertThat(countRowsInTable("experiment_option"), is(2));

        final Experiment persistedExperiment = repository.findExperimentBy(homePageExperimentName);

        assertThat(persistedExperiment, is(notNullValue()));
        assertThat(persistedExperiment.countSpecifiedSubjectOutcomes(), is(2));
        assertThat(persistedExperiment.countOutcomesOf(option1), is(2));
        assertThat(persistedExperiment.countOutcomesOf(option2), is(1));
    }

    @Test
    public void updatesExperimentsLifecycleData() {
        experiment.addOption(new Option("index1.html"));
        experiment.addOption(new Option("index2.html"));

        repository.store(experiment);
        assertThat(countRowsInTable("experiment"), is(1));
        assertThat(experiment.startedOn(), is(nullValue()));
        assertThat(experiment.endedOn(), is(nullValue()));

        experiment.start();
        experiment.end();

        repository.store(experiment);

        final Experiment retrievedExperiment = repository.findExperimentBy(homePageExperimentName);
        assertThat(retrievedExperiment.startedOn(), is(notNullValue()));
        assertThat(retrievedExperiment.endedOn(), is(notNullValue()));
    }

    @Test
    public void findsManyExperimentsByNameMatching() {
        String commonPrefix = "homePageTest";
        Experiment googleHomePageTest = new ABExperiment(new ExperimentName(commonPrefix + "_google"));
        Experiment twitterHomePageTest = new ABExperiment(new ExperimentName(commonPrefix + "_twitter"));
        Experiment loginBehaviorTest = new ABExperiment(new ExperimentName("loginTest"));

        assertThat(repository.findExperimentNamesBy(commonPrefix).size(), is(0));

        repository.store(googleHomePageTest);
        repository.store(twitterHomePageTest);
        repository.store(loginBehaviorTest);

        final List<ExperimentName> names = repository.findExperimentNamesBy(commonPrefix);
        assertThat(names.size(), is(2));
        assertThat(names, hasItem(new ExperimentName("homePageTest_google")));
        assertThat(names, hasItem(new ExperimentName("homePageTest_twitter")));
    }

    @Test
    public void findingExperimentNamesRequiresMinimumSearchTermLength() {
        Experiment ex = new ABExperiment(new ExperimentName("homePageTest_google"));
        repository.store(ex);

        assertThat(repository.findExperimentNamesBy("hom").size(), is(0));
        assertThat(repository.findExperimentNamesBy("home").size(), is(0));
        assertThat(repository.findExperimentNamesBy("homeP").size(), is(1));
    }
}
