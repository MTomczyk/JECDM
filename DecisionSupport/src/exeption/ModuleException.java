package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when executing processes
 * of {@link system.modules.AbstractModule}.
 *
 * @author MTomczyk
 */
public class ModuleException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public ModuleException(String message, Class<?> handler)
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
    public ModuleException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }
}
