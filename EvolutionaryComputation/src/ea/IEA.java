package ea;

import criterion.Criteria;
import exception.EAException;
import os.ObjectiveSpaceManager;
import population.SpecimensContainer;
import random.IRandom;
import reproduction.ReproductionStrategy;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import system.ds.DecisionSupportSystem;

/**
 * Interface for classes representing evolutionary algorithms.
 *
 * @author MTomczyk
 */
public interface IEA
{
    /**
     * Init method. Should be treated as the first generation during which an initial population is constructed,
     * evaluated, and other auxiliary operations (e.g., sorting) are performed.
     *
     * @throws EAException an exception can be thrown
     */
    void init() throws EAException;

    /**
     * Should execute one step of the evolutionary algorithm.
     *
     * @param timestamp generation; steady-state repeat
     * @throws EAException the exception can be thrown
     */
    void step(EATimestamp timestamp) throws EAException;

    /**
     * Getter for the method's name.
     *
     * @return method's name
     */
    String getName();

    /**
     * Getter for the method's auxiliary ID.
     *
     * @return method's auxiliary id
     */
    int getID();

    /**
     * Getter for the current generation.
     *
     * @return current generation
     */
    int getCurrentGeneration();

    /**
     * Getter for the current steady-state repeat.
     *
     * @return current steady-state repeat
     */
    int getCurrentSteadyStateRepeat();

    /**
     * Getter for the current population size.
     *
     * @return population size
     */
    int getPopulationSize();

    /**
     * Getter for the offspring size.
     *
     * @return offspring size
     */
    int getOffspringSize();

    /**
     * Default getter for the number of steady state repeats the method is supposed to run for.
     *
     * @return 1 (for generational algorithms)
     */
    default int getExpectedNumberOfSteadyStateRepeats()
    {
        return 1;
    }

    /**
     * Getter for the objective space manager.
     *
     * @return objective space manager
     */
    ObjectiveSpaceManager getObjectiveSpaceManager();


    /**
     * Getter for the auxiliary object constructing normalization functions using data on the current known bounds on
     * the relevant part of the objective space. Primarily used in the context of evolutionary multi-objective
     * optimization. The default implementation returns a new instance of {@link StandardLinearBuilder}.
     *
     * @return normalization builder object
     */
    default INormalizationBuilder getNormalizationBuilder()
    {
        return new StandardLinearBuilder();
    }

    /**
     * Getter for the criteria considered when solving the problem.
     *
     * @return considered criteria
     */
    Criteria getCriteria();

    /**
     * Getter for the random number generator maintained by the EA.
     *
     * @return random number generator
     */
    IRandom getR();

    /**
     * Should return an information on whether the total execution time (as well as other implementation-dependent
     * times) are measured or not (in ms).
     *
     * @return true, if the execution times are being measured when executing the method; false otherwise
     */
    boolean getComputeExecutionTimes();

    /**
     * Getter for the specimens container maintained by the EA (wraps e.g., current population, mating pool,
     * offspring).
     *
     * @return specimens container
     */
    SpecimensContainer getSpecimensContainer();

    /**
     * Getter for the total execution time that has passed when executing the init and step methods (in ms).
     *
     * @return the total execution time (in ms)
     */
    double getExecutionTime();

    /**
     * Getter for the decision support system (returns null if a method does not employ any).
     *
     * @return decision support system (null, if not employed)
     */
    default DecisionSupportSystem getDecisionSupportSystem()
    {
        return null;
    }

    /**
     * Auxiliary method for disposing data.
     */
    default void dispose()
    {

    }

    /**
     * Getter for the reproduction strategy employed by the evolutionary algorithm.
     *
     * @return reproduction strategy employed by the evolutionary algorithm
     */
    default ReproductionStrategy getReproductionStrategy()
    {
        return null;
    }
}
