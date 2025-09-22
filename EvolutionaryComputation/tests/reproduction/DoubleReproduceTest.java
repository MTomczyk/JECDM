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
 * Provides various tests for {@link DoubleReproduce}.
 *
 * @author MTomczyk
 */
class DoubleReproduceTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        DoubleReproduce doubleReproduce = new DoubleReproduce((p1, p2, R) -> new double[]{1.0d, 2.0d, 3.0d});
        {
            EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                    Criteria.constructCriteria("C", 1, false));
            EA ea = new EA(pEA);
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
                TestUtils.compare("The number of offspring to construct = 2 for the Parents object being processed, " +
                        "but no dedicated reproducer is provided", () -> doubleReproduce.createOffspring(ea));
            }
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
                ArrayList<Specimen> off = null;
                String msg = null;
                try
                {
                    off = doubleReproduce.createOffspring(ea);
                } catch (PhaseException e)
                {
                    msg = e.getMessage();
                }
                assertNull(msg);
                assertNotNull(off);
                assertEquals(1, off.size());
                TestUtils.assertEquals(new double[]{1.0d, 2.0d, 3.0d}, off.get(0).getDoubleDecisionVector(), 1.0E-6);
            }
        }

    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        DoubleReproduce doubleReproduce = DoubleReproduce.getInstanceTO((p1, p2, R) -> new double[][]{
                new double[]{0.0d, 0.0d, 0.0d},
                new double[]{1.0d, 1.0d, 1.0d},
        });
        EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                Criteria.constructCriteria("C", 1, false));
        EA ea = new EA(pEA);
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
            TestUtils.compare("The number of offspring to construct = 1 for the Parents object being processed, " +
                    "but no dedicated reproducer is provided", () -> doubleReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = doubleReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(2, off.size());
            TestUtils.assertEquals(new double[]{0.0d, 0.0d, 0.0d}, off.get(0).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{1.0d, 1.0d, 1.0d}, off.get(1).getDoubleDecisionVector(), 1.0E-6);
        }
    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        DoubleReproduce doubleReproduce = DoubleReproduce.getInstanceMO(new DoubleReproduce.IReproduceMO[]{
                new DoubleReproduce.IReproduceMO()
                {
                    @Override
                    public double[][] reproduce(double[] p1, double[] p2, IRandom R)
                    {
                        return new double[][]{
                                new double[]{1.0d, 0.0d, 0.0d},
                                new double[]{0.0d, 1.0d, 0.0d},
                                new double[]{0.0d, 0.0d, 1.0d}
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
                    "but no dedicated reproducer is provided", () -> doubleReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 3));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = doubleReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(3, off.size());
            TestUtils.assertEquals(new double[]{1.0d, 0.0d, 0.0d}, off.get(0).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 1.0d, 0.0d}, off.get(1).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 0.0d, 1.0d}, off.get(2).getDoubleDecisionVector(), 1.0E-6);
        }
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        DoubleReproduce doubleReproduce = DoubleReproduce.getInstance((p1, p2, R) -> new double[]{0.0d, 1.0d},
                (p1, p2, R) -> new double[][]{
                        {0.0d, 0.0d, 0.0d},
                        {1.0d, 1.0d, 1.0d}
                }, new DoubleReproduce.IReproduceMO[]{
                        new DoubleReproduce.IReproduceMO()
                        {
                            @Override
                            public double[][] reproduce(double[] p1, double[] p2, IRandom R)
                            {
                                return new double[][]{
                                        {1.0d, 0.0d, 0.0d, 0.0d},
                                        {0.0d, 1.0d, 0.0d, 0.0d},
                                        {0.0d, 0.0d, 1.0d, 0.0d},
                                        {0.0d, 0.0d, 0.0d, 1.0d}
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
                off = doubleReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(7, off.size());
            TestUtils.assertEquals(new double[]{1.0d, 0.0d, 0.0d, 0.0d}, off.get(0).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 1.0d, 0.0d, 0.0d}, off.get(1).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 0.0d, 1.0d, 0.0d}, off.get(2).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 0.0d, 0.0d, 1.0d}, off.get(3).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 1.0d}, off.get(4).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{0.0d, 0.0d, 0.0d}, off.get(5).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new double[]{1.0d, 1.0d, 1.0d}, off.get(6).getDoubleDecisionVector(), 1.0E-6);
        }
    }
}