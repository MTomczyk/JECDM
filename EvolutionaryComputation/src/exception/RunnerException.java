package exception;

/**
 * Represents an exception triggered when running the evolutionary algorithm.
 *
 * @author MTomczyk
 */
public class RunnerException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public RunnerException(String message, Class<?> handler)
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
    public RunnerException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
