package container.trial;

import ea.IEA;
import exception.TrialException;
import io.trial.ITrialSaver;
import runner.IRunner;
import scenario.Scenario;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Provides auxiliary methods for data validation (for trial-level).
 *
 * @author MTomczyk
 */
public class Validator
{
    /**
     * Reference to the currently processed scenario.
     */
    private final Scenario _scenario;

    /**
     * ID of a trial being currently processed.
     */
    private final int _trialID;

    /**
     * Parameterized constructor.
     *
     * @param scenario reference to the currently processed scenario
     * @param trialID  ID of a trial being currently processed
     */
    public Validator(Scenario scenario, int trialID)
    {
        _scenario = scenario;
        _trialID = trialID;
    }

    /**
     * Validates if the runner object is instantiated.
     *
     * @param runner reference to the runner
     * @throws TrialException the exception is thrown when trial savers are not provided (or are not unique)
     */
    protected void validateRunner(IRunner runner) throws TrialException
    {
        if (runner == null)
            throw new TrialException("The Runner is not instantiated", null, this.getClass(), _scenario, _trialID);
    }

    /**
     * Validates if the EA object is instantiated.
     *
     * @param ea reference to the evolutionary algorithm
     * @throws TrialException the exception is thrown when trial savers are not provided (or are not unique)
     */
    protected void validateEA(IEA ea) throws TrialException
    {
        if (ea == null)
            throw new TrialException("The Evolutionary Algorithm is not instantiated", null, this.getClass(), _scenario, _trialID);
    }

    /**
     * Validates if the trial savers are provided and are unique (based on a suffix comparison).
     *
     * @param trialSavers list of trial savers (objects responsible for storing trial-level data).
     * @throws TrialException the exception is thrown when trial savers are not provided (or are not unique)
     */
    protected void validateTrialSavers(LinkedList<ITrialSaver> trialSavers) throws TrialException
    {
        if (trialSavers == null)
            throw new TrialException("The trial savers are not provided (the list is null)", null, this
                    .getClass(), _scenario, _trialID);
        if (trialSavers.isEmpty())
            throw new TrialException("The trial savers are not provided (the list is empty)", null, this
                    .getClass(), _scenario, _trialID);

        Set<String> suffixes = new HashSet<>();
        for (ITrialSaver ts : trialSavers)
        {
            if (ts == null)
                throw new TrialException("One of the file savers is null", null, this.getClass(), _scenario, _trialID);
            if (suffixes.contains(ts.getFileSuffix()))
                throw new TrialException("The trial savers' file suffixes are not unique (" + ts.getFileSuffix() + " is duplicated)",
                        null, this.getClass(), _scenario, _trialID);
            suffixes.add(ts.getFileSuffix());
        }
    }

}
