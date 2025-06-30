package tools.feedbackgenerators;

import model.internals.value.AbstractValueInternalModel;
import random.IRandom;
import scenario.Scenario;
import space.normalization.INormalization;

/**
 * Auxiliary interface for objects responsible for providing an artificial decision maker (used to evaluate alternatives
 * ina scenario/trial being currently processed).
 *
 * @author MTomczyk
 */
public interface IDMModelProvider
{
    /**
     * The main method.
     *
     * @param scenario       scenario being currently processed
     * @param t              trial being currently processed
     * @param normalizations normalizations associated with the alternatives space
     * @param R              random number generator
     * @return artificial decision maker's internal model
     */
    AbstractValueInternalModel getModel(Scenario scenario, int t, INormalization[] normalizations, IRandom R);
}
