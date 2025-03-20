package relation.dominance;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import org.junit.jupiter.api.Test;
import print.PrintUtils;
import utils.Constants;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Several tests for the {@link relation.dominance.DominanceUtils} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class DominanceUtilsTest
{
    /**
     * Test 1.
     */
    @Test
    public void testDominationMatrix_1()
    {
        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 5, 3);
        Criteria c = Criteria.constructCriteria("C", 2, false);

        {
            double[] e = {0.0d, 0.0d};
            a.get(0).setPerformanceVector(e);
        }
        {
            double[] e = {1.0d, 0.0d};
            a.get(1).setPerformanceVector(e);
        }
        {
            double[] e = {0.0d, 1.0d};
            a.get(2).setPerformanceVector(e);
        }
        {
            double[] e = {1.0d, 1.0d};
            a.get(3).setPerformanceVector(e);
        }
        {
            double[] e = {1.0d, 2.0d};
            a.get(4).setPerformanceVector(e);
        }

        {
            boolean[][] m = DominanceUtils.createDominanceMatrix(a, c, Constants.EPSILON);

            boolean[][] exp =
                    {{false, true, true, true, true},
                            {false, false, false, true, true},
                            {false, false, false, true, true},
                            {false, false, false, false, true},
                            {false, false, false, false, false},};

            for (int i = 0; i < m.length; i++)
            {
                for (int j = 0; j < m.length; j++)
                {
                    assertEquals(exp[i][j], m[i][j]);
                }
            }
        }
        {
            boolean[][] m = DominanceUtils.createDominanceMatrix(a, c,
                    new Dominance(c, Constants.EPSILON), Constants.EPSILON);

            boolean[][] exp =
                    {{false, true, true, true, true},
                            {false, false, false, true, true},
                            {false, false, false, true, true},
                            {false, false, false, false, true},
                            {false, false, false, false, false},};

            for (int i = 0; i < m.length; i++)
            {
                for (int j = 0; j < m.length; j++)
                {
                    assertEquals(exp[i][j], m[i][j]);
                }
            }
        }
    }

    /**
     * Test 2.
     */
    @Test
    public void testIsGoodAtLeastAs()
    {
        Criteria c = Criteria.constructCriteria("C", 3, false);
        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 2, 3);
        boolean[] mask = {true, false, true};

        {
            double e = 0.01;
            double[] e0 = {1.0d, 1.0d, 1.0d};
            double[] e1 = {1.0d, 1.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isGoodAtLeastAs(a.get(0), a.get(1), c, e);
            assertTrue(q);
        }

        {
            double e = 0.01;
            double[] e0 = {1.0d, 1.0d, 1.0d};
            double[] e1 = {1.0d, 2.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isGoodAtLeastAs(a.get(0), a.get(1), c, mask, e);
            assertTrue(q);
        }

        {
            double e = 1.01;
            double[] e0 = {1.0d, 1.0d, 2.0d};
            double[] e1 = {2.0d, 2.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isGoodAtLeastAs(a.get(0), a.get(1), c, mask, e);
            assertTrue(q);
        }

        {
            double e = 0.9999;
            double[] e0 = {1.0d, 1.0d, 2.0d};
            double[] e1 = {2.0d, 2.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isGoodAtLeastAs(a.get(0), a.get(1), c, mask, e);
            assertFalse(q);
        }

        {
            double e = 0.01;
            double[] e0 = {1.0d, 2.0d, 2.0d};
            double[] e1 = {2.0d, 2.0d, 2.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isGoodAtLeastAs(a.get(0), a.get(1), c, e);
            assertTrue(q);
        }

        {
            double e = 0.01;
            double[] e0 = {1.0d, 2.0d, 2.0d};
            double[] e1 = {2.0d, 2.0d, 2.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isGoodAtLeastAs(a.get(1), a.get(0), c, e);
            assertFalse(q);
        }
    }

    /**
     * Test 3.
     */
    @Test
    public void testIsDominating()
    {
        Criteria c = Criteria.constructCriteria("C", 3, false);
        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 2, 3);
        {
            double e = 0.01;
            double[] e0 = {1.0d, 1.0d, 1.0d};
            double[] e1 = {1.0d, 1.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isDominating(a.get(0), a.get(1), c, e);
            assertFalse(q);
        }

        {
            double e = 0.01;
            double[] e0 = {0.0d, 1.0d, 1.0d};
            double[] e1 = {1.0d, 1.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isDominating(a.get(0), a.get(1), c, e);
            assertTrue(q);
        }

        {
            double e = 0.01;
            double[] e0 = {0.0d, 0.0d, 0.0d};
            double[] e1 = {1.0d, 1.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isDominating(a.get(0), a.get(1), c, e);
            assertTrue(q);
        }

        {
            double e = 1.01;
            double[] e0 = {0.0d, 1.0d, 1.0d};
            double[] e1 = {1.0d, 1.0d, 1.0d};
            a.get(0).setPerformanceVector(e0);
            a.get(1).setPerformanceVector(e1);
            boolean q = DominanceUtils.isDominating(a.get(0), a.get(1), c, e);
            assertFalse(q);
        }
    }



    /**
     * Test 4.
     */
    @Test
    public void testDominationMatrix_5()
    {
        ArrayList<Alternative> a = Alternative.getAlternativeArray("A", 5, 3);
        Criteria c = Criteria.constructCriteria("C", 2, false);

        {
            double[] e = {0.0d, 0.0d};
            a.get(0).setPerformanceVector(e);
        }
        {
            double[] e = {1.0d, 0.0d};
            a.get(1).setPerformanceVector(e);
        }
        {
            double[] e = {0.0d, 1.0d};
            a.get(2).setPerformanceVector(e);
        }
        {
            double[] e = {1.0d, 1.0d};
            a.get(3).setPerformanceVector(e);
        }
        {
            double[] e = {1.0d, 2.0d};
            a.get(4).setPerformanceVector(e);
        }

        double[][] alternatives = new double[a.size()][];
        for (int i = 0; i < alternatives.length; i++) alternatives[i] = a.get(i).getPerformanceVector();

        {
            boolean[][] m = DominanceUtils.createDominanceMatrix(alternatives, c);

            PrintUtils.print2dBooleans(m);

            boolean[][] exp =
                    {{false, true, true, true, true},
                            {false, false, false, true, true},
                            {false, false, false, true, true},
                            {false, false, false, false, true},
                            {false, false, false, false, false},};

            for (int i = 0; i < m.length; i++)
                for (int j = 0; j < m.length; j++)
                    assertEquals(exp[i][j], m[i][j]);
        }
    }


    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        double[][] input = new double[][]{{1.0d, 1.0d}};
        Criteria c = Criteria.constructCriteria("C", 2, false);
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(1, output.length);
    }

    /**
     * Test 6.
     */
    @Test
    void test6()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };


        Criteria c = Criteria.constructCriteria("C", 2, false);
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(1, output.length);
        assertEquals(1.0d, output[0][0], 0.0000001d);
        assertEquals(1.0d, output[0][1], 0.0000001d);
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria("C", 2, true);
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(1, output.length);
        assertEquals(4.0d, output[0][0], 0.0000001d);
        assertEquals(4.0d, output[0][1], 0.0000001d);
    }

    /**
     * Test 8.
     */
    @Test
    void test8()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{true, false});
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(2, output.length);
        assertEquals(4.0d, output[0][0], 0.0000001d);
        assertEquals(4.0d, output[0][1], 0.0000001d);
        assertEquals(3.0d, output[1][0], 0.0000001d);
        assertEquals(1.0d, output[1][1], 0.0000001d);
    }

    /**
     * Test 9.
     */
    @Test
    void test9()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{false, true});
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(2, output.length);
        assertEquals(4.0d, output[0][0], 0.0000001d);
        assertEquals(4.0d, output[0][1], 0.0000001d);
        assertEquals(1.0d, output[1][0], 0.0000001d);
        assertEquals(3.0d, output[1][1], 0.0000001d);
    }

    /**
     * Test 10.
     */
    @Test
    void test10()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {4.0d, 4.0d},
                };

        Criteria c = Criteria.constructCriteria("C", 2, true);
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(2, output.length);
        assertEquals(4.0d, output[0][0], 0.0000001d);
        assertEquals(4.0d, output[0][1], 0.0000001d);
        assertEquals(4.0d, output[1][0], 0.0000001d);
        assertEquals(4.0d, output[1][1], 0.0000001d);
    }

    /**
     * Test 11.
     */
    @Test
    void test11()
    {
        double[][] input = new double[][]
                {
                        {1.0d, 4.0d},
                        {2.0d, 2.0d},
                        {4.0d, 1.0d},
                };

        Criteria c = Criteria.constructCriteria("C", 2, false);
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(3, output.length);
        assertEquals(1.0d, output[0][0], 0.0000001d);
        assertEquals(4.0d, output[0][1], 0.0000001d);
        assertEquals(2.0d, output[1][0], 0.0000001d);
        assertEquals(2.0d, output[1][1], 0.0000001d);
        assertEquals(4.0d, output[2][0], 0.0000001d);
        assertEquals(1.0d, output[2][1], 0.0000001d);
    }

    /**
     * Test 12.
     */
    @Test
    void test12()
    {
        double[][] input = new double[][]
                {
                        {2.0d, 2.0d, 2.0d},
                        {0.0d, 1.0d, 3.0d},
                        {3.0d, 1.0d, 0.0d},
                        {1.0d, 1.0d, 1.0d},
                };

        Criteria c = Criteria.constructCriteria("C", 3, false);
        double[][] output = DominanceUtils.getNonDominatedVectors(input, c);
        assertEquals(3, output.length);
        assertEquals(0.0d, output[0][0], 0.0000001d);
        assertEquals(1.0d, output[0][1], 0.0000001d);
        assertEquals(3.0d, output[0][2], 0.0000001d);
        assertEquals(3.0d, output[1][0], 0.0000001d);
        assertEquals(1.0d, output[1][1], 0.0000001d);
        assertEquals(0.0d, output[1][2], 0.0000001d);
        assertEquals(1.0d, output[2][0], 0.0000001d);
        assertEquals(1.0d, output[2][1], 0.0000001d);
        assertEquals(1.0d, output[2][2], 0.0000001d);
    }



    /**
     * Test 13.
     */
    @Test
    void test13()
    {
        double[][] input = new double[][]{{1.0d, 1.0d}};
        Criteria c = Criteria.constructCriteria("C", 2, false);
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(1, output.size());
    }


    /**
     * Test 14.
     */
    @Test
    void test14()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria("C", 2, false);
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(1, output.size());
        assertEquals(1.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
    }

    /**
     * Test 15.
     */
    @Test
    void test15()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria("C", 2, true);
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(1, output.size());
        assertEquals(4.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(4.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
    }

    /**
     * Test 16.
     */
    @Test
    void test16()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{true, false});
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(2, output.size());
        assertEquals(4.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(4.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
        assertEquals(3.0d, output.get(1).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, output.get(1).getPerformanceVector()[1], 0.0000001d);
    }

    /**
     * Test 17.
     */
    @Test
    void test17()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {3.0d, 3.0d},
                        {2.0d, 2.0d},
                        {1.0d, 1.0d},
                        {3.0d, 1.0d},
                        {1.0d, 3.0d}
                };

        Criteria c = Criteria.constructCriteria(new String[]{"C1", "C2"}, new boolean[]{false, true});
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(2, output.size());
        assertEquals(4.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(4.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
        assertEquals(1.0d, output.get(1).getPerformanceVector()[0], 0.0000001d);
        assertEquals(3.0d, output.get(1).getPerformanceVector()[1], 0.0000001d);
    }

    /**
     * Test 18.
     */
    @Test
    void test18()
    {
        double[][] input = new double[][]
                {
                        {4.0d, 4.0d},
                        {4.0d, 4.0d},
                };

        Criteria c = Criteria.constructCriteria("C", 2, true);
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(2, output.size());
        assertEquals(4.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(4.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
        assertEquals(4.0d, output.get(1).getPerformanceVector()[0], 0.0000001d);
        assertEquals(4.0d, output.get(1).getPerformanceVector()[1], 0.0000001d);
    }

    /**
     * Test 19.
     */
    @Test
    void test19()
    {
        double[][] input = new double[][]
                {
                        {1.0d, 4.0d},
                        {2.0d, 2.0d},
                        {4.0d, 1.0d},
                };

        Criteria c = Criteria.constructCriteria("C", 2, false);
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(3, output.size());
        assertEquals(1.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(4.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
        assertEquals(2.0d, output.get(1).getPerformanceVector()[0], 0.0000001d);
        assertEquals(2.0d, output.get(1).getPerformanceVector()[1], 0.0000001d);
        assertEquals(4.0d, output.get(2).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, output.get(2).getPerformanceVector()[1], 0.0000001d);
    }

    /**
     * Test 20.
     */
    @Test
    void test20()
    {
        double[][] input = new double[][]
                {
                        {2.0d, 2.0d, 2.0d},
                        {0.0d, 1.0d, 3.0d},
                        {3.0d, 1.0d, 0.0d},
                        {1.0d, 1.0d, 1.0d},
                };

        Criteria c = Criteria.constructCriteria("C", 3, false);
        ArrayList<Alternative> output = DominanceUtils.getNonDominatedAlternatives(new Alternatives(Alternative.getAlternativeArray("A", input)), c);
        assertEquals(3, output.size());
        assertEquals(0.0d, output.get(0).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, output.get(0).getPerformanceVector()[1], 0.0000001d);
        assertEquals(3.0d, output.get(0).getPerformanceVector()[2], 0.0000001d);
        assertEquals(3.0d, output.get(1).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, output.get(1).getPerformanceVector()[1], 0.0000001d);
        assertEquals(0.0d, output.get(1).getPerformanceVector()[2], 0.0000001d);
        assertEquals(1.0d, output.get(2).getPerformanceVector()[0], 0.0000001d);
        assertEquals(1.0d, output.get(2).getPerformanceVector()[1], 0.0000001d);
        assertEquals(1.0d, output.get(2).getPerformanceVector()[2], 0.0000001d);
    }




}