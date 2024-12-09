package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t2_refiner;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.RefinerException;
import interaction.refine.filters.termination.ITerminationFilter;
import interaction.refine.filters.termination.RequiredSpread;
import interaction.refine.filters.termination.TerminationResult;
import space.Range;
import space.os.ObjectiveSpace;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This tutorial focuses on the {@link interaction.refine.filters.termination.ITerminationFilter}
 * interface ({@link interaction.refine.filters.termination.RequiredSpread}).
 *
 * @author MTomczyk
 */
public class Tutorial2a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create the termination filter: the required spread in the normalized space should be at least 0.1 for
        // at least one objective to avoid termination.
        ITerminationFilter terminationFilter = new RequiredSpread(0.1d);

        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        // OS spanned over [0, 2]^2 bounds
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 2.0d), new boolean[2]);

        // Iteration 0: (termination should not be called):
        {
            // Not pointless, the final values were decomposed into the normalized part and the multiplier
            @SuppressWarnings("PointlessArithmeticExpression")
            ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A",
                    new double[][]
                            {
                                    {1.0d * 2.0d, 0.7d * 2.0d},
                                    {0.5d * 2.0d, 0.6d * 2.0d},
                                    {0.3d * 2.0d, 0.5d * 2.0d},
                                    {0.1d * 2.0d, 0.1d * 2.0d},
                                    {0.5d * 2.0d, 0.2d * 2.0d}
                            });

            // Create the context (use the Common class).
            DMContext context = Common.getContext(0, new Alternatives(alternatives), startingTimestamp, criteria, os);

            try
            {
                // Question the filter:
                TerminationResult result = terminationFilter.shouldTerminate(context);

                // Print the result:
                System.out.println(result);
            } catch (RefinerException e)
            {
                throw new RuntimeException(e);
            }
        }

        // Iteration 1: (termination should be called):
        {
            ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A",
                    new double[][]
                            {
                                    {0.0d * 2.0d, 0.1d * 2.0d},
                                    {0.05d * 2.0d, 0.15d * 2.0d},
                                    {0.099999999d * 2.0d, 0.199999999d * 2.0d}, // Slightly increase the first/second value to avoid termination
                            });

            // Create the context (use the Common class).
            DMContext context = Common.getContext(1, new Alternatives(alternatives), startingTimestamp, criteria, os);

            try
            {
                // Question the filter:
                TerminationResult result = terminationFilter.shouldTerminate(context);

                // Print the result:
                System.out.println(result);
            } catch (RefinerException e)
            {
                throw new RuntimeException(e);
            }
        }


    }
}
