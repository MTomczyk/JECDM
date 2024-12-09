package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred during decision-maker-system-related processing
 * (see {@link system.dm.DecisionMakerSystem}).
 *
 * @author MTomczyk
 */
public class DecisionMakerSystemException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public DecisionMakerSystemException(String message, Class<?> handler)
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
    public DecisionMakerSystemException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
