package system.model;

import dmcontext.DMContext;
import model.internals.AbstractInternalModel;
import system.AbstractReport;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Report on the last call of {@link ModelSystem#updateModel()}
 *
 * @author MTomczyk
 */
public class Report<T extends AbstractInternalModel> extends AbstractReport
{
    /**
     * Preference mode name.
     */
    public String _modelName;

    /**
     * Provides the final (consistent) model bundle.
     */
    public model.constructor.Report<T> _report = null;

    /**
     * Flag indicating whether the inconsistency occurred.
     */
    public boolean _inconsistencyOccurred = false;

    /**
     * Report on inconsistency handling.
     */
    public inconsistency.Report<T> _reportOnInconsistencyHandling = null;

    /**
     * Parameterized constructor.
     *
     * @param dmContext       current decision-making context
     * @param modelName preference model name
     */
    protected Report(DMContext dmContext, String modelName)
    {
        super(dmContext);
        _modelName = modelName;
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
        lines.add(ind + "Report for model = " + _modelName);
        lines.add(ind + "Inconsistency occurred = " + _inconsistencyOccurred);
        if ((_inconsistencyOccurred) && (_reportOnInconsistencyHandling != null))
        {
            lines.add(ind + "Report on inconsistency handling:");
            String[] S = _reportOnInconsistencyHandling.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }

        if (_report != null)
        {
            lines.add(ind + "Report on the consistent model bundle:");
            String[] S = _report.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }

        return StringUtils.getArrayFromList(lines);
    }
}
