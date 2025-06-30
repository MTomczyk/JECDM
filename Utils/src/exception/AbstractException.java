package exception;

/**
 * Top-level class for custom exceptions.
 *
 * @author MTomczyk
 */
public abstract class AbstractException extends java.lang.Exception
{
    /**
     * Class (class) that caught the exception
     */
    protected final Class<?> _handler;

    /**
     * Class (class) that thrown the exception
     */
    protected final Class<?> _source;

    /**
     * Line of code that caused the exception.
     */
    protected final Integer _line;

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     */
    public AbstractException(String message, Class<?> handler)
    {
        this(message, handler, (Throwable) null);
    }


    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     * @param source exception source
     */
    public AbstractException(String message, Class<?> handler, Class<?> source)
    {
        super(message, null);
        _handler = handler;
        _line = null;
        _source = source;
    }

    /**
     * Parameterized constructor
     *
     * @param message exception message
     * @param handler class that caught the exception
     * @param cause   can be null; if provided, additional information can be retrieved
     */
    public AbstractException(String message, Class<?> handler, Throwable cause)
    {
        super(message, cause);
        _handler = handler;
        if ((getCause() != null) && (getCause().getStackTrace() != null) && (getCause().getStackTrace().length > 0)
                && (getCause().getStackTrace()[0] != null))
        {
            _line = getCause().getStackTrace()[0].getLineNumber();
            _source = getCause().getStackTrace()[0].getClass();
        }
        else
        {
            _line = null;
            _source = null;
        }
    }

    /**
     * Returns the string representing the reason for exception. The format is: "(handler = 'handler', reason = 'message')",
     * or "(handler = 'handler', line in throwing class = 'line', reason = 'message')" if the cause is provided via the constructor,
     *
     * @return reason message
     */
    public String getDetailedReasonMessage()
    {
        String hName = null;
        if (_handler != null) hName = _handler.getName();
        if ((_line != null) && (_source != null))
            return "(handler = " + hName + ", line in throwing class = " + _line + ", throwing class = "
                    + _source + ", reason = " + getMessage() + ")";
        return "(handler = " + hName + ", reason = " + getMessage() + ")";
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Exception handled by: ");
        if (_handler != null) sb.append(_handler.getName());
        else sb.append("null");
        sb.append(", ");
        sb.append("caused by: ");
        if (_source != null) sb.append(_source.getName());
        else sb.append("null");
        sb.append(", ");
        sb.append("happened in line: ");
        if (_line != null) sb.append(_line);
        else sb.append("null");
        sb.append(", ");
        sb.append("message: ");
        if (getMessage() != null) sb.append(getMessage());
        else sb.append("null");
        return sb.toString();
    }

}
