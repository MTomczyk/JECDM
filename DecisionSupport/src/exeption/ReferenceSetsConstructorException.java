package exeption;

import exception.AbstractException;

/**
 * Exception class for wrapping the errors that occurred when constructing alternatives reference sets
 * (see {@link interaction.reference.ReferenceSetsConstructor}).
 *
 * @author MTomczyk
 */
public class ReferenceSetsConstructorException extends AbstractException
{
    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that handled the exception
     */
    public ReferenceSetsConstructorException(String message, Class<?> handler)
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
    public ReferenceSetsConstructorException(String message, Class<?> handler, Throwable cause)
    {
        super(message, handler, cause);
    }

}
