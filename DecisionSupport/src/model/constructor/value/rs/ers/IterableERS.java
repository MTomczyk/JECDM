package model.constructor.value.rs.ers;

import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Iterable extension of {@link ERS}. This extension is not computationally efficient, so it should not be used in
 * practical applications. However, it grants access to initialize, step, and finalize methods, allowing the
 * sampler's performance to be examined iteratively.
 *
 * @author MTomczyk
 */
public class IterableERS<T extends AbstractValueInternalModel> extends ERS<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel> extends ERS.Params<T>
    {
        /**
         * Parameterized constructor.
         *
         * @param M  considered space dimensionality
         * @param RM random model generator
         */
        public Params(IRandomModel<T> RM, int M)
        {
            super(RM, M);
        }

        /**
         * If true, models array in report is derived from the queue after each step call.
         */
        public boolean _passModels = false;
    }

    /**
     * If true, models array in report is derived from the queue after each step call.
     */
    private final boolean _passModels;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public IterableERS(Params<T> p)
    {
        super(p);
        _passModels = p._passModels;
    }


    /**
     * Calls the protected {@link ERS#initializeStep(Report, LinkedList)} method.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return indicates whether to prematurely terminate (true)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public boolean initializeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        boolean r = super.initializeStep(bundle, preferenceInformation);
        // update bundle
        bundle._models = _models;
        return r;
    }

    /**
     * Executes the protected {@link ERS#executeStep(Report, LinkedList)}.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return returns the constructed model
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public T executeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        T r = super.executeStep(bundle, preferenceInformation);
        // update bundle
        if (_passModels)
        {
            if (bundle._models == null) bundle._models = new ArrayList<>(getModelsQueue().getQueue().size());
            else bundle._models.clear();
            for (SortedModel<T> t : getModelsQueue().getQueue())
                if (t._isCompatible) bundle._models.add(t._model);
        }
        return r;
    }

    /**
     * Executes the protected {@link ERS#finalizeStep(Report, LinkedList)}.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void finalizeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        super.finalizeStep(bundle, preferenceInformation);
    }
}
