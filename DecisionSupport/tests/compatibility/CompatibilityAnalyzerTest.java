package compatibility;

import alternative.Alternative;
import history.PreferenceInformationWrapper;
import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Provides various tests for {@link CompatibilityAnalyzer}.
 *
 * @author MTomczyk
 */
class CompatibilityAnalyzerTest
{
    /**
     * Test 1 (compatibility degree for PC)
     */
    @Test
    void test1()
    {
        {
            double[][] a = new double[][]{
                    {0.2d, 0.5d},
                    {0.7d, 0.2d},
                    {0.3d, 0.3d},
                    {0.6d, 0.2d},
                    {0.7d, 0.4d}
            };

            double[][] b = new double[][]{
                    {0.1d, 0.4d},
                    {0.5d, 0.2d},
                    {0.3d, 0.3d},
                    {0.9d, 0.4d},
                    {0.4d, 0.7d}
            };

            double[][] m1 = new double[][]
                    {
                            {0.5d, 0.5d},
                            {0.2d, 0.8d},
                            {0.3d, 0.7d},
                            {0.9d, 0.1d},
                            {0.4d, 0.6d}
                    };

            double[][] m2 = new double[][]
                    {
                            {0.3d, 0.7d},
                            {0.7d, 0.3d},
                            {0.6d, 0.4d},
                            {0.1d, 0.9d},
                            {0.6d, 0.4d}
                    };

            double[] e1 = new double[]{-0.05d, 0.0d, 0.0d, 0.27d, 0.14d};
            double[] e2 = new double[]{-0.07d, -0.14d, 0.0d, 0.18d, -0.14d};

            CompatibilityAnalyzer CA = new CompatibilityAnalyzer();

            for (int i = 0; i < 5; i++)
            {
                Alternative A1 = new Alternative("A1", a[i]);
                Alternative A2 = new Alternative("A2", b[i]);
                PairwiseComparison PC = PairwiseComparison.getPreference(A1, A2);
                AbstractValueInternalModel M1 = new LNorm(m1[i], Double.POSITIVE_INFINITY);
                AbstractValueInternalModel M2 = new LNorm(m2[i], Double.POSITIVE_INFINITY);

                assertEquals(e1[i], CA.calculateCompatibilityDegreeWithValueModel(PC, M1), 0.001d);
                assertEquals(e2[i], CA.calculateCompatibilityDegreeWithValueModel(PC, M2), 0.001d);
            }
        }
    }

    /**
     * Test 2 (compatibility degree for indifference)
     */
    @Test
    void test2()
    {
        {
            double[][] a = new double[][]{
                    {0.5d, 0.5d},
                    {0.3d, 0.7d},
            };

            double[][] b = new double[][]{
                    {0.5d, 0.5d},
                    {0.7d, 0.3d},
            };

            double[][] m1 = new double[][]
                    {
                            {0.5d, 0.5d},
                            {0.2d, 0.8d},
                    };

            double[][] m2 = new double[][]
                    {
                            {0.3d, 0.7d},
                            {0.6d, 0.4d},
                    };

            double[] e1 = new double[]{0.0d, -0.32d};
            double[] e2 = new double[]{0.0d, -0.14d};

            CompatibilityAnalyzer CA = new CompatibilityAnalyzer();

            for (int i = 0; i < 2; i++)
            {
                Alternative A1 = new Alternative("A1", a[i]);
                Alternative A2 = new Alternative("A2", b[i]);
                PairwiseComparison PC = PairwiseComparison.getIndifference(A1, A2);
                AbstractValueInternalModel M1 = new LNorm(m1[i], Double.POSITIVE_INFINITY);
                AbstractValueInternalModel M2 = new LNorm(m2[i], Double.POSITIVE_INFINITY);

                assertEquals(e1[i], CA.calculateCompatibilityDegreeWithValueModel(PC, M1), 0.001d);
                assertEquals(e2[i], CA.calculateCompatibilityDegreeWithValueModel(PC, M2), 0.001d);
            }
        }
    }

    /**
     * Test 3 (compatibility degree for incomparability)
     */
    @Test
    void test3()
    {
        {
            double[][] a = new double[][]{
                    {0.5d, 0.5d},
                    {0.3d, 0.7d},
            };

            double[][] b = new double[][]{
                    {0.5d, 0.5d},
                    {0.7d, 0.3d},
            };

            double[][] m1 = new double[][]
                    {
                            {0.5d, 0.5d},
                            {0.2d, 0.8d},
                    };

            double[][] m2 = new double[][]
                    {
                            {0.3d, 0.7d},
                            {0.6d, 0.4d},
                    };

            CompatibilityAnalyzer CA = new CompatibilityAnalyzer();

            for (int i = 0; i < 2; i++)
            {
                Alternative A1 = new Alternative("A1", a[i]);
                Alternative A2 = new Alternative("A2", b[i]);
                PairwiseComparison PC = PairwiseComparison.getIncomparable(A1, A2);
                AbstractValueInternalModel M1 = new LNorm(m1[i], Double.POSITIVE_INFINITY);
                AbstractValueInternalModel M2 = new LNorm(m2[i], Double.POSITIVE_INFINITY);

                assertNull(CA.calculateCompatibilityDegreeWithValueModel(PC, M1));
                assertNull(CA.calculateCompatibilityDegreeWithValueModel(PC, M2));
            }
        }
    }

    /**
     * Test 4 (compatibility degree for incomparability)
     */
    @Test
    void test4()
    {

        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
        assertNull(CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(null, null));
        assertNull(CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(new LinkedList<>(), null));

        Alternative A1 = new Alternative("A1", new double[]{0.4d, 0.6d});
        Alternative A2 = new Alternative("A2", new double[]{0.2d, 0.8d});
        PairwiseComparison PC1 = PairwiseComparison.getPreference(A1, A2);
        LinkedList<PreferenceInformationWrapper> pes = new LinkedList<>();
        pes.add(PreferenceInformationWrapper.getTestInstance(PC1));
        assertNull(CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(pes, null));
    }

    /**
     * Test 5 (compatibility degree for incomparability)
     */
    @Test
    void test5()
    {

        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();

        Alternative A1 = new Alternative("A1", new double[]{0.4d, 0.6d});
        Alternative A2 = new Alternative("A2", new double[]{0.2d, 0.8d});
        PairwiseComparison PC1 = PairwiseComparison.getPreference(A1, A2);

        Alternative A3 = new Alternative("A3", new double[]{0.5d, 0.5d});
        Alternative A4 = new Alternative("A4", new double[]{1.0d, 1.0d});
        PairwiseComparison PC2 = PairwiseComparison.getPreference(A3, A4);

        AbstractValueInternalModel M = new LNorm(new double[]{0.5d, 0.5d}, Double.POSITIVE_INFINITY);

        LinkedList<PreferenceInformationWrapper> pes = new LinkedList<>();
        pes.add(PreferenceInformationWrapper.getTestInstance(PC1));
        pes.add(PreferenceInformationWrapper.getTestInstance(PC2));

        double dcd = CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(pes, M);
        assertEquals(0.1d, dcd, 0.00001d);
    }
}