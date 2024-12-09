package io.scenario;

import exception.ScenarioException;
import indicator.IIndicator;
import statistics.IStatistic;

import java.util.LinkedList;

/**
 * Simple wrapper for instances of {@link IScenarioSaver}.
 *
 * @author MTomczyk
 */
public class ScenarioSavers
{
    /**
     * Wrapped savers.
     */
    private final LinkedList<IScenarioSaver> _savers;

    /**
     * Parameterized constructor.
     *
     * @param savers savers
     */
    public ScenarioSavers(LinkedList<IScenarioSaver> savers)
    {
        _savers = savers;
    }


    /**
     * A method for notifying the savers that the processing of indicator-related data begins.
     * The method can also notify the savers about the number of generations throughout which the trial-level data was collected.
     *
     * @param indicator   indicator
     * @param generations number of generations
     * @throws ScenarioException scenario-level exception can be thrown
     */
    public void notifyIndicatorProcessingBegins(IIndicator indicator, int generations) throws ScenarioException
    {
        for (IScenarioSaver s : _savers) s.notifyIndicatorProcessingBegins(indicator, generations);
    }

    /**
     * This method pre-calculates the statistics from the obtained raw trial results for a given generation and passes
     * them to savers (along with the generation number and raw trial results).
     *
     * @param trialResults raw trial results
     * @param statistics   statistic functions to be applied to raw trial results
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    public void pushData(double[] trialResults, IStatistic[] statistics, int generation) throws ScenarioException
    {
        double[] stats = new double[statistics.length];
        for (int i = 0; i < stats.length; i++) stats[i] = statistics[i].calculate(trialResults.clone());
        for (IScenarioSaver s : _savers) s.pushData(trialResults.clone(), stats, generation);  // cloning for safety
    }

    /**
     * A method for notifying the savers that the data processing linked to the currently involved indicator ends.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    public void notifyIndicatorProcessingEnds() throws ScenarioException
    {
        for (IScenarioSaver s : _savers) s.notifyIndicatorProcessingEnds();
    }

    /**
     * Request each saver to create a file.
     *
     * @throws ScenarioException scenario-level exception can be thrown and propagated higher
     */
    public void createFiles() throws ScenarioException
    {
        for (IScenarioSaver s : _savers) s.create();
    }

    /**
     * Request each saver to close a file.
     *
     * @throws ScenarioException scenario-level exception can be thrown and propagated higher
     */
    public void closeFiles() throws ScenarioException
    {
        for (IScenarioSaver s : _savers) s.close();
    }
}
