package ea;

import criterion.Criteria;
import exception.EAException;
import exception.PhaseException;
import os.ObjectiveSpaceManager;
import phase.*;
import population.Specimen;
import population.SpecimensContainer;
import print.PrintUtils;
import random.IRandom;
import system.ds.DecisionSupportSystem;

/**
 * Base implementation of an EA.
 *
 * @author MTomczyk
 */

public class EA
{
    /**
     * Params container (supportive class used for parameterizing the EA).
     */
    public static class Params
    {
        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param name     name of the EA
         * @param criteria considered criteria
         */
        public Params(String name, Criteria criteria)
        {
            _name = name;
            _criteria = criteria;
        }

        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         * @param bundle   EA bundle (provides phases and other essential fields, e.g., osManager and name)
         */
        public Params(Criteria criteria, AbstractEABundle bundle)
        {
            _name = bundle._name;
            _criteria = criteria;
            _osManager = bundle._osManager;
            PhasesBundle.copyPhasesFromBundleToEA(bundle._phasesBundle, this);
        }


        /**
         * Name of the EA.
         */
        public String _name = "EA";

        /**
         * Unique ID of the EA
         */
        public int _id = 0;

        /**
         * Considered criteria.
         */
        public Criteria _criteria;

        /**
         * Random number generator.
         */
        public IRandom _R;

        /**
         * Population size.
         */
        public int _populationSize = 0;

        /**
         * Offspring size.
         */
        public int _offspringSize = 0;

        /**
         * Class responsible for storing and updating EA's knowledge on the objective space.
         */
        public ObjectiveSpaceManager _osManager;

        /**
         * "Compute times" flag. If true: EA measures total execution time (in ms).
         */
        public boolean _computeExecutionTime = true;

        /**
         * "Compute phases times" flag. If true: the EA measures individual phase's execution times (in ms).
         */
        public boolean _computePhasesExecutionTimes = false;

        /**
         * "Init starts" phase.
         */
        public AbstractInitStartsPhase _initStarts = null;

        /**
         * "Construct initial population" phase.
         */
        public AbstractConstructInitialPopulationPhase _constructInitialPopulation;

        /**
         * "Assign specimens IDs" phase.
         */
        public AbstractAssignSpecimenIDs _assignSpecimenIDs = new AssignSpecimensIDs();

        /**
         * "Evaluate" phase.
         */
        public AbstractEvaluatePhase _evaluate;

        /**
         * "Sort" phase.
         */
        public AbstractSortPhase _sort;

        /**
         * "Init ends" phase.
         */
        public AbstractInitEndsPhase _initEnds = null;

        /**
         * "Prepare step" phase.
         */
        public AbstractPrepareStepPhase _prepareStep = null;

        /**
         * "Construct mating pool" phase.
         */
        public AbstractConstructMatingPoolPhase _constructMatingPool = new ConstructMatingPool();

        /**
         * "Select parents" phase.
         */
        public AbstractSelectParentsPhase _selectParents;

        /**
         * "Reproduce" phase.
         */
        public AbstractReproducePhase _reproduce;

        /**
         * "Merge" phase.
         */
        public AbstractMergePhase _merge = new Merge();

        /**
         * "Remove" phase.
         */
        public AbstractRemovePhase _remove;

        /**
         * "Finalize step" phase.
         */
        public AbstractFinalizeStepPhase _finalizeStep = new FinalizeStep();

        /**
         * "Update objective space" phase.
         */
        public AbstractUpdateOSPhase _updateOS = null;
    }

    /**
     * Name of the EA.
     */
    protected String _name;

    /**
     * Unique ID of the EA
     */
    protected int _id;

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
     * Class responsible for storing and updating EA's knowledge on the objective space.
     */
    protected ObjectiveSpaceManager _osManager;

    /**
     * Considered criteria.
     */
    protected Criteria _criteria;

    /**
     * Random number generator.
     */
    protected IRandom _R;

    /**
     * "Compute times" flag. If true: EA measures total execution time.
     */
    protected boolean _computeExecutionTimes;

    /**
     * "Compute phases times" flag. If true: the EA measures individual phase's execution times.
     */
    protected boolean _computePhasesExecutionTimes;

    /**
     * Field storing the total execution time  (in ms).
     */
    protected double _executionTime;

    /**
     * Field storing phases' individual execution times (indexed as imposed in {@link PhasesIDs})  (in ms).
     */
    protected double[] _phasesExecutionTimes;

