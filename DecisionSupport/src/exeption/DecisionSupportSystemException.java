package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred at higher-level (decision support system).
 *
 * @author MTomczyk
 */
public class DecisionSupportSystemException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public DecisionSupportSystemException(String message, Class<?> handler)
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
    public DecisionSupportSystemException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
