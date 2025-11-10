package emo.interactive.ktscone.cdemo;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Provides various tests associated with {@link Utils}.
 *
 * @author MTomczyk
 */
class UtilsTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        double[] e = new double[]{0.0d, 1.0d, 2.0d, 0.0d, 1.0d, 6.0d};
        LinkedList<LinkedList<Integer>> fronts = Utils.getConeFronts(e);

        int[][] exp = new int[][]
                {
                        {0, 3},
                        {1, 4},
                        {2},
                        {5}
                };

        assertEquals(exp.length, fronts.size());
        for (int i = 0; i < exp.length; i++)
        {
            assertEquals(exp[i].length, fronts.get(i).size());
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], fronts.get(i).get(j));
        }
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        double[] e = new double[]{0.0d};
        LinkedList<LinkedList<Integer>> fronts = Utils.getConeFronts(e);

        int[][] exp = new int[][]
                {
                        {0}
                };

        assertEquals(exp.length, fronts.size());
        for (int i = 0; i < exp.length; i++)
        {
            assertEquals(exp[i].length, fronts.get(i).size());
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], fronts.get(i).get(j));
        }
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        double[] e = new double[]{100.0d};
        LinkedList<LinkedList<Integer>> fronts = Utils.getConeFronts(e);

        int[][] exp = new int[][]
                {
                        {0}
                };

        assertEquals(exp.length, fronts.size());
        for (int i = 0; i < exp.length; i++)
        {
            assertEquals(exp[i].length, fronts.get(i).size());
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], fronts.get(i).get(j));
        }
    }

    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        double[] e = new double[]{4.0d, 1.0d, 9.0d, 0.0d};
        LinkedList<LinkedList<Integer>> fronts = Utils.getConeFronts(e);

        int[][] exp = new int[][]
                {
                        {3},
                        {1},
                        {0},
                        {2}
                };

        assertEquals(exp.length, fronts.size());
        for (int i = 0; i < exp.length; i++)
        {
            assertEquals(exp[i].length, fronts.get(i).size());
            for (int j = 0; j < exp[i].length; j++)
                assertEquals(exp[i][j], fronts.get(i).get(j));
        }
    }
}