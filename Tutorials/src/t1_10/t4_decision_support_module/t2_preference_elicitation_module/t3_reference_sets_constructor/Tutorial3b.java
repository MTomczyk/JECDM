package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t3_reference_sets_constructor;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import interaction.reference.ReferenceSet;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.IValidator;
import interaction.reference.validator.RequiredSpread;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link IReferenceSetConstructor}
 * interface ({@link interaction.reference.constructor.RandomPairs}).
 *
 * @author MTomczyk
 */
public class Tutorial3b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create RNG:
        IRandom R = new MersenneTwister64(0);

        // A pair cannot be bounded by [0.1]^2 cube in the normalized space (illegal pair)
        IValidator[] validators = new IValidator[]{new RequiredSpread(0.1d)};

        // Create random pairs reference sets constructor:
        // RNG, comparators, 2 pairs should be returned per request
        RandomPairs randomPairs = new RandomPairs(validators, 2);

        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        // OS spanned over [0, 2]^2 bounds
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 2.0d), new boolean[2]);


        // Not pointless, the final values were decomposed into the normalized part and the multiplier
        @SuppressWarnings("PointlessArithmeticExpression")
        AbstractAlternatives<?> alternatives = new Alternatives(Alternative.getAlternativeArray("A",
                new double[][]
                        {
                                {1.0d * 2.0d, 0.1d * 2.0d}, // A0
                                {0.25d * 2.0d, 0.15d * 2.0d}, // A1
                                {0.2d * 2.0d, 0.2d * 2.0d}, // A2
                                {0.1d * 2.0d, 1.0d * 2.0d}, // A3
                        }));

        // Thus, the possible pairs are (5): A0-A1, A0-A2, A0-A3, A1-A3, A2-A3.
        // Ax-Ax and A1-A2 pairs are prohibited.

        DMContext context = Common.getContext(0, alternatives, startingTimestamp, criteria, os, R);

        try
        {
            // Let's generate the results 10000 times and analyze their correctness.
            int trials = 10000;


            // Distribution matrix (row = first alternative; column = second alternative).
            int[][] hits = new int[4][4];

            for (int t = 0; t < trials; t++)
            {
                // The expected number of pairs to construct per request must be 2.
                LinkedList<ReferenceSet> referenceSets = randomPairs.constructReferenceSets(context, alternatives);
                if (referenceSets.size() != 2)
                    throw new ReferenceSetsConstructorException("The number of reference sets does not equal 2", Tutorial3b.class);

                // Analyze the reference sets (2):
                for (ReferenceSet rs : referenceSets)
                {
                    AbstractAlternatives<?> ra = rs.getAlternatives();
                    // The number of stored alternatives must be 2:
                    if (ra.size() != 2)
                        throw new ReferenceSetsConstructorException("The number of alternatives does not equal 2", Tutorial3b.class);

                    // Construct the distribution matrix:
                    int i = Integer.parseInt(ra.get(0).getName().substring(1, 2));
                    int j = Integer.parseInt(ra.get(1).getName().substring(1, 2));
                    hits[i][j]++;
                }
            }

            PrintUtils.print2dIntegers(hits);

            // Verify the matrix (additionally, the uniform distribution of obtained results could be tested):
            int[][] prohibited = new int[][]{{0, 0}, {1, 1}, {2, 2}, {3, 3}, {1, 2}, {2, 1}};
            for (int[] p : prohibited)
                if (hits[p[0]][p[1]] != 0)
                    throw new ReferenceSetsConstructorException("A pair [A" + p[0] +
                            ", A" + p[1] + "] should not be selected", Tutorial3b.class);

        } catch (ReferenceSetsConstructorException e)
        {
            throw new RuntimeException(e);
        }
    }
}
