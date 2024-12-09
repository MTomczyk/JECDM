package interaction.trigger;

import dmcontext.DMContext;
import system.AbstractReport;
import utils.StringUtils;

import java.util.LinkedList;

/**
 * Auxiliary class that stores the results of the interaction analysis.
 *
 * @author MTomczyk
 */
public class Result extends AbstractReport
{

    /**
     * Flag notifying whether to interact.
     */
    public boolean _shouldInteract = false;

    /**
     * This auxiliary field is used to store callers names (rules that decided upon the interaction).
     */
    public LinkedList<String> _callers;

    /**
     * Parameterized constructor.
     *
     * @param dmContext decision-making context
     */
    public Result(DMContext dmContext)
    {
        super(dmContext);
    }

    /**
     * Auxiliary method constructing the string representation of the result. Each line does not end with a new line
     * symbol and is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    @Override
    public String[] getStringRepresentation(int indent)
    {
        String ind = StringUtils.getIndent(indent);
        LinkedList<String> lines = new LinkedList<>();
        lines.add(ind + "Should interact = " + _shouldInteract);
        lines.add(ind + "Processing time  = " + _processingTime + " ms");
        applyBasicLines(lines, ind);

        if ((_callers != null) && (!_callers.isEmpty()))
        {
            lines.add(ind + "Reasons for interaction:");
            for (String s : _callers) lines.add(ind + " - " + s);
        }
        return StringUtils.getArrayFromList(lines);
    }
}
