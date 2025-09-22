package reproduction;

import criterion.Criteria;
import ea.EA;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import population.Parents;
import population.Specimen;
import population.SpecimensContainer;
import random.IRandom;
import random.L32_X64_MIX;
import utils.TestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link IntReproduce}.
 *
 * @author MTomczyk
 */
class IntReproduceTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        IntReproduce intReproduce = new IntReproduce((p1, p2, R) -> new int[]{1, 2, 3});
        {
            EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                    Criteria.constructCriteria("C", 1, false));
            EA ea = new EA(pEA);
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
                TestUtils.compare("The number of offspring to construct = 2 for the Parents object being processed, " +
                        "but no dedicated reproducer is provided", () -> intReproduce.createOffspring(ea));
            }
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
                ArrayList<Specimen> off = null;
                String msg = null;
                try
                {
                    off = intReproduce.createOffspring(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(off);
                assertEquals(1, off.size());
                TestUtils.assertEquals(new int[]{1, 2, 3}, off.get(0).getIntDecisionVector());
            }
        }

    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        IntReproduce intReproduce = IntReproduce.getInstanceTO((p1, p2, R) -> new int[][]{
                new int[]{0, 0, 0},
                new int[]{1, 1, 1},
        });
        EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                Criteria.constructCriteria("C", 1, false));
        EA ea = new EA(pEA);
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
            TestUtils.compare("The number of offspring to construct = 1 for the Parents object being processed, " +
                    "but no dedicated reproducer is provided", () -> intReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = intReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(2, off.size());
            TestUtils.assertEquals(new int[]{0, 0, 0}, off.get(0).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{1, 1, 1}, off.get(1).getIntDecisionVector());
        }
    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        IntReproduce intReproduce = IntReproduce.getInstanceMO(new IntReproduce.IReproduceMO[]{
                new IntReproduce.IReproduceMO()
                {
                    @Override
                    public int[][] reproduce(int[] p1, int[] p2, IRandom R)
                    {
                        return new int[][]{
                                new int[]{1, 0, 0},
                                new int[]{0, 1, 0},
                                new int[]{0, 0, 1}
                        };
                    }

                    @Override
                    public int getNoOffspring()
                    {
                        return 3;
                    }
                }
        });

        EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                Criteria.constructCriteria("C", 1, false));
        EA ea = new EA(pEA);
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
            TestUtils.compare("The number of offspring to construct = 2 for the Parents object being processed, " +
                    "but no dedicated reproducer is provided", () -> intReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 3));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = intReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(3, off.size());
            TestUtils.assertEquals(new int[]{1, 0, 0}, off.get(0).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 1, 0}, off.get(1).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 0, 1}, off.get(2).getIntDecisionVector());
        }
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        IntReproduce intReproduce = IntReproduce.getInstance((p1, p2, R) -> new int[]{0, 1},
                (p1, p2, R) -> new int[][]{
                        {0, 0, 0},
                        {1, 1, 1}
                }, new IntReproduce.IReproduceMO[]{
                        new IntReproduce.IReproduceMO()
                        {
                            @Override
                            public int[][] reproduce(int[] p1, int[] p2, IRandom R)
                            {
                                return new int[][]{
                                        {1, 0, 0, 0},
                                        {0, 1, 0, 0},
                                        {0, 0, 1, 0},
                                        {0, 0, 0, 1}
                                };
                            }

                            @Override
                            public int getNoOffspring()
                            {
                                return 4;
                            }
                        }
                });

        EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                Criteria.constructCriteria("C", 1, false));
        EA ea = new EA(pEA);
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 4));
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = intReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(7, off.size());
            TestUtils.assertEquals(new int[]{1, 0, 0, 0}, off.get(0).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 1, 0, 0}, off.get(1).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 0, 1, 0}, off.get(2).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 0, 0, 1}, off.get(3).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 1}, off.get(4).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{0, 0, 0}, off.get(5).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{1, 1, 1}, off.get(6).getIntDecisionVector());
        }
    }
}