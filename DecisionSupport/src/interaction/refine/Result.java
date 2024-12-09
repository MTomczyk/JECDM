package interaction.refine;

import alternative.AbstractAlternatives;
import dmcontext.DMContext;
import interaction.Status;
import system.AbstractReport;
import utils.StringUtils;

import java.util.LinkedList;

/**
 * Container-like class representing a result of creating a refined alternatives set (see {@link Refiner}).
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
     * If true, the termination was caused by a termination filter.
     */
    public boolean _terminatedDueToTerminationFilter = false;

    /**
     * This field can be used to store an additional message when the processing is terminated by a termination filter.
     */
    public String _terminatedDueToTerminationFilterMessage = null;

    /**
     * Processing time, in ms, linked to termination filters.
     */
    public long _terminationFiltersProcessingTime = 0;

    /**
     * Reduction size, i.e., how many alternatives were rejected by reduction filters.
     */
    public int _reductionSize;

    /**
     * Processing time linked to reduction filters.
     */
    public long _reductionFiltersProcessingTime = 0;

    /**
     * Field storing the alternatives that where not initially filtered aut.
     */
    public AbstractAlternatives<?> _refinedAlternatives;

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
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
        @SuppressWarnings("DuplicatedCode")
        String ind = StringUtils.getIndent(indent);
        LinkedList<String> lines = new LinkedList<>();
        lines.add(ind + "Status = " + _status);
        applyBasicLines(lines, ind);
        lines.add(ind + "Terminated due to termination filter = " + _terminatedDueToTerminationFilter);
        if (_terminatedDueToTerminationFilterMessage != null)
            lines.add(ind + "Terminated due to termination filter message = " + _terminatedDueToTerminationFilterMessage);
        lines.add(ind + "Termination filters processing time = " + _terminationFiltersProcessingTime + " ms");
        lines.add(ind + "Reduction size = " + _reductionSize);
        lines.add(ind + "Reduction filters processing time = " + _reductionFiltersProcessingTime + " ms");

        if (_refinedAlternatives != null)
        {
            StringBuilder s = new StringBuilder("Final alternatives = ");
            for (int i = 0; i < _refinedAlternatives.size(); i++)
            {
                s.append(_refinedAlternatives.get(i).getName());
                if (i < _refinedAlternatives.size() - 1) s.append(", ");
            }
            lines.add(ind + s);
        }

        return StringUtils.getArrayFromList(lines);
    }


}
