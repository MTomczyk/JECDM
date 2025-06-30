package model.constructor.value.rs.ers;

import dmcontext.DMContext;
import model.internals.AbstractInternalModel;

/**
 * Customized extension of {@link model.constructor.Report} for {@link ERS}.
 *
 * @author MTomczyk
 */
public class Report<T extends AbstractInternalModel> extends model.constructor.value.rs.Report<T>
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
}
