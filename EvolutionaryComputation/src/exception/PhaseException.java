package exception;

/**
 * Represents an exception triggered when executing a phase {@link phase.IPhase}.
 *
 * @author MTomczyk
 */
public class PhaseException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public PhaseException(String message, Class<?> handler)
    {
        super(message, handler);
    }

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     * @param cause   can be null; if provided, additional information can be retrieved
     */
    public PhaseException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
