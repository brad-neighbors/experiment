package com.incandescent.lean.experiment;

/**
 * Encapsulates an experiment that can have multiple outcomes (beyond just two).
 * @author Brad Neighbors
 */
public class MultiOutcomeExperiment extends Experiment {

    /**
     * Creates the experiment.
     * @param experimentId the experiment identifier
     */
    public MultiOutcomeExperiment(ExperimentId experimentId) {
        super(experimentId);
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
