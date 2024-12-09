package inconsistency;

import dmcontext.DMContext;
import exeption.InconsistencyHandlerException;
import model.internals.AbstractInternalModel;

/**
 * Abstract implementation of {@link IInconsistencyHandler}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractInconsistencyHandler<T extends AbstractInternalModel> implements IInconsistencyHandler<T>
{
    /**
     * Handler's name (string representation).
     */
    protected final String _name;

    /**
     * If true, all the model bundles generated during consistency reintroduction are collected and returned in a report.
     */
    protected final boolean _storeAllStates;

    /**
     * Current decision-making context.
     */
    protected DMContext _dmContext;

    /**
     * Parameterized constructor.
     *
     * @param name                          handler's name
     * @param storeAllModelBundlesGenerated if true, all the model bundles generated during consistency reintroduction are collected and returned in a report
     */
    public AbstractInconsistencyHandler(String name, boolean storeAllModelBundlesGenerated)
    {
        _name = name;
        _storeAllStates = storeAllModelBundlesGenerated;
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext current decision-making context
     * @throws InconsistencyHandlerException the exception can be thrown and propagated higher
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws InconsistencyHandlerException
    {
        _dmContext = dmContext;
    }

    /**
     * Auxiliary method that can be used to unregister the current decision-making context {@link DMContext}.
     *
     */
    @Override
    public void unregisterDecisionMakingContext()
    {
        _dmContext = null;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }

}
