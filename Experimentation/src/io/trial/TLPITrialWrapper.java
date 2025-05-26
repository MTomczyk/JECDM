package io.trial;

import container.global.AbstractGlobalDataContainer;
import exception.ScenarioException;
import exception.TrialException;
import indicator.IIndicator;
import scenario.Scenario;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Container-like class for {@link TLPerIndicator} objects. It maps each involved trial ID with one
 * {@link TLPerIndicator} object.
 *
 * @author MTomczyk
 */
public class TLPITrialWrapper
{
    /**
     * Maps trial ID into trial loader per indicator object.
     */
    private HashMap<Integer, TLPerIndicator> _loaders;

    /**
     * List representation of the map's values.
     */
    private LinkedList<TLPerIndicator> _loadersList;

    /**
     * Parameterized constructor.
     *
     * @param GDC global data container
     */
    public TLPITrialWrapper(AbstractGlobalDataContainer GDC)
    {
        _loaders = new HashMap<>(GDC.getNoEnabledTrials());
        _loadersList = new LinkedList<>();
    }


    /**
     * The main method for supplying a map object with trial loaders.
     *
     * @param referenceLoader       reference to the reference loader
     * @param performanceIndicators reference to the performance indicators
     * @param path                  full path to the folder where the result files are stored (without a path separator)
     * @param scenario              scenario being currently processed
     * @param trialID               trial ID
     * @throws TrialException trial-level exception can be thrown and propagated higher
     */
    public void addLoaders(ITrialLoader referenceLoader,
                           IIndicator[] performanceIndicators,
                           String path,
                           Scenario scenario,
                           int trialID) throws TrialException
    {

        try
        {
            TLPerIndicator tlLoaders = new TLPerIndicator(referenceLoader, performanceIndicators, path, scenario, trialID);
            _loaders.put(trialID, tlLoaders);
            _loadersList.add(tlLoaders);
        } catch (Exception e)
        {
            throw new TrialException(e.getMessage(), this.getClass(), e, scenario, trialID);
        }
    }

    /**
     * Getter for the object storing loaders linked to indicators for a given trial ID.
     *
     * @param scenario scenario being currently processed
     * @param trialID  trial ID
     * @return trial loaders linked to indicators
     * @throws ScenarioException the exception will be thrown if loaders for a requested trial ID cannot be found
     */
    public TLPerIndicator getTLPerIndicator(Scenario scenario, int trialID) throws ScenarioException
    {
        if (!_loaders.containsKey(trialID))
            throw new ScenarioException("There are no loaders for a requested trial ID =" + trialID,
                    null, this.getClass(), scenario);
        return _loaders.get(trialID);
    }

    /**
     * Instantiates file input streams of all loaders.
     *
     * @throws TrialException trial-level exception can be thrown
     */
    public void openAllFiles() throws TrialException
    {
        for (TLPerIndicator tl : _loadersList) tl.openResultsFiles();
    }

    /**
     * Instantiates file input streams of all loaders.
     *
     * @throws TrialException trial level exception can be thrown
     */
    public void closeAllFiles() throws TrialException
    {
        for (TLPerIndicator tl : _loadersList) tl.closeResultsFiles();
    }


    /**
     * Auxiliary method for clearing the data.
     */
    public void dispose()
    {
        for (TLPerIndicator tl : _loaders.values()) tl.dispose();
        _loaders = null;
        _loadersList = null;
    }
}
