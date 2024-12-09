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
     * @param handler class that handled the exception
     * @param cause   can be null; if provided, additional information can be retrieved
     */
    public EAException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
