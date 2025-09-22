package emo;

import ea.AbstractEABuilder;
import ea.EA;
import emo.interactive.StandardDSSBuilder;
import emo.interactive.iemod.IEMODBuilder;
import exception.EAException;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating preference-based EMO algorithms with a standard decision-support system
 * ({@link emo.interactive.StandardDSSBuilder}; e.g., {@link IEMODBuilder#getInstance()}).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractStandardDSSEMOABuilder<T extends EA,
        M extends AbstractValueInternalModel> extends AbstractEMOABuilder<T>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public AbstractStandardDSSEMOABuilder(IRandom R)
    {
        super(R);
    }

    /**
     * The object assisting in constructing a decision support system for IEMO/D.
     */
    protected StandardDSSBuilder<M> _dssBuilder;

    /**
     * Setter for the object assisting in constructing a decision support system for IEMO/D.
     *
     * @param dssBuilder the object assisting in constructing a decision support system for IEMO/D
     */
    public void setStandardDSSBuilder(StandardDSSBuilder<M> dssBuilder)
    {
        _dssBuilder = dssBuilder;
    }

    /**
     * Getter for the object assisting in constructing a decision support system for IEMO/D.
     *
     * @return the object assisting in constructing a decision support system for IEMO/D
     */
    public StandardDSSBuilder<M> getDSSBuilder()
    {
        return _dssBuilder;
    }

    /**
     * Auxiliary method for performing a simple data validation. It is called by default
     * by {@link AbstractEABuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (_dssBuilder == null)
            throw EAException.getInstanceWithSource("The decision support system builder has not been provided",
                    this.getClass());
        _dssBuilder.validate();
    }
}
