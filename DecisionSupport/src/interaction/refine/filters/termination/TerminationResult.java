package interaction.refine.filters.termination;

/**
 * Auxiliary result-like class reporting on the termination filter outcome
 *
 * @author MTomczyk
 */
public class TerminationResult
{
    /**
     * If true, the result indicates termination; false otherwise.
     */
    public final boolean _shouldTerminate;

    /**
     * Provides a message linked to the termination.
     */
    public final String _message;

    /**
     * Parameterized constructor.
     *
     * @param shouldTerminate if true, the result indicates termination; false otherwise
     * @param message         provides a message linked to the termination
     */
    public TerminationResult(boolean shouldTerminate, String message)
    {
        _shouldTerminate = shouldTerminate;
        _message = message;
    }

    /**
     * Prints the string representation.
     * @return string representation
     */
    public String toString()
    {
        return "Should terminate = " + _shouldTerminate + "; message = " + _message;
    }
}
