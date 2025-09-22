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
 * Provides various tests for {@link BoolReproduce}.
 *
 * @author MTomczyk
 */
class BoolReproduceTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        BoolReproduce boolReproduce = new BoolReproduce((p1, p2, R) -> new boolean[]{true, false, true});
        {
            EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                    Criteria.constructCriteria("C", 1, false));
            EA ea = new EA(pEA);
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
                TestUtils.compare("The number of offspring to construct = 2 for the Parents object being processed, " +
                        "but no dedicated reproducer is provided", () -> boolReproduce.createOffspring(ea));
            }
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
                ArrayList<Specimen> off = null;
                String msg = null;
                try
                {
                    off = boolReproduce.createOffspring(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(off);
                assertEquals(1, off.size());
                TestUtils.assertEquals(new boolean[]{true, false, true}, off.get(0).getBooleanDecisionVector());
            }
        }

    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        BoolReproduce boolReproduce = BoolReproduce.getInstanceTO((p1, p2, R) -> new boolean[][]{
                new boolean[]{true, false, false},
                new boolean[]{false, true, true},
        });
        EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                Criteria.constructCriteria("C", 1, false));
        EA ea = new EA(pEA);
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
            TestUtils.compare("The number of offspring to construct = 1 for the Parents object being processed, " +
                    "but no dedicated reproducer is provided", () -> boolReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = boolReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(2, off.size());
            TestUtils.assertEquals(new boolean[]{true, false, false}, off.get(0).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, true, true}, off.get(1).getBooleanDecisionVector());
        }
    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        BoolReproduce boolReproduce = BoolReproduce.getInstanceMO(new BoolReproduce.IReproduceMO[]{
                new BoolReproduce.IReproduceMO()
                {
                    @Override
                    public boolean[][] reproduce(boolean[] p1, boolean[] p2, IRandom R)
                    {
                        return new boolean[][]{
                                new boolean[]{true, false, false},
                                new boolean[]{false, true, false},
                                new boolean[]{false, false, true}
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
                    "but no dedicated reproducer is provided", () -> boolReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 3));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = boolReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(3, off.size());
            TestUtils.assertEquals(new boolean[]{true, false, false}, off.get(0).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, true, false}, off.get(1).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, false, true}, off.get(2).getBooleanDecisionVector());
        }
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        BoolReproduce boolReproduce = BoolReproduce.getInstance((p1, p2, R) -> new boolean[]{true, true},
                (p1, p2, R) -> new boolean[][]{
                        {false, false, false},
                        {true, true, true}
                }, new BoolReproduce.IReproduceMO[]{
                        new BoolReproduce.IReproduceMO()
                        {
                            @Override
                            public boolean[][] reproduce(boolean[] p1, boolean[] p2, IRandom R)
                            {
                                return new boolean[][]{
                                        {true, false, false, false},
                                        {false, true, false, false},
                                        {false, false, true, false},
                                        {false, false, false, true}
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
                off = boolReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(7, off.size());
            TestUtils.assertEquals(new boolean[]{true, false, false, false}, off.get(0).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, true, false, false}, off.get(1).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, false, true, false}, off.get(2).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, false, false, true}, off.get(3).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{true, true}, off.get(4).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, false, false}, off.get(5).getBooleanDecisionVector());
            TestUtils.assertEquals(new boolean[]{true, true, true}, off.get(6).getBooleanDecisionVector());
        }
    }
}