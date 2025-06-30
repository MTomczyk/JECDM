package io.scenario;

import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import scenario.Scenario;
import statistics.IStatistic;

/**
 * Interface for classes responsible for preparing scenario-level results files by aggregating results stored
 * in per-trial files.
 *
 * @author MTomczyk
 */
public interface IScenarioSaver
{
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
     * @throws ScenarioException the scenario-level exception can be cast 
     */
    IScenarioSaver getInstance(String path,
                               String filename,
                               Scenario scenario,
                               int[] trialIDs,
                               IIndicator[] indicators,
                               IStatistic[] statistics) throws ScenarioException;

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    String getFileSuffix();

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws ScenarioException the scenario-level exception can be thrown (e.g., then the requested path is invalid)
     */
    void create() throws ScenarioException;

    /**
     * A method for notifying the saver that the processing of indicator-related data begins. The method can also notify
     * the saver about the number of generations throughout which the trial-level data was collected.
     *
     * @param indicator   indicator
     * @param generations number of generations
     * @throws ScenarioException scenario-level exception can be thrown
     */
    void notifyIndicatorProcessingBegins(IIndicator indicator, int generations) throws ScenarioException;

    /**
     * The main method for pushing data to be stored. The data provided is assumed to come from all trials concerning
     * one generation and one indicator (the generation number is not provided; it is assumed that all generations per
     * indicator are provided sequentially). IMPORTANT NOTE: The data supplied is supposed to be organized. The double
     * vectors from different indicators cannot be interlaced. As mentioned above, these vectors will also come in the
     * ascending order of generations, and each will completely cover the results obtained within a generation.
     *
     * @param trialResults raw trial results
     * @param statistics statistics calculated from trial results (1:1 mapping with statistic objects stored in {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    void pushData(double[] trialResults, double [] statistics, int generation) throws ScenarioException;

    /**
     * A method for notifying the saver that the data processing linked to the currently involved indicator ends.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    void notifyIndicatorProcessingEnds() throws ScenarioException;

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    void close() throws ScenarioException;
}
