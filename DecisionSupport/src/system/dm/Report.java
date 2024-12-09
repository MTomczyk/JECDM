package system.dm;

import dmcontext.DMContext;
import system.AbstractReport;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Report on the last call of {@link DecisionMakerSystem#updateModels()}
 *
 * @author MTomczyk
 */
public class Report extends AbstractReport
{
    /**
     * Reports on model updates.
     */
    public LinkedList<system.model.Report<?>> _reportsOnModelUpdates = null;

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     */
    protected Report(DMContext dmContext)
    {
        super(dmContext);
    }

    /**
     * Creates and returns the string representation of the report. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    public String [] getStringRepresentation(int indent)
    {
        if (_reportsOnModelUpdates == null) return new String[0];
        LinkedList<String> lines = new LinkedList<>();
        String ind = StringUtils.getIndent(indent);
        applyBasicLines(lines, ind);
        lines.add(ind + "Reports on models updates:");
        if ((_reportsOnModelUpdates == null) || (_reportsOnModelUpdates.isEmpty()))
            lines.add(ind + "  None");
        else
        {
            for (system.model.Report<?> r: _reportsOnModelUpdates)
            {
                String [] S = r.getStringRepresentation(indent + 2);
                lines.addAll(Arrays.asList(S));
            }
        }
        return StringUtils.getArrayFromList(lines);
    }


}
