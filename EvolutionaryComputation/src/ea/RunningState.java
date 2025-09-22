package ea;

import exception.EAException;

/**
 * Auxiliary method representing the EA's current running state.
 *
 * @author MTomczyk
 */
class RunningState
{
    /**
     * Expected possible transitions between consecutive timestamps.
     */
    enum Transition
    {
        /**
         * Pre-init transition.
         */
        PRE_INIT,

        /**
         * From 0-th to 1-st generation transition.
         */
        FROM_0GENERATION,

        /**
         * In-generation transition.
         */
        IN_GENERATION,

        /**
         * To-new-generation transition.
         */
        NEW_GENERATION
    }

    /**
     * Auxiliary states determining if the last expected SSR has been reached or not.
     */
    enum State
    {
        /**
         * Last expected SSR has been reached.
         */
        LAST_SSR_REACHED,

        /**
         * Last expected SSR has not been reached.
         */
        LAST_SSR_NOT_REACHED,

        /**
         * Between generations params adjustment.
         */
        BETWEEN_GENERATIONS_PARAMS_ADJUSTMENT,
    }

    /**
     * Current timestamp: current generation and steady-state repeat.
     */
    private EATimestamp _currentTimestamp;

    /**
     * The expected number of steady-state repeats the method is expected to run for in the current generation.
     * IMPORTANT: it is assumed that the expected number of steady-state repeats cannot change when processing a single
     * generation (i.e., in-between subsequent steady-state repeats)
     */
    private int _currentExpectedNumberOfSSR;

    /**
     * The number of steady-state repeats the method was supposed to run for in the most recent (past) generation.
     * IMPORTANT: it is assumed that the expected number of steady-state repeats cannot change when processing a single
     * generation (i.e., in-between subsequent steady-state repeats)
     */
    private int _numberOfSSRInPreviousGeneration;

    /**
     * Expected transition between states.
     */
    private Transition _expectedTransition;

    /**
     * Current state.
     */
    private State _state;

    /**
     * Flag indicating whether the total execution time (as well as other implementation-dependent times) are measured
     * or not (in ms).
     */
    private final boolean _computeExecutionTimes;

    /**
     * Field storing the total execution time  (in ms).
     */
    private double _executionTime;

    /**
     * Supportive field for measuring execution times.
     */
    private long _startTime;

    /**
     * Default constructor.
     */
    public RunningState()
    {
        this(true);
    }


    /**
     * Parameterized constructor.
     *
     * @param computeExecutionTimes if true, the algorithm is supposed to measure the execution times; false otherwise
     */
    public RunningState(boolean computeExecutionTimes)
    {
        _currentTimestamp = null;
        _expectedTransition = Transition.PRE_INIT;
        _state = State.BETWEEN_GENERATIONS_PARAMS_ADJUSTMENT;
        _numberOfSSRInPreviousGeneration = 1;
        _currentExpectedNumberOfSSR = 1;
        _computeExecutionTimes = computeExecutionTimes;
        _executionTime = 0.0d;
    }


    /**
     * Auxiliary method that assesses the expected transition type based on the current internal data.
     */
    protected void assessExpectedTransition()
    {
        if (_currentTimestamp == null) _expectedTransition = Transition.PRE_INIT;
        else if ((_currentTimestamp._generation == 0) && (_currentTimestamp._steadyStateRepeat == 0))
            _expectedTransition = Transition.FROM_0GENERATION;
        else if (_currentTimestamp._steadyStateRepeat == _currentExpectedNumberOfSSR - 1)
            _expectedTransition = Transition.NEW_GENERATION;
        else _expectedTransition = Transition.IN_GENERATION;
    }

