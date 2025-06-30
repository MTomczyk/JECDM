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
     * Parameterized constructor.
     *
     * @param generation        generation number
     * @param steadyStateRepeat steady-state repeat number
     */
    public EATimestamp(int generation, int steadyStateRepeat)
    {
        _generation = generation;
        _steadyStateRepeat = steadyStateRepeat;
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

}
