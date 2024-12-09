package emo.utils.decomposition.neighborhood.constructor;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.LNorm;
import emo.utils.decomposition.neighborhood.Neighborhood;
import emo.utils.decomposition.similarity.lnorm.Cos;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link InsertionSortConstructor} class.
 *
 * @author MTomczyk
 */
class InsertionSortConstructorTest
{

    /**
     * Test 1.
     */
    @Test
    void getNeighborhood1()
    {
        int familyID = 5;
        int cuts = 99;
        IGoal[] G = GoalsFactory.getLNormsDND(2, cuts, 2.0d);
        Family F = new Family(new FamilyID(familyID), G);
        assertEquals(cuts + 1, F.getSize());
        assertEquals(familyID, F.getID().getArrayIndex());

        INeighborhoodConstructor C = new InsertionSortConstructor();

        int neighborhoodSize = 1;
        Neighborhood N = C.getNeighborhood(F, new Euclidean(), neighborhoodSize);

        for (int i = 0; i < cuts + 1; i++)
        {
            GoalID[] nb = N.getNeighborhood(i);
            assertEquals(neighborhoodSize, nb.length);
            assertEquals(familyID, nb[0].getFamilyArrayIndex());
            assertEquals(i, nb[0].getGoalArrayIndex());
        }
    }

    /**
     * Test 2.
     */
    @Test
    void getNeighborhood2()
    {
        int familyID = 3;
        int cuts = 99;

        IGoal[] G = GoalsFactory.getLNormsDND(2, cuts, 2.0d);
        Family F = new Family(new FamilyID(familyID), G);
        assertEquals(cuts + 1, F.getSize());
        assertEquals(familyID, F.getID().getArrayIndex());

        INeighborhoodConstructor C = new InsertionSortConstructor();

        int neighborhoodSize = 2; //there will be ties
        Neighborhood N = C.getNeighborhood(F, new Euclidean(), neighborhoodSize);

        for (int i = 0; i < cuts + 1; i++)
        {
            GoalID[] nb = N.getNeighborhood(i);
            assertEquals(neighborhoodSize, nb.length);
            assertEquals(familyID, nb[0].getFamilyArrayIndex());

            assertEquals(i, nb[0].getGoalArrayIndex());

            if (i == 0) assertEquals(1, nb[1].getGoalArrayIndex());
            else if (i == cuts) assertEquals(cuts - 1, nb[1].getGoalArrayIndex());
            else assertTrue((nb[1].getGoalArrayIndex() == i - 1) || (nb[1].getGoalArrayIndex() == i + 1));
        }
    }

    /**
     * Test 3.
     */
    @Test
    void getNeighborhood3()
    {
        int familyID = 3;
        int cuts = 99;

        IGoal[] G = GoalsFactory.getLNormsDND(2, cuts, 2.0d);
        Family F = new Family(new FamilyID(familyID), G);
        assertEquals(cuts + 1, F.getSize());
        assertEquals(familyID, F.getID().getArrayIndex());

        INeighborhoodConstructor C = new InsertionSortConstructor();

        int neighborhoodSize = 3; //there will be ties
        Neighborhood N = C.getNeighborhood(F, new Euclidean(), neighborhoodSize);

        for (int i = 0; i < cuts + 1; i++)
        {
            GoalID[] nb = N.getNeighborhood(i);
            assertEquals(neighborhoodSize, nb.length);
            assertEquals(familyID, nb[0].getFamilyArrayIndex());

            assertEquals(i, nb[0].getGoalArrayIndex());

            if (i == 0)
            {
                assertEquals(1, nb[1].getGoalArrayIndex());
                assertEquals(2, nb[2].getGoalArrayIndex());
            }
            else if (i == cuts)
            {
                assertEquals(cuts - 1, nb[1].getGoalArrayIndex());
                assertEquals(cuts - 2, nb[2].getGoalArrayIndex());
            }
            else
            {
                boolean check1 = (nb[1].getGoalArrayIndex() == i - 1) || (nb[2].getGoalArrayIndex() == i - 1);
                boolean check2 = (nb[1].getGoalArrayIndex() == i + 1) || (nb[2].getGoalArrayIndex() == i + 1);
                assertTrue(check1 && check2);
            }
        }
    }

