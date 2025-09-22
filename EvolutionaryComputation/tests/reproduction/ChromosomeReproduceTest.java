package reproduction;

import criterion.Criteria;
import ea.EA;
import exception.PhaseException;
import org.junit.jupiter.api.Test;
import population.Chromosome;
import population.Parents;
import population.Specimen;
import population.SpecimensContainer;
import random.IRandom;
import random.L32_X64_MIX;
import utils.TestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ChromosomeReproduce}.
 *
 * @author MTomczyk
 */
class ChromosomeReproduceTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        ChromosomeReproduce chromosomeReproduce = new ChromosomeReproduce((c1, c2, R) -> new Chromosome(new int[]{1, 2, 3}));
        {
            EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                    Criteria.constructCriteria("C", 1, false));
            EA ea = new EA(pEA);
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
                TestUtils.compare("The number of offspring to construct = 2 for the Parents object being processed, " +
                        "but no dedicated reproducer is provided", () -> chromosomeReproduce.createOffspring(ea));
            }
            {
                ea.setSpecimensContainer(new SpecimensContainer());
                ea.getSpecimensContainer().setParents(new ArrayList<>());
                ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
                ArrayList<Specimen> off = null;
                String msg = null;
                try
                {
                    off = chromosomeReproduce.createOffspring(ea);
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
        ChromosomeReproduce chromosomeReproduce = ChromosomeReproduce.getInstanceTO((p1, p2, R) -> new Chromosome[]{
                new Chromosome(new int[]{0, 1, 2}),
                new Chromosome(new boolean[]{false, true})
        });
        EA.Params pEA = new EA.Params("EA", 0, new L32_X64_MIX(0), true,
                Criteria.constructCriteria("C", 1, false));
        EA ea = new EA(pEA);
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 1));
            TestUtils.compare("The number of offspring to construct = 1 for the Parents object being processed, " +
                    "but no dedicated reproducer is provided", () -> chromosomeReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 2));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = chromosomeReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(2, off.size());
            TestUtils.assertEquals(new int[]{0, 1, 2}, off.get(0).getIntDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, true}, off.get(1).getBooleanDecisionVector());
        }
    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        ChromosomeReproduce chromosomeReproduce = ChromosomeReproduce.getInstanceMO(new ChromosomeReproduce.IReproduceMO[]{
                new ChromosomeReproduce.IReproduceMO()
                {
                    @Override
                    public Chromosome[] reproduce(Chromosome p1, Chromosome p2, IRandom R)
                    {
                        return new Chromosome[]{
                                new Chromosome(new int[]{4, 5, 6}),
                                new Chromosome(new boolean[]{true, false}),
                                new Chromosome(new double[]{1.0d, 1.0d})
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
                    "but no dedicated reproducer is provided", () -> chromosomeReproduce.createOffspring(ea));
        }
        {
            ea.setSpecimensContainer(new SpecimensContainer());
            ea.getSpecimensContainer().setParents(new ArrayList<>());
            ea.getSpecimensContainer().getParents().add(new Parents(new Specimen(), new Specimen(), 3));
            ArrayList<Specimen> off = null;
            String msg = null;
            try
            {
                off = chromosomeReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(off);
            assertEquals(3, off.size());
            TestUtils.assertEquals(new int[]{4, 5, 6}, off.get(0).getIntDecisionVector());
            TestUtils.assertEquals(new boolean[]{true, false}, off.get(1).getBooleanDecisionVector());
            TestUtils.assertEquals(new double[]{1.0d, 1.0d}, off.get(2).getDoubleDecisionVector(), 1.0E-6);
        }
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        ChromosomeReproduce chromosomeReproduce = ChromosomeReproduce.getInstance(
                (c1, c2, R) -> new Chromosome(new boolean[]{false, true}),

                (p1, p2, R) -> new Chromosome[]{
                        new Chromosome(new int[]{1, 2, 3}),
                        new Chromosome(new double[]{5.0d, 5.0d})
                },
                new ChromosomeReproduce.IReproduceMO[]{
                        new ChromosomeReproduce.IReproduceMO()
                        {
                            @Override
                            public Chromosome[] reproduce(Chromosome p1, Chromosome p2, IRandom R)
                            {
                                return new Chromosome[]{
                                        new Chromosome(new boolean[]{true, true}),
                                        new Chromosome(new double[]{4.0d, 1.0d, 1.0d}),
                                        new Chromosome(new int[]{6, 6, 6, 6}),
                                        new Chromosome(new int[]{4, 4, 4})
                                };
                            }

                            @Override
                            public int getNoOffspring()
                            {
                                return 4;
                            }
                        }
                }
        );

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
                off = chromosomeReproduce.createOffspring(ea);
            } catch (PhaseException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
            assertNotNull(off);
            assertEquals(7, off.size());
            TestUtils.assertEquals(new boolean[]{true, true}, off.get(0).getBooleanDecisionVector());
            TestUtils.assertEquals(new double[]{4.0d, 1.0d, 1.0d}, off.get(1).getDoubleDecisionVector(), 1.0E-6);
            TestUtils.assertEquals(new int[]{6, 6, 6, 6}, off.get(2).getIntDecisionVector());
            TestUtils.assertEquals(new int[]{4, 4, 4}, off.get(3).getIntDecisionVector());
            TestUtils.assertEquals(new boolean[]{false, true}, off.get(4).getBooleanDecisionVector());
            TestUtils.assertEquals(new int[]{1, 2, 3}, off.get(5).getIntDecisionVector());
            TestUtils.assertEquals(new double[]{5.0d, 5.0d}, off.get(6).getDoubleDecisionVector(), 1.0E-6);
        }
    }
}