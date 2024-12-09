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
     * @param msg exception message
     * @param handler          class that handled the exception
     */
    public GlobalException(String msg, Class<?> handler)
    {
        this(msg, handler, null);
    }

    /**
     * Parameterized exception.
     *
     * @param msg exception message
     * @param handler          class that handled the exception
     * @param cause            can be null; if provided, additional information can be retrieved
     */
    public GlobalException(String msg, Class<?> handler, Throwable cause)
    {
        super(msg, handler, cause, null, null, null, null);
    }
}
