package emo.utils.decomposition.moead;

import alternative.Alternative;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.pbi.Euclidean;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link MOEADGoalsManager}.
 *
 * @author MTomczyk
 */
class MOEADGoalsManagerTest
{

    /**
     * Test 1
     */
    @Test
    void test1()
    {
        double[][] w = new double[][]
                {
                        {0.0d, 1.0d},
                        {0.1d, 0.9d},
                        {0.2d, 0.8d},
                        {0.3d, 0.7d},
                        {0.4d, 0.6d},
                        {0.5d, 0.5d},
                        {0.6d, 0.4d},
                        {0.7d, 0.3d},
                        {0.8d, 0.2d},
                        {0.9d, 0.1d},
                        {1.0d, 0.0d}
                };

        IGoal[] G = GoalsFactory.getLNorms(w, Double.POSITIVE_INFINITY, null);
        MOEADGoalsManager.Params pG = new MOEADGoalsManager.Params(G, new Euclidean(), 3);
        pG._alloc = (families, R) -> {
            GoalID[] ids = new GoalID[11];
            for (int i = 0; i < 11; i++) ids[i] = new GoalID(new FamilyID(0), 10 - i);
            return ids;
        };

        MOEADGoalsManager GM = new MOEADGoalsManager(pG);
        assertTrue(GM.validate());

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", w);

        ArrayList<Specimen> specimens = new ArrayList<>(11);
        for (int i = 0; i < 11; i++)
        {
            Specimen specimen = new Specimen(2, new SpecimenID(0, 0, 0, i));
            specimen.setAlternative(alternatives.get(i));
            specimens.add(specimen);
        }

        GM.establishNeighborhood();
        GM.determineUpdatesSequence(null);
        SpecimensContainer SC = new SpecimensContainer(specimens);
        GM.makeArbitraryAssignments(SC);

        for (int i = 0; i < 11; i++)
        {
            GoalID base = new GoalID(new FamilyID(0), i);
            GoalID[] ids = GM.getCurrentNeighborhood(base);
            assertEquals(base, ids[0]);
            if (i == 0)
            {
                assertEquals(new GoalID(new FamilyID(0), 1), ids[1]);
                assertEquals(new GoalID(new FamilyID(0), 2), ids[2]);
            }
            else if (i == 10)
            {
                assertEquals(new GoalID(new FamilyID(0), 9), ids[1]);
                assertEquals(new GoalID(new FamilyID(0), 8), ids[2]);
            }
            else
            {
                boolean t1 = new GoalID(new FamilyID(0), i - 1).equals(ids[1]);
                boolean t2 = new GoalID(new FamilyID(0), i - 1).equals(ids[2]);
                assertTrue(t1 || t2);
                assertFalse(t1 && t2);
                t1 = new GoalID(new FamilyID(0), i + 1).equals(ids[1]);
                t2 = new GoalID(new FamilyID(0), i + 1).equals(ids[2]);
                assertTrue(t1 || t2);
                assertFalse(t1 && t2);
            }
        }

        Assignment[] assignments = GM.getFamilies()[0].getAssignments();
        assertEquals(11, assignments.length);
        for (int i = 0; i < 11; i++)
        {
            assertEquals(1, assignments[i].getSpecimens().size());
            assertEquals("A" + (10 - i), assignments[i].getSpecimens().get(0).getAlternative().getName());
        }

        {
            GM.determineUpdatesSequence(null);
            Specimen off = new Specimen(2, new SpecimenID(0, 1, 0, 0));
            Alternative oa = new Alternative("O0", new double[]{0.7d, 0.0d});
            off.setAlternative(oa);
            GM.executeUpdate(off, new GoalID(new FamilyID(0), 0), SC);

            assignments = GM.getFamilies()[0].getAssignments();
            assertEquals(11, assignments.length);
            assertEquals("A10", assignments[0].getSpecimens().get(0).getAlternative().getName());
            for (int i = 1; i < 3; i++)
            {
                assertEquals(1, assignments[i].getSpecimens().size());
                assertEquals("O0", assignments[i].getSpecimens().get(0).getAlternative().getName());
            }
            for (int i = 3; i < 10; i++)
            {
                assertEquals(1, assignments[i].getSpecimens().size());
                assertEquals("A" + (10 - i), assignments[i].getSpecimens().get(0).getAlternative().getName());
            }
        }

        {
            GM.determineUpdatesSequence(null);
            Specimen off = new Specimen(2, new SpecimenID(0, 1, 4, 0));
            Alternative oa = new Alternative("O1", new double[]{0.39d, 0.39d});
            off.setAlternative(oa);
            GM.executeUpdate(off, new GoalID(new FamilyID(0), 5), SC);

            assignments = GM.getFamilies()[0].getAssignments();
            assertEquals(11, assignments.length);
            assertEquals("A10", assignments[0].getSpecimens().get(0).getAlternative().getName());
            for (int i = 1; i < 3; i++)
            {
                assertEquals(1, assignments[i].getSpecimens().size());
                assertEquals("O0", assignments[i].getSpecimens().get(0).getAlternative().getName());
            }
            assertEquals("A" + (10 - 3), assignments[3].getSpecimens().get(0).getAlternative().getName());
            for (int i = 4; i < 7; i++)
            {
                assertEquals(1, assignments[i].getSpecimens().size());
                assertEquals("O1", assignments[i].getSpecimens().get(0).getAlternative().getName());
            }
            for (int i = 7; i < 10; i++)
            {
                assertEquals(1, assignments[i].getSpecimens().size());
                assertEquals("A" + (10 - i), assignments[i].getSpecimens().get(0).getAlternative().getName());
            }
        }


        w = new double[][]
                {
                        {0.25d, 0.75d},
                        {0.3d, 0.7d},
                        {0.35d, 0.65d},
                        {0.4d, 0.6d},
                        {0.45d, 0.55d},
                        {0.5d, 0.5d},
                        {0.55d, 0.45d},
                        {0.6d, 0.4d},
                        {0.65d, 0.35d},
                        {0.7d, 0.3d},
                        {0.75d, 0.25d}
                };

        G = GoalsFactory.getLNorms(w, Double.POSITIVE_INFINITY, null);
        GM.getFamilies()[0].replaceGoals(G);
        GM.establishNeighborhood();
        GM.makeBestAssignments(SC);
        GM.updatePopulationAsImposedByAssignments(SC);
        //GM.determineUpdatesSequence(); do not need to call
        assignments = GM.getFamilies()[0].getAssignments();

        String[] names = new String[]{"O0", "O0", "O0", "O1", "O1", "O1", "O1", "O1", "A3", "A3", "A3"};
        assertEquals(11, assignments.length);
        for (int i = 0; i < 11; i++)
        {
            assertEquals(1, assignments[i].getSpecimens().size());
            assertEquals(names[i], assignments[i].getSpecimens().get(0).getAlternative().getName());
        }

        assertEquals(11, SC.getPopulation().size());
        for (int i = 0; i < 11; i++)
            assertEquals(names[i], SC.getPopulation().get(i).getAlternative().getName());
    }
}