    /**
     * Supportive field for measuring execution times.
     */
    protected long _startTime = 0;

    /**
     * Supportive field for measuring execution times.
     */
    protected long _stopTime = 0;

    /**
     * Array of phases (indexed as imposed in {@link PhasesIDs}).
     */
    protected IPhase[] _phases;

    /**
     * Default constructor (for extending classes only).
     */
    protected EA()
    {
        this((IRandom) null);
    }

    /**
     * Parameterized constructor (for extending classes only).
     *
     * @param R random number generator
     */
    protected EA(IRandom R)
    {
        _R = R;
    }

    /**
     * Parameterized constructor. Sets EA as imposed by the provided params container.
     *
     * @param p params container
     */
    public EA(Params p)
    {
        _name = p._name;
        _criteria = p._criteria;
        _id = p._id;

        _populationSize = p._populationSize;
        _offspringSize = p._offspringSize;

        _osManager = p._osManager;
        if (_osManager != null) _osManager.setEA(this);
        _R = p._R;

        _computeExecutionTimes = p._computeExecutionTime;
        _computePhasesExecutionTimes = p._computePhasesExecutionTimes;

        _executionTime = 0.0d;
        _phasesExecutionTimes = new double[PhasesIDs._phaseNames.length];
        _currentTimestamp = new EATimestamp(0, 0);

        _phases = new IPhase[PhasesIDs._phaseNames.length];
        _phases[PhasesIDs.PHASE_INIT_STARTS] = p._initStarts;
        _phases[PhasesIDs.PHASE_CONSTRUCT_INITIAL_POPULATION] = p._constructInitialPopulation;
        _phases[PhasesIDs.PHASE_ASSIGN_SPECIMENS_IDS] = p._assignSpecimenIDs;
        _phases[PhasesIDs.PHASE_EVALUATE] = p._evaluate;
        _phases[PhasesIDs.PHASE_SORT] = p._sort;
        _phases[PhasesIDs.PHASE_INIT_ENDS] = p._initEnds;
        _phases[PhasesIDs.PHASE_PREPARE_STEP] = p._prepareStep;
        _phases[PhasesIDs.PHASE_CONSTRUCT_MATING_POOL] = p._constructMatingPool;
        _phases[PhasesIDs.PHASE_SELECT_PARENTS] = p._selectParents;
        _phases[PhasesIDs.PHASE_REPRODUCE] = p._reproduce;
        _phases[PhasesIDs.PHASE_MERGE] = p._merge;
        _phases[PhasesIDs.PHASE_REMOVE] = p._remove;
        _phases[PhasesIDs.PHASE_UPDATE_OS] = p._updateOS;
        _phases[PhasesIDs.PHASE_FINALIZE_STEP] = p._finalizeStep;
    }

    /**
     * Initializes the evolutionary process.
     *
     * @throws EAException the exception can be thrown 
     */
    public void init() throws EAException
    {
        startMeasuringTime();
        _currentTimestamp = new EATimestamp(0, 0);

        executePhase(PhasesIDs.PHASE_INIT_STARTS);
        executePhase(PhasesIDs.PHASE_CONSTRUCT_INITIAL_POPULATION);
        executePhase(PhasesIDs.PHASE_ASSIGN_SPECIMENS_IDS);
        executePhase(PhasesIDs.PHASE_EVALUATE);
        executePhase(PhasesIDs.PHASE_UPDATE_OS);
        executePhase(PhasesIDs.PHASE_SORT);
        executePhase(PhasesIDs.PHASE_INIT_ENDS);

        stopMeasuringTime();
    }

    /**
     * Executes one step of the evolutionary algorithm.
     *
     * @param timestamp generation; steady-state repeat
     * @throws EAException the exception can be thrown 
     */
    public void step(EATimestamp timestamp) throws EAException
    {
        startMeasuringTime();
        _currentTimestamp = timestamp;
        executePhase(PhasesIDs.PHASE_PREPARE_STEP);
        executePhase(PhasesIDs.PHASE_CONSTRUCT_MATING_POOL);
        executePhase(PhasesIDs.PHASE_SELECT_PARENTS);
        executePhase(PhasesIDs.PHASE_REPRODUCE);
        executePhase(PhasesIDs.PHASE_ASSIGN_SPECIMENS_IDS);
        executePhase(PhasesIDs.PHASE_EVALUATE);
        executePhase(PhasesIDs.PHASE_UPDATE_OS);
        executePhase(PhasesIDs.PHASE_MERGE);
        executePhase(PhasesIDs.PHASE_SORT);
        executePhase(PhasesIDs.PHASE_REMOVE);
        executePhase(PhasesIDs.PHASE_FINALIZE_STEP);
        stopMeasuringTime();
    }

