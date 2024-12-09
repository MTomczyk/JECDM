package emo.utils.decomposition.nsgaiii;

import alternative.Alternative;
import criterion.Criteria;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.PointLineProjection;
import emo.utils.front.FNDSorting;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;
import population.Specimens;
import relation.dominance.Dominance;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Various tests for the {@link NSGAIIIGoalsManager} class.
 *
 * @author MTomczyk
 */
class NSGAIIIGoalsManagerTest
{

    /**
     * Tests assignment/niching procedures.
     */
    @Test
    void executeAssignmentStep()
    {
        double[][] rp = new double[][]{{1.0d, 0.0d}, {0.75d, 0.25d}, {0.5d, 0.5d}, {0.25d, 0.75}, {0.0d, 1.0d}};
        IGoal[] G = new IGoal[5];
        for (int i = 0; i < 5; i++)
            G[i] = new PointLineProjection(new space.scalarfunction.PointLineProjection(rp[i]));
        double[][] e = new double[][]{{4.0d, 1.0d}, {3.5d, 3.0d}, {1.0d, 4.5d}, {1.0d, 3.0d}, {3.0d, 1.0d}};
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", 5, 2);
        ArrayList<Specimen> specimens = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            alternatives.get(i).setPerformanceVector(e[i]);
            Specimen S = new Specimen(2, new SpecimenID(0, 0, 0, i));
            S.setAlternative(alternatives.get(i));
            specimens.add(S);
        }

        Assignment[] A = new Assignment[5];
        for (int i = 0; i < 5; i++)
        {
            A[i] = new Assignment(G[i]);
            A[i].clearLists();
            for (int j = 0; j < 5; j++) A[i].insertSpecimen(specimens.get(j));
        }

        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                double eval = G[i].evaluate(specimens.get(j));
                System.out.print(eval + " ");
            }
            System.out.println();
        }

        NSGAIIIGoalsManager.Params pGM = new NSGAIIIGoalsManager.Params(G);
        NSGAIIIGoalsManager GM = new NSGAIIIGoalsManager(pGM);
        FNDSorting fND = new FNDSorting(new Dominance(Criteria.constructCriteria("C", 2, false)));
        LinkedList<LinkedList<Integer>> fronts = fND.getFrontAssignments(new Specimens(specimens), 3);
        assertEquals(2, fronts.size());
        assertEquals(2, fronts.getFirst().size());
        assertEquals(3, fronts.getFirst().getFirst());
        assertEquals(4, fronts.getFirst().get(1));
        assertEquals(3, fronts.get(1).size());
        assertEquals(2, fronts.get(1).getFirst());
        assertEquals(0, fronts.get(1).get(1));
        assertEquals(1, fronts.get(1).get(2));
        GM.executeAssignmentStep(specimens, fronts);

        Assignment[] a = GM.getFamilies()[0].getAssignments();
        assertEquals(0.0d, a[0].getNicheCount(), 0.00000001d);
        assertEquals(0, a[0].getSpecimens().size());

        assertEquals(1.0d, a[1].getNicheCount(), 0.00000001d);
        assertEquals(1, a[1].getSpecimens().size());
        assertEquals("A0", a[1].getSpecimens().getFirst().getAlternative().getName());

        assertEquals(0.0d, a[2].getNicheCount(), 0.00000001d);
        assertEquals(1, a[2].getSpecimens().size());
        assertEquals("A1", a[2].getSpecimens().getFirst().getAlternative().getName());

        assertEquals(1.0d, a[3].getNicheCount(), 0.00000001d);
        assertEquals(1, a[3].getSpecimens().size());
        assertEquals("A2", a[3].getSpecimens().getFirst().getAlternative().getName());

        assertEquals(0.0d, a[4].getNicheCount(), 0.00000001d);
        assertEquals(0, a[4].getSpecimens().size());

        LinkedList<Assignment> assignments = GM.getAssignmentsWithMinimalNicheCount(false);
        assertEquals(3, assignments.size());
        assertEquals(0, assignments.getFirst().getSpecimens().size());
        assertEquals(1, assignments.get(1).getSpecimens().size());
        assertEquals(0, assignments.get(2).getSpecimens().size());

        assignments = GM.getAssignmentsWithMinimalNicheCount(true);
        assertEquals(1, assignments.size());
        assertEquals(1, assignments.getFirst().getSpecimens().size());

    }
}