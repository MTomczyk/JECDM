package io.trial;

import exception.TrialException;
import indicator.IIndicator;
import scenario.Scenario;

/**
 * Container-like class for {@link ITrialLoader}. During the processing initialization, the reference trial loaders
 * (provided by the programmer) are cloned so that each such loader produces one copy per indicator (per-indicator results
 * are stored in different files). These clones are stored within this class's instances.
 *
 * @author MTomczyk
 */
public class TLPerIndicator
{
    /**
     * One loader (clone of the reference loader) per indicator.
     */
    private ITrialLoader[] _loaderPerIndicator;

    /**
     * Parameterized constructor.
     *
     * @param referenceLoader reference loader
     * @param indicators      performance indicators
     * @param path            full path to the folder where the result files are stored (without a path separator)
     * @param scenario        scenario being currently processed
     * @param trialID         ID of the test run being currently processed
     * @throws TrialException trial-level exception can be thrown 
     */
    public TLPerIndicator(ITrialLoader referenceLoader,
                          IIndicator[] indicators,
                          String path,
                          Scenario scenario,
                          int trialID) throws TrialException
    {
        try
        {
            _loaderPerIndicator = new ITrialLoader[indicators.length];
            for (int i = 0; i < indicators.length; i++)
                _loaderPerIndicator[i] = referenceLoader.getInstance(path, indicators[i].getName(), scenario, trialID);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, scenario, trialID);
        }
    }

    /**
     * Auxiliary method for retrieving the results that are stored in the files.
     *
     * @param size determines the number of elements to load (by default = doubles)
     * @return loaded data stored as a two-dimensional matrix, where the first dimension is linked to different loaders,
     * while the latter to data with a specified size
     * @throws TrialException trial-level exception can be thrown 
     */
    public double[][] retrieve(int size) throws TrialException
    {
        double[][] result = new double[_loaderPerIndicator.length][];
        for (int i = 0; i < _loaderPerIndicator.length; i++)
            result[i] = _loaderPerIndicator[i].retrieve(size);
        return result;
    }


    /**
     * Auxiliary method for opening the trial-level results files.
     *
     * @throws TrialException trial-level exception can be thrown 
     */
    public void openResultsFiles() throws TrialException
    {
        for (ITrialLoader tl : _loaderPerIndicator) tl.load();
    }

    /**
     * Auxiliary method for closing trial-level results files (closes, e.g., FileOutputStreams).
     *
     * @throws TrialException trial-level exception can be thrown 
     */
    public void closeResultsFiles() throws TrialException
    {
        for (ITrialLoader tl : _loaderPerIndicator) tl.close();
    }

    /**
     * Getter for the trial loader associated with an indicator.
     *
     * @param indicatorID indicator ID (corresponds to array index)
     * @return trial loader
     */
    public ITrialLoader getTrialLoaderForIndicator(int indicatorID)
    {
        return _loaderPerIndicator[indicatorID];
    }

    /**
     * Auxiliary method for clearing the data.
     */
    public void dispose()
    {
        _loaderPerIndicator = null;
    }
}
