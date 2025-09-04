package system;

import dmcontext.DMContext;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Abstract report-like class. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractReport
{
    /**
     * Indicates the processing time (in ms).
     */
    public double _processingTime;

    /**
     * Timestamp (iteration).
     */
    public int _iteration;

    /**
     * Timestamp (date and time associated with the decision-making context).
     */
    public LocalDateTime _dmContextDateTime;

    /**
     * Timestamp (date and time associated with the object creation).
     */
    public LocalDateTime _reportCreationDateTime;

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     */
    protected AbstractReport(DMContext dmContext)
    {
        _iteration = dmContext.getCurrentIteration();
        _dmContextDateTime = dmContext.getCurrentDateTime();
        _reportCreationDateTime = LocalDateTime.now();
    }

    /**
     * Auxiliary method for printing the report.
     */
    public void printStringRepresentation()
    {
        printStringRepresentation(0);
    }

    /**
     * Auxiliary method for printing the report.
     *
     * @param indent indent
     */
    public void printStringRepresentation(int indent)
    {
        String[] lines = getStringRepresentation(indent);
        if (lines != null) for (String l : lines) System.out.println(l);
    }

    /**
     * Auxiliary method that applied basic statistic to the string representation being constructed.
     *
     * @param lines  lines being constructed
     * @param indent indent
     */
    protected void applyBasicLines(LinkedList<String> lines, String indent)
    {
        lines.add(indent + "Iteration = " + _iteration);
        lines.add(indent + "Date and time (decision-making context) = " + StringUtils.getTimestamp(_dmContextDateTime));
        lines.add(indent + "Date and time (report creation) = " + StringUtils.getTimestamp(_reportCreationDateTime));
    }

    /**
     * Creates and returns the string representation of the report. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @return string representation
     */
    public String[] getStringRepresentation()
    {
        return getStringRepresentation(0);
    }

    /**
     * Creates and returns the string representation of the report. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    public String[] getStringRepresentation(int indent)
    {
        return null;
    }
}
