package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when refining alternatives superset
 * (see {@link interaction.refine.Refiner}).
 *
 * @author MTomczyk
 */
public class RefinerException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public RefinerException(String message, Class<?> handler)
    {
        this(message, handler, null);
    }

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     * @param cause   can be null; if provided, additional information can be retrieved
     */
    public RefinerException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }

}
