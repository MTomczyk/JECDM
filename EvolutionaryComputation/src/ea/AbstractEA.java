package ea;

import criterion.Criteria;
import exception.EAException;
import os.ObjectiveSpaceManager;
import population.Specimen;
import population.SpecimensContainer;
import print.PrintUtils;
import random.IRandom;
import system.ds.DecisionSupportSystem;

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
         * Population size.
         */
        public int _populationSize = 0;

        /**
         * Offspring size.
         */
        public int _offspringSize = 0;

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
     */
    protected int _populationSize;

    /**
     * Offspring size.
     */
    protected int _offspringSize;

    /**
     * Current timestamp: current generation and steady-state repeat.
     */
    protected EATimestamp _currentTimestamp;

    /**
     * Specimens container maintained by the EA (wraps e.g., current population, mating pool, offspring).
     */
    protected SpecimensContainer _specimensContainer;

    /**
     * Class responsible for storing and updating EA's data on the objective space.
     */
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
     */
    protected final boolean _computeExecutionTimes;

    /**
     * Field storing the total execution time  (in ms).
     */
    protected double _executionTime;

    /**
     * Supportive field for measuring execution times.
     */
    protected long _startTime = 0;

    /**
     * Supportive field for measuring execution times.
     */
    protected long _stopTime = 0;


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
        _computeExecutionTimes = p._computeExecutionTimes;
        _criteria = p._criteria;
    }

    /**
     * Supportive method for calculating execution times. Starts measuring the execution time.
     */
    protected void startMeasuringTime()
    {
        if (_computeExecutionTimes) _startTime = System.nanoTime();
    }

    /**
     * Supportive method for calculating execution times. Stops measuring the execution time.
     */
    protected void stopMeasuringTime()
    {
        if (_computeExecutionTimes)
        {
            _stopTime = System.nanoTime();
            updateTotalElapsedExecutionTime((double) (_stopTime - _startTime) / 1000000.0);
        }
    }

    /**
     * Updates total execution time.
     *
     * @param time delta time
     */
    private void updateTotalElapsedExecutionTime(double time)
    {
        _executionTime += time;
    }

    /**
     * Init method. Should be treated as the first generation during which an initial population is constructed,
     * evaluated, and other auxiliary operations (e.g., sorting) are performed.
     *
     * @throws EAException an exception can be thrown
     */
    @Override
    public void init() throws EAException
    {

    }

    /**
     * Executes one step of the evolutionary algorithm.
     *
     * @param timestamp generation; steady-state repeat
     * @throws EAException the exception can be thrown
     */
    @Override
    public void step(EATimestamp timestamp) throws EAException
    {
        _currentTimestamp = timestamp;
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
     * @return current generation
     */
    @Override
    public int getCurrentGeneration()
    {
        return _currentTimestamp._generation;
    }

    /**
     * Getter for the current steady-state repeat.
     *
     * @return current steady-state repeat
     */
    @Override
    public int getCurrentSteadyStateRepeat()
    {
        return _currentTimestamp._steadyStateRepeat;
    }

    /**
     * Getter for the current population size.
     *
     * @return population size
     */
    @Override
    public int getPopulationSize()
    {
        return _populationSize;
    }

    /**
     * Setter for the population size.
     *
     * @param populationSize population size
     */
    protected void setPopulationSize(int populationSize)
    {
        _populationSize = populationSize;
    }

    /**
     * Getter for the offspring size.
     *
     * @return offspring size
     */
    @Override
    public int getOffspringSize()
    {
        return _offspringSize;
    }

    /**
     * Setter for the  offspring size.
     *
     * @param offspringSize offspring size
     */
    protected void setOffspringSize(int offspringSize)
    {
        _offspringSize = offspringSize;
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
        return _computeExecutionTimes;
    }

    /**
     * Getter for the specimens container maintained by the EA (wraps e.g., current population, mating pool, offspring).
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
        return _executionTime;
    }

    /**
     * Getter for the decision support system (returns null if a method does not employ any). To be extended.
     *
     * @return decision support system (null, if not employed)
     */
    @Override
    public DecisionSupportSystem getDecisionSupportSystem()
    {
        return null;
    }

    /**
     * Auxiliary method for disposing data.
     */
    @Override
    public void dispose()
    {

    }

    /**
     * Prints basic info on the current population.
     */
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
