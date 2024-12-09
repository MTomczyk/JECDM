package summary;

import utils.StringUtils;

import java.time.LocalDateTime;

/**
 * Abstract class that manages fields/functionalities common to various summary-related classes.
 *
 * @author MTomczyk
 */


class AbstractSummary
{
    /**
     * Indent used when generating the messages.
     */
    protected final int _indent;

    /**
     * If true, the termination happened due to exception.
     */
    protected boolean _terminationDueToException = false;

    /**
     * Exception message captured (each line individually).
     */
    protected String[] _exceptionMessage = null;

    /**
     * Timestamp indicating when the execution process started.
     */
    protected LocalDateTime _startTimestamp;

    /**
     * Timestamp indicating when the execution process ended
     */
    protected LocalDateTime _stopTimestamp;

    /**
     * Parameterized constructor.
     *
     * @param indent indent used when generating the messages
     */
    public AbstractSummary(int indent)
    {
        _indent = indent;
    }

    /**
     * Auxiliary method for building a summarizing string.
     *
     * @param sb     string builder being currently used
     * @param indent the number of spaces to be put in front of each line to be printed
     */
    protected void applyBasicStatistics(StringBuilder sb, int indent)
    {
        String ls = System.lineSeparator();
        String ind = StringUtils.getIndent(indent);

        sb.append(ind).append("Started = ").append(StringUtils.getTimestamp(_startTimestamp)).append(ls);
        sb.append(ind).append("Ended = ").append(StringUtils.getTimestamp(_stopTimestamp)).append(ls);
        sb.append(ind).append("Took = ").append(StringUtils.getDeltaTime(_startTimestamp, _stopTimestamp)).append(ls);
        sb.append(ind).append("Termination due to exception = ").append(_terminationDueToException).append(ls);
        if ((_terminationDueToException) && (_exceptionMessage != null))
        {
            sb.append(ind).append("Exception message: ").append(ls);
            for (String l : _exceptionMessage) sb.append(ind).append(l);
        }
    }

    /**
     * Getter for the flag indicating whether termination happened due to exception.
     *
     * @return true = termination happened due to exception; false otherwise.
     */
    public boolean isTerminatedDueToException()
    {
        return _terminationDueToException;
    }

    /**
     * Setter for the flag indicating whether termination happened due to exception.
     *
     * @param terminationDueToException the flag
     */
    public void setTerminationDueToException(boolean terminationDueToException)
    {
        _terminationDueToException = terminationDueToException;
    }

    /**
     * Getter for the exception message captured (each line individually).
     *
     * @return the exception message (lines)
     */
    public String[] getExceptionMessage()
    {
        return _exceptionMessage;
    }

    /**
     * Setter for the exception message captured (each line individually).
     *
     * @param exceptionMessage the exception message (lines)
     */
    public void setExceptionMessage(String[] exceptionMessage)
    {
        _exceptionMessage = exceptionMessage;
    }

    /**
     * Getter for the timestamp indicating when the execution process started.
     *
     * @return the timestamp
     */
    public LocalDateTime getStartTimestamp()
    {
        return _startTimestamp;
    }

    /**
     * Setter for the timestamp indicating when the execution process started.
     *
     * @param startTimestamp the timestamp
     */
    public void setStartTimestamp(LocalDateTime startTimestamp)
    {
        _startTimestamp = startTimestamp;
    }

    /**
     * Getter for the timestamp indicating when the execution process ended.
     *
     * @return the timestamp
     */
    public LocalDateTime getStopTimestamp()
    {
        return _stopTimestamp;
    }

    /**
     * Setter for the timestamp indicating when the execution process ended.
     *
     * @param stopTimestamp the timestamp
     */
    public void setStopTimestamp(LocalDateTime stopTimestamp)
    {
        _stopTimestamp = stopTimestamp;
    }
}
