package emo.utils.decomposition.goal;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for {@link GoalsFactory} class.
 *
 * @author MTomczyk
 */
class GoalsFactoryTest
{

    /**
     * Test 1.
     */
    @Test
    void getLNorms1()
    {
        IGoal[] G = GoalsFactory.getLNormsDND(2, 4, Double.POSITIVE_INFINITY);
        Family F = new Family(new FamilyID(5), G);

        assertEquals(5, F.getSize());

        double[][] exp = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.25d, 0.75d},
                        {0.5d, 0.5d},
                        {0.75d, 0.25d},
                        {1.0d, 0.0d},
                };

        assertEquals(5, F.getID().getArrayIndex());
        for (int i = 0; i < 5; i++)
        {
            assertEquals(i, F.getGoal(i).getID()._arrayIndex);
            assertEquals(exp[i][0], F.getGoals()[i].getParams()[0][0], 0.00001d);
            assertEquals(exp[i][1], F.getGoals()[i].getParams()[0][1], 0.00001d);
            assertEquals(Double.POSITIVE_INFINITY, F.getGoals()[0].getParams()[1][0]);
        }
    }


    /**
     * Test 2.
     */
    @Test
    void getLNorms2()
    {
        IGoal [] G = GoalsFactory.getLNormsDND(3, 4, Double.POSITIVE_INFINITY);
        Family F = new Family(new FamilyID(7), G);

        assertEquals(15, F.getSize());

        double[][] exp = new double[][]
                {
                        {0.0d, 0.0, 1.0d},
                        {0.0d, 0.25d, 0.75d},
                        {0.0d, 0.5d, 0.5d},
                        {0.0d, 0.75d, 0.25d},
                        {0.0d, 1.0d, 0.0d},
                        {0.25d, 0.0d, 0.75d},
                        {0.25d, 0.25d, 0.5d},
                        {0.25d, 0.5d, 0.25d},
                        {0.25d, 0.75d, 0.0d},
                        {0.5d, 0.0d, 0.5d},
                        {0.5d, 0.25d, 0.25d},
                        {0.5d, 0.5d, 0.0d},
                        {0.75d, 0.0d, 0.25d},
                        {0.75d, 0.25d, 0.0d},
                        {1.0d, 0.0d, 0.0d}
                };

        assertEquals(7, F.getID().getArrayIndex());
        for (int i = 0; i < 5; i++)
        {
            assertEquals(i, F.getGoal(i).getID()._arrayIndex);
            assertEquals(exp[i][0], F.getGoals()[i].getParams()[0][0], 0.00001d);
            assertEquals(exp[i][1], F.getGoals()[i].getParams()[0][1], 0.00001d);
            assertEquals(Double.POSITIVE_INFINITY, F.getGoals()[0].getParams()[1][0]);
        }

    }


}