package system.modules.updater;

import dmcontext.DMContext;
import system.AbstractReport;
import system.dm.DM;
import utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Report-like class associated with {@link ModelsUpdaterModule} processing.
 *
 * @author MTomczyk
 */


public class Report extends AbstractReport
{
    /**
     * Reference to the decision makers' identifiers.
     */
    public final DM [] _DMs;

    /**
     * Most recent reports on the models updates.
     */
    public HashMap<DM, system.dm.Report> _modelsUpdatesReports;

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     * @param DMs reference to the decision makers' identifiers
     */
    protected Report(DMContext dmContext, DM [] DMs)
    {
        super(dmContext);
        _DMs = DMs;
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

        lines.add(ind + "REPORT ON THE MODELS UPDATES CALL");
        applyBasicLines(lines, ind);

        for (DM dm : _DMs)
        {
            lines.add(ind + "Report for decision maker = " + dm.getName() + ":");
            system.dm.Report r = null;
            if (_modelsUpdatesReports != null) r = _modelsUpdatesReports.get(dm);
            if (r == null) lines.add(ind + "  None");
            else
            {
                String[] S = r.getStringRepresentation(indent + 2);
                lines.addAll(Arrays.asList(S));
            }
        }

        return StringUtils.getArrayFromList(lines);
    }


}
