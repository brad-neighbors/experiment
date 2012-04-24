package com.incandescent.lean.experiment;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Encapsulates an experiment that can only have two options, the classic "A/B" test.
 * @author Brad Neighbors
 */
public class AorBExperiment extends Experiment {

    /**
     * Creates the experiment with the two options.
     * @param name the experiment identifier
     * @param optionA the first option (A)
     * @param optionB the other option (B)
     */
    public AorBExperiment(ExperimentName name, Option optionA, Option optionB) {
        super(name);
        addOption(optionA);
        addOption(optionB);
    }

    /**
     * Creates the experiment with the two options to be supplied later.
     * @param name the experiment name
     */
    public AorBExperiment(ExperimentName name) {
        super(name);
    }

    @Override
    public void addOption(Option option) {
        notNull(option, "Parameter option cannot be null.");
        if (options.size() == 2) {
            throw new IllegalArgumentException("Cannot add more than two options in an AorBExperiment");
        }
        if (options.isEmpty()) {
            super.addOption(option);
        } else {
            super.addOption(option);
        }
    }

    /**
     * Specifies that the given subject will always evaluate to be given the outcome defined as Option A.
     * @param subject the subject to receive option A during the experiment
     */
    public void specifySubjectOutcomeA(Subject subject) {
        if (options.size() == 0) {
            throw new IllegalStateException("Cannot specify an outcome of option A until set.");
        }
        super.specifySubjectOutcome(subject, options.first());
    }

    /**
     * Specifies that the give subject will always evaluate to be given the outcome defined as Option B.
     * @param subject the subject to receive option B during the experiment
     */
    public void specifySubjectOutcomeB(Subject subject) {
        if (options.size() < 2) {
            throw new IllegalStateException("Cannot specify an outcome of option B until set.");
        }
        super.specifySubjectOutcome(subject, options.last());
    }

    /**
     * @return The number of times option A was evaluated during the experiment.
     */
    public int countOutcomesOfOptionA() {
        return super.countOutcomesOf(options.first());
    }

    /**
     * @return The number of times option B was evaluated during the experiment.
     */
    public int countOutcomesOfOptionB() {
        return super.countOutcomesOf(options.last());
    }

    @Override
    public void start() {
        if (options.size() != 2) {
            throw new IllegalStateException("Cannot start AorBExperiment until both options have been set.");
        }
        super.start();
    }
}
