package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when triggering the elicitation process
 * (see {@link interaction.trigger.InteractionTrigger}).
 *
 * @author MTomczyk
 */
public class TriggerException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public TriggerException(String message, Class<?> handler)
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
    public TriggerException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }

}
