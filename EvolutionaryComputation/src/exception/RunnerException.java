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

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     * @param source  exception source
     */
    public RunnerException(String message, Class<?> handler, Class<?> source)
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
    public static RunnerException getInstanceWithSource(String message, Class<?> source)
    {
        return new RunnerException(message, null, source);
    }
}
