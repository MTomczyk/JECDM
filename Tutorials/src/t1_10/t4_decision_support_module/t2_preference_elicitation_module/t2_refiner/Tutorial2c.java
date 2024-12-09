package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t2_refiner;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.RefinerException;
import interaction.refine.Refiner;
import interaction.refine.Result;
import space.Range;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This tutorial focuses on the {@link interaction.refine.Refiner} interface.
 *
 * @author MTomczyk
 */
public class Tutorial2c
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Creates default object instance (default parameterization). Uses the RequiredSpread as the termination filter
        // Next, reduction filters involve RemoveDuplicatesInObjectiveSpace and RemoveDominated (in the provided order).
        Refiner.Params pR = Refiner.Params.getDefault(0.1d);
        Refiner refiner = new Refiner(pR);

        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2), new boolean[2]);
        INormalizationBuilder normalizationBuilder = new StandardLinearBuilder();

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", new double[][]
                {
                        {0.0d, 0.6d},
                        {0.1d, 0.1d},
                        {0.5d, 0.4d}, // dominated
                        {0.0d, 0.6d}, // duplicated + dominated
                        {0.3d, 0.0d},
                        {0.1d, 0.1d}, // duplicated
                        {0.3d, 0.2d}, // dominated
                        {0.1d, 0.3d}, // dominated
                });

        // Create the context:
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._currentIteration = 0;
        pDMC._currentAlternativesSuperset = new Alternatives(alternatives);
        pDMC._osChanged = false;
        pDMC._currentOS = os;
        pDMC._normalizationBuilder = normalizationBuilder;
        DMContext context = new DMContext(pDMC, criteria, startingTimestamp);

        try
        {
            // Perform the refining process and print the result:
            Result result = refiner.refine(context);
            result.printStringRepresentation();
        } catch (RefinerException e)
        {
            throw new RuntimeException(e);
        }
    }
}
