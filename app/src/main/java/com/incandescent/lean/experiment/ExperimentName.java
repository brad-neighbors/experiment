package com.incandescent.lean.experiment;

import com.incandescent.value.StringValue;

import static org.apache.commons.lang.Validate.isTrue;

/**
 * Identifies an experiment.
 * @author Brad Neighbors
 */
public class ExperimentName extends StringValue {

    public static final int MINIMUM_LENGTH = 5;

    /**
     * Creates the name value.
     * Experiments must have names longer than 5 characters.
     * @param name the name of the experiment
     */
    public ExperimentName(String name) {
        super(name);
        isTrue(name.length() >= MINIMUM_LENGTH, "Experiments must have names of aat least 5 characters");
    }
}
