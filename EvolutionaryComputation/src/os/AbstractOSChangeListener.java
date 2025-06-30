package os;

import ea.EA;
import exception.PhaseException;
import space.normalization.builder.INormalizationBuilder;
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
     */
    protected final INormalizationBuilder _builder;

    /**
     * Parameterized constructor.
     *
     * @param builder normalization builder: builds normalization based on the info on the objective space.
     */
    public AbstractOSChangeListener(INormalizationBuilder builder)
    {
        _builder = builder;
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
    public void action(EA ea, ObjectiveSpace os, ObjectiveSpace prevOS) throws PhaseException
    {

    }
}
