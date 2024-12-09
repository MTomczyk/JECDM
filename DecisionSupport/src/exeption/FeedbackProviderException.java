package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when handling interactions (providing a feedback).
 * (see {@link interaction.refine.Refiner}).
 *
 * @author MTomczyk
 */
public class FeedbackProviderException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public FeedbackProviderException(String message, Class<?> handler)
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
    public FeedbackProviderException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }

}
