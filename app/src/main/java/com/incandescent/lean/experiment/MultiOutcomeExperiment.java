package com.incandescent.lean.experiment;

import java.util.Date;

/**
 * Encapsulates an experiment that can have multiple outcomes (beyond just two).
 * @author Brad Neighbors
 */
public class MultiOutcomeExperiment extends Experiment {

    /**
     * Creates the experiment.
     * @param experimentName the experiment identifier
     */
    public MultiOutcomeExperiment(ExperimentName experimentName) {
        super(experimentName);
    }

    @Override
    public void addOption(Option option) {
        super.addOption(option);
    }

    @Override
    public void specifySubjectOutcome(Subject subject, Option option) {
        super.specifySubjectOutcome(subject, option);
    }
}
