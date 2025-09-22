package exception;

/**
 * Represents an exception triggered when executing an EA {@link ea.EA}.
 *
 * @author MTomczyk
 */
public class EAException extends AbstractException
{

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public EAException(String message, Class<?> handler)
    {
        super(message, handler);
    }

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     * @param source  exception source
     */
    public EAException(String message, Class<?> handler, Class<?> source)
    {
        super(message, handler, source);
    }

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     * @param cause   can be null; if provided, additional information can be retrieved
     */
    public EAException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }

    /**
     * Factory-like method.
     *
     * @param message exception message
     * @param source  exception source
     * @return an exception instance that can be thrown and propagated higher
     */
    public static EAException getInstanceWithSource(String message, Class<?> source)
    {
        return new EAException(message, null, source);
    }
}
