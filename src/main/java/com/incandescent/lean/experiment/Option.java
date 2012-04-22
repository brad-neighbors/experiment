package com.incandescent.lean.experiment;

import com.incandescent.value.*;

/**
 * Identifies an outcome of an experiment.
 * @author Brad Neighbors
 */
public class Option extends StringValue {

    private Integer id;
    private Integer experimentId;

    public Option(String optionName) {
        super(optionName);
    }
}