    /**
     * Conditionally performs indicated phase (condition = phase is not null)
     *
     * @param phaseID id of the phase to be performed
     * @throws EAException the exception can be thrown 
     */
    private void executePhase(int phaseID) throws EAException
    {
        if (_phases[phaseID] != null)
        {
            try
            {
                PhaseReport report = _phases[phaseID].perform(this);
                if (report._elapsedTime != null) updatePhasesTotalExecutionTimes(report._elapsedTime, phaseID);
            } catch (PhaseException e)
            {
                throw new EAException("Error occurred when executing phase = " + _phases[phaseID].getName() + " " +
                        e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }
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
     * Updates phase total execution time.
     *
     * @param time  delta time
     * @param phase phase id
     */
    public void updatePhasesTotalExecutionTimes(double time, int phase)
    {
        _phasesExecutionTimes[phase] += time;
    }

    /**
     * Getter for the name of the method.
     *
     * @return method's name
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Getter for the method's id.
     *
     * @return method's id
     */
    public int getID()
    {
        return _id;
    }

    /**
     * Getter for the population size.
     *
     * @return population size
     */
    public int getPopulationSize()
    {
        return _populationSize;
    }

    /**
     * Setter for criteria.
     *
     * @param criteria criteria
     */
    protected void setCriteria(Criteria criteria)
    {
        _criteria = criteria;
    }

    /**
     * Setter for the population size.
     *
     * @param populationSize new population size
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
    public int getOffspringSize()
    {
        return _offspringSize;
    }

    /**
     * Setter for the offspring size.
     *
     * @param offspringSize new offspring size
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
    public ObjectiveSpaceManager getObjectiveSpaceManager()
    {
        return _osManager;
    }

    /**
     * Getter for the considered criteria.
     *
     * @return criteria
     */
    public Criteria getCriteria()
    {
        return _criteria;
    }

    /**
     * Getter for the random number generator.
     *
     * @return random number generator
     */
    public IRandom getR()
    {
        return _R;
    }

    /**
     * Getter for the "compute execution times" flag.
     *
     * @return "compute execution times" flag
     */
    public boolean getComputeExecutionTimes()
    {
        return _computeExecutionTimes;
    }

    /**
     * Getter for the "compute execution times of different phases of the algorithm" flag.
     *
     * @return "compute execution times of different phases of the algorithm" flag
     */
    public boolean getComputePhasesExecutionTimes()
    {
        return _computePhasesExecutionTimes;
    }

    /**
     * Getter for the current steady-state repeat.
     *
     * @return current steady-state repeat
     */
    public int getCurrentSteadyStateRepeat()
    {
        return _currentTimestamp._steadyStateRepeat;
    }

    /**
     * Getter for the current generation.
     *
     * @return current generation
     */
    public int getCurrentGeneration()
    {
        return _currentTimestamp._generation;
    }

    /**
     * Getter for the specimens container maintained by the EA (wraps e.g., current population, mating pool, offspring).
     *
     * @return specimens container
     */
    public SpecimensContainer getSpecimensContainer()
    {
        return _specimensContainer;
    }

    /**
     * Setter for the specimens container maintained by the EA (e.g., current population, mating pool, offspring).
     *
     * @param specimensContainer current population
     */
    public void setSpecimensContainer(SpecimensContainer specimensContainer)
    {
        _specimensContainer = specimensContainer;
    }

    /**
     * Returns the total execution time that has passed (in ms).
     *
     * @return the total execution time
     */
    public double getExecutionTime()
    {
        return _executionTime;
    }

    /**
     * Returns the total execution time that has passed (in ms).
     *
     * @param phase phase id (see {@link PhasesIDs}).
     * @return the total execution time of a phase
     */
    public double getPhaseExecutionTime(int phase)
    {
        return _phasesExecutionTimes[phase];
    }

    /**
     * Getter for the decision support system (returns null if a method does not employ any). To be extended.
     *
     * @return decision support system (null, if not employed)
     */
    public DecisionSupportSystem getDecisionSupportSystem()
    {
        return null;
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
