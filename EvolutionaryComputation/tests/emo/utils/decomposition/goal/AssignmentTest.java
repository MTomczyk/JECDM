package emo.utils.decomposition.goal;

import alternative.Alternative;
import emo.utils.decomposition.goal.definitions.PointLineProjection;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link Assignment} class.
 *
 * @author MTomczyk
 */
class AssignmentTest
{

    /**
     * Tests insertions sort.
     */
    @Test
    void insertSpecimen()
    {
        double[][] rp = new double[][]{{1.0d, 0.0d}, {0.75d, 0.25d}, {0.5d, 0.5d}, {0.25d, 0.75}, {0.0d, 1.0d}};
        IGoal[] G = new IGoal[5];
        for (int i = 0; i < 5; i++)
            G[i] = new PointLineProjection(new space.scalarfunction.PointLineProjection(rp[i]));
        double[][] e = new double[][]{{4.0d, 1.0d}, {3.5d, 3.0d}, {1.0d, 4.5d}, {0.0d, 5.0d}, {3.0d, 0.5d}};
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

        int[][] order = new int[][]{
                {4, 0, 1, 2, 3},
                {0, 4, 1, 2, 3},
                {1, 4, 0, 2, 3},
                {2, 3, 1, 4, 0},
                {3, 2, 4, 1, 0}
        };

        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                double eval = G[i].evaluate(specimens.get(j));
                System.out.print(eval + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < 5; i++)
        {
            assertEquals(5, A[i].getSpecimens().size());
            assertEquals(5, A[i].getEvaluations().size());
            for (int j = 0; j < 5; j++)
            {
                assertEquals(alternatives.get(order[i][j]).getName(),
                        A[i].getSpecimens().get(j).getAlternative().getName());
                assertEquals(G[i].evaluate(specimens.get(order[i][j])), A[i].getEvaluations().get(j), 0.0000001d);
            }
        }

    }
}