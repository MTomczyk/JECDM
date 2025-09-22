package ea;

/**
 * Wrapper for the generation number and the steady-state repeat.
 *
 * @author MTomczyk
 */


public class EATimestamp
{
    /**
     * Time = generation.
     */
    public final int _generation;

    /**
     * Time = steady-state repeat.
     */
    public final int _steadyStateRepeat;

    /**
     * Parameterized constructor (sets the steady-state repeat number to 0).
     *
     * @param generation generation number (at least 0; capped at 0)
     */
    public EATimestamp(int generation)
    {
        this(generation, 0);
    }

    /**
     * Parameterized constructor.
     *
     * @param generation        generation number (at least 0; capped at 0)
     * @param steadyStateRepeat steady-state repeat number (at least 0; capped at 0)
     */
    public EATimestamp(int generation, int steadyStateRepeat)
    {
        _generation = Math.max(0, generation);
        _steadyStateRepeat = Math.max(0, steadyStateRepeat);
    }

    /**
     * Returns the string representation: "[Generation = number; Steady-state repeat = number]"
     *
     * @return string representation.
     */
    @Override
    public String toString()
    {
        return String.format("[Generation = %d; Steady-state repeat = %d]", _generation, _steadyStateRepeat);
    }

    /**
     * Clones the object.
     *
     * @return the clone (deep copy)
     */
    public EATimestamp getClone()
    {
        return new EATimestamp(_generation, _steadyStateRepeat);
    }


    /**
     * The "equals" method. Two timestamps are considered equal when they have the same internal states (the same
     * generation and steady state repeat values)
     *
     * @param obj other object for comparison
     * @return true, if objects are equal; false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        EATimestamp eaTimestamp = (EATimestamp) obj;
        if (eaTimestamp._generation != _generation) return false;
        return eaTimestamp._steadyStateRepeat == _steadyStateRepeat;
    }
}
