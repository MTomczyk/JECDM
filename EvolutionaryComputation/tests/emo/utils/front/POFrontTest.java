package emo.utils.front;

import alternative.Alternative;
import alternative.Alternatives;
import model.internals.value.scalarizing.LNorm;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Provides various tests for {@link POFront}.
 *
 * @author MTomczyk
 */
class POFrontTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        double[][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        double[][] e = new double[][]{
                {0.081484834, 0.710850042},
                {0.801876511, 0.116252344},
                {0.326434938, 0.963424104},
                {0.644221772, 0.553057844},
                {0.872897134, 0.996322402},
                {0.663236541, 0.259193031},
                {0.941267528, 0.143190521},
                {0.333208419, 0.639930389},
                {0.46279844, 0.927060814},
                {0.04855188, 0.058450836},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        POFront POF = new POFront();

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models);

            int[][] exp = new int[][]{
                    {9},
                    {0, 1, 5, 7},
                    {2, 3, 6, 8},
                    {4}
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models, 0);
            assertEquals(0, fronts.size());
        }

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models, 1);

            int[][] exp = new int[][]{
                    {9},
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }

        for (int k = 2; k < 6; k++)
        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models, k);

            int[][] exp = new int[][]{
                    {9},
                    {0, 1, 5, 7},
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }

        for (int k = 6; k < 10; k++)
        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models, k);

            int[][] exp = new int[][]{
                    {9},
                    {0, 1, 5, 7},
                    {2, 3, 6, 8},
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }

        for (int k = 10; k < 15; k++)
        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models, k);

            int[][] exp = new int[][]{
                    {9},
                    {0, 1, 5, 7},
                    {2, 3, 6, 8},
                    {4}
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        double[][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        double[][] e = new double[][]{
                {0.0d, 0.0d},
                {0.5d, 0.5d},
                {1.0d, 1.0d},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        POFront POF = new POFront();

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models);

            int[][] exp = new int[][]{
                    {0}, {1}, {2}
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        double[][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        double[][] e = new double[][]{
                {0.0d, 0.0d},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        POFront POF = new POFront();

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models);

            int[][] exp = new int[][]{
                    {0},
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }
    }


    /**
     * Test 4.
     */
    @Test
    void test4()
    {
        double[][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        double[][] e = new double[][]{
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        POFront POF = new POFront();

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models);
            assertEquals(0, fronts.size());
        }
    }


    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        double[][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        double[][] e = new double[][]{
                {0.0d, 1.0d}, {1.0d, 0.0d}
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        POFront POF = new POFront();

        {
            LinkedList<LinkedList<Integer>> fronts = POF.getFrontAssignments(new Alternatives(alternatives), models);

            int[][] exp = new int[][]{
                    {0, 1},
            };

            assertEquals(exp.length, fronts.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts.get(i).get(j));
            }
        }
    }


    /**
     * Test 6 (random crash test).
     */
    @Test
    void test6()
    {
        IRandom R = new MersenneTwister64(0);

        int T = 100;
        int A = 100;
        int M = 20;
        for (int t = 0; t < T; t++)
        {

            double[][] w = new double[M][2];
            for (int i = 0; i < M; i++)
            {
                w[i][0] = R.nextDouble();
                w[i][1] = 1.0d - w[i][0];
            }

            ArrayList<LNorm> models = new ArrayList<>(M);
            for (int i = 0; i < M; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

            double[][] e = new double[A][2];
            for (int i = 0; i < A; i++)
            {
                e[i][0] = R.nextDouble();
                e[i][1] = R.nextDouble();
            }

            ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

            POFront POF = new POFront();
            POF.getFrontAssignments(new Alternatives(alternatives), models);
        }
    }

    /**
     * Test 7.
     */
    @Test
    void test7()
    {
        double[][] w = new double[21][2];
        for (int i = 0; i < 21; i++)
        {
            w[i][0] = (double) i / 20.0d;
            w[i][1] = 1.0d - w[i][0];
        }

        ArrayList<LNorm> models = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

        double[][] e = new double[][]{
                {0.081484834, 0.710850042},
                {0.801876511, 0.116252344},
                {0.326434938, 0.963424104},
                {0.644221772, 0.553057844},
                {0.872897134, 0.996322402},
                {0.663236541, 0.259193031},
                {0.941267528, 0.143190521},
                {0.333208419, 0.639930389},
                {0.46279844, 0.927060814},
                {0.04855188, 0.058450836},
        };

        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

        POFront POF1 = new POFront();

        {
            LinkedList<LinkedList<Integer>> fronts1 = POF1.getFrontAssignments(new Alternatives(alternatives), models);

            int[][] exp = new int[][]{
                    {9},
                    {0, 1, 5, 7},
                    {2, 3, 6, 8},
                    {4}
            };

            assertEquals(exp.length, fronts1.size());
            for (int i = 0; i < exp.length; i++)
            {
                assertEquals(exp[i].length, fronts1.get(i).size());
                for (int j = 0; j < exp[i].length; j++) assertEquals(exp[i][j], fronts1.get(i).get(j));
            }

        }
    }


    /**
     * Test 8 (time analysis test).
     */
    @Test
    void test8()
    {
        IRandom R = new MersenneTwister64(0);

        int T = 5;
        int[] A = new int[]{5, 10, 20, 50, 100, 500, 1000};
        int[] M = new int[]{5, 10, 20, 50, 100, 500, 1000};
        for (int a: A)
        {
            for (int m: M)
            {
                long t1 = 0;

                for (int t = 0; t < T; t++)
                {
                    double[][] w = new double[m][2];
                    for (int i = 0; i < m; i++)
                    {
                        w[i][0] = R.nextDouble();
                        w[i][1] = 1.0d - w[i][0];
                    }

                    ArrayList<LNorm> models = new ArrayList<>(m);
                    for (int i = 0; i < m; i++) models.add(new LNorm(w[i], Double.POSITIVE_INFINITY));

                    double[][] e = new double[a][2];
                    for (int i = 0; i < a; i++)
                    {
                        e[i][0] = R.nextDouble();
                        e[i][1] = R.nextDouble();
                    }

                    ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", e);

                    POFront POF = new POFront();

                    long start = System.nanoTime();
                    POF.getFrontAssignments(new Alternatives(alternatives), models);
                    t1 += (System.nanoTime() - start) / 1000000;
                }

                double aT1 = (double) t1 / T;
                System.out.println("Time results for alternatives = " + a + " and models = " + m + ": " + aT1);
            }
        }



    }
}