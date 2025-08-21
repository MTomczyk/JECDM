package runner;

import ea.EA;
import ea.IEA;
import exception.RunnerException;

/**
 * Interface for classes responsible for executing EAs.
 *
 * @author MTomczyk
 */
public interface IRunner
{
    /**
     * Initializes EAs. Default implementation: the method runs pre, main, and post-init phases (in the given order).
     *
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void init() throws RunnerException;

    /**
     * Executes the evolution (from scratch, i.e., calls inits etc.) for the specified number of generations.
     *
     * @param generations specified number of generations (the same for each evolutionary algorithm)
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void executeEvolution(int generations) throws RunnerException;

    /**
     * Executes the evolution (from scratch, i.e., calls inits etc.) for the specified number of generations.
     *
     * @param generations specified number of generations; each element corresponds to a different evolutionary algorithm
     *                    (1:1) mapping; hence, the array length should equal the number of algorithms
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void executeEvolution(int[] generations) throws RunnerException;

    /**
     * Performs a single generation of EAs. Default implementation: the method runs pre, main, and post init phases (in the given order).
     *
     * @param generation       current generation number
     * @param generationLimits limits for the number of generations an EA is allowed run (one element per each EA, 1:1 mapping;
     *                         note that the generation counter starts from 0);
     *                         this field excludes those EAs who have reached their generation limit;
     *                         can be null -> not used
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void executeSingleGeneration(int generation, int[] generationLimits) throws RunnerException;

    /**
     * Executes a single steady-state repeat of an EA.
     * Default implementation: the method runs pre, main, and post init phases (in the given order).
     *
     * @param ea                evolutionary algorithm whose single steady-state repeat is to be executed
     * @param generation        current generation number
     * @param steadyStateRepeat steady-state repeat number
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void executeSingleSteadyStateRepeat(IEA ea, int generation, int steadyStateRepeat) throws RunnerException;

    /**
     * Stops simulations. Default implementation: the method runs pre, main, and post init phases (in the given order).
     *
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void stop() throws RunnerException;

    /**
     * Optional method for terminating the execution and clearing data.
     *
     * @throws RunnerException the exception can be captured when executing the evolutionary algorithm 
     */
    void dispose() throws RunnerException;

    /**
     * Sets the number of steady-state repeats for a given EA (pointed by index). The method terminates
     * if the internal steadyStateRepeats is null or the pointer is invalid.
     * @param steadyStateRepeats new steady-state repeats number
     * @param index index pointing to the EA
     */
    void setSteadyStateRepeatsFor(int steadyStateRepeats, int index);
}
