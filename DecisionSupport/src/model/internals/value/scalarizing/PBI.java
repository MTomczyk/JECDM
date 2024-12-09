package model.internals.value.scalarizing;

import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;

/**
 * Extension of {@link AbstractInternalModel} for using the PBI as internal models.
 * Wrapper for {@link space.scalarfunction.PBI}.
 *
 * @author MTomczyk
 */
public class PBI extends AbstractScalarizingFunctionInternalModel implements IInternalModel
{
    /**
     * Parameterized constructor.
     *
     * @param pbi instance of {@link space.scalarfunction.PBI} to be wrapped
     */
    public PBI(space.scalarfunction.PBI pbi)
    {
        super(pbi.toString(), pbi);
    }
}
