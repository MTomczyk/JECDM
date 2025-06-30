package model.constructor.value.rs.frs;

import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.constructor.IConstructor;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.LinkedList;

/**
 * Iterable extension of {@link FRS}. This extension is not computationally efficient, so it should not be used in
 * practical applications. However, it grants access to initialize, step, and finalize methods, allowing the
 * sampler's performance to be examined iteratively.
 *
 *
 * @author MTomczyk
 */
public class IterableFRS<T extends AbstractValueInternalModel> extends FRS<T> implements IConstructor<T>
{
    /**
     * Params container.
     */
    public static class Params<T extends AbstractValueInternalModel> extends FRS.Params<T>
    {
        /**
         * Parameterized constructor.
         *
         * @param RM random model generator
         */
        public Params(IRandomModel<T> RM)
        {
            super(RM);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public IterableFRS(Params<T> p)
    {
        super(p);
    }

    /**
     * Calls the protected {@link FRS#initializeStep(Report, LinkedList)} method.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return indicates whether to prematurely terminate (true)
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    public boolean initializeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        return super.initializeStep(bundle, preferenceInformation);
    }

    /**
     * Execute the protected {@link FRS#executeStep(Report, LinkedList)}.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return returns the constructed model
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    public T executeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        return super.executeStep(bundle, preferenceInformation);
    }

    /**
     * Executes the protected {@link FRS#finalizeStep(Report, LinkedList)}.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown 
     */
    @Override
    public void finalizeStep(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        super.finalizeStep(bundle, preferenceInformation);
    }
}
