package selection;

import ea.EA;
import org.junit.jupiter.api.Test;
import population.Parents;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;
import statistics.Statistics;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Several tests for the {@link selection.Tournament} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class TournamentTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        IRandom R = new MersenneTwister64(System.currentTimeMillis());
        EA.Params pEA = new EA.Params();
        pEA._populationSize = 5;
        pEA._offspringSize = 5;
        pEA._R = R;
        pEA._name = "genetic";
        pEA._id = 0;
        EA ea = new EA(pEA);

        ArrayList<Specimen> S = new ArrayList<>(5);
        int[] O = new int[]{3, 4, 1, 0, 2};

        HashMap<Specimen, Integer> map = new HashMap<>();
        for (int i = 0; i < 5; i++)
        {
            Specimen s = new Specimen(1, new SpecimenID(ea.getID(), 0, 0, i));
            s.setAuxScore(O[i]);
            S.add(s);
            map.put(s, i);
        }
        ea.setSpecimensContainer(new SpecimensContainer());
        ea.getSpecimensContainer().setMatingPool(S);

        {
            //-----------------------------------------------------
            Tournament.Params TP = new Tournament.Params();
            TP._noParentsPerOffspring = 2;
            TP._withReplacement = true;
            TP._size = 1;
            TP._noOffspring = 5;
            Tournament TS = new Tournament(TP);

            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = TS.selectParents(ea.getSpecimensContainer().getMatingPool(), R);
                for (Parents P : parents)
                {
                    int id1 = map.get(P._parents.get(0));
                    int id2 = map.get(P._parents.get(1));
                    cnt[id1][id2]++;
                }
            }

            PrintUtils.print2dIntegers(cnt);
            System.out.println();
            double s = 5 * t;
            for (int j = 0; j < 5; j++)
                for (int jj = 0; jj < 5; jj++)
                    assertTrue(Statistics.getRelativeDifference(1.0d / 25.0d, (double) cnt[j][jj] / s) < 0.05d);
        }

        {
            Tournament.Params TP = new Tournament.Params();
            TP._noParentsPerOffspring = 2;
            TP._withReplacement = true;
            TP._size = 2;
            TP._noOffspring = 5;
            Tournament TS = new Tournament(TP);


            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = TS.selectParents(ea.getSpecimensContainer().getMatingPool(), R);
                for (Parents P : parents)
                {
                    int id1 = map.get(P._parents.get(0));
                    int id2 = map.get(P._parents.get(1));
                    cnt[id1][id2]++;
                }
            }

            PrintUtils.print2dIntegers(cnt);
            System.out.println();

            //    int [] O = new int[]{3,4,1,0,2};
            @SuppressWarnings("PointlessArithmeticExpression")
            double[] pW = new double[]{
                    (1.0d / 5.0d * 3.0d / 5.0d) + (1.0d / 5.0d * 3.0d / 5.0d) + (1.0d / 5.0d * 1.0d / 5.0d),
                    (1.0d / 5.0d * 4.0d / 5.0d) + (1.0d / 5.0d * 4.0d / 5.0d) + (1.0d / 5.0d * 1.0d / 5.0d),
                    (1.0d / 5.0d * 1.0d / 5.0d) + (1.0d / 5.0d * 1.0d / 5.0d) + (1.0d / 5.0d * 1.0d / 5.0d),
                    (1.0d / 5.0d * 0.0d / 5.0d) + (1.0d / 5.0d * 0.0d / 5.0d) + (1.0d / 5.0d * 1.0d / 5.0d),
                    (1.0d / 5.0d * 2.0d / 5.0d) + (1.0d / 5.0d * 2.0d / 5.0d) + (1.0d / 5.0d * 1.0d / 5.0d),
            };

            double s = 5 * t;
            for (int j = 0; j < 5; j++)
                for (int jj = 0; jj < 5; jj++)
                    assertTrue(Statistics.getRelativeDifference(pW[j] * pW[jj], (double) cnt[j][jj] / s) < 0.05d);

        }

        {
            Tournament.Params TP = new Tournament.Params();
            TP._noParentsPerOffspring = 2;
            TP._withReplacement = false;
            TP._size = 1;
            TP._useSampling = true;
            TP._noOffspring = 5;
            Tournament TS = new Tournament(TP);


            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = TS.selectParents(ea.getSpecimensContainer().getMatingPool(), R);
                for (Parents P : parents)
                {
                    int id1 = map.get(P._parents.get(0));
                    int id2 = map.get(P._parents.get(1));
                    cnt[id1][id2]++;
                }
            }

            PrintUtils.print2dIntegers(cnt);
            System.out.println();

            double s = 5 * t;
            for (int j = 0; j < 5; j++)
                for (int jj = 0; jj < 5; jj++)
                {
                    double expected = 0.0d;
                    if (j != jj) expected = 1.0d / (25.0d - 5.0d);
                    if (Double.compare(expected, 0.0d) == 0) assertEquals(0.0d, (double) cnt[j][jj] / s, 0.0001d);
                    else assertTrue(Statistics.getRelativeDifference(expected, (double) cnt[j][jj] / s) < 0.05d);
                }

        }

        {
            Tournament.Params TP = new Tournament.Params();
            TP._noParentsPerOffspring = 2;
            TP._withReplacement = false;
            TP._size = 1;
            TP._useSampling = false;
            TP._noOffspring = 5;
            Tournament TS = new Tournament(TP);


            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = TS.selectParents(ea.getSpecimensContainer().getMatingPool(), R);
                for (Parents P : parents)
                {
                    int id1 = map.get(P._parents.get(0));
                    int id2 = map.get(P._parents.get(1));
                    cnt[id1][id2]++;
                }
            }

            PrintUtils.print2dIntegers(cnt);
            System.out.println();

            double s = 5 * t;
            for (int j = 0; j < 5; j++)
                for (int jj = 0; jj < 5; jj++)
                {
                    double expected = 0.0d;
                    if (j != jj) expected = 1.0d / (25.0d - 5.0d);
                    if (Double.compare(expected, 0.0d) == 0) assertEquals(0.0d, (double) cnt[j][jj] / s, 0.0001d);
                    else assertTrue(Statistics.getRelativeDifference(expected, (double) cnt[j][jj] / s) < 0.05d);
                }

        }

        {
            Tournament.Params TP = new Tournament.Params();
            TP._noParentsPerOffspring = 5;
            TP._withReplacement = false;
            TP._size = 1;
            TP._useSampling = false;
            TP._noOffspring = 5;
            Tournament TS = new Tournament(TP);


            int[][][][][] cnt = new int[5][5][5][5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = TS.selectParents(ea.getSpecimensContainer().getMatingPool(), R);
                for (Parents P : parents)
                {
                    assertEquals(5, P._parents.size());
                    int id1 = map.get(P._parents.get(0));
                    int id2 = map.get(P._parents.get(1));
                    int id3 = map.get(P._parents.get(2));
                    int id4 = map.get(P._parents.get(3));
                    int id5 = map.get(P._parents.get(4));
                    cnt[id1][id2][id3][id4][id5]++;
                }
            }

            int min  = Integer.MAX_VALUE;
            int max  = Integer.MIN_VALUE;


            for (int a1 = 0; a1 < 5; a1++)
                for (int a2 = 0; a2 < 5; a2++)
                    for (int a3 = 0; a3 < 5; a3++)
                        for (int a4 = 0; a4 < 5; a4++)
                            for (int a5 = 0; a5 < 5; a5++)
                            {
                                //noinspection PointlessArithmeticExpression
                                if (((1+a1) * (1+a2) * (1+a3) * (1+a4) * (1+a5) == 1 * 2 * 3 * 4 * 5) &&
                                        ((1+a1) + (1+a2) + (1+a3) + (1+a4) + (1+a5) ==  1 + 2 + 3 + 4 + 5))
                                {
                                    assertTrue( cnt[a1][a2][a3][a4][a5] > 0);
                                    if (cnt[a1][a2][a3][a4][a5] > max) max = cnt[a1][a2][a3][a4][a5];
                                    if (cnt[a1][a2][a3][a4][a5] < min) min = cnt[a1][a2][a3][a4][a5];
                                }
                                else
                                {
                                    assertEquals(0, cnt[a1][a2][a3][a4][a5]);
                                }

                            }

            System.out.println(min + " " + max);
            System.out.println((double) Math.abs(max - min) / t );
            assertTrue((double) Math.abs(max - min) / t < 0.01d);

        }
    }
}