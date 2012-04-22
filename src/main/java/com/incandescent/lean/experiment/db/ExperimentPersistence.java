package com.incandescent.lean.experiment.db;

import com.incandescent.lean.experiment.Experiment;
import com.incandescent.lean.experiment.ExperimentName;
import com.incandescent.lean.experiment.MultiOutcomeExperiment;

/**
 * Specifies how experiments may be persisted.
 * @author Brad Neighbors
 */
public interface ExperimentPersistence {

    /**
     * Stores (insert or update) an experiment in a database.
     * @param experiment the experiment to persist
     */
    void store(Experiment experiment);

    /**
     * Finds the MultiOutcomeExperiment with the specified name.
     * @param name the experiment name
     * @return The experiment, or <code>null</code> if not found.
     */
    MultiOutcomeExperiment findMultiOutcomeExperimentBy(ExperimentName name);
}
