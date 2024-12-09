package preference;

import alternative.Alternative;
import compatibility.IAnalyzer;
import compatibility.PairwiseComparisonAnalyzer;
import model.internals.value.AbstractValueInternalModel;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import preference.indirect.PairwiseComparison;
import relation.Relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Provides various tests for {@link Alternative}.
 *
 * @author MTomczyk
 */
class PairwiseComparisonTest
{
    /**
     * Test 1
     */
    @Test
    void test1()
    {
        {
            PairwiseComparison PC = PairwiseComparison.getPreference(new Alternative("A1", 1), new Alternative("A2", 1));
            assertEquals("A1", PC.getPreferredAlternative().getName());
            assertEquals("A2", PC.getNotPreferredAlternative().getName());
            assertEquals("A1", PC.getFirstAlternative().getName());
            assertEquals("A2", PC.getSecondAlternative().getName());
            assertEquals(Relations.PREFERENCE, PC.getRelation());
            System.out.println(PC);
            assertEquals("Pairwise comparison: A1 PREFERENCE A2", PC.toString());
        }
    }

    /**
     * Test 2 (compatibility degree for preference)
     */
    @Test
    void test2()
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

            for (int i = 0; i < 5; i++)
            {
                Alternative A1 = new Alternative("A1", a[i]);
                Alternative A2 = new Alternative("A2", b[i]);
                PairwiseComparison PC = PairwiseComparison.getPreference(A1, A2);
                AbstractValueInternalModel M1 = new LNorm(m1[i], Double.POSITIVE_INFINITY);
                AbstractValueInternalModel M2 = new LNorm(m2[i], Double.POSITIVE_INFINITY);

                IAnalyzer pcA = new PairwiseComparisonAnalyzer();
                assertEquals(e1[i], pcA.calculateCompatibilityDegreeWithValueModel(PC, M1), 0.001d);
                assertEquals(e2[i], pcA.calculateCompatibilityDegreeWithValueModel(PC, M2), 0.001d);
            }
        }
    }

    /**
     * Test 3 (compatibility degree for indifference)
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

            double[] e1 = new double[]{0.0d, -0.32d};
            double[] e2 = new double[]{0.0d, -0.14d};

            for (int i = 0; i < 2; i++)
            {
                Alternative A1 = new Alternative("A1", a[i]);
                Alternative A2 = new Alternative("A2", b[i]);
                PairwiseComparison PC = PairwiseComparison.getIndifference(A1, A2);
                AbstractValueInternalModel M1 = new LNorm(m1[i], Double.POSITIVE_INFINITY);
                AbstractValueInternalModel M2 = new LNorm(m2[i], Double.POSITIVE_INFINITY);

                IAnalyzer pcA = new PairwiseComparisonAnalyzer();
                assertEquals(e1[i], pcA.calculateCompatibilityDegreeWithValueModel(PC, M1), 0.001d);
                assertEquals(e2[i], pcA.calculateCompatibilityDegreeWithValueModel(PC, M2), 0.001d);
            }
        }
    }

    /**
     * Test 4 (compatibility degree for incomparability)
     */
    @Test
    void test4()
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


            for (int i = 0; i < 2; i++)
            {
                Alternative A1 = new Alternative("A1", a[i]);
                Alternative A2 = new Alternative("A2", b[i]);
                PairwiseComparison PC = PairwiseComparison.getIncomparable(A1, A2);
                AbstractValueInternalModel M1 = new LNorm(m1[i], Double.POSITIVE_INFINITY);
                AbstractValueInternalModel M2 = new LNorm(m2[i], Double.POSITIVE_INFINITY);

                IAnalyzer pcA = new PairwiseComparisonAnalyzer();
                assertNull(pcA.calculateCompatibilityDegreeWithValueModel(PC, M1));
                assertNull(pcA.calculateCompatibilityDegreeWithValueModel(PC, M2));
            }
        }
    }
}