package interaction.trigger;

import java.time.LocalDateTime;

/**
 * Provides auxiliary data on postponing.
 *
 * @author MTomczyk
 */
public class Postpone
{
    /**
     * Reason for postponing
     */
    public final Reason _reason;

    /**
     * Timestamp (iteration).
     */
    public final int _iteration;

    /**
     * Timestamp (date and time).
     */
    public final LocalDateTime _dateTime;

    /**
     * Parameterized constructor.
     *
     * @param reason    reason for postponing
     * @param iteration timestamp (iteration)
     * @param dateTime  timestamp (date and time)
     */
    public Postpone(Reason reason, int iteration, LocalDateTime dateTime)
    {
        _reason = reason;
        _iteration = iteration;
        _dateTime = dateTime;
    }

    /**
     * Auxiliary method for printing the result (its string representation).
     */
    public void printResult()
    {
        System.out.println(getStringRepresentation());
    }

    /**
     * Auxiliary method constructing the string representation (report).
     *
     * @return string representation
     */
    public String getStringRepresentation()
    {
        return "Reason for postponing = " + _reason + System.lineSeparator() +
                "Timestamp (iteration) = " + _iteration + System.lineSeparator() +
                "Timestamp (date and time) = " + _iteration + System.lineSeparator();
    }
}