    /**
     * Test 4.
     */
    @Test
    void getNeighborhood4()
    {
        IGoal[] G = new IGoal[3];
        G[0] = new LNorm(new space.scalarfunction.LNorm(new double[]{0.0d, 1.0d}, 2.0d, null));
        G[1] = new LNorm(new space.scalarfunction.LNorm(new double[]{1.0d, 0.0d}, 2.0d, null));
        G[2] = new LNorm(new space.scalarfunction.LNorm(new double[]{0.5d, 0.4d}, 2.0d, null));
        Family F = new Family(new FamilyID(0), G);

        INeighborhoodConstructor NC = new InsertionSortConstructor();
        Neighborhood N = NC.getNeighborhood(F, new Euclidean(), 2);

        assertEquals(3, N.getNeighborhoodMatrix().length);
        for (int i = 0; i < 3; i++) assertEquals(2, N.getNeighborhood(i).length);

        assertEquals(0, N.getNeighborhood(0)[0].getGoalArrayIndex());
        assertEquals(2, N.getNeighborhood(0)[1].getGoalArrayIndex());

        assertEquals(1, N.getNeighborhood(1)[0].getGoalArrayIndex());
        assertEquals(2, N.getNeighborhood(1)[1].getGoalArrayIndex());

        assertEquals(2, N.getNeighborhood(2)[0].getGoalArrayIndex());
        assertEquals(1, N.getNeighborhood(2)[1].getGoalArrayIndex());

        assertEquals(0, N.getNeighborhood(0)[0].getFamilyArrayIndex());
        assertEquals(0, N.getNeighborhood(0)[1].getFamilyArrayIndex());

        assertEquals(0, N.getNeighborhood(1)[0].getFamilyArrayIndex());
        assertEquals(0, N.getNeighborhood(1)[1].getFamilyArrayIndex());

        assertEquals(0, N.getNeighborhood(2)[0].getFamilyArrayIndex());
        assertEquals(0, N.getNeighborhood(2)[1].getFamilyArrayIndex());
    }

    /**
     * Test 5.
     */
    @Test
    void getNeighborhood5()
    {
        IGoal[] G = new IGoal[3];
        G[0] = new LNorm(new space.scalarfunction.LNorm(new double[]{0.0d, 1.0d}, 2.0d, null));
        G[1] = new LNorm(new space.scalarfunction.LNorm(new double[]{1.0d, 0.0d}, 2.0d, null));
        G[2] = new LNorm(new space.scalarfunction.LNorm(new double[]{0.5d, 0.4d}, 2.0d, null));
        Family F = new Family(new FamilyID(0), G);

        INeighborhoodConstructor NC = new InsertionSortConstructor();
        Neighborhood N = NC.getNeighborhood(F, new Cos(), 2);

        assertEquals(3, N.getNeighborhoodMatrix().length);
        for (int i = 0; i < 3; i++) assertEquals(2, N.getNeighborhood(i).length);

        assertEquals(0, N.getNeighborhood(0)[0].getGoalArrayIndex());
        assertEquals(2, N.getNeighborhood(0)[1].getGoalArrayIndex());

        assertEquals(1, N.getNeighborhood(1)[0].getGoalArrayIndex());
        assertEquals(2, N.getNeighborhood(1)[1].getGoalArrayIndex());

        assertEquals(2, N.getNeighborhood(2)[0].getGoalArrayIndex());
        assertEquals(1, N.getNeighborhood(2)[1].getGoalArrayIndex());

        assertEquals(0, N.getNeighborhood(0)[0].getFamilyArrayIndex());
        assertEquals(0, N.getNeighborhood(0)[1].getFamilyArrayIndex());

        assertEquals(0, N.getNeighborhood(1)[0].getFamilyArrayIndex());
        assertEquals(0, N.getNeighborhood(1)[1].getFamilyArrayIndex());

        assertEquals(0, N.getNeighborhood(2)[0].getFamilyArrayIndex());
        assertEquals(0, N.getNeighborhood(2)[1].getFamilyArrayIndex());
    }
}