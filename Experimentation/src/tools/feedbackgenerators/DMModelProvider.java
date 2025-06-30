package tools.feedbackgenerators;

import model.internals.value.AbstractValueInternalModel;
import random.IRandom;
import scenario.Scenario;
import space.normalization.INormalization;

/**
 * Standard implementation of {@link IDMModelProvider} for providing DM's based on simple value-function models.
 *
 * @author MTomczyk
 */
public class DMModelProvider<T extends AbstractValueInternalModel> extends DimensionsDependent implements IDMModelProvider
{
    /**
     * Auxiliary interface for objects responsible for providing DM's internal models.
     *
     * @param <T> model definition
     */
    public interface IInternalModel<T extends AbstractValueInternalModel>
    {
        /**
         * The main method
         *
         * @param scenario       scenario definition
         * @param t              trial being currently processed
         * @param M              the number of criteria/objectives
         * @param normalizations normalization functions used to normalize alternatives' evaluations within the built model
         * @param R              random number generator
         * @return DM's internal model
         */
        T getModel(Scenario scenario, int t, int M, INormalization[] normalizations, IRandom R);
    }

    /**
     * DM's internal model provider.
     */
    private final IInternalModel<T> _internalModel;

    /**
     * Parameterized constructor.
     *
     * @param dimensionsKey key associated with the number of criteria/objectives
     * @param internalModel internal model provider
     */
    public DMModelProvider(String dimensionsKey, IInternalModel<T> internalModel)
    {
        super(dimensionsKey);
        _internalModel = internalModel;
    }

    /**
     * The main method.
     *
     * @param scenario       scenario being currently processed
     * @param t              trial being currently processed
     * @param normalizations normalizations associated with the alternatives space
     * @param R              random number generator
     * @return decision maker's object
     */
    @Override
    public T getModel(Scenario scenario, int t, INormalization[] normalizations, IRandom R)
    {
        return _internalModel.getModel(scenario, t, getM(scenario), normalizations, R);
    }
}
