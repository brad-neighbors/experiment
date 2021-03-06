package com.incandescent.lean.experiment;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Brad Neighbors
 */
@SuppressWarnings("unchecked")
public class ExperimentTest {

    private Option optionA, optionB, optionC;
    private ExampleExperiment abcExperiment;

    @BeforeMethod
    public void prepare() {
        optionA = new Option("A");
        optionB = new Option("B");
        optionC = new Option("C");

        abcExperiment = new ExampleExperiment(new ExperimentName("Home Page"));
        abcExperiment.addOption(optionA);
        abcExperiment.addOption(optionB);
        abcExperiment.addOption(optionC);
    }

    /** A simple extension of the abstract Experiment to test with. */
    private static class ExampleExperiment extends Experiment {

        public ExampleExperiment(ExperimentName name) {
            super(name);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void experimentsMustHaveAnIdentifier() {
        new ExampleExperiment(null);
    }

    @Test
    public void canRandomlyBeGivenOutcome() {
        abcExperiment.start();
        assertThat(abcExperiment.randomOutcome(), is(anyOf(equalTo(optionA), equalTo(optionB), equalTo(optionC))));
    }

    @Test(expectedExceptions = ExperimentNotYetStartedException.class)
    public void cannotBeRandomlyGivenOutcomeIfNotYetStarted() {
        abcExperiment.randomOutcome();
    }

    @Test(expectedExceptions = ExperimentEndedException.class)
    public void cannotBeRandomlyGivenOutcomeIfExperimentEnded() {
        abcExperiment.start();
        abcExperiment.end();
        abcExperiment.randomOutcome();
    }

    @Test
    public void reportsCountOfOutcomesChosen() {
        abcExperiment.start();
        abcExperiment.randomOutcome();
        assertThat(1, is(anyOf(
          equalTo(abcExperiment.countOutcomesOf(optionA)),
          equalTo(abcExperiment.countOutcomesOf(optionB)),
          equalTo(abcExperiment.countOutcomesOf(optionC)))));
    }

    @Test
    public void ifSubjectSpecifiedToGetOptionThenItWillAlwaysHaveThatOutcome() {
        abcExperiment.start();

        Subject johnDoe = new Subject("John Doe");
        abcExperiment.specifySubjectOutcome(johnDoe, optionB);

        for (int i = 0; i < 10; i++) {
            assertThat(abcExperiment.evaluateOutcomeFor(johnDoe), is(optionB));
        }
        assertThat(abcExperiment.countOutcomesOf(optionB), is(10));

        Subject janeDoe = new Subject("Jane Doe");
        abcExperiment.specifySubjectOutcome(janeDoe, optionC);

        for (int i = 0; i < 100; i++) {
            assertThat(abcExperiment.evaluateOutcomeFor(janeDoe), is(optionC));
        }
        assertThat(abcExperiment.countOutcomesOf(optionC), is(equalTo(100)));
    }

    @Test(expectedExceptions = ExperimentNotYetStartedException.class)
    public void cannotEvaluateOutcomeIfExperimentHasNotStarted() {
        abcExperiment.evaluateOutcomeFor(new Subject("John Doe"));
    }

    @Test
    public void ifSubjectHasNotBeenSpecifiedToGetOutcomeThenItWillGetARandomOutcome() {
        Subject johnDoe = new Subject("John Doe");
        abcExperiment.start();
        for (int i = 0; i < 10; i++) {
            assertThat(abcExperiment.evaluateOutcomeFor(johnDoe),
              is(anyOf(equalTo(optionA), equalTo(optionB), equalTo(optionC))));
        }
    }

    @Test(expectedExceptions = UnknownOptionException.class)
    public void cannotSpecifyAnUnknownOptionAsAnOutcomeForASubject() {
        Subject johnDoe = new Subject("John Doe");
        abcExperiment.specifySubjectOutcome(johnDoe, new Option("unknown"));
    }

    @Test(expectedExceptions = ExperimentStartedException.class)
    public void cannotAddOptionAfterStarted() {
        abcExperiment.start();
        abcExperiment.addOption(new Option("D"));
    }

    @Test(expectedExceptions = ExperimentEndedException.class)
    public void cannotEvaluateAnyMoreOutcomesAfterEndingTheExperiment() {
        abcExperiment.start();
        abcExperiment.end();
        abcExperiment.evaluateOutcomeFor(new Subject("John Doe"));
    }
}
