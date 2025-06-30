package model.constructor.value.rs;

import model.constructor.IConstructor;
import model.constructor.random.IRandomModel;
import model.constructor.value.AbstractSamplingConstructor;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.rs.iterationslimit.IIterationsLimit;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

/**
 * Abstract class that provides fundamentals for rejection-sampling-based model constructors (e.g., {@link FRS}).
 *
 * @author MTomczyk
 */
public abstract class AbstractRejectionSampling<T extends AbstractValueInternalModel>
        extends AbstractSamplingConstructor<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel> extends AbstractSamplingConstructor.Params<T>
    {
        /**
         * The limit for the number of attempts to improve the set of generated models.
         * Can be conditional (see {@link IIterationsLimit}).
         */
        public IIterationsLimit _iterationsLimit = null;

        /**
         * Random model generator.
         */
        public final IRandomModel<T> _RM;

        /**
         * Parameterized constructor.
         *
         * @param RM random model generator
         */
        public Params(IRandomModel<T> RM)
        {
            _RM = RM;
        }
    }

    /**
     * The limit for the number of attempts to improve the set of generated models.
     * Can be conditional (see {@link IIterationsLimit}).
     */
    protected final IIterationsLimit _iterationsLimit;

    /**
     * Random model generator.
     */
    protected final IRandomModel<T> _RM;

    /**
     * Captured RNG.
     */
    protected IRandom _R;

    /**
     * Parameterized constructor.
     *
     * @param name name of the model constructor
     * @param p    params container
     */
    public AbstractRejectionSampling(String name, Params<T> p)
    {
        super(name, p);
        _iterationsLimit = p._iterationsLimit;
        _RM = p._RM;
        _normalizationUpdaters.add(_RM::setNormalizations);
    }
}
