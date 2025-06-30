package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when reintroducing consistency.
 * (see {@link inconsistency.IInconsistencyHandler}).
 *
 * @author MTomczyk
 */
public class InconsistencyHandlerException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public InconsistencyHandlerException(String message, Class<?> handler)
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
    public InconsistencyHandlerException(String message, Class<?> handler, Class<?> source)
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
    public InconsistencyHandlerException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
