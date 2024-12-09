package emo.utils.density;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import emo.utils.front.FNDSorting;
import org.junit.jupiter.api.Test;
import population.Specimen;
import population.SpecimenID;
import relation.dominance.Dominance;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link CrowdingDistance} class.
 *
 * @author MTomczyk
 */
class CrowdingDistanceTest
{
    /**
     * Test 1.
     */
    @Test
    public void testGetDistanceFront1()
    {
        Criteria c = Criteria.constructCriteria("C", 2, false);

        INormalization[] n = new INormalization[2];
        n[0] = new Linear(0.0d, 10.0d);
        n[1] = new Linear(0.0d, 10.0d);

        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 6, 2);

        {
            a.get(2).setPerformanceAt(1.0d, 0);
            a.get(2).setPerformanceAt(6.0d, 1);
        }
        {
            a.get(4).setPerformanceAt(2.0d, 0);
            a.get(4).setPerformanceAt(3.0d, 1);
        }
        {
            a.get(0).setPerformanceAt(3.0d, 0);
            a.get(0).setPerformanceAt(2.0d, 1);
        }
        {
            a.get(5).setPerformanceAt(3.0d, 0);
            a.get(5).setPerformanceAt(4.0d, 1);
        }
        {
            a.get(1).setPerformanceAt(4.0d, 0);
            a.get(1).setPerformanceAt(3.0d, 1);
        }
        {
            a.get(3).setPerformanceAt(7.0d, 0);
            a.get(3).setPerformanceAt(7.0d, 1);
        }

        ArrayList<Specimen> s = new ArrayList<>(6);
        for (int i = 0; i < 6; i++)
        {
            s.add(new Specimen(2, new SpecimenID(0, 0, 0, i)));
            s.get(i).setAlternative(a.get(i));
        }

        FNDSorting F = new FNDSorting(new Dominance(c));
        LinkedList<LinkedList<Integer>> fronts = F.getFrontAssignments(new Alternatives(a));
        CrowdingDistance CD = new CrowdingDistance(c._no);

        assertEquals(3, fronts.size());

        int[] expFrontSizes = {3, 2, 1};
        int[][] expAID = new int[][]
                {
                        {0, 2, 4}, {1, 5}, {3}
                };
        double[][] expCDs = new double[][]
                {
                        {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.3d}, {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, {Double.POSITIVE_INFINITY}
                };

        for (int i = 0; i < 3; i++)
        {
            LinkedList<Integer> f = fronts.get(i);
            double[] cd = CD.calculateCrowdingDistanceInFront(f, s, n, Double.POSITIVE_INFINITY);

            assertEquals(expFrontSizes[i], f.size());
            for (int j = 0; j < f.size(); j++)
            {
                System.out.println(i + " " + j);
                assertEquals(expAID[i][j], f.get(j));
                assertEquals(expCDs[i][j], cd[j], 0.0001d);
            }
        }
    }

    /**
     * Test 2.
     */
    @Test
    public void testGetDistanceFront2()
    {
        Criteria c = Criteria.constructCriteria("C", 2, false);

        INormalization[] n = new INormalization[2];
        n[0] = new Linear(0.0d, 2.0d);
        n[1] = new Linear(0.0d, 4.0d);

        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 5, 2);

        {
            a.get(0).setPerformanceAt(0.0d, 0);
            a.get(0).setPerformanceAt(4.0d, 1);
        }
        {
            a.get(1).setPerformanceAt(0.5d, 0);
            a.get(1).setPerformanceAt(2.0d, 1);
        }
        {
            a.get(2).setPerformanceAt(1.0d, 0);
            a.get(2).setPerformanceAt(1.0d, 1);
        }
        {
            a.get(3).setPerformanceAt(1.5d, 0);
            a.get(3).setPerformanceAt(0.5d, 1);
        }
        {
            a.get(4).setPerformanceAt(2.0d, 0);
            a.get(4).setPerformanceAt(0.0d, 1);
        }


        ArrayList<Specimen> s = new ArrayList<>(5);
        for (int i = 0; i < 5; i++)
        {
            s.add(new Specimen(2, new SpecimenID(0, 0, 0, i)));
            s.get(i).setAlternative(a.get(i));
        }

        FNDSorting F = new FNDSorting(new Dominance(c));
        LinkedList<LinkedList<Integer>> fronts = F.getFrontAssignments(new Alternatives(a));
        assertEquals(1, fronts.size());

        CrowdingDistance CD = new CrowdingDistance(c._no);


        int[] expFrontSizes = {5};
        int[][] expAID = new int[][]
                {
                        {0, 1, 2, 3, 4}
                };
        double[][] expCDs = new double[][]
                {
                        {2.0d,
                                ((1.0d / 2.0d) + (3.0d / 4.0d)) / 2.0d,
                                ((1.0d / 2.0d) + (1.5d / 4.0d)) / 2.0d,
                                ((1.0d / 2.0d) + (1.0d / 4.0d)) / 2.0d,
                                2.0d}
                };

        for (int i = 0; i < 1; i++)
        {
            LinkedList<Integer> f = fronts.get(i);
            double[] cd = CD.calculateCrowdingDistanceInFront(f, s, n, 2.0d);

            assertEquals(expFrontSizes[i], f.size());
            for (int j = 0; j < f.size(); j++)
            {
                assertEquals(expAID[i][j], f.get(j));
                assertEquals(expCDs[i][j], cd[j], 0.0001d);
            }
        }
    }
}