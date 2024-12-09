package interaction.reference.constructor;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import exeption.RefinerException;
import interaction.Status;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.Result;
import interaction.reference.validator.IValidator;
import interaction.reference.validator.RequiredSpread;
import interaction.refine.Refiner;
import org.junit.jupiter.api.Test;
import print.PrintUtils;
import random.IRandom;
import random.MersenneTwister64;
import system.dm.DM;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link RandomPairs}.
 *
 * @author MTomczyk
 */
class RandomPairsTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        RandomPairs RP = new RandomPairs();
        Criteria C = Criteria.constructCriteria("C", 2, false);
        DMContext dmContext = new DMContext(C, null,
                new Alternatives(Alternative.getAlternativeArray("A", new double[][]{{}, {}})), null, false, 0);
        String msg = null;
        try
        {
            RP.constructReferenceSets(dmContext, dmContext.getCurrentAlternativesSuperset());
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertEquals("The random number generator is not supplied by the decision-making context", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void test2()
    {
        Refiner refiner = Refiner.getDefault(0.001d);
        ReferenceSetsConstructor RSC = ReferenceSetsConstructor.getDefault(1.0d);
        assertNotNull(refiner);
        assertNotNull(RSC);

        Criteria C = Criteria.constructCriteria("C", 2, false);
        double[][] evals = new double[][]
                {
                        {0.0d, 7.0d},
                        {1.0d, 4.0d},
                        {6.0d, 4.0d},
                        {3.0d, 2.0d},
                        {3.5d, 1.5d},
                        {7.0d, 1.0d},
                        {10.0d, 0.0d},
                        {7.0d, 1.0d},
                };
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);


        int trials = 1000000;
        int[][] selected = new int[8][8];

        boolean[][] expectedMask = new boolean[][]
                {
                        {false, true, false, true, true, true, true, false},
                        {true, false, false, true, true, true, true, false},
                        {false, false, false, false, false, false, false, false},
                        {true, true, false, false, false, true, true, false},
                        {true, true, false, false, false, true, true, false},
                        {true, true, false, true, true, false, true, false},
                        {true, true, false, true, true, true, false, false},
                        {false, false, false, false, false, false, false, false}
                };

        int sum = 0;
        for (boolean[] row : expectedMask)
            for (boolean c : row)
                if (c) sum++;
        double expected = 1.0d / sum;

        IRandom R = new MersenneTwister64(0);

        for (int t = 0; t < trials; t++)
        {
            String msg = null;
            DMContext dmContext = new DMContext(C, null, new Alternatives(alternatives), null, false, 0, null, R);
            Result result = null;
            try
            {
                interaction.refine.Result r = refiner.refine(dmContext);
                result = RSC.constructReferenceSets(dmContext, null, r);
            } catch (ReferenceSetsConstructorException | RefinerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(result);
            assertEquals(1, result._referenceSetsContainer.getNoSets());
            assertEquals(1, result._referenceSetsContainer.getCommonReferenceSets().getNoSets());
            assertTrue(result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().containsKey(2));
            assertEquals(1, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).size());
            assertEquals(2, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).get(0).getSize());
            ReferenceSet rs = result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).get(0);
            int idx1 = Integer.parseInt(rs.getAlternatives().get(0).getName().substring(1, 2));
            int idx2 = Integer.parseInt(rs.getAlternatives().get(1).getName().substring(1, 2));
            selected[idx1][idx2]++;
        }

        PrintUtils.print2dIntegers(selected);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!expectedMask[i][j])
                    assertEquals(0, selected[i][j]);
                else
                {
                    double prop = (double) selected[i][j] / trials;
                    assertEquals(expected, prop, 0.01d);
                }
            }
        }
    }

    /**
     * Test 3.
     */
    @Test
    void test3()
    {
        IRandom R = new MersenneTwister64(0);

        Refiner refiner = Refiner.getDefault(0.001d);
        ReferenceSetsConstructor RSC = ReferenceSetsConstructor.getDefault(0.1d);
        assertNotNull(refiner);
        assertNotNull(RSC);


        Criteria C = Criteria.constructCriteria("C", 2, false);
        double[][] evals = new double[][]
                {
                        {0.0d, 7.0d},
                        {1.0d, 4.0d},
                        {6.0d, 4.0d},
                        {3.0d, 2.0d},
                        {3.5d, 1.5d},
                        {7.0d, 1.0d},
                        {10.0d, 0.0d},
                        {7.0d, 1.0d},
                };
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);


        int trials = 1000000;
        int[][] selected = new int[8][8];

        boolean[][] expectedMask = new boolean[][]
                {
                        {false, true, false, true, true, true, true, false},
                        {true, false, false, true, true, true, true, false},
                        {false, false, false, false, false, false, false, false},
                        {true, true, false, false, true, true, true, false},
                        {true, true, false, true, false, true, true, false},
                        {true, true, false, true, true, false, true, false},
                        {true, true, false, true, true, true, false, false},
                        {false, false, false, false, false, false, false, false}
                };

        int sum = 0;
        for (boolean[] row : expectedMask)
            for (boolean c : row)
                if (c) sum++;
        double expected = 1.0d / sum;

        for (int t = 0; t < trials; t++)
        {
            String msg = null;
            DMContext dmContext = new DMContext(C, null, new Alternatives(alternatives), null, false, 0, null, R);
            Result result = null;
            try
            {
                interaction.refine.Result r = refiner.refine(dmContext);
                result = RSC.constructReferenceSets(dmContext, null, r);
            } catch (ReferenceSetsConstructorException | RefinerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(result);
            assertEquals(1, result._referenceSetsContainer.getCommonReferenceSets().getNoSets());
            assertTrue(result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().containsKey(2));
            assertEquals(1, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).size());
            assertEquals(2, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).get(0).getSize());
            ReferenceSet rs = result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).get(0);
            int idx1 = Integer.parseInt(rs.getAlternatives().get(0).getName().substring(1, 2));
            int idx2 = Integer.parseInt(rs.getAlternatives().get(1).getName().substring(1, 2));
            selected[idx1][idx2]++;
        }

        PrintUtils.print2dIntegers(selected);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!expectedMask[i][j])
                    assertEquals(0, selected[i][j]);
                else
                {
                    double prop = (double) selected[i][j] / trials;
                    assertEquals(expected, prop, 0.01d);
                }
            }
        }
    }


    /**
     * Test 4.
     */
    @Test
    void tes4()
    {
        IRandom R = new MersenneTwister64(0);

        Refiner refiner = Refiner.getDefault(0.001d);
        ReferenceSetsConstructor.Params pRSC = ReferenceSetsConstructor.Params.getDefault(1.1d);
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new RandomPairs(new IValidator[]{new RequiredSpread(1.1d)}, 1, 0));
        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(pRSC);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(refiner);
        assertNotNull(RSC);


        Criteria C = Criteria.constructCriteria("C", 2, false);
        double[][] evals = new double[][]
                {
                        {0.0d, 5.0d},
                        {1.0d, 4.0d},
                        {6.0d, 4.0d},
                        {3.0d, 2.0d},
                        {3.5d, 1.5d},
                        {7.0d, 1.0d},
                        {8.0d, 0.0d},
                        {7.0d, 1.0d},
                };
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);


        int trials = 1000000;
        int[][] selected = new int[8][8];

        boolean[][] expectedMask = new boolean[][]
                {
                        {false, false, false, true, true, true, true, false},
                        {false, false, false, true, true, true, true, false},
                        {false, false, false, false, false, false, false, false},
                        {true, true, false, false, false, true, true, false},
                        {true, true, false, false, false, true, true, false},
                        {true, true, false, true, true, false, false, false},
                        {true, true, false, true, true, false, false, false},
                        {false, false, false, false, false, false, false, false}
                };

        int[][] prop = new int[][]
                {
                        {0, 0, 0, 3, 1, 1, 1, 0},
                        {0, 0, 0, 3, 1, 1, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 0, 3, 1, 0},
                        {1, 1, 0, 0, 0, 3, 1, 0},
                        {3, 1, 0, 1, 1, 0, 0, 0},
                        {3, 1, 0, 1, 1, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0}
                };
        int sum = 0;
        for (int[] row : prop)
            for (int c : row) sum += c;

        for (int t = 0; t < trials; t++)
        {

            DMContext dmContext = new DMContext(C, null, new Alternatives(alternatives),
                    null, false, 0, null, R);
            Result result = null;
            try
            {
                interaction.refine.Result r = refiner.refine(dmContext);
                result = RSC.constructReferenceSets(dmContext, null, r);
            } catch (ReferenceSetsConstructorException | RefinerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(result);
            assertEquals(1, result._referenceSetsContainer.getCommonReferenceSets().getNoSets());
            assertTrue(result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().containsKey(2));
            assertEquals(1, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).size());
            assertEquals(2, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).get(0).getSize());
            ReferenceSet rs = result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).get(0);
            int idx1 = Integer.parseInt(rs.getAlternatives().get(0).getName().substring(1, 2));
            int idx2 = Integer.parseInt(rs.getAlternatives().get(1).getName().substring(1, 2));
            selected[idx1][idx2]++;
        }

        PrintUtils.print2dIntegers(selected);

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (!expectedMask[i][j])
                    assertEquals(0, selected[i][j]);
                else
                {
                    double exp = (double) prop[i][j] / sum;
                    double act = (double) selected[i][j] / trials;
                    assertEquals(exp, act, 0.01d);
                }
            }
        }
    }


    /**
     * Test 5.
     */
    @Test
    void test5()
    {
        IRandom R = new MersenneTwister64(0);

        Refiner refiner = Refiner.getDefault(0.001d);
        ReferenceSetsConstructor.Params pRSC = ReferenceSetsConstructor.Params.getDefault(100.0d);
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new RandomPairs(new IValidator[]{new RequiredSpread(100.0d)}, 1, 10));
        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(pRSC);
        String msg = null;
        try
        {
            RSC.validate(new DM[0]);
        } catch (ReferenceSetsConstructorException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(refiner);
        assertNotNull(RSC);

        Criteria C = Criteria.constructCriteria("C", 2, false);
        double[][] evals = new double[][]
                {
                        {0.0d, 5.0d},
                        {1.0d, 4.0d},
                        {6.0d, 4.0d},
                        {3.0d, 2.0d},
                        {3.5d, 1.5d},
                        {7.0d, 1.0d},
                        {8.0d, 0.0d},
                        {7.0d, 1.0d},
                };
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A", evals);


        int trials = 1000000;

        for (int t = 0; t < trials; t++)
        {

            DMContext dmContext = new DMContext(C, null, new Alternatives(alternatives), null, false, 0, null, R);
            Result result = null;
            try
            {
                interaction.refine.Result r = refiner.refine(dmContext);
                result = RSC.constructReferenceSets(dmContext, null, r);
            } catch (ReferenceSetsConstructorException | RefinerException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(result);
            assertEquals(0, result._referenceSetsContainer.getCommonReferenceSets().getNoSets());
            assertEquals(Status.PROCESS_ENDED_BUT_NO_REFERENCE_SETS_WERE_CONSTRUCTED, result._status);
            assertEquals(0, result._referenceSetsContainer.getCommonReferenceSets().getUniqueSizes().length);
            assertEquals(0, result._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().size());
            assertTrue(result._processingTime >= 0);
        }

    }

}