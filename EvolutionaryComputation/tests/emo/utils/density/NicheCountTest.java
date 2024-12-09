package emo.utils.density;

import alternative.Alternative;
import criterion.Criteria;
import emo.utils.front.FNDSorting;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;
import population.Specimens;
import relation.dominance.Dominance;
import space.distance.Euclidean;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for {@link NicheCount} class.
 *
 * @author MTomczyk
 */
class NicheCountTest
{
    /**
     * Test 1
     */
    @Test
    void getNicheCountInFront1()
    {
        INormalization[] N = new INormalization[]{new Linear(0.0d, 1.0d), new Linear(0.0d, 1.0)};
        Criteria C = Criteria.constructCriteria("C", 2, false);

        double [][] E = new double[][]{{0.1, 0.9}, {0.22, 0.84}, {0.3, 0.65}, {0.37, 0.62},
                {0.46,0.54}, {0.55,0.45}, {0.65,0.3}, {0.82,0.1}};

        FNDSorting FS = new FNDSorting(new Dominance(C));

        ArrayList<Alternative> A = Alternative.getAlternativeArray("A", 8, 2);
        for (int i = 0; i < 8; i++) A.get(i).setPerformanceVector(E[i]);
        ArrayList<Specimen> SPEC = new ArrayList<>(8);
        for (int i = 0; i < 8; i++)
        {
            SPEC.add(new Specimen(2, new SpecimenID(0,0,0,i)));
            SPEC.get(i).setAlternative(A.get(i));
        }

        LinkedList<LinkedList<Integer>> F = FS.getFrontAssignments(new Specimens(SPEC));
        assertEquals(1, F.size());

        double [] exp = new double[]{1.199,
                1.199,
                1.742,
                2.097,
                1.635,
                1.279,
                1.0,
                1.0,
        };

        NicheCount NC = new NicheCount(new Euclidean(N), 0.15);

        for (LinkedList<Integer> f : F)
        {
            double [] nc = NC.getNicheCountInFront(f, SPEC, N);
            for (int i = 0; i < nc.length; i++) assertEquals(exp[i], nc[i], 0.01d);
        }
    }

    /**
     * Test 2
     */
    @Test
    void getNicheCountInFront2()
    {
        INormalization [] N = new INormalization[]{new Linear(0.0d, 1.0d), new Linear(0.0d, 1.0)};
        Criteria C = Criteria.constructCriteria("C", 2, false);

        double [][] E = new double[][]{{0.1, 0.9}, {0.22, 0.84},
                {0.3, 0.65}, {0.37, 0.62}, {0.46,0.54}, {0.55,0.45}, {0.65,0.3}, {0.82,0.1}, {1.0, 0.2}};

        FNDSorting FS = new FNDSorting(new Dominance(C));

        ArrayList<Alternative> A = Alternative.getAlternativeArray("A", 9, 2);
        for (int i = 0; i < 9; i++) A.get(i).setPerformanceVector(E[i]);
        ArrayList<Specimen> SPEC = new ArrayList<>(9);
        for (int i = 0; i < 9; i++)
        {
            SPEC.add(new Specimen(2, new SpecimenID(0,0,0,i)));
            SPEC.get(i).setAlternative(A.get(i));
        }

        LinkedList<LinkedList<Integer>> F = FS.getFrontAssignments(new Specimens(SPEC));
        assertEquals(2, F.size());

        double [] exp = new double[]{
                1.199,
                1.199,
                1.742,
                2.097,
                1.635,
                1.279,
                1.0,
                1.0,
                1.0,
        };

        NicheCount NC = new NicheCount(new Euclidean(N), 0.15);
        int idx = 0;
        for (LinkedList<Integer> f: F)
        {
            double[] r = NC.getNicheCountInFront(f, SPEC, N);
            for (double v : r) assertEquals(exp[idx++], v, 0.01);
        }

    }
}