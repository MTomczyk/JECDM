package io.utils.pusher;

import exception.ScenarioException;
import statistics.IStatistic;

/**
 * Auxiliary interface for objects that are wrappers for various savers.
 * It unifies the pushing procedure.
 *
 * @author MTomczyk
 */
public interface IPusher
{
    /**
     * The main method for pushing data to be stored. The data provided is assumed to come from all trials concerning
     * one generation and one indicator (the generation number is not provided; it is assumed that all generations per
     * indicator are provided sequentially). IMPORTANT NOTE: The data supplied is supposed to be organized. The double
     * vectors from different indicators cannot be interlaced. As mentioned above, these vectors will also come in the
     * ascending order of generations, and each will completely cover the results obtained within a generation.
     *
     * @param trialResults raw trial results
     * @param statistics   statistic functions to be applied to raw trial results
     * @param generation   current generation number
     * @throws ScenarioException the scenario-level exception can be thrown 
     */
    void pushData(double[] trialResults, IStatistic[] statistics, int generation) throws ScenarioException;
}
