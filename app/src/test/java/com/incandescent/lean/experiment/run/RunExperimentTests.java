package com.incandescent.lean.experiment.run;

import com.incandescent.lean.experiment.Experiment;
import com.incandescent.lean.experiment.ExperimentName;
import com.incandescent.lean.experiment.MultiOutcomeExperiment;
import com.incandescent.lean.experiment.Option;
import com.incandescent.lean.experiment.Subject;
import com.incandescent.lean.experiment.db.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@ContextConfiguration(locations = {"classpath:com/incandescent/lean/experiment/db/testDb.xml"})
public class RunExperimentTests extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private ExperimentRepository repository;

    @Test
    public void canRunMultiOutcomeExperiment() {
        final ExperimentName name = new ExperimentName("Home Page Test");
        assertThat(repository.findExperimentBy(name), is(nullValue()));

        final MultiOutcomeExperiment homePageExperiment = new MultiOutcomeExperiment(name);

        final Option homePage1 = new Option("home1.html");
        final Option homePage2 = new Option("home2.html");
        final Option homePage3 = new Option("home3.html");

        homePageExperiment.addOption(homePage1);
        homePageExperiment.addOption(homePage2);
        homePageExperiment.addOption(homePage3);

        Subject server1 = new Subject("host server 1");
        Subject server2 = new Subject("host server 3");
        Subject server3 = new Subject("host server 2");

        homePageExperiment.specifySubjectOutcome(server1, homePage1);
        homePageExperiment.specifySubjectOutcome(server3, homePage2);
        homePageExperiment.specifySubjectOutcome(server2, homePage3);

        repository.store(homePageExperiment);

        Experiment experiment = repository.findExperimentBy(name);
        assertThat(experiment, is(notNullValue()));

        experiment.start();

        // evaluate to homePage1 3 times
        assertThat(homePage1, is(experiment.evaluateOutcomeFor(server1)));
        assertThat(homePage1, is(experiment.evaluateOutcomeFor(server1)));
        assertThat(homePage1, is(experiment.evaluateOutcomeFor(server1)));
        assertThat("home1.html", is(experiment.evaluateOutcomeFor(server1).val()));

        // evaluate to homePage2 twice
        assertThat(homePage2, is(experiment.evaluateOutcomeFor(server3)));
        assertThat(homePage2, is(experiment.evaluateOutcomeFor(server3)));

        // evaluate to option 3 once
        assertThat(homePage3, is(experiment.evaluateOutcomeFor(server2)));

        repository.store(experiment);

        experiment = repository.findExperimentBy(name);

        assertThat("Experiment has 3 options", experiment.countOptions(), is(3));
        assertThat("Experiment has 3 specified subject outcomes", experiment.countSpecifiedSubjectOutcomes(), is(3));
        assertThat("Experiment had homePage1 evaluated 4 times", experiment.countOutcomesFor(homePage1), is(4));
        assertThat("Experiment had homePage2 evaluated 2 times", experiment.countOutcomesFor(homePage2), is(2));
        assertThat("Experiment had homePage3 evaluated once", experiment.countOutcomesFor(homePage3), is(1));

        try {
            ((MultiOutcomeExperiment)experiment).addOption(new Option("home4.html"));
            fail("Should not have been able to create a new option in an already running experiment");
        } catch (IllegalStateException ise) {
        }

        experiment.end();

        try {
            experiment.evaluateOutcomeFor(server1);
            fail("Cannot evaluate outcome because experiment has ended.");
        } catch (IllegalStateException ise) {
        }

        repository.store(experiment);

        experiment = repository.findExperimentBy(name);

        try {
            experiment.evaluateOutcomeFor(server1);
            fail("Cannot evaluate outcome because experiment has ended.");
        } catch (IllegalStateException ise) {
        }
    }
}
