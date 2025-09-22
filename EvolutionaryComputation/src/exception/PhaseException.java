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

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     * @param source  exception source
     */
    public PhaseException(String message, Class<?> handler, Class<?> source)
    {
        super(message, handler, source);
    }

    /**
     * Factory-like method.
     *
     * @param message exception message
     * @param source  exception source
     * @return an instance of an exception to be thrown
     */
    public static PhaseException getInstanceWithSource(String message, Class<?> source)
    {
        return new PhaseException(message, null, source);
    }
}
