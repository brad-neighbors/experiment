package com.incandescent.lean.experiment;

import static org.apache.commons.lang.Validate.notNull;

/**
 * Encapsulates an experiment that can only have two options, the classic "A/B" test.
 * @author Brad Neighbors
 */
public class AorBExperiment extends Experiment {

    private final Option optionA;
    private final Option optionB;

    /**
     * Creates the experiment
     * @param id the experiment identifier
     * @param optionA the first option (A)
     * @param optionB the other option (B)
     */
    public AorBExperiment(ExperimentId id, Option optionA, Option optionB) {
        super(id);
        notNull(optionA, "Option A cannot be null.");
        notNull(optionB, "Option B cannot be null.");

        this.optionA = optionA;
        addOption(optionA);

        this.optionB = optionB;
        addOption(optionB);
    }

    /**
     * Specifies that the given subject will always evaluate to be given the outcome defined as Option A.
     * @param subject the subject to receive option A during the experiment
     */
    public void specifySubjectOutcomeA(Subject subject) {
        super.specifySubjectOutcome(subject, optionA);
    }

    /**
     * Specifies that the give subject will always evaluate to be given the outcome defined as Option B.
     * @param subject the subject to receive option B during the experiment
     */
    public void specifySubjectOutcomeB(Subject subject) {
        super.specifySubjectOutcome(subject, optionB);
    }

    /**
     * @return The number of times option A was evaluated during the experiment.
     */
    public int countOptionAOutcomes() {
        return super.countOutcomesFor(optionA);
    }

    /**
     * @return The number of times option B was evaluated during the experiment.
     */
    public int countOptionBOutcomes() {
        return super.countOutcomesFor(optionB);
    }
}
