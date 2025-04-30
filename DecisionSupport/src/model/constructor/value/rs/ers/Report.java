package model.constructor.value.rs.ers;

import dmcontext.DMContext;
import model.internals.AbstractInternalModel;

import java.util.LinkedList;

/**
 * Customized extension of {@link model.constructor.Report} for {@link ERS}.
 *
 * @author MTomczyk
 */
public class Report<T extends AbstractInternalModel> extends model.constructor.Report<T>
{
    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     */
    public Report(DMContext dmContext)
    {
        super(dmContext);
    }


    /**
     * Auxiliary method that can be overwritten to add extra log lines (used by {@link model.constructor.Report#getStringRepresentation(int)}).
     *
     * @param lines lines being processed
     * @param ind   base indent
     */
    @Override
    protected void addExtraLogLines(LinkedList<String> lines, String ind)
    {
        // do nothing yet
    }
}
