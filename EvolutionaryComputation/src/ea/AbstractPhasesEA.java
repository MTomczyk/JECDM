package ea;

import criterion.Criteria;
import exception.EAException;
import exception.PhaseException;
import phase.IPhase;
import phase.PhaseAssignment;
import phase.PhaseReport;
import phase.PhasesBundle;
import population.SpecimensContainer;
import random.IRandom;

/**
 * Abstract implementation of {@link IEA} that employs phases as functional blocks constituting various steps of
 * the evolutionary process.
 *
 * @author MTomczyk
 */
public class AbstractPhasesEA extends AbstractEA implements IEA
{
    /**
     * Params container.
     */
    public static class Params extends AbstractEA.Params
    {
        /**
         * All phases (functional blocks) that are to be executed by the EA when calling the init and step methods
         * (in the provided order). When initializing the object, phases assigned to the INIT and STEP methods are kept
         * compactly in separate arrays ({@link AbstractPhasesEA#_initPhases} and {@link AbstractPhasesEA#_stepPhases}).
         */
        public PhaseAssignment[] _phases;

        /**
         * Flag indicating whether the phases' individual execution times should be measured (in ms).
         */
        public boolean _computePhasesExecutionTimes = false;

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
            super(name, id, R, computeExecutionTimes, criteria);
        }

        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         * @param bundle   EA bundle
         */
        protected Params(Criteria criteria, AbstractEABundle bundle)
        {
            super(bundle._name, 0, null, true, criteria);
            _phases = PhasesBundle.getPhasesAssignmentsFromBundle(bundle._phasesBundle);
        }
    }

    /**
     * Phases assigned for the init method ({@link IEA#init()}; executed in the provided order).
     */
    protected final IPhase[] _initPhases;

    /**
     * Phases assigned for the step method ({@link IEA#step(EATimestamp)} ()}; executed in the provided order).
     */
    protected final IPhase[] _stepPhases;

    /**
     * Flag indicating whether the phases' individual execution times should be measured (in ms).
     */
    protected boolean _computePhasesExecutionTimes;

    /**
     * Field storing phases' individual execution times (assigned to the init method; in ms).
     */
    protected double[] _initPhasesExecutionTimes;

    /**
     * Field storing phases' individual execution times (assigned to the init method; in ms).
     */
    protected double[] _stepPhasesExecutionTimes;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractPhasesEA(Params p)
    {
        super(p);

        _populationSize = p._populationSize;
        _offspringSize = p._offspringSize;

        _osManager = p._osManager;
        if (_osManager != null) _osManager.setEA(this);

        _initPhases = establishPhases(p._phases, PhaseAssignment.Assignment.INIT);
        _stepPhases = establishPhases(p._phases, PhaseAssignment.Assignment.STEP);
        _executionTime = 0.0d;

        _computePhasesExecutionTimes = p._computePhasesExecutionTimes;
        if (_computePhasesExecutionTimes)
        {
            if (_initPhases != null) _initPhasesExecutionTimes = new double[_initPhases.length];
            if (_stepPhases != null) _stepPhasesExecutionTimes = new double[_stepPhases.length];
        }
    }

    /**
     * Auxiliary method that derives phases matching a specific assignment.
     *
     * @param phases     phases assignments
     * @param assignment relevant assignment
     * @return phases matching a specific assignment
     */
    protected static IPhase[] establishPhases(PhaseAssignment[] phases, PhaseAssignment.Assignment assignment)
    {
        int no = 0;
        if (phases == null) return null;
        for (PhaseAssignment pa : phases)
            if ((pa != null) && (pa._assignment.equals(assignment))) no++;
        if (no == 0) return null;
        IPhase[] parsedPhases = new IPhase[no];
        no = 0;
        for (PhaseAssignment pa : phases)
            if ((pa != null) && (pa._assignment.equals(assignment)))
                parsedPhases[no++] = pa._phase;
        return parsedPhases;
    }

    /**
     * Initializes the evolutionary process.
     *
     * @throws EAException the exception can be thrown
     */
    @Override
    public void init() throws EAException
    {
        startMeasuringTime();
        _currentTimestamp = new EATimestamp(0, 0);
        executePhases(_initPhases, _initPhasesExecutionTimes);
        stopMeasuringTime();
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
        startMeasuringTime();
        _currentTimestamp = timestamp;
        executePhases(_stepPhases, _stepPhasesExecutionTimes);
        stopMeasuringTime();
    }

    /**
     * Auxiliary method for executing the phases assigned either to the INIT or STEP method (controlled by the input).
     *
     * @param phases         phases to be executed
     * @param executionTimes associated total execution times
     * @throws EAException the exception can be thrown
     */
    private void executePhases(IPhase[] phases, double[] executionTimes) throws EAException
    {
        if (phases != null)
        {
            for (int i = 0; i < phases.length; i++)
            {
                if (phases[i] != null)
                {
                    PhaseReport report = executePhase(phases[i]);
                    if (_computePhasesExecutionTimes) executionTimes[i] += report._elapsedTime;
                }
            }
        }
    }

    /**
     * Executes indicated phase.
     *
     * @param phase phase to be performed
     * @return report on the execution
     * @throws EAException the exception can be thrown
     */
    private PhaseReport executePhase(IPhase phase) throws EAException
    {
        try
        {
            return phase.perform(this);
        } catch (PhaseException e)
        {
            throw new EAException("Error occurred when executing phase = " + phase.getName() + " " +
                    e.getDetailedReasonMessage(), this.getClass(), e);
        }
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
     * Returns the phase execution time that has passed (in ms).
     *
     * @param phase      phase index (when passing the phases to the EA, the init and step-assigned phases are stored in
     *                   separate arrays; indices start from 0)
     * @param assignment field indicating whether a phase assigned to the INIT or STEP method is considered
     * @return the total execution time of a phase
     */
    public double getPhaseExecutionTime(int phase, PhaseAssignment.Assignment assignment)
    {
        if (assignment.equals(PhaseAssignment.Assignment.INIT)) return _initPhasesExecutionTimes[phase];
        return _stepPhasesExecutionTimes[phase];
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
}
