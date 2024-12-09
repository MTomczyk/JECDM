package relation.equalevaluations;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import org.junit.jupiter.api.Test;
import space.Vector;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Several tests for the {@link EqualEvaluationsUtils} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class EqualEvaluationsTest
{

    /**
     * Test 1.
     */
    @Test
    public void testIsEqual()
    {
        Criteria c = Criteria.constructCriteria("C", 3, false);
        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 2, 3);
        boolean[] mask = {true, false, true};
        {
            double e = 0.01;
            double[] e0 = {1.0d, 2.0d, 1.0d};
            double[] e1 = {2.0d, 3.0d, 2.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);

            boolean q = EqualEvaluationsUtils.isEqualTo(a.get(0), a.get(1), c._no, e);
            assertFalse(q);
            q = EqualEvaluationsUtils.isEqualTo(a.get(1), a.get(0), c._no, e);
            assertFalse(q);
            e = 1.0001d;
            q = EqualEvaluationsUtils.isEqualTo(a.get(1), a.get(0), c._no, e);
            assertTrue(q);
        }

        {
            double e = 0.01;
            double[] e0 = {1.0d, 2.0d, 1.0d};
            double[] e1 = {1.0d, 5.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = EqualEvaluationsUtils.isEqualTo(a.get(0), a.get(1), c._no, mask, e);
            assertTrue(q);
            q = EqualEvaluationsUtils.isEqualTo(a.get(0), a.get(1), c._no, e);
            assertFalse(q);
        }
    }

    /**
     * Test 2 (remove duplicates).
     */
    @Test
    void test2_RemoveDuplicates()
    {
        {
            double[][] alternatives = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {0.0d, 1.0d, 3.0d}, // ok
                            {2.0d, 2.0d, 2.0d}, // x
                            {1.0d, 1.0d, 1.0d}, // ok
                            {1.0d, 1.0d, 1.0d}, // x
                            {1.0d, 1.0d}, // ok
                            {1.0d, 1.0d}, // x
                            {0.0d, 1.0d, 3.0d}, // x
                            {0.0d, 1.0d, 2.0d, 3.0d} // ok
                    };

            double[][] expected1 = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {0.0d, 1.0d, 3.0d}, // ok
                            {1.0d, 1.0d, 1.0d}, // ok
                            {1.0d, 1.0d}, // ok
                            {0.0d, 1.0d, 2.0d, 3.0d} // ok
                    };
            {
                double[][] u = EqualEvaluationsUtils.removeDuplicates(alternatives, 0.1d);
                assertEquals(u.length, expected1.length);
                for (int i = 0; i < expected1.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected1[i], u[i]));
            }
            {
                double[][] u = EqualEvaluationsUtils.removeDuplicates(alternatives, 0.0d);
                assertEquals(u.length, expected1.length);
                for (int i = 0; i < expected1.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected1[i], u[i]));
            }

            double[][] expected2 = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {1.0d, 1.0d}, // ok
                            {0.0d, 1.0d, 2.0d, 3.0d} // ok
                    };
            {
                double[][] u = EqualEvaluationsUtils.removeDuplicates(alternatives, 10000.0d);
                assertEquals(u.length, expected2.length);
                for (int i = 0; i < expected2.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected2[i], u[i]));
            }
        }

        {
            double[][] alternatives = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                    };

            double[][] expected = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                    };
            {
                double[][] u = EqualEvaluationsUtils.removeDuplicates(alternatives, 0.0d);
                assertEquals(u.length, expected.length);
                for (int i = 0; i < expected.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected[i], u[i]));
            }
        }
    }

    /**
     * Test 3 (remove duplicates).
     */
    @Test
    void test3_RemoveDuplicates()
    {
        {
            double[][] alternatives = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {0.0d, 1.0d, 3.0d}, // ok
                            {2.0d, 2.0d, 2.0d}, // x
                            {1.0d, 1.0d, 1.0d}, // ok
                            {1.0d, 1.0d, 1.0d}, // x
                            {1.0d, 1.0d}, // ok
                            {1.0d, 1.0d}, // x
                            {0.0d, 1.0d, 3.0d}, // x
                            {0.0d, 1.0d, 2.0d, 3.0d} // ok
                    };

            double[][] expected1 = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {0.0d, 1.0d, 3.0d}, // ok
                            {1.0d, 1.0d, 1.0d}, // ok
                            {1.0d, 1.0d}, // ok
                            {0.0d, 1.0d, 2.0d, 3.0d} // ok
                    };

            AbstractAlternatives<?> As = new Alternatives(Alternative.getAlternativeArray("A", alternatives));

            {
                AbstractAlternatives<?> u = EqualEvaluationsUtils.removeDuplicates(As, 0.1d);
                assertEquals(u.size(), expected1.length);
                for (int i = 0; i < expected1.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected1[i], u.get(i).getPerformanceVector()));
            }
            {
                AbstractAlternatives<?> u = EqualEvaluationsUtils.removeDuplicates(As, 0.0d);
                assertEquals(u.size(), expected1.length);
                for (int i = 0; i < expected1.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected1[i], u.get(i).getPerformanceVector()));
            }

            double[][] expected2 = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {1.0d, 1.0d}, // ok
                            {0.0d, 1.0d, 2.0d, 3.0d} // ok
                    };
            {
                AbstractAlternatives<?> u = EqualEvaluationsUtils.removeDuplicates(As, 10000.0d);
                assertEquals(u.size(), expected2.length);
                for (int i = 0; i < expected2.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected2[i], u.get(i).getPerformanceVector()));
            }
        }

        {
            double[][] alternatives = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                            {2.0d, 2.0d, 2.0d}, // x
                    };

            double[][] expected = new double[][]
                    {
                            {2.0d, 2.0d, 2.0d}, // ok
                    };
            {
                ArrayList<Alternative> As = Alternative.getAlternativeArray("A", alternatives);

                AbstractAlternatives<?> u = EqualEvaluationsUtils.removeDuplicates(new Alternatives(As), 0.0d);
                assertEquals(u.size(), expected.length);
                for (int i = 0; i < expected.length; i++)
                    assertTrue(Vector.areVectorsEqual(expected[i], u.get(i).getPerformanceVector()));
            }
        }
    }

}