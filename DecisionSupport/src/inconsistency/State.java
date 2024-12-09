package inconsistency;

import history.PreferenceInformationWrapper;
import model.constructor.Report;
import model.internals.AbstractInternalModel;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Auxiliary class that represents the state of the inconsistency reintroduction process.
 *
 * @author MTomczyk
 */
public class State<T extends AbstractInternalModel>
{
    /**
     * Constructed model bundle.
     */
    public final Report<T> _report;

    /**
     * Preference information used to construct the bundle.
     */
    public final LinkedList<PreferenceInformationWrapper> _preferenceInformation;

    /**
     * Attempt number.
     */
    public final int _attempt;

    /**
     * Parameterized constructor.
     *
     * @param report           constructed model bundle
     * @param preferenceInformation preference information used to construct the bundle
     * @param attempt               attempt number
     */
    public State(Report<T> report, LinkedList<PreferenceInformationWrapper> preferenceInformation, int attempt)
    {
        _report = report;
        _preferenceInformation = preferenceInformation;
        _attempt = attempt;
    }


    /**
     * Creates and returns the string representation of the state. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @return string representation
     */
    public String[] getStringRepresentation()
    {
        return getStringRepresentation(0);
    }

    /**
     * Creates and returns the string representation of the state. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    public String[] getStringRepresentation(int indent)
    {
        LinkedList<String> lines = new LinkedList<>();
        String ind = StringUtils.getIndent(indent);

        lines.add(ind + "State for attempt = " + _attempt);
        lines.add(ind + "Data on model bundle:");
        String [] S = _report.getStringRepresentation(indent + 2);
        lines.addAll(Arrays.asList(S));
        lines.add(ind + "Data on preference information:");
        if ((_preferenceInformation == null) || (_preferenceInformation.isEmpty()))
            lines.add(ind + "  None");
        else for (PreferenceInformationWrapper pw : _preferenceInformation) lines.add(ind + "  " + pw.toString());
        return StringUtils.getArrayFromList(lines);
    }

    /**
     * Auxiliary method for printing the string representation.
     */
    public void printStringRepresentation()
    {
        System.out.print(this);
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        String[] lines = getStringRepresentation(0);
        StringBuilder sb = new StringBuilder();
        for (String l : lines) sb.append(l).append(System.lineSeparator());
        return sb.toString();
    }
}
