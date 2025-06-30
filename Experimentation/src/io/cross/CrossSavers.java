package io.cross;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import exception.ScenarioException;
import indicator.IIndicator;
import scenario.Scenario;
import statistics.IStatistic;
import unified.UnifiedIndicators;
import unified.UnifiedStatistics;

import java.util.LinkedList;

/**
 * Simple wrapper for instances of {@link ICrossSaver}.
 *
 * @author MTomczyk
 */
public class CrossSavers
{
    /**
     * Wrapped savers.
     */
    private final LinkedList<ICrossSaver> _savers;

    /**
     * Parameterized constructor.
     *
     * @param savers savers
     */
    public CrossSavers(LinkedList<ICrossSaver> savers)
    {
        _savers = savers;
    }

    /**
     * Methods for notifying the stored savers about unified indicators and statistics (unified across all crossed scenarios).
     *
     * @param indicators unified indicators
     * @param statistics unified statistics
     */
    public void notifyAboutUnifiedData(UnifiedIndicators indicators, UnifiedStatistics statistics)
    {
        for (ICrossSaver s : _savers) s.notifyAboutUnifiedData(indicators, statistics);
    }


    /**
     * Request each saver to create a file.
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    public void createFiles() throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.create();
    }

    /**
     * Method for notifying the savers that the processing begins (prior to executing any scenario).
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    public void notifyProcessingBegins() throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.notifyProcessingBegins();
    }

    /**
     * Method for notifying the savers that the processing begins.
     *
     * @param scenario scenario that is to be processed
     * @param SDC      scenario data container linked to the scenario being currently processed (read-only)
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    public void notifyScenarioProcessingBegins(Scenario scenario, AbstractScenarioDataContainer SDC) throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.notifyScenarioProcessingBegins(scenario, SDC);
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data begins.
     *
     * @param indicator indicator
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be thrown
     */
    public void notifyIndicatorProcessingBegins(IIndicator indicator) throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.notifyIndicatorProcessingBegins(indicator);
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data ends.
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be thrown
     */
    public void notifyIndicatorProcessingEnds() throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.notifyIndicatorProcessingEnds();
    }

    /**
     * This method pre-calculates the statistics from the obtained raw trial results for a given generation and passes
     * them to savers (along with the generation number and raw trial results).
     *
     * @param trialResults raw trial results
     * @param statistics   statistic functions to be applied to raw trial results
     * @param generation   current generation number
     * @throws ScenarioException the scenario-level exception can be thrown
     */
    public void pushData(double[] trialResults, IStatistic[] statistics, int generation) throws ScenarioException
    {
        double[] stats = new double[statistics.length];
        for (int i = 0; i < stats.length; i++) stats[i] = statistics[i].calculate(trialResults.clone());
        for (ICrossSaver s : _savers) s.pushData(trialResults.clone(), stats, generation);  // cloning for safety
    }

    /**
     * Method for notifying the savers that the processing ends.
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    public void notifyScenarioProcessingEnds() throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.notifyScenarioProcessingEnds();
    }


    /**
     * Method for notifying the savers that the processing begins (after all scenarios are processed).
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    public void notifyProcessingEnds() throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.notifyProcessingEnds();
    }

    /**
     * Request each saver to close a file.
     *
     * @throws CrossedScenariosException the crossed-scenarios-level exception can be cast 
     */
    public void closeFiles() throws CrossedScenariosException
    {
        for (ICrossSaver s : _savers) s.close();
    }

}
