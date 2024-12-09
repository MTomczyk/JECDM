package t1_10.t4_decision_support_module.t1_concepts.t1_decision_making_context;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import space.Range;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

import java.time.LocalDateTime;

/**
 * This tutorial focuses on creating a decision-making context (params container).
 *
 * @author MTomczyk
 */
public class Tutorial1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create object instance:
        DMContext.Params pDMC = new DMContext.Params();
        // Specify current iteration (e.g., generation):
        pDMC._currentIteration = 0;
        // Specify the alternatives: A0 = [0.0, 0.0] and A1 = [1.0, 1.0] (performance vectors)
        pDMC._currentAlternativesSuperset = new Alternatives(
                Alternative.getAlternativeArray("A",
                        new double[][]{{0.0d, 0.0d}, {1.0d, 1.0d}}));
        // Specify current OS (e.g., [0, 1]^2 bounds with the objectives to be minimized):
        pDMC._currentOS = new ObjectiveSpace(Range.getDefaultRanges(2), new boolean[2]);
        // Specify that the OS has not changed since the previous call:
        pDMC._osChanged = false;
        // Specify the normalization builder:
        pDMC._normalizationBuilder = new StandardLinearBuilder();

        // Typically  created by the DecisionSupportSystem object (sets the criteria and the current local date and time):
        DMContext context = new DMContext(pDMC, Criteria.constructCriteria("C", 2, false), LocalDateTime.now());

        // Print the basic data about the context:
        System.out.println(context);
    }
}
