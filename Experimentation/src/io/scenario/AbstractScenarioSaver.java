package io.scenario;

import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import io.AbstractIO;
import scenario.Scenario;
import statistics.IStatistic;

import java.io.FileOutputStream;

/**
 * Abstract implementation of {@link IScenarioSaver}. Provides common fields/functionalities.
 *
 * @author MTomczyk
 */


public abstract class AbstractScenarioSaver extends AbstractIO implements IScenarioSaver
{
    /**
     * File output stream object that can be used to store the data.
     */
    protected FileOutputStream _fileOutputStream;

    /**
     * IDs of trial whose results are to be processed.
     */
    protected int[] _trialIDs;

    /**
     * Performance indicators employed when assessing the performance of EAs.
     */
    protected IIndicator[] _indicators;

    /**
     * Statistic functions used to aggregate the data.
     */
    protected IStatistic[] _statistics;

    /**
     * Currently processed indicator.
     */
    protected IIndicator _currentIndicator;

    /**
     * Expected number of generations to be processed
     */
    protected int _expectedNumberOfGenerations;

    /**
     * Parameterized constructor.
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   currently processed scenario
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     */
    public AbstractScenarioSaver(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics)
    {
        super(path, filename, scenario, 1);
        _trialIDs = trialIDs;
        _indicators = indicators;
        _statistics = statistics;
    }

    /**
     * Creates a new instance of the object. Intended to be used by {@link ScenariosSummarizer} to clone the
     * initial object instance one time per each scenario involved (i.e., one clone will be mapped to one scenario).
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   scenario being currently processed
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     * @return new object instance
     * @throws ScenarioException scenario-level exception can be cast and propagated higher
     */
    @Override
    public IScenarioSaver getInstance(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics) throws ScenarioException
    {
        throw new ScenarioException("The \"get instance\" method is not implemented", null, this.getClass(), _scenario);
    }

    /**
     * Creates and returns the full path based on the folder path, filename, and saver's suffix (e.g., extension).
     *
     * @param path     full path to the folder where the file should be stored (without a path separator)
     * @param filename the filename (without the suffix, e.g., extension)
     * @return full path
     */
    @Override
    protected String getFullPath(String path, String filename)
    {
        return super.getFullPath(path, filename) + getFileSuffix();
    }

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown (e.g., then the requested path is invalid)
     */
    @Override
    public void create() throws ScenarioException
    {
        throw new ScenarioException("The \"create\" method is not implemented", null, this.getClass(), _scenario);
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data begins. The method can also notify
     * the saver about the number of generations throughout which the trial-level data was collected.
     *
     * @param indicator   indicator
     * @param generations number of generations
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingBegins(IIndicator indicator, int generations) throws ScenarioException
    {
        _currentIndicator = indicator;
        _expectedNumberOfGenerations = generations;
    }

    /**
     * The main method for pushing data to be stored. The data provided is assumed to come from all trials concerning
     * one generation and one indicator (the generation number is not provided; it is assumed that all generations per
     * indicator are provided sequentially). IMPORTANT NOTE: The data supplied is supposed to be organized. The double
     * vectors from different indicators cannot be interlaced. As mentioned above, these vectors will also come in the
     * ascending order of generations, and each will completely cover the results obtained within a generation.
     *
     * @param trialResults trial results
     * @param statistics   statistics calculated from trial results (1:1 mapping with statistic objects stored in {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        throw new ScenarioException("The \"push data\" method is not implemented", null, this.getClass(), _scenario);
    }

    /**
     * A method for notifying the saver that the data processing linked to the currently involved indicator ends.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingEnds() throws ScenarioException
    {
        _currentIndicator = null;
        _expectedNumberOfGenerations = 0;
    }

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void close() throws ScenarioException
    {
        throw new ScenarioException("The \"close\" method is not implemented", null, this.getClass(), _scenario);
    }

}
