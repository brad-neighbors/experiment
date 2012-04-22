package com.incandescent.lean.experiment;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        new AorBExperiment(new ExperimentId("123"), null, optionB);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void mustSpecifyOptionB() {
        new AorBExperiment(new ExperimentId("123"), optionA, null);
    }

    @Test
    public void canSpecifyOutcomeOfEitherOption() {
        final AorBExperiment experiment = new AorBExperiment(new ExperimentId("123"), optionA, optionB);
        final Subject johnDoe = new Subject("John Doe");
        final Subject janeDoe = new Subject("Jane Doe");
        final Subject fooBar = new Subject("Foo Bar");
        experiment.specifySubjectOutcomeA(johnDoe);
        experiment.specifySubjectOutcomeB(janeDoe);
        experiment.specifySubjectOutcomeB(fooBar);

        experiment.start();

        assertThat(experiment.evaluateOutcomeFor(johnDoe), is(optionA));
        assertThat(experiment.countOptionAOutcomes(), is(1));

        assertThat(experiment.evaluateOutcomeFor(janeDoe), is(optionB));
        assertThat(experiment.evaluateOutcomeFor(fooBar), is(optionB));
        assertThat(experiment.countOptionBOutcomes(), is(2));
    }
}
