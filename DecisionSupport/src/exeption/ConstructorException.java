package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when updating (constructing) preference models given the
 * decision maker's preference information (see {@link model.constructor.IConstructor}).
 *
 * @author MTomczyk
 */
public class ConstructorException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public ConstructorException(String message, Class<?> handler)
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
    public ConstructorException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }

}
