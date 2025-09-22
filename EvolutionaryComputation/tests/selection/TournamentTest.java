package selection;

import alternative.Alternative;
import ea.EA;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import population.Parents;
import population.Specimen;
import population.SpecimenID;
import population.SpecimensContainer;
import print.PrintUtils;
import random.IRandom;
import random.L32_X64_MIX;
import random.MersenneTwister64;
import reproduction.ReproductionStrategy;
import statistics.Statistics;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

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
        EA.Params pEA = new EA.Params("", null);
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
            Tournament TS = new Tournament(TP);

            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = null;
                String msg = null;
                try
                {
                    parents = TS.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(parents);
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
            Tournament TS = new Tournament(TP);


            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = null;
                String msg = null;
                try
                {
                    parents = TS.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(parents);
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
            Tournament TS = new Tournament(TP);


            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = null;
                String msg = null;
                try
                {
                    parents = TS.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(parents);
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
            Tournament TS = new Tournament(TP);


            int[][] cnt = new int[5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = null;
                String msg = null;
                try
                {
                    parents = TS.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(parents);
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
            Tournament TS = new Tournament(TP);


            int[][][][][] cnt = new int[5][5][5][5][5];

            int t = 1000000;
            for (int i = 0; i < t; i++)
            {
                ArrayList<Parents> parents = null;
                String msg = null;
                try
                {
                    parents = TS.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(parents);
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

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;


            for (int a1 = 0; a1 < 5; a1++)
                for (int a2 = 0; a2 < 5; a2++)
                    for (int a3 = 0; a3 < 5; a3++)
                        for (int a4 = 0; a4 < 5; a4++)
                            for (int a5 = 0; a5 < 5; a5++)
                            {
                                //noinspection PointlessArithmeticExpression
                                if (((1 + a1) * (1 + a2) * (1 + a3) * (1 + a4) * (1 + a5) == 1 * 2 * 3 * 4 * 5) &&
                                        ((1 + a1) + (1 + a2) + (1 + a3) + (1 + a4) + (1 + a5) == 1 + 2 + 3 + 4 + 5))
                                {
                                    assertTrue(cnt[a1][a2][a3][a4][a5] > 0);
                                    if (cnt[a1][a2][a3][a4][a5] > max) max = cnt[a1][a2][a3][a4][a5];
                                    if (cnt[a1][a2][a3][a4][a5] < min) min = cnt[a1][a2][a3][a4][a5];
                                } else
                                {
                                    assertEquals(0, cnt[a1][a2][a3][a4][a5]);
                                }

                            }

            System.out.println(min + " " + max);
            System.out.println((double) Math.abs(max - min) / t);
            assertTrue((double) Math.abs(max - min) / t < 0.01d);

        }
    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        Tournament.Params TP = new Tournament.Params();
        TP._noParentsPerOffspring = 2;
        TP._withReplacement = true;
        TP._size = 2;
        TP._useSampling = false;
        TP._comparator = (A, B) -> A.getName().equals("B") ? 1 : 0;
        Tournament TS = new Tournament(TP);

        EA.Params pEA = new EA.Params("", null);
        pEA._populationSize = 100;
        pEA._offspringSize = 100;
        pEA._R = new L32_X64_MIX(0);
        pEA._name = "genetic";
        pEA._id = 0;
        EA ea = new EA(pEA);

        int[] hist = new int[2];

        for (int i = 0; i < 100; i++)
        {
            ArrayList<Specimen> specimen = new ArrayList<>(2);
            specimen.add(new Specimen(new SpecimenID(ea.getID(), i, 0, 0)));
            specimen.add(new Specimen(new SpecimenID(ea.getID(), i, 0, 1)));
            specimen.get(0).setAlternative(new Alternative("A", 2));
            specimen.get(1).setAlternative(new Alternative("B", 2));
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setMatingPool(specimen);
            ArrayList<Parents> parents = null;
            String msg = null;
            try
            {
                parents = TS.selectParents(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(parents);
            assertEquals(pEA._offspringSize, parents.size());
            for (Parents pa : parents)
            {
                assertEquals(2, pa._parents.size());
                if (pa._parents.get(0).getName().equals("A")) hist[0]++;
                if (pa._parents.get(1).getName().equals("A")) hist[0]++;
                if (pa._parents.get(0).getName().equals("B")) hist[1]++;
                if (pa._parents.get(1).getName().equals("B")) hist[1]++;
            }
        }

        PrintUtils.printVectorOfIntegers(hist);
        int s = hist[0] + hist[1];
        assertEquals(20000, hist[0] + hist[1]);
        assertEquals((double) hist[0] / s, 0.25d, 1.0E-2);
        assertEquals((double) hist[1] / s, 0.75d, 1.0E-2);
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        ReproductionStrategy[] RS = new ReproductionStrategy[8];
        RS[1] = ReproductionStrategy.getDefaultStrategy();
        RS[2] = new ReproductionStrategy(new ReproductionStrategy.Params(2));
        RS[3] = new ReproductionStrategy(new ReproductionStrategy.Params(2));
        RS[4] = new ReproductionStrategy(new ReproductionStrategy.Params(3));
        RS[5] = new ReproductionStrategy(new ReproductionStrategy.Params(3));
        RS[6] = ReproductionStrategy.getDynamicStrategy((ea, counter, noExpectedOffspringGenerated) ->
                1 + ea.getR().nextInt(3));
        {
            ReproductionStrategy.Params pRS = new ReproductionStrategy.Params();
            pRS._enableOffspringThresholding = false;
            pRS._isReproductionStrategyConstant = false;
            pRS._noOffspringFromParentsGenerator = (ea, counter, noExpectedOffspringGenerated) -> {
                if (counter == 0) return 5;
                if (counter == 1) return 2;
                if (counter == 2) return 2;
                return 5;
            };
            RS[7] = new ReproductionStrategy(pRS);
        }


        int[] os = new int[]{5, 5, 5, 6, 8, 12, 10, 10};
        String[] msgs = new String[]{
                null,
                null,
                "It is expected to generate 2 offspring solutions from one Parents object, but it is not a " +
                        "divisor of the expected total offspring size of 5 (reproduction limit = 2147483647; capped to 5)," +
                        " and offspring thresholding is set to disabled.",
                null,
                "It is expected to generate 3 offspring solutions from one Parents object, but it is not a " +
                        "divisor of the expected total offspring size of 8 (reproduction limit = 2147483647; capped to 8)," +
                        " and offspring thresholding is set to disabled.",
                null,
                null,
                "The dynamic generation of the expected number of offspring to produce indicates the number of 5. When " +
                        "added to the already generated numbers (9), it would exceed the expected total offspring size " +
                        "of 10 (the offspring thresholding is set to disabled; the true offspring size is 10;" +
                        " reproduction limit = 2147483647; capped to 10)."
        };
        Integer[] ps = new Integer[]{5, 5, null, 3, null, 4, null, null};
        Integer[] total = new Integer[]{100000, 100000, null, 60000, null, 80000, null, null};

        for (int i = 0; i < RS.length; i++)
        {
            IRandom R = new MersenneTwister64(System.currentTimeMillis());
            EA.Params pEA = new EA.Params("", null);
            pEA._populationSize = 3;
            pEA._offspringSize = os[i];
            pEA._R = R;
            pEA._id = 0;
            pEA._reproductionStrategy = RS[i];
            EA ea = new EA(pEA);
            int T = 10000;
            ISelect S = new Tournament(1); // 1 makes it entirely random
            ArrayList<Specimen> specimen = new ArrayList<>();
            specimen.add(new Specimen(new SpecimenID(0, 0, 0, 0)));
            specimen.add(new Specimen(new SpecimenID(0, 0, 0, 1)));
            specimen.add(new Specimen(new SpecimenID(0, 0, 0, 2)));
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setMatingPool(specimen);
            int[] hist = new int[3];

            String msg = null;
            for (int t = 0; t < T; t++)
            {
                ArrayList<Parents> parents = null;
                try
                {
                    parents = S.selectParents(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }

                assertEquals(msgs[i], msg);

                if (msg == null)
                {
                    assertNotNull(parents);
                    if (ps[i] != null) assertEquals(ps[i], parents.size());
                    int ts = 0;
                    for (Parents p : parents)
                    {
                        assertNotNull(p._parents);
                        assertEquals(2, p._parents.size());
                        for (Specimen s : p._parents)
                            hist[s.getID()._no]++;

                        if (RS[i] == null) assertEquals(1, p._noOffspringToConstruct);
                        else if (RS[i].isReproductionStrategyConstant())
                            assertEquals(RS[i].getConstantNoOffspringFromParents(), p._noOffspringToConstruct);

                        ts += p._noOffspringToConstruct;
                    }
                    assertEquals(pEA._offspringSize, ts);
                } else break;
            }

            if (msg == null)
            {
                if ((RS[i] != null) && (RS[i].isReproductionStrategyConstant()))
                {
                    assertNotNull(total);
                    assertEquals(total[i], hist[0] + hist[1] + hist[2]);
                    assertEquals(1.0d / 3.0d, (double) hist[0] / total[i], 1.0E-2);
                    assertEquals(1.0d / 3.0d, (double) hist[1] / total[i], 1.0E-2);
                    assertEquals(1.0d / 3.0d, (double) hist[2] / total[i], 1.0E-2);
                }
            }
        }
    }
}