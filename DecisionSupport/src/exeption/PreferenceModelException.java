package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred during preference-model-related processing (see {@link PreferenceModelException}).
 *
 * @author MTomczyk
 */
public class PreferenceModelException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public PreferenceModelException(String message, Class<?> handler)
    {
        this(message, handler, (Class<?>) null);
    }


    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     * @param source exception source
     */
    public PreferenceModelException(String message, Class<?> handler, Class<?> source)
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
    public PreferenceModelException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
