package com.incandescent.lean.experiment.db;

import com.incandescent.lean.experiment.Experiment;
import com.incandescent.lean.experiment.ExperimentName;

/**
 * Specifies how experiments may be persisted.
 * @author Brad Neighbors
 */
public interface ExperimentRepository {

    /**
     * Stores (insert or update) an experiment in a database.
     * @param experiment the experiment to persist
     */
    void store(Experiment experiment);

    /**
     * Finds the Experiment with the specified name.
     * @param name the experiment name
     * @return The experiment, or <code>null</code> if not found.
     */
    Experiment findExperimentBy(ExperimentName name);
}