package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t2_refiner;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import alternative.IAlternativeWrapper;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.RefinerException;
import interaction.refine.filters.reduction.IReductionFilter;
import interaction.refine.filters.reduction.RemoveDominated;
import interaction.refine.filters.reduction.RemoveDuplicatesInOS;
import print.PrintUtils;
import space.Range;
import space.os.ObjectiveSpace;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;

/**
 * This tutorial focuses on the {@link interaction.refine.filters.reduction.IReductionFilter}
 * interface ({@link RemoveDuplicatesInOS}).
 *
 * @author MTomczyk
 */
public class Tutorial2b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2), new boolean[2]);

        AbstractAlternatives<?> alternatives = new Alternatives(Alternative.getAlternativeArray("A", new double[][]
                {
                        {0.0d, 0.6d},
                        {0.1d, 0.1d},
                        {0.5d, 0.4d}, // dominated
                        {0.0d, 0.6d}, // duplicated + dominated
                        {0.3d, 0.0d},
                        {0.1d, 0.1d}, // duplicated
                        {0.3d, 0.2d}, // dominated
                        {0.1d, 0.3d}, // dominated
                }));

        // Create the context (use the Common class).
        DMContext context = Common.getContext(0, alternatives, startingTimestamp, criteria, os);


        {
            IReductionFilter removeDuplicates = new RemoveDuplicatesInOS(Math.pow(0.1d, 10));
            try
            {
                alternatives = removeDuplicates.reduce(context, alternatives);
                System.out.println("Remain (after removing duplicates):");
                for (IAlternativeWrapper a : alternatives)
                    System.out.println(a.getName() + ": " + PrintUtils.getVectorOfDoubles(a.getPerformanceVector(), 2));
            } catch (RefinerException e)
            {
                throw new RuntimeException(e);
            }
        }

        {
            IReductionFilter removeDominated = new RemoveDominated();
            try
            {
                alternatives = removeDominated.reduce(context, alternatives);
                System.out.println("Remain (after removing dominated):");
                for (IAlternativeWrapper a : alternatives)
                    System.out.println(a.getName() + ": " + PrintUtils.getVectorOfDoubles(a.getPerformanceVector(), 2));
            } catch (RefinerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
