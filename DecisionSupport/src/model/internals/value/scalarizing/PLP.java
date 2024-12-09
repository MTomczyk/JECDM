package model.internals.value.scalarizing;

import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;

/**
 * Extension of {@link AbstractInternalModel} for using the PLP (point-line projection) as internal models.
 * Wrapper for {@link space.scalarfunction.PointLineProjection}.
 *
 * @author MTomczyk
 */
public class PLP extends AbstractScalarizingFunctionInternalModel implements IInternalModel
{
    /**
     * Parameterized constructor.
     *
     * @param plp instance of {@link space.scalarfunction.PointLineProjection} to be wrapped
     */
    public PLP(space.scalarfunction.PointLineProjection plp)
    {
        super(plp.toString(), plp);
    }
}
