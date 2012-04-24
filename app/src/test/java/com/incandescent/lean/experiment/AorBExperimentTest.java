package com.incandescent.lean.experiment;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Brad Neighbors
 */
public class AorBExperimentTest {

    private Option optionA;
    private Option optionB;

    @BeforeTest
    public void prepare() {
        optionA = new Option("A");
        optionB = new Option("B");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mustSpecifyOptionA() {
        new AorBExperiment(new ExperimentName("123"), null, optionB);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mustSpecifyOptionB() {
        new AorBExperiment(new ExperimentName("123"), optionA, null);
    }

    @Test
    public void canSpecifyOutcomeOfEitherOption() {
        final AorBExperiment experiment = new AorBExperiment(new ExperimentName("123"), optionA, optionB);
        final Subject johnDoe = new Subject("John Doe");
        final Subject janeDoe = new Subject("Jane Doe");
        final Subject fooBar = new Subject("Foo Bar");
        experiment.specifySubjectOutcomeA(johnDoe);
        experiment.specifySubjectOutcomeB(janeDoe);
        experiment.specifySubjectOutcomeB(fooBar);

        experiment.start();

        assertThat(experiment.evaluateOutcomeFor(johnDoe), is(optionA));
        assertThat(experiment.countOutcomesOfOptionA(), is(1));

        assertThat(experiment.evaluateOutcomeFor(janeDoe), is(optionB));
        assertThat(experiment.evaluateOutcomeFor(fooBar), is(optionB));
        assertThat(experiment.countOutcomesOfOptionB(), is(2));
    }

    @Test
    public void canAddOnlyTwoOptions() {
        final ExperimentName name = new ExperimentName("home page test");
        final AorBExperiment experiment = new AorBExperiment(name);
        experiment.addOption(optionA);
        experiment.addOption(optionB);

        try {
            experiment.addOption(new Option("page3"));
            fail("Should have only allowed adding two options to an AorBExperiment.");
        } catch (IllegalArgumentException iae) {
        }

        final AorBExperiment experiment2 = new AorBExperiment(name, optionA, optionB);
        try {
            experiment2.addOption(new Option("page3"));
            fail("Should have only allowed adding two options to an AorBExperiment.");
        } catch (IllegalArgumentException iae) {
        }
    }

    @Test
    public void cannotStartExperimentUnlessBothOptionsHaveBeenSet() {
        final ExperimentName name = new ExperimentName("home page test");
        final AorBExperiment experiment = new AorBExperiment(name);
        experiment.addOption(optionA);

        try {
            experiment.start();
            fail("Cannot start AorBExperiment until both options have been set.");
        } catch (IllegalStateException iae) {
        }
    }

    @Test
    public void cannotSpecifyASubjectOutcomeForOptionAIfNotYetSet() {
        final ExperimentName name = new ExperimentName("home page test");
        final AorBExperiment experiment = new AorBExperiment(name);
        try {
            experiment.specifySubjectOutcomeA(new Subject("server1"));
            fail("Should have not allowed outcome A to be specified because it has not been set");
        } catch (IllegalStateException ise) {
        }

    }

    @Test
    public void cannotSpecifyBSubjectOutcomeForOptionAIfNotYetSet() {
        final ExperimentName name = new ExperimentName("home page test");
        final AorBExperiment experiment = new AorBExperiment(name);
        experiment.addOption(optionA);

        try {
            experiment.specifySubjectOutcomeB(new Subject("server1"));
            fail("Should have not allowed outcome B to be specified because it has not been set");
        } catch (IllegalStateException ise) {
        }

    }
}
