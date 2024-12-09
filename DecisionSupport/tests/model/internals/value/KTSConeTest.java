package model.internals.value;

import alternative.Alternative;
import model.internals.value.scalarizing.KTSCone;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import space.normalization.INormalization;
import space.normalization.minmax.Linear;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Provides various tests for {@link KTSCone}.
 *
 * @author MTomczyk
 */
class KTSConeTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        LinkedList<PairwiseComparison> PCs = new LinkedList<>();
        PCs.add(PairwiseComparison.getPreference(new Alternative("P0", new double[]{0.0d, 1.0d}),
                new Alternative("P1", new double[]{1.0d, 0.0d})));

        KTSCone cones = new KTSCone(PCs);

        double[][] e = new double[][]
                {
                        {0.0d, 0.0d},
                        {2.0d, 1.0d},
                        {4.0d, 0.0d},
                        {3.0d, 3.0d},
                        {6.0d, 4.0d},

                        {1.0d, 2.0d},
                        {2.0d, 3.0d},
                        {3.0d, 4.0d},
                        {4.0d, 5.0d},
                        {5.0d, 6.0d},
                        {1.0d, 5.0d},
                        {0.0d, 6.0d}
                };

        double[] expected = new double[]{1.0d, 1.0d, 1.0d, 1.0d, 1.0d,
                0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        for (int i = 0; i < e.length; i++)
        {
            double score = cones.evaluate(alternatives.get(i));
            assertEquals(expected[i], score, 0.000001d);
        }
    }


    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        LinkedList<PairwiseComparison> PCs = new LinkedList<>();
        PCs.add(PairwiseComparison.getPreference(new Alternative("P0", new double[]{0.0d, 1.0d}),
                new Alternative("P1", new double[]{1.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P2", new double[]{0.0d, 2.0d}),
                new Alternative("P3", new double[]{6.0d, 0.0d})));

        KTSCone cones = new KTSCone(PCs);

        double[][] e = new double[][]
                {
                        {0.0d, 0.0d}, // 1
                        {2.0d, 1.0d}, // 1
                        {4.0d, 0.0d}, // 2
                        {3.0d, 3.0d}, // 1
                        {6.0d, 4.0d}, // 1

                        {1.0d, 2.0d}, // 0
                        {2.0d, 3.0d}, // 0
                        {3.0d, 4.0d}, // 0
                        {4.0d, 5.0d}, // 0
                        {5.0d, 6.0d}, // 0
                        {1.0d, 5.0d}, // 0
                        {0.0d, 6.0d} // 0
                };

        double[] expected = new double[]{1.0d, 1.0d, 2.0d, 1.0d, 1.0d,
                0.0d, 0.0d, 0.0d, 0d, 0.0d, 0.0d, 0.0d};
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        for (int i = 0; i < e.length; i++)
        {
            double score = cones.evaluate(alternatives.get(i));
            assertEquals(expected[i], score, 0.000001d);
        }
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        LinkedList<PairwiseComparison> PCs = new LinkedList<>();
        PCs.add(PairwiseComparison.getPreference(new Alternative("P0", new double[]{0.0d, 1.0d}),
                new Alternative("P1", new double[]{1.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P2", new double[]{0.0d, 2.0d}),
                new Alternative("P3", new double[]{6.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P4", new double[]{1.0d, 0.0d}),
                new Alternative("P5", new double[]{0.0d, 2.0d})));


        KTSCone cones = new KTSCone(PCs);

        double[][] e = new double[][]
                {
                        {0.0d, 0.0d}, // 1
                        {2.0d, 1.0d}, // 1
                        {4.0d, 0.0d}, // 2
                        {3.0d, 3.0d}, // 1
                        {6.0d, 4.0d}, // 1

                        {1.0d, 2.0d}, // 1
                        {2.0d, 3.0d}, // 0
                        {3.0d, 4.0d}, // 0
                        {4.0d, 5.0d}, // 0
                        {5.0d, 6.0d}, // 0
                        {1.0d, 5.0d}, // 1
                        {0.0d, 6.0d} // 1
                };

        double[] expected = new double[]{1.0d, 1.0d, 2.0d, 1.0d, 1.0d,
                1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 1.0d};
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        for (int i = 0; i < e.length; i++)
        {
            double score = cones.evaluate(alternatives.get(i));
            assertEquals(expected[i], score, 0.000001d);
        }
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        LinkedList<PairwiseComparison> PCs = new LinkedList<>();
        PCs.add(PairwiseComparison.getPreference(new Alternative("P0", new double[]{0.0d, 2.0d}),
                new Alternative("P1", new double[]{2.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P2", new double[]{0.0d, 4.0d}),
                new Alternative("P3", new double[]{12.0d, 0.0d})));
        PCs.add(PairwiseComparison.getPreference(new Alternative("P4", new double[]{2.0d, 0.0d}),
                new Alternative("P5", new double[]{0.0d, 4.0d})));


        KTSCone cones = new KTSCone(PCs, new INormalization[]{new Linear(0.0d, 2.0d),
                new Linear(0.0d, 2.0d)});

        double[][] e = new double[][]
                {
                        {0.0d, 0.0d}, // 1
                        {4.0d, 2.0d}, // 1
                        {8.0d, 0.0d}, // 2
                        {6.0d, 6.0d}, // 1
                        {12.0d, 8.0d}, // 1

                        {2.0d, 4.0d}, // 1
                        {4.0d, 6.0d}, // 0
                        {6.0d, 8.0d}, // 0
                        {8.0d, 10.0d}, // 0
                        {10.0d, 12.0d}, // 0
                        {2.0d, 10.0d}, // 1
                        {0.0d, 12.0d} // 1
                };

        double[] expected = new double[]{1.0d, 1.0d, 2.0d, 1.0d, 1.0d,
                1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 1.0d};
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        for (int i = 0; i < e.length; i++)
        {
            double score = cones.evaluate(alternatives.get(i));
            assertEquals(expected[i], score, 0.000001d);
        }
    }

}