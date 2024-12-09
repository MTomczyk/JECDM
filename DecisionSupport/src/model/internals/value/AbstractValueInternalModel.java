package model.internals.value;

import model.internals.AbstractInternalModel;
import model.internals.IInternalModel;

/**
 * Extension of {@link AbstractInternalModel} dedicated to value models.
 *
 * @author MTomczyk
 */
public abstract class AbstractValueInternalModel extends AbstractInternalModel implements IInternalModel
{
    /**
     * Parameterized constructor.
     *
     * @param name model name
     */
    public AbstractValueInternalModel(String name)
    {
        super(name);
    }
}
