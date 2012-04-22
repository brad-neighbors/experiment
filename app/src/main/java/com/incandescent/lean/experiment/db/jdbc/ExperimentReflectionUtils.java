package com.incandescent.lean.experiment.db.jdbc;

import com.incandescent.lean.experiment.Experiment;
import com.incandescent.lean.experiment.Option;
import com.incandescent.lean.experiment.Subject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A set of utilities to read and write fields on Experiments, used as a way to maintain good API encapsulation
 * on the Experiment domain class.
 * @author Brad Neighbors
 */
public class ExperimentReflectionUtils {

    /**
     * Retrieves the experiment's outcome counts.
     * @param experiment the experiment to interrogate
     * @return A map of the evaluation counts for all the options in the experiment.
     */
    static Map<Option, Integer> getOutcomeCounts(Experiment experiment) {
        try {
            Field outcomeCountsField = Experiment.class.getDeclaredField("outcomeCounts");
            outcomeCountsField.setAccessible(true);
            return (Map<Option, Integer>) outcomeCountsField.get(experiment);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
        return new HashMap<Option, Integer>();
    }

    /**
     * Retrieves the experiment's options.
     * @param experiment the experiment to interrogate
     * @return The options available in the experiment.
     */
    static Set<Option> getOptions(Experiment experiment) {
        try {
            Field optionsField = Experiment.class.getDeclaredField("options");
            optionsField.setAccessible(true);
            return (Set<Option>) optionsField.get(experiment);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
        return new HashSet<Option>();
    }

    /**
     * Retrieves the specified subject outcomes of the experiment.
     * @param experiment the experiment to interrogate
     * @return The subject outcomes that have been specified in the experiment.
     */
    static Map<Subject, Option> getOutcomes(Experiment experiment) {
        try {
            Field outcomesField = Experiment.class.getDeclaredField("outcomes");
            outcomesField.setAccessible(true);
            return (Map<Subject, Option>) outcomesField.get(experiment);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
        return new HashMap<Subject, Option>();
    }

    /**
     * Sets a field on an experiment.
     * @param experiment the experiment
     * @param fieldName the name of the field declared on the experiment class
     * @param fieldValue the value to set the field to
     */
    static void setExperimentField(Experiment experiment, String fieldName, Object fieldValue) {
        try {
            final Field field = Experiment.class.getDeclaredField(fieldName);
            final boolean wasAccessible = field.isAccessible();
            if (!wasAccessible) {
                field.setAccessible(true);
            }
            field.set(experiment, fieldValue);
            if (!wasAccessible) {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
    }

    /**
     * Gets the experiment's database primary key.
     * @param experiment the experiment to interrogate
     * @return The primary key id.
     */
    static Integer getExperimentId(Experiment experiment) {
        try {
            Field idField = Experiment.class.getDeclaredField("id");
            idField.setAccessible(true);
            return (Integer) idField.get(experiment);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
        return null;
    }
}