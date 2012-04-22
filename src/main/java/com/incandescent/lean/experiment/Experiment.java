package com.incandescent.lean.experiment;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.lang.Validate.notNull;

/**
 * Encapsulates an experiment.
 * <p/>
 * An experiment has multiple possible options.
 * Calling <code>evaluateOutcomeFor(Subject)</code> will result in one of the possible options that have been added using
 * <code>addOption(Option)</code>.
 * <p/>
 * The experiment can also be interrogated to see how many subjects have received certain outcomes using
 * <code>countOutcomesFor(Option)</code>.
 * <p/>
 * Experiments can have their options (possible outcomes) expanded before the experiment has begun, but not after.
 * Furthermore, subjects can be specified to have certain outcomes throughout the lifecycle of the experiment.
 * And one the experiment has ended, subjects cannot have their outcomes evaluated anymore either.
 *
 * @author Brad Neighbors
 */
public abstract class Experiment {

    protected ExperimentId id;
    protected Date startDate;
    private Date endDate;
    protected final Map<Subject, Option> outcomes = new HashMap<Subject, Option>();
    protected final Set<Option> options = new HashSet<Option>();
    private Map<Option, Integer> outcomeCounts = new HashMap<Option, Integer>();

    /**
     * Creates a new experiment.
     * <p/>
     * At this point, options can be added, subjects can be specified to receive certain outcomes, but not outcomes
     * can be evaluated (either with a subject or randomly) until the experiment is started.
     * @param id the experiment identifier
     */
    public Experiment(ExperimentId id) {
        notNull(id, "Experiments require a non null identifier.");
        this.id = id;
    }

    /**
     * Adds a possible outcome to this experiment.
     * @param option the possible outcome
     */
    protected void addOption(Option option) {
        if (experimentHasStarted()) {
            throw new ExperimentStartedException(
              format("Cannot add option %s as experiment %s has already been started", option, id));
        }
        options.add(option);
    }

    /**
     * Specifies that the given subject will always have the outcome specified by the given option.
     * @param subject the subject
     * @param option the outcome the specified subject will always evaluate to in the experiment
     */
    protected void specifySubjectOutcome(Subject subject, Option option) {
        if (!options.contains(option)) {
            throw new UnknownOptionException(format("Option %s not known in experiment %s", option, id));
        }

        outcomes.put(subject, option);
    }

    /**
     * Evaluates the subject and produces an outcome from the possible outcomes specified as options.
     * <p/>
     * If the subject has not previously been specified to have a determined outcome, they will receive a random outcome
     * of the possible options.
     * @param subject the subject
     * @return The outcome for the subject.
     * @throws ExperimentNotYetStartedException If the experiment has not yet started.
     * @throws ExperimentEndedException If the experiment is already finished.
     */
    public Option evaluateOutcomeFor(Subject subject) {
        if (!experimentHasStarted()) {
            throw new ExperimentNotYetStartedException(
              format("Cannot evaluate outcome for %s in experiment %s because it hasn't started yet", subject, id));
        }
        if (experimentHasEnded()) {
            throw new ExperimentEndedException(
              format("Cannot evaluate outcome for %s in experiment %s because it has ended.", subject, id));
        }
        if (outcomes.containsKey(subject)) {
            final Option option = outcomes.get(subject);
            incrementChosenCountFor(option);
            return option;
        }
        return randomOutcome();
    }

    /**
     * Gives a random outcome.
     * @return a random outcome based on the options added to the experiment.
     * @throws ExperimentNotYetStartedException If the experiment hasn't yet started.
     * @throws ExperimentEndedException If the experiment is already finished.
     */
    public Option randomOutcome() {
        if (!experimentHasStarted()) {
            throw new ExperimentNotYetStartedException(
              format("Cannot get a random outcome in experiment %s because it hasn't started yet", id));
        }
        if (experimentHasEnded()) {
            throw new ExperimentEndedException(
              format("Cannot get a random outcome in experiment %s because it has ended.", id));
        }
        final int index = new Random().nextInt(options.size());
        final Option chosen = (Option) options.toArray()[index];
        incrementChosenCountFor(chosen);
        return chosen;
    }

    /**
     * Counts the occurences that the specified option was returned as an outcome during the experiment.
     * @param option the possible outcome
     * @return The number of times the outcome happened in the experiment.
     */
    public int countOutcomesFor(Option option) {
        if (outcomeCounts.containsKey(option)) {
            return outcomeCounts.get(option);
        }
        return 0;
    }

    /**
     * Starts the experiment.  After this no further options can be added as possible outcomes, and outcome evaluation
     * can begin.
     */
    public void start() {
        startDate = new Date();
    }

    private boolean experimentHasStarted() {
        return startDate != null;
    }

    private void incrementChosenCountFor(Option chosen) {
        if (outcomeCounts.containsKey(chosen)) {
            outcomeCounts.put(chosen, outcomeCounts.get(chosen) + 1);
        } else {
            outcomeCounts.put(chosen, 1);
        }
    }

    /** Ends the experiment.  After this no further outcomes may be evaluated. */
    public void end() {
        endDate = new Date();
    }

    private boolean experimentHasEnded() {
        return endDate != null;
    }
}
