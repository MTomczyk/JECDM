package io.trial;

import exception.TrialException;
import indicator.IIndicator;
import scenario.Scenario;

/**
 * Container-like class for {@link io.trial.ITrialSaver}. During the processing initialization, the reference trial savers
 * (provided by the programmer) are cloned so that each such saver produces one copy per indicator (per-indicator results
 * are stored in different files). These clones are stored within this class's instances.
 *
 * @author MTomczyk
 */
public class TSPerIndicator
{
    /**
     * One saver (clone of the reference saver) per indicator (1:1 mapping).
     */
    private ITrialSaver[] _saverPerIndicator;

    /**
     * Parameterized constructor.
     *
     * @param referenceSaver reference saver
     * @param indicators     performance indicators
     * @param path           full path to the folder where the result file should be stored (without a path separator)
     * @param scenario       scenario being currently processed
     * @param trialID        ID of the test run being currently processed
     * @throws TrialException trial-level exception can be thrown and propagated higher
     */
    public TSPerIndicator(ITrialSaver referenceSaver,
                          IIndicator[] indicators,
                          String path,
                          Scenario scenario,
                          int trialID) throws TrialException
    {
        try
        {
            _saverPerIndicator = new ITrialSaver[indicators.length];
            for (int i = 0; i < indicators.length; i++)
                _saverPerIndicator[i] = referenceSaver.getInstance(path, indicators[i].getName(),
                        scenario, trialID);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, scenario, trialID);
        }
    }

    /**
     * Auxiliary method for storing the results in the files.
     *
     * @param results current results matrix (rows = linked to indicators, columns = linked to generations)
     * @param offset  determines the offset in the columns (starting index)
     * @param length  determines the number of columns involved in data saving
     * @throws TrialException trial-level exception can be thrown and propagated higher
     */
    public void pushResults(double[][] results, int offset, int length) throws TrialException
    {
        for (int i = 0; i < _saverPerIndicator.length; i++)
            _saverPerIndicator[i].store(results[i], offset, length);
    }

    /**
     * Auxiliary method for creating trial-level results files.
     *
     * @throws TrialException trial-level exception can be thrown and propagated higher
     */
    public void createResultsFiles() throws TrialException
    {
        for (ITrialSaver ts : _saverPerIndicator) ts.create();
    }

    /**
     * Auxiliary method for closing trial-level results files (closes, e.g., FileOutputStreams).
     *
     * @throws TrialException trial-level exception can be thrown and propagated higher
     */
    public void closeResultsFiles() throws TrialException
    {
        for (ITrialSaver ts : _saverPerIndicator) ts.close();
    }

    /**
     * Auxiliary method for clearing the data.
     */
    public void dispose()
    {
        _saverPerIndicator = null;
    }
}
