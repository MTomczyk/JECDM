package t1_10.t4_decision_support_module.t2_preference_elicitation_module;

import alternative.AbstractAlternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import random.IRandom;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

import java.time.LocalDateTime;

/**
 * Provides common functionalities.
 *
 * @author MTomczyk
 */
public class Common
{
    /**
     * Creates the decision-making context
     *
     * @param iteration    iteration number
     * @param alternatives alternatives
     * @param startingTime system starting time
     * @param criteria     criteria array
     * @param os           objective space
     * @return decision-making context
     */
    public static DMContext getContext(int iteration, AbstractAlternatives<?> alternatives, LocalDateTime startingTime,
                                       Criteria criteria, ObjectiveSpace os)
    {
        return getContext(iteration, alternatives, startingTime, criteria, os, null);
    }

    /**
     * Creates the decision-making context
     *
     * @param iteration    iteration number
     * @param alternatives alternatives
     * @param startingTime system starting time
     * @param criteria     criteria array
     * @param os           objective space
     * @param R            random number generator
     * @return decision-making context
     */
    public static DMContext getContext(int iteration, AbstractAlternatives<?> alternatives, LocalDateTime startingTime,
                                       Criteria criteria, ObjectiveSpace os, IRandom R)
    {
        // Create the context:
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._currentIteration = iteration;
        pDMC._currentAlternativesSuperset = alternatives;
        pDMC._normalizationBuilder = new StandardLinearBuilder();
        pDMC._currentOS = os;
        pDMC._osChanged = false;
        pDMC._R = R;
        return new DMContext(pDMC, criteria, startingTime);
    }
}
