package ea;

import criterion.Criteria;
import exception.EAException;
import os.ObjectiveSpaceManager;
import population.Specimen;
import population.SpecimensContainer;
import print.PrintUtils;
import random.IRandom;
import reproduction.ReproductionStrategy;
import space.normalization.builder.INormalizationBuilder;

import java.util.Objects;

/**
 * Abstract implementation of {@link IEA}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public class AbstractEA implements IEA
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Name of the EA.
         */

        public String _name;

        /**
         * Unique ID of the EA
         */
        public int _id;

        /**
         * Random number generator.
         */
        public IRandom _R;

        /**
         * Flag indicating whether the total execution time (as well as other implementation-dependent times) are
         * measured or not (in ms).
         */
        public boolean _computeExecutionTimes;

        /**
         * Considered criteria.
         */
        public Criteria _criteria;

        /**
         * Class responsible for storing and updating EA's data on the objective space.
         */
        public ObjectiveSpaceManager _osManager;

        /**
         * Auxiliary object constructing normalization functions using data on the current known bounds on the relevant
         * part of the objective space. Primarily used in the context of evolutionary multi-objective optimization.
         */
        protected INormalizationBuilder _normalizationBuilder = null;

        /**
         * Population size.
         */
        public int _populationSize = 0;

        /**
         * Offspring size. This field determines the number of offspring solutions to be generated within one execution
         * of the {@link AbstractEA#step(EATimestamp)} method (a complete generation or steady-state repeat).
         */
        public int _offspringSize = 0;

        /**
         * Reproduction strategy employed by the evolutionary algorithm. If null, it will be instantiated as a default
         * strategy that is considered constant and expects to produce one offspring specimen from one parent selection
         * during reproduction (see {@link ReproductionStrategy#getDefaultStrategy()}).
         */
        public ReproductionStrategy _reproductionStrategy = null;

        /**
         * The expected number of steady-state repeats the method is expected to run for.
         */
        public int _expectedNumberOfSteadyStateRepeats = 1;

        /**
         * This field serves as an additional cap on the number of offspring solutions that can be generated in one
         * generation. In most implementations, it can be left set to the max value. E.g., in generational
         * implementations, the number of offspring to produce is primarily determined by
         * {@link Params#_offspringSize}. In most implementations, it can be left set to the max value. E.g., in
         * generational implementations, the number of offspring to produce is primarily determined by
         * {@link Params#_offspringSize}. As for the steady-state implementations, they typically set
         * {@link Params#_offspringSize} to 1 and assume "multiple parents to one offspring" reproduction scheme, which
         * ultimately leads to producing the same number of offspring as the population size. Nonetheless, assume that
         * "multiple parents to two offspring" scheme is used in a steady-state algorithm. It would lead to producing
         * twice as many offspring as the population size dictates. If this is a desired strategy, then this field can
         * be left as it is. However, if one wants to impose an additional threshold, it can be done via this field. The
         * algorithm measures the number of constructed offspring throughout the generation. If the number would exceed
         * the allowed number, executing redundant steady-state repeats is skipped.
         */
        public int _offspringLimitPerGeneration = Integer.MAX_VALUE;

        /**
         * Parameterized constructor.
         *
         * @param name                  name of the EA
         * @param id,                   unique ID of the EA
         * @param R                     random number generator.
         * @param computeExecutionTimes flag indicating whether the total execution time (as well as other
         *                              implementation-dependent times) are measured or not (in ms)
         * @param criteria              considered criteria
         */
        protected Params(String name, int id, IRandom R, boolean computeExecutionTimes, Criteria criteria)
        {
            _name = name;
            _id = id;
            _R = R;
            _computeExecutionTimes = computeExecutionTimes;
            _criteria = criteria;
        }
    }

    /**
     * Name of the EA.
     */
    protected final String _name;

    /**
     * Unique ID of the EA
     */
    protected final int _id;

    /**
     * Population size.
     *
     * @deprecated visibility is to be changed to Private; use public (@link AbstractEA#getPopulationSize()) and
     * protected (@link AbstractEA#setPopulationSize(int)).
     */
    @Deprecated
    protected int _populationSize;

    /**
     * Offspring size. This field determines the number of offspring solutions to be generated within one execution of
     * the {@link AbstractEA#step(EATimestamp)} method (a complete generation or steady-state repeat).
     *
     * @deprecated visibility is to be changed to Private; use public ({@link AbstractEA#getOffspringSize()}) and
     * protected ({@link AbstractEA#setOffspringSize(int)}).
     */
    @Deprecated
    protected int _offspringSize;

    /**
     * Current timestamp: current generation and steady-state repeat (does nothing in the current version).
     *
     * @deprecated field moved to {@link RunningState} with the Private visibility; use public
     * ({@link AbstractEA#getCurrentGeneration()}, {@link AbstractEA#getCurrentSteadyStateRepeat()}) as for getters.
     */
    @Deprecated
    protected EATimestamp _currentTimestamp;

    /**
     * Data container for the EA's running state (current generation, steady-state number, etc.)
     */
    private final RunningState _runningState;

    /**
     * The reproduction strategy employed by the evolutionary algorithm.
     */
    protected final ReproductionStrategy _reproductionStrategy;

    /**
     * Specimens container maintained by the EA (wraps e.g., current population, mating pool, offspring).
     */
    protected SpecimensContainer _specimensContainer;

    /**
     * Class responsible for storing and updating EA's data on the objective space.
     *
     * @deprecated Field visibility is to change to Private. Use {@link AbstractEA#getObjectiveSpaceManager()}.
     */
    @Deprecated
    protected ObjectiveSpaceManager _osManager;

    /**
     * Considered criteria.
     */
    protected Criteria _criteria;

    /**
     * Random number generator.
     */
    protected final IRandom _R;

    /**
     * Flag indicating whether the total execution time (as well as other implementation-dependent times) are measured
     * or not (in ms).
     *
     * @deprecated field moved to {@link RunningState}; this one will be removed in the future
     */
    @Deprecated
    protected final boolean _computeExecutionTimes = true;

    /**
     * Field storing the total execution time  (in ms).
     *
     * @deprecated field moved to {@link RunningState}; this one will be removed in the future
     */
    @Deprecated
    protected double _executionTime;

    /**
     * Supportive field for measuring execution times.
     *
     * @deprecated field moved to {@link RunningState}; this one will be removed in the future
     */
    @Deprecated
    protected long _startTime = 0;

    /**
     * Supportive field for measuring execution times.
     *
     * @deprecated field moved to {@link RunningState}; this one will be removed in the future
     */
    @Deprecated
    protected long _stopTime = 0;

    /**
     * Auxiliary object constructing normalization functions using data on the current known bounds on the relevant
     * part of the objective space. Primarily used in the context of evolutionary multi-objective optimization.
     */
    private final INormalizationBuilder _normalizationBuilder;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractEA(Params p)
    {
        _name = p._name;
        _id = p._id;
        _R = p._R;
        _normalizationBuilder = p._normalizationBuilder;
        _reproductionStrategy = Objects.requireNonNullElseGet(p._reproductionStrategy,
                ReproductionStrategy::getDefaultStrategy);
        setPopulationSize(p._populationSize);
        setOffspringSize(p._offspringSize);
        setOffspringLimitPerGeneration(p._offspringLimitPerGeneration);
        _runningState = new RunningState(p._computeExecutionTimes);
        _runningState.assessExpectedTransition();
        _criteria = p._criteria;
    }

    /**
     * Supportive method for calculating execution times. Starts measuring the execution time.
     */
    protected void startMeasuringTime()
    {
        _runningState.startMeasuringTime();
    }

    /**
     * Supportive method for calculating execution times. Stops measuring the execution time.
     */
    protected void stopMeasuringTime()
    {
        _runningState.stopMeasuringTime();
    }


    /**
     * Updates total execution time.
     *
     * @param time delta time
     * @deprecated the method is moved to {@link RunningState}; this one has no effect now, it will be removed in the future
     */
    @Deprecated
    private void updateTotalElapsedExecutionTime(double time)
    {

    }

    /**
     * Init method. Should be treated as the first generation during which an initial population is constructed,
     * evaluated, and other auxiliary operations (e.g., sorting) are performed. This is a default implementation
     * that only sets a proper timestamp.
     *
     * @throws EAException an exception can be thrown
     */
    @Override
    public void init() throws EAException
    {
        EATimestamp timestamp = new EATimestamp(0, 0);
        if (!_runningState.areTimestampsConsistent(timestamp))
            _runningState.throwExceptionOnInconsistentTimestamps(timestamp);
        updateCurrentTimestamp(timestamp);
        _specimensContainer = new SpecimensContainer();
        _specimensContainer.resetSpecimensConstructedDuringGenerationCounter();
    }

    /**
     * Executes one step of the evolutionary algorithm. This is a default implementation
     * that only sets a proper timestamp.
     *
     * @param timestamp generation; steady-state repeat
     * @throws EAException the exception can be thrown
     */
    @Override
    public void step(EATimestamp timestamp) throws EAException
    {
        if (!_runningState.areTimestampsConsistent(timestamp))
            _runningState.throwExceptionOnInconsistentTimestamps(timestamp);
        updateCurrentTimestamp(timestamp);
        if (timestamp._steadyStateRepeat == 0) // beginning of the generation
        {
            // reset the counter
            _specimensContainer.resetSpecimensConstructedDuringGenerationCounter();
        }
    }

    /**
     * Setter for the current timestamp.
     *
     * @param timestamp new timestamp to be set
     */
    protected void updateCurrentTimestamp(EATimestamp timestamp)
    {
        _runningState.updateCurrentTimestamp(timestamp);
        _runningState.assessExpectedTransition();
    }


    /**
     * Setter for the current timestamp. The method also replaces the previous timestamp (field) with the current one
     * (before setting the input) in the internal {@link RunningState} object.
     *
     * @param currentTimestamp current timestamp
     * @deprecated the method has no use from now on and will be removed in the future; the timestamp setting is
     * controlled by the step and init methods
     */
    @Deprecated
    protected void setCurrentTimestamp(EATimestamp currentTimestamp)
    {

    }

    /**
     * Getter for the current timestamp.
     *
     * @return current timestamp
     */
    protected EATimestamp getCurrentTimestamp()
    {
        return _runningState.getCurrentTimestamp();
    }

    /**
     * Getter for the method's name.
     *
     * @return method's name
     */
    @Override
    public String getName()
    {
        return _name;
    }

    /**
     * Getter for the method's auxiliary ID.
     *
     * @return method's auxiliary id
     */
    @Override
    public int getID()
    {
        return _id;
    }

    /**
     * Getter for the current generation.
     *
     * @return current generation (-1, if the current timestamp is not set)
     */
    @Override
    public int getCurrentGeneration()
    {
        return _runningState.getCurrentTimestamp()._generation;
    }

    /**
     * Getter for the current steady-state repeat.
     *
     * @return current steady-state repeat (-1, if the current timestamp is not set)
     */
    @Override
    public int getCurrentSteadyStateRepeat()
    {
        return _runningState.getCurrentTimestamp()._steadyStateRepeat;
    }

    /**
     * Getter for the population size. Note that it reads and returns the proper field value (integer). It does not
     * examine the population array size kept in {@link SpecimensContainer}.
     *
     * @return population size
     */
    @Override
    public int getPopulationSize()
    {
        return _populationSize;
    }

    /**
     * Getter for the number of steady-state repeats the method is supposed to run for.
     *
     * @return the number of steady-state repeats the method is supposed to run for
     */
    @Override
    public int getExpectedNumberOfSteadyStateRepeats()
    {
        return _runningState.getExpectedNumberOfSteadyStateRepeats();
    }

    /**
     * Setter for the number of steady-state repeats the method is supposed to run for. The method also replaces the
     * previous number (field) with the current one (before setting the input) in the internal {@link RunningState}
     * object. IMPORTANT: this method can be called only just after the method finishes processing a single generation,
     * i.e., its current steady-state repeat number reached the last possible one; or is in initial state (current
     * timestamp = null). If not an exception will be thrown.
     *
     * @param expectedNumberOfSteadyStateRepeats the number of steady-state repeats the method is supposed to run for
     * @throws EAException an exception can be thrown and propagated higher
     */
    protected void updateExpectedNumberOfSteadyStateRepeats(int expectedNumberOfSteadyStateRepeats) throws EAException
    {
        _runningState.updateExpectedNumberOfSteadyStateRepeats(expectedNumberOfSteadyStateRepeats);
    }

    /**
     * Auxiliary methods that sets numberOfSSRInPreviousGeneration as imposed by currentExpectedNumberOfSSR
     * in {@link RunningState} (for testing).
     */
    protected void transitSSRData()
    {
        _runningState.transitSSRData();
    }

    /**
     * Setter for the population size. Note that it only stores the field value (integer). It does not re-size the
     * corresponding specimen array kept in {@link SpecimensContainer}.
     *
     * @param populationSize population size
     */
    protected void setPopulationSize(int populationSize)
    {
        _populationSize = populationSize;
    }


    /**
     * Getter for the offspring size. Note that it reads and returns the relevant field value (integer). It does not
     * examine the offspring array size kept in {@link SpecimensContainer}. This field determines the number of
     * offspring solutions to be generated within one execution of the {@link AbstractEA#step(EATimestamp)} method
     * (a complete generation or steady-state repeat).
     *
     * @return offspring size
     */
    @Override
    public int getOffspringSize()
    {
        return _offspringSize;
    }

    /**
     * Setter for the population size. Note that it only stores the field value (integer). It does not re-size the
     * corresponding specimen array kept in {@link SpecimensContainer}. This field determines the number of offspring
     * solutions to be generated within one execution of the {@link AbstractEA#step(EATimestamp)} method (a complete
     * generation or steady-state repeat).
     *
     * @param offspringSize offspring size
     */
    protected void setOffspringSize(int offspringSize)
    {
        _offspringSize = offspringSize;
    }


    /**
     * Setter for the field that serves as an additional cap on the number of offspring solutions that can be generated
     * in one generation. In most implementations, it can be left set to the max value. E.g., in generational
     * implementations, the number of offspring to produce is primarily determined by
     * {@link Params#_offspringSize}. In most implementations, it can be left set to the max value. E.g., in
     * generational implementations, the number of offspring to produce is primarily determined by
     * {@link Params#_offspringSize}. As for the steady-state implementations, they typically set
     * {@link Params#_offspringSize} to 1 and assume "multiple parents to one offspring" reproduction scheme, which
     * ultimately leads to producing the same number of offspring as the population size. Nonetheless, assume that
     * "multiple parents to two offspring" scheme is used in a steady-state algorithm. It would lead to producing
     * twice as many offspring as the population size dictates. If this is a desired strategy, then this field can
     * be left as it is. However, if one wants to impose an additional threshold, it can be done via this field. The
     * algorithm measures the number of constructed offspring throughout the generation. If the number would exceed
     * the allowed number, executing redundant steady-state repeats is skipped.
     *
     * @param offspringLimitPerGeneration offspring limit per generation
     */
    protected void setOffspringLimitPerGeneration(int offspringLimitPerGeneration)
    {
        _reproductionStrategy.setOffspringLimitPerGeneration(offspringLimitPerGeneration);
    }

    /**
     * Getter for the field value that serves as an additional cap on the number of offspring solutions that can be
     * generated in one generation. In most implementations, it can be left set to the max value. E.g., in generational
     * implementations, the number of offspring to produce is primarily determined by
     * {@link Params#_offspringSize}. In most implementations, it can be left set to the max value. E.g., in
     * generational implementations, the number of offspring to produce is primarily determined by
     * {@link Params#_offspringSize}. As for the steady-state implementations, they typically set
     * {@link Params#_offspringSize} to 1 and assume "multiple parents to one offspring" reproduction scheme, which
     * ultimately leads to producing the same number of offspring as the population size. Nonetheless, assume that
     * "multiple parents to two offspring" scheme is used in a steady-state algorithm. It would lead to producing
     * twice as many offspring as the population size dictates. If this is a desired strategy, then this field can
     * be left as it is. However, if one wants to impose an additional threshold, it can be done via this field. The
     * algorithm measures the number of constructed offspring throughout the generation. If the number would exceed
     * the allowed number, executing redundant steady-state repeats is skipped.
     *
     * @return offspring limit per generation
     */
    public int getOffspringLimitPerGeneration()
    {
        return _reproductionStrategy.getOffspringLimitPerGeneration();
    }

    /**
     * Setter for the objective space manager.
     *
     * @param osManager objective space manager
     */
    protected void setObjectiveSpaceManager(ObjectiveSpaceManager osManager)
    {
        _osManager = osManager;
    }

    /**
     * Getter for the objective space manager.
     *
     * @return objective space manager
     */
    @Override
    public ObjectiveSpaceManager getObjectiveSpaceManager()
    {
        return _osManager;
    }


    /**
     * Getter for the auxiliary object constructing normalization functions using data on the current known bounds on
     * the relevant part of the objective space. Primarily used in the context of evolutionary multi-objective
     * optimization.
     */
    @Override
    public INormalizationBuilder getNormalizationBuilder()
    {
        return _normalizationBuilder;
    }


    /**
     * Getter for the considered criteria.
     *
     * @return criteria
     */
    @Override
    public Criteria getCriteria()
    {
        return _criteria;
    }

    /**
     * Getter for the random number generator.
     *
     * @return random number generator
     */
    @Override
    public IRandom getR()
    {
        return _R;
    }

    /**
     * Getter for the flag indicating whether the total execution time (as well as other implementation-dependent times)
     * are measured or not (in ms).
     *
     * @return flag indicating whether the total execution time (as well as other implementation-dependent times) are
     * measured or not (in ms)
     */
    @Override
    public boolean getComputeExecutionTimes()
    {
        return _runningState.areExecutionTimesMeasurable();
    }

    /**
     * Getter for the specimens container maintained by the EA (wraps e.g., current population, mating pool,
     * offspring).
     *
     * @return specimens container
     */
    @Override
    public SpecimensContainer getSpecimensContainer()
    {
        return _specimensContainer;
    }

    /**
     * Returns the total execution time that has passed (in ms).
     *
     * @return the total execution time
     */
    @Override
    public double getExecutionTime()
    {
        return _runningState.getExecutionTime();
    }

    /**
     * Getter for the reproduction strategy employed by the evolutionary algorithm.
     *
     * @return reproduction strategy employed by the evolutionary algorithm
     */
    @Override
    public ReproductionStrategy getReproductionStrategy()
    {
        return _reproductionStrategy;
    }

    /**
     * Auxiliary method that checks if the timestamp being currently set is a direct successor of the timestamp being
     * previously assigned.
     *
     * @param newTimestamp new timestamp to be set
     * @return true, if the current timestamp is a direct successor of the previous one; false otherwise
     */
    protected boolean areTimestampsConsistent(EATimestamp newTimestamp)
    {
        return _runningState.areTimestampsConsistent(newTimestamp);
    }

    /**
     * Setter for the execution time (use with caution; the measurement should be done in ms).
     *
     * @param executionTime new total execution time (overwrites the previous one stored).
     */
    protected void setExecutionTime(double executionTime)
    {
        _runningState.setExecutionTime(executionTime);
    }


    /**
     * Prints basic info on the current population.
     *
     * @deprecated to be removed in future releases
     */
    @Deprecated
    public void printBasicPopulationInfo()
    {
        for (int i = 0; i < _specimensContainer.getPopulation().size(); i++)
        {
            Specimen s = _specimensContainer.getPopulation().get(i);
            System.out.print(i + " ||| " + String.format("%.4f : ", s.getAlternative().getAuxScore()));
            String str = PrintUtils.getVectorOfDoubles(s.getAlternative().getPerformanceVector(), 4);
            System.out.print(str + "  | ");
            if (s.getIntDecisionVector() != null) PrintUtils.printVectorOfIntegers(s.getIntDecisionVector());
            else if (s.getDoubleDecisionVector() != null)
                PrintUtils.printVectorOfDoubles(s.getDoubleDecisionVector(), 4);
            else System.out.println();
        }
    }
}
