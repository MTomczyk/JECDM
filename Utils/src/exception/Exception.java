package exception;


/**
 * Default implementation of {@link AbstractException}.
 *
 * @author MTomczyk
 */
public class Exception extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     */
    public Exception(String message, Class<?> handler)
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
    public Exception(String message, Class<?> handler, Class<?> source)
    {
        super(message, handler, source);
    }

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     * @param cause   can be null; if provided, additional information can be retrieved
     */
    public Exception(String message, Class<?> handler, Throwable cause)
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
    public static Exception getInstanceWithSource(String message, Class<?> source)
    {
        return new Exception(message, null, source);
    }
}
