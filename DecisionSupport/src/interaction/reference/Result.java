package interaction.reference;

import dmcontext.DMContext;
import interaction.Status;
import system.AbstractReport;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Container-like class representing a result of creating reference sets (see {@link ReferenceSetsConstructor}).
 *
 * @author MTomczyk
 */
public class Result extends AbstractReport
{
    /**
     * Processing status.
     */
    public Status _status = null;

    /**
     * Passed result of the refining process.
     */
    public interaction.refine.Result _refiningResults;

    /**
     * If true, the process was terminated because there were not enough alternatives (check immediately after the reduction phase is completed).
     */
    public boolean _terminatedDueToNotEnoughAlternatives = false;

    /**
     * Auxiliary message related to having not enough alternatives can be passed via this field.
     */
    public String _terminatedDueToNotEnoughAlternativesMessage = null;

    /**
     * Constructed reference sets (container).
     */
    public ReferenceSetsContainer _referenceSetsContainer;


    /**
     * Parameterized constructor.
     *
     * @param dmContext             current decision-making context
     * @param refiningResults of the refining process
     */
    public Result(DMContext dmContext, interaction.refine.Result refiningResults)
    {
        super(dmContext);
        _refiningResults = refiningResults;
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
        lines.add(ind + "Status = " + _status);
        applyBasicLines(lines, ind);
        lines.add(ind + "Terminated due to having not enough alternatives = " + _terminatedDueToNotEnoughAlternatives);
        lines.add(ind + "Terminated due to having not enough alternatives message = " + _terminatedDueToNotEnoughAlternativesMessage);
        lines.add(ind + "Processing time = " + _processingTime  + " ms");

        if (_refiningResults != null)
        {
            lines.add(ind + "Results of the refining process:");
            String [] S = _refiningResults.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }
        else
        {
            lines.add(ind + "Results of the refining process:");
            lines.add(ind + "  None");
        }


        if (_referenceSetsContainer != null)
        {
            lines.add(ind + "Reference sets container: ");
            String [] S = _referenceSetsContainer.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }
        else
        {
            lines.add(ind + "Reference sets container:");
            lines.add(ind + "  None");
        }

        return StringUtils.getArrayFromList(lines);
    }


}