    /**
     * Setter for the new current timestamp. It also assesses the current state.
     *
     * @param newTimestamp new timestamp
     */
    protected void updateCurrentTimestamp(EATimestamp newTimestamp)
    {
        _currentTimestamp = newTimestamp;
        if (_currentTimestamp != null)
        {
            if (_currentTimestamp._steadyStateRepeat + 1 == _currentExpectedNumberOfSSR)
                _state = State.LAST_SSR_REACHED;
            else _state = State.LAST_SSR_NOT_REACHED;
        } else _state = State.LAST_SSR_REACHED;
    }

    /**
     * Auxiliary methods that sets numberOfSSRInPreviousGeneration as imposed by currentExpectedNumberOfSSR
     * in {@link RunningState} (for testing).
     */
    protected void transitSSRData()
    {
        _numberOfSSRInPreviousGeneration = _currentExpectedNumberOfSSR;
    }

    /**
     * Setter for the number of steady-state repeats the method is supposed to run for. The method also replaces the
     * previous number (field) with the current one (before setting the input) in the internal {@link RunningState}
     * object. IMPORTANT: this method can be called only just after the method finishes processing a single generation,
     * i.e., its current steady-state repeat number reached the last possible one; or is in initial state (current
     * timestamp = null). If not, an exception will be thrown.
     *
     * @param expectedNumberOfSSR the number of steady-state repeats to be executed in the upcoming
     *                            generation
     * @throws EAException an exception can be thrown and propagated higher
     */
    protected void updateExpectedNumberOfSteadyStateRepeats(int expectedNumberOfSSR) throws EAException
    {
        if ((_currentTimestamp != null) &&
                ((_currentTimestamp._generation != 0) || (_currentTimestamp._steadyStateRepeat != 0)) &&
                (_state.equals(State.LAST_SSR_NOT_REACHED)))
            throw EAException.getInstanceWithSource("The expected number of steady-state repeats cannot be set, " +
                    "as the current timestamp is not null (the method is not in the pre-run stage), " +
                    "is not in the 0-th generation and 0-th SSR, " +
                    "and the current steady-state repeat number is not the last possible (meaning that a whole generation " +
                    "has not passed): the current timestamp = " + _currentTimestamp + "; the number" +
                    " of SSR in the previous generation = " + _numberOfSSRInPreviousGeneration + "; the new expected " +
                    "number of SSR = " + expectedNumberOfSSR, this.getClass());

        if (!_expectedTransition.equals(Transition.PRE_INIT))
            _expectedTransition = Transition.NEW_GENERATION; // Go to new generation

        if (_state.equals(State.LAST_SSR_REACHED))
        {
            _numberOfSSRInPreviousGeneration = _currentExpectedNumberOfSSR;
            _state = State.BETWEEN_GENERATIONS_PARAMS_ADJUSTMENT;
        }

        _currentExpectedNumberOfSSR = expectedNumberOfSSR;
    }

    /**
     * Supportive method for calculating execution times. Starts measuring the execution time.
     * The method has no effect if {@link RunningState#_computeExecutionTimes} is false.
     */
    protected void startMeasuringTime()
    {
        if (_computeExecutionTimes) _startTime = System.nanoTime();
    }

    /**
     * Getter for the method execution time (in ms)
     *
     * @return the execution time
     */
    protected double getExecutionTime()
    {
        return _executionTime;
    }

    /**
     * Setter for the execution time (use with caution; the measurement should be done in ms).
     *
     * @param executionTime new total execution time (overwrites the previous one stored).
     */
    protected void setExecutionTime(double executionTime)
    {
        _executionTime = executionTime;
    }

    /**
     * Returns the flag indicating whether the execution times are to be computed throughout the method execution.
     *
     * @return true, if the execution times are measurable; false otherwise
     */
    protected boolean areExecutionTimesMeasurable()
    {
        return _computeExecutionTimes;
    }

    /**
     * Supportive method for calculating execution times. Stops measuring the execution time.
     * The method has no effect if {@link RunningState#_computeExecutionTimes} is false.
     */
    protected void stopMeasuringTime()
    {
        if (_computeExecutionTimes)
        {
            long stopTime = System.nanoTime();
            updateTotalElapsedExecutionTime((double) (stopTime - _startTime) / 1000000.0);
        }
    }

