package com.incandescent.lean.experiment;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MultiOutcomeExperimentTest {

    @Test
    public void atTheMomentNotMuchMoreThanAnExtensionOfAnExperiment() {
        final MultiOutcomeExperiment experiment = new MultiOutcomeExperiment(new ExperimentId("123"));

        final Option option1 = new Option("1");
        final Option option2 = new Option("2");
        experiment.addOption(option1);
        experiment.addOption(option2);

        final Subject johnDoe = new Subject("John Doe");
        final Subject janeDoe = new Subject("Jane Doe");
        experiment.specifySubjectOutcome(johnDoe, option1);
        experiment.specifySubjectOutcome(janeDoe, option2);

        experiment.start();
        assertThat(experiment.evaluateOutcomeFor(johnDoe), is(option1));
        assertThat(experiment.evaluateOutcomeFor(janeDoe), is(option2));
    }
}
