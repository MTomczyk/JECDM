package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when updating the history of preference elicitation
 * (see {@link history.History}).
 *
 * @author MTomczyk
 */
public class HistoryException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public HistoryException(String message, Class<?> handler)
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
    public HistoryException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
