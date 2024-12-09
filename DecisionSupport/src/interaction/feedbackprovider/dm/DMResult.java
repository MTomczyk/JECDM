package interaction.feedbackprovider.dm;

import dmcontext.DMContext;
import preference.IPreferenceInformation;
import system.AbstractReport;
import system.dm.DM;
import utils.StringUtils;

import java.util.LinkedList;

/**
 * Wrapper for the feedback provided by a single decision maker.
 *
 * @author MTomczyk
 */


public class DMResult extends AbstractReport
{

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     * @param dm  associated decision maker
     */
    public DMResult(DMContext dmContext, DM dm)
    {
        super(dmContext);
        _dm = dm;
    }

    /**
     * Associated decision maker.
     */
    private final DM _dm;

    /**
     * All feedback caught (associated with the DM).
     */
    public LinkedList<IPreferenceInformation> _feedback = null;


    /**
     * Auxiliary method constructing the string representation of the result. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @return string representation
     */
    @Override
    public String[] getStringRepresentation()
    {
        return getStringRepresentation(0);
    }

    /**
     * Auxiliary method constructing the string representation of the result. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public String[] getStringRepresentation(int indent)
    {
        String ind = StringUtils.getIndent(indent);
        if (_dm == null) return new String[]{ind + "No DMs are provided"};
        if (_feedback == null) return new String[]{ind + "No feedback is provided"};
        LinkedList<String> lines = new LinkedList<>();
        String ind2 = StringUtils.getIndent(indent + 2);
        String ind4 = StringUtils.getIndent(indent + 4);

        lines.add(ind + "Feedback provided by decision maker = " + _dm.getName() + ":");
        applyBasicLines(lines, ind2);
        lines.add(ind2 + "Retrieving time = " + _processingTime + " ms");

        if ((_feedback == null) || (_feedback.isEmpty()))
        {
            lines.add(ind + "Feedback:");
            lines.add(ind + "  None");
        }
        else
        {
            lines.add(ind2 + "Feedback: ");
            for (IPreferenceInformation pi : _feedback) lines.add(ind4 + pi.toString());
        }

        return StringUtils.getArrayFromList(lines);
    }
}
