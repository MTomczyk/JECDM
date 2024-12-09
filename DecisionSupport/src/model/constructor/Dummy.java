package model.constructor;

import compatibility.CompatibilityAnalyzer;
import model.internals.AbstractInternalModel;

/**
 * Dummy implementation of {@link IConstructor} (does nothing, all the methods are inherited from the abstract class).
 *
 * @author MTomczyk
 */


public class Dummy<T extends AbstractInternalModel> extends AbstractConstructor<T> implements IConstructor<T>
{
    /**
     * Parameterized constructor.
     */
    public Dummy()
    {
        super("Dummy", new CompatibilityAnalyzer());
    }

}
