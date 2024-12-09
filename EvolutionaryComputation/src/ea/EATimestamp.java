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

}
