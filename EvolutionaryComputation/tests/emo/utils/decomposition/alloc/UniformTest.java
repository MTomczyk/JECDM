package emo.utils.decomposition.alloc;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import org.junit.jupiter.api.Test;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Uniform} class.
 *
 * @author MTomczyk
 */
class UniformTest
{

    /**
     * Test 1 (testing uniform distribution).
     */
    @Test
    void getAllocations()
    {
        int family1Size = 5;
        int family2Size = 10;
        int total = family1Size + family2Size;

        int trials = 1000000;

        double[][] M = new double[total][total];


        IGoal [] G1 = GoalsFactory.getLNormsDND(2, family1Size - 1, 1.0d);
        IGoal [] G2 = GoalsFactory.getLNormsDND( 2, family2Size - 1, 1.0d);
        Family[] families = new Family[]{new Family(new FamilyID(0), G1),
                new Family(new FamilyID(1), G2)};

        IRandom R = new MersenneTwister64(0); // 0 for reproducibility
        IAlloc A = new Uniform();

        for (int t = 0; t < trials; t++)
        {
            GoalID[] GL = A.getAllocations(families, R);
            //do inspection
            for (int pos = 0; pos < GL.length; pos++)
            {
                int pnt = GL[pos].getGoalArrayIndex();
                if (GL[pos].getFamilyArrayIndex() == 1) pnt += families[0].getSize();
                M[pnt][pos]++;
            }
        }

        for (int a = 0; a < total; a++)
            for (int b = 0; b < total; b++) M[a][b] /= trials;

        double expected = 1.0d / total;

        for (int a = 0; a < total; a++)
            for (int b = 0; b < total; b++)
                assertEquals(expected, M[a][b], 0.01d);

        PrintUtils.print2dDoubles(M, 3);
    }
}