package os;

import ea.IEA;
import exception.PhaseException;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

/**
 * Abstract implementation of the {@link IOSChangeListener} interface.
 *
 * @author MTomczyk
 */

public abstract class AbstractOSChangeListener implements IOSChangeListener
{
    /**
     * Normalization builder: builds normalization based on the info on the objective space.
     *
     * @deprecated this field is to be removed in future releases; get the builder form {@link IEA#getNormalizationBuilder()}.
     */
    @Deprecated
    protected final INormalizationBuilder _builder = new StandardLinearBuilder();

    /**
     * Parameterized constructor.
     *
     * @param builder (parameter is ignored) normalization builder: builds normalization based on the info on the
     *                objective space.
     * @deprecated this constructor is to be removed from future releases
     */
    @Deprecated
    public AbstractOSChangeListener(INormalizationBuilder builder)
    {

    }

    /**
     * Default constructor.
     */
    public AbstractOSChangeListener()
    {

    }


    /**
     * Action to be performed when there is a change in the objective space.
     *
     * @param ea     evolutionary algorithm
     * @param os     objective space (updated)
     * @param prevOS objective space (outdated; for comparison)
     * @throws PhaseException the exception can be thrown
     */
    @Override
    public void action(IEA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {

    }
}
