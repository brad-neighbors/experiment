package com.incandescent.lean.experiment;

import com.incandescent.value.*;

/**
 * Identifies the subject in an experiment.
 * @author Brad Neighbors
 */
public class Subject extends StringValue {

    private Integer id;
    private Integer optionId;

    public Subject(String value) {
        super(value);
    }
}
