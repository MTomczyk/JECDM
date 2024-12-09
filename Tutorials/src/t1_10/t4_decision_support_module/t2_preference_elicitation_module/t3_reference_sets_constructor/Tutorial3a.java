package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t3_reference_sets_constructor;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.constructor.AllAlternatives;
import interaction.reference.constructor.IReferenceSetConstructor;

import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link interaction.reference.constructor.IReferenceSetConstructor}
 * interface ({@link AllAlternatives}).
 *
 * @author MTomczyk
 */
public class Tutorial3a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // All alternatives = treats the input refined alternatives as the reference set (useful for practical applications)
        IReferenceSetConstructor allAlternatives = new AllAlternatives();

        // Create the context:
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._currentIteration = 0;
        DMContext context = new DMContext(pDMC, null, null);

        // Input refined alternatives:
        AbstractAlternatives<?> refinedAlternatives = new Alternatives(Alternative.getAlternativeArray("A", new double[][]{
                {1.0d, 0.1d}, {0.2d, 2.0d}, {0.1d, 1.0d}
        }));

        try
        {
            LinkedList<ReferenceSet> referenceSets = allAlternatives.constructReferenceSets(context, refinedAlternatives);

            // Number of reference sets (should be one):
            System.out.println("No. reference sets = " + referenceSets.size());
            ReferenceSet rs = referenceSets.getFirst();
            System.out.println(rs);

        } catch (ReferenceSetsConstructorException e)
        {
            throw new RuntimeException(e);
        }
    }
}
