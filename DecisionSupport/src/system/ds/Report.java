package system.ds;

import dmcontext.DMContext;
import system.AbstractReport;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;


/**
 * Report-like class associated with {@link system.ds.DecisionSupportSystem} processing
 * ({@link DecisionSupportSystem#executeProcess(DMContext.Params)} method).
 *
 * @author MTomczyk
 */


public class Report extends AbstractReport
{
    /**
     * Report on the preference elicitation process.
     */
    public system.modules.elicitation.Report _elicitationReport = null;

    /**
     * Report on the models update process.
     */
    public system.modules.updater.Report _updateReport = null;

    /**
     * Parameterized constructor.
     *
     * @param dmContext               current decision-making context
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
    @Override
    public String[] getStringRepresentation(int indent)
    {
        LinkedList<String> lines = new LinkedList<>();
        String ind = StringUtils.getIndent(indent);
        lines.add(ind + "REPORT ON THE DECISION SUPPORT SYSTEM PROCESS EXECUTION");
        applyBasicLines(lines, ind);
        lines.add(ind + "Processing time = " + _processingTime + " ms");

        lines.add(ind + "Preference elicitation module report:");
        if (_elicitationReport == null) lines.add(ind + "  None");
        else
        {
            String [] S = _elicitationReport.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }

        lines.add(ind + "Models updater module report:");
        if (_updateReport == null) lines.add(ind + "  None");
        else
        {
            String [] S = _updateReport.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }

        return StringUtils.getArrayFromList(lines);
    }
}
