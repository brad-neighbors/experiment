package com.incandescent.lean.experiment;

import com.incandescent.value.*;

/**
 * Identifies an outcome of an experiment.
 * @author Brad Neighbors
 */
public class Option extends StringValue implements Comparable<Option> {

    /** Database primary key. */
    private Integer id;

    /** Database foreign key. */
    private Integer experimentId;

    /** Sequence so that options can be ordered in the experiment. */
    protected Integer sequence = 0;

    /**
     * Creates the option.
     * @param optionName the name or value of the option.
     */
    public Option(String optionName) {
        super(optionName);
    }

    @Override
    public int compareTo(Option option) {
        return sequence.compareTo(option.sequence);
    }
}
