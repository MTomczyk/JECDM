package exception;


/**
 * Custom exception that captures unwanted system behavior that happens on the global level.
 *
 * @author MTomczyk
 */
public class GlobalException extends AbstractExperimentationException
{
    /**
     * Parameterized exception.
     *
     * @param msg     exception message
     * @param handler class that handled the exception (can be null)
     */
    public GlobalException(String msg, Class<?> handler)
    {
        this(msg, handler, (Throwable) null);
    }

    /**
     * Parameterized exception.
     *
     * @param msg     exception message
     * @param handler class that handled the exception (can be null)
     * @param source  source of exception (can be null)
     */
    public GlobalException(String msg, Class<?> handler, Class<?> source)
    {
        super(msg, handler, source, null, null, null, null);
    }

    /**
     * Parameterized exception.
     *
     * @param msg     exception message
     * @param handler class that handled the exception (can be null)
     * @param cause   can be null; if provided, additional information can be retrieved (can be null)
     */
    public GlobalException(String msg, Class<?> handler, Throwable cause)
    {
        super(msg, handler, cause, null, null, null, null);
    }
}
