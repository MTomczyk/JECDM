package io.cross;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import exception.ScenarioException;
import executor.CrossSummarizer;
import indicator.IIndicator;
import scenario.CrossedScenarios;
import scenario.Scenario;
import unified.UnifiedIndicators;
import unified.UnifiedStatistics;

/**
 * Interfaces for classes responsible for saving the results of cross-analysis.
 *
 * @author MTomczyk
 */
public interface ICrossSaver
{
    /**
     * Creates a new instance of the object. Intended to be used by {@link CrossSummarizer} to clone the
     * initial object instance one time per each crossed scenarios object involved (i.e., one clone will be mapped
     * to one such object).
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @return new object instance
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    ICrossSaver getInstance(String path, String filename, CrossedScenarios crossedScenarios) throws CrossedScenariosException;

    /**
     * Method for notifying the object about unified indicators and statistics (unified across all crossed scenarios).
     *
     * @param indicators unified indicators
     * @param statistics unified statistics
     */
    void notifyAboutUnifiedData(UnifiedIndicators indicators, UnifiedStatistics statistics);

    /**
     * Method for notifying the savers that the processing begins (prior to executing any scenario).
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    void notifyProcessingBegins() throws CrossedScenariosException;

    /**
     * Method for notifying the savers that the processing begins.
     *
     * @param scenario scenario that is to be processed
     * @param SDC      scenario data container linked to the scenario being currently processed (read-only)
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    void notifyScenarioProcessingBegins(Scenario scenario, AbstractScenarioDataContainer SDC) throws CrossedScenariosException;

    /**
     * A method for notifying the saver that the processing of indicator-related data begins.
     *
     * @param indicator indicator
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    void notifyIndicatorProcessingBegins(IIndicator indicator) throws CrossedScenariosException;

    /**
     * A method for notifying the saver that the processing of indicator-related data ends.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    void notifyIndicatorProcessingEnds() throws CrossedScenariosException;

    /**
     * The main method for pushing data to be stored. The data provided is assumed to come from all trials concerning
     * one generation and one indicator (the generation number is not provided; it is assumed that all generations per
     * indicator are provided sequentially). IMPORTANT NOTE: The data supplied is supposed to be organized. The double
     * vectors from different indicators cannot be interlaced. As mentioned above, these vectors will also come in the
     * ascending order of generations, and each will completely cover the results obtained within a generation.
     *
     * @param trialResults raw trial results
     * @param statistics   statistics calculated from trial results (1:1 mapping with statistic objects stored in {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException;

    /**
     * Method for notifying the savers that the processing ends.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    void notifyScenarioProcessingEnds() throws CrossedScenariosException;


    /**
     * Method for notifying the savers that the processing begins (after all scenarios are processed).
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    void notifyProcessingEnds() throws CrossedScenariosException;


    /**
     * Returns the level (dimensionality) the saver can operate with.
     *
     * @return the dedicated level
     */
    int getDedicatedLevel();

    /**
     * Returns a suffix intended to be added to the file name (including the file extension).
     *
     * @return file suffix
     */
    String getFileSuffix();

    /**
     * Should be implemented to provide saver's default name (not file suffix).
     *
     * @return saver's default name
     */
    String getDefaultName();

    /**
     * The implementation should create a file (and overwrite it if already exists) and instantiate the output stream.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    void create() throws CrossedScenariosException;

    /**
     * The implementation should close the maintained output stream.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    void close() throws CrossedScenariosException;

    /**
     * Can be implemented to notify {@link CrossSummarizer} that the saver's instance (creation) should be skipped given
     * the crossed scenarios setting.
     *
     * @param crossedScenarios crossed scenarios
     * @return true, if the instance should be skipped; false otherwise
     */
    boolean shouldBeSkipped(CrossedScenarios crossedScenarios);


}