    /**
     * Updates total execution time. The method has no effect if {@link RunningState#_computeExecutionTimes} is false.
     *
     * @param time delta time
     */
    private void updateTotalElapsedExecutionTime(double time)
    {
        _executionTime += time;
    }

    /**
     * Getter for the current timestamp.
     *
     * @return current timestamp
     */
    protected EATimestamp getCurrentTimestamp()
    {
        return _currentTimestamp;
    }

    /**
     * Getter for the number of steady-state repeats the method is supposed to run for.
     *
     * @return the number of steady-state repeats the method is supposed to run for
     */
    public int getExpectedNumberOfSteadyStateRepeats()
    {
        return _currentExpectedNumberOfSSR;
    }


    /**
     * Auxiliary method that checks if the timestamp to be set is a direct successor of the timestamp being currently
     * assigned.
     *
     * @param nT new timestamp to be set
     * @return true, if the current timestamp is a direct successor of the previous one; false otherwise
     */
    protected boolean areTimestampsConsistent(EATimestamp nT)
    {
        EATimestamp pT = _currentTimestamp;
        if (nT == null) return false; // should never happen
        if (nT._steadyStateRepeat >= _currentExpectedNumberOfSSR) return false; // should never happen
        if (_expectedTransition.equals(Transition.PRE_INIT))
        {
            if (nT._generation != 0) return false;
            return nT._steadyStateRepeat == 0;
        } else if (_expectedTransition.equals(Transition.FROM_0GENERATION))
        {
            if (nT._generation != 1) return false;
            return nT._steadyStateRepeat == 0;
        } else if (_expectedTransition.equals(Transition.IN_GENERATION))
        {
            if (nT._generation != pT._generation) return false;
            return nT._steadyStateRepeat == pT._steadyStateRepeat + 1;
        } else if (_expectedTransition.equals(Transition.NEW_GENERATION))
        {
            if (nT._generation != pT._generation + 1) return false;
            return nT._steadyStateRepeat == 0;
        }
        return true;
    }

    /**
     * An auxiliary method associated with {@link RunningState#areTimestampsConsistent(EATimestamp)}. It generates
     * an exception in the case when the two consecutive timestamps are inconsistent.
     *
     * @param newTimestamp new timestamp to be set
     * @throws EAException an exception can be thrown and propagated higher
     */
    protected void throwExceptionOnInconsistentTimestamps(EATimestamp newTimestamp) throws EAException
    {
        String prev = _currentTimestamp != null ? _currentTimestamp.toString() : "null";
        String curr = newTimestamp != null ? newTimestamp.toString() : "null";
        String msg = null;
        if (_expectedTransition.equals(Transition.PRE_INIT))
            msg = "The method should transit from the pre-init state to 0-th generation, but the previous timestamp is "
                    + prev + " while the current is " + curr;
        else if (_expectedTransition.equals(Transition.FROM_0GENERATION))
            msg = "The method should transit directly from 0th to 1st generation, but the previous timestamp is " + prev
                    + " while the current is " + curr;
        else if (_expectedTransition.equals(Transition.NEW_GENERATION))
            msg = "The method should transit from one generation to another, but the previous timestamp is " + prev +
                    " while the current is " + curr + " (the previously expected number of steady-state repeats equals "
                    + _numberOfSSRInPreviousGeneration + " while the current is set to " + _currentExpectedNumberOfSSR + ")";
        else if (_expectedTransition.equals(Transition.IN_GENERATION))
        {
            msg = "The method should transit from one steady-state repeat to another, but the previous timestamp is " + prev +
                    " while the current is " + curr + " (the current number of steady-state repeats to execute is set to "
                    + _currentExpectedNumberOfSSR + ")";
        }
        throw EAException.getInstanceWithSource(msg, this.getClass());
    }
}
