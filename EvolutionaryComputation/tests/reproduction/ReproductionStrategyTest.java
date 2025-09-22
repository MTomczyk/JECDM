package reproduction;

import ea.EA;
import org.junit.jupiter.api.Test;
import random.L32_X64_MIX;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ReproductionStrategy}.
 *
 * @author MTomczyk
 */
class ReproductionStrategyTest
{
    /**
     * Tests {@link ReproductionStrategy#isOneOffspringFromOneParents()}
     */
    @Test
    void isOneOffspringFromOneParents()
    {
        assertTrue(new ReproductionStrategy(-1).isOneOffspringFromOneParents());
        assertTrue(new ReproductionStrategy(0).isOneOffspringFromOneParents());
        assertTrue(new ReproductionStrategy(1).isOneOffspringFromOneParents());
        assertFalse(new ReproductionStrategy(2).isOneOffspringFromOneParents());
        assertFalse(new ReproductionStrategy(3).isOneOffspringFromOneParents());
        assertTrue(new ReproductionStrategy(0).isReproductionStrategyConstant());
        assertFalse(new ReproductionStrategy(1).isOffspringThresholdingEnabled());
    }

    /**
     * Tests {@link ReproductionStrategy#areTwoOffspringFromParents()}}
     */
    @Test
    void isTwoOffspringFromParents()
    {
        assertFalse(new ReproductionStrategy(-1).areTwoOffspringFromParents());
        assertFalse(new ReproductionStrategy(0).areTwoOffspringFromParents());
        assertFalse(new ReproductionStrategy(1).areTwoOffspringFromParents());
        assertTrue(new ReproductionStrategy(2).areTwoOffspringFromParents());
        assertFalse(new ReproductionStrategy(3).areTwoOffspringFromParents());
        assertTrue(new ReproductionStrategy(0).isReproductionStrategyConstant());
        assertFalse(new ReproductionStrategy(1).isOffspringThresholdingEnabled());
    }

    /**
     * Tests {@link ReproductionStrategy#isOffspringThresholdingEnabled()}
     */
    @Test
    void isOffspringThresholdingEnabled()
    {
        for (boolean f : new boolean[]{false, true})
        {
            ReproductionStrategy.Params p = new ReproductionStrategy.Params();
            p._enableOffspringThresholding = f;
            ReproductionStrategy RS = new ReproductionStrategy(p);
            assertEquals(f, RS.isOffspringThresholdingEnabled());
        }
    }


    /**
     * Performs various tests.
     */
    @Test
    void test()
    {
        {
            assertEquals(10, new ReproductionStrategy(10).getConstantNoOffspringFromParents());
            assertEquals(5, new ReproductionStrategy(5).getConstantNoOffspringFromParents());
            assertEquals(1, new ReproductionStrategy(-5).getConstantNoOffspringFromParents());
        }
        {
            ReproductionStrategy.Params p = new ReproductionStrategy.Params();
            p._isReproductionStrategyConstant = false;
            p._noOffspringFromParentsGenerator = null;
            ReproductionStrategy RS = new ReproductionStrategy(p);
            assertNull(RS.getNoOffspringFromParentsGenerator());
            assertTrue(RS.isReproductionStrategyConstant());
        }
        {
            ReproductionStrategy.Params p = new ReproductionStrategy.Params();
            p._isReproductionStrategyConstant = true;
            p._noOffspringFromParentsGenerator = (ea, counter, noOffspringGenerated) -> 0;
            ReproductionStrategy RS = new ReproductionStrategy(p);
            assertNotNull(RS.getNoOffspringFromParentsGenerator());
            assertTrue(RS.isReproductionStrategyConstant());
        }
        {
            ReproductionStrategy.Params p = new ReproductionStrategy.Params();
            p._isReproductionStrategyConstant = false;
            p._noOffspringFromParentsGenerator = (ea, counter, noOffspringGenerated) -> 0;
            ReproductionStrategy RS = new ReproductionStrategy(p);
            assertNotNull(RS.getNoOffspringFromParentsGenerator());
            assertFalse(RS.isReproductionStrategyConstant());
        }
        {
            ReproductionStrategy RS = ReproductionStrategy.getDynamicStrategy((ea, counter, noExpectedOffspringGenerated) -> 1);
            assertFalse(RS.isReproductionStrategyConstant());
            assertTrue(RS.isOffspringThresholdingEnabled());
            assertNotNull(RS.getNoOffspringFromParentsGenerator());
        }
        {
            int[] hist = new int[2];
            int T = 10000;
            EA.Params pEA = new EA.Params("", null);
            pEA._populationSize = 100;
            pEA._offspringSize = 100;
            pEA._R = new L32_X64_MIX(0);
            pEA._name = "genetic";
            pEA._id = 0;
            EA ea = new EA(pEA);
            ReproductionStrategy.Params p = new ReproductionStrategy.Params();
            p._isReproductionStrategyConstant = false;
            p._noOffspringFromParentsGenerator =
                    (ea1, counter, noOffspringGenerated) -> ea1.getR().nextBoolean() ? 1 : 2;
            ReproductionStrategy RS = new ReproductionStrategy(p);
            assertNotNull(RS.getNoOffspringFromParentsGenerator());
            assertFalse(RS.isReproductionStrategyConstant());

            int sum = 0;
            for (int t = 0; t < T; t++)
            {
                int no = RS.getNoOffspringFromParentsGenerator().getNoOffspringPerParents(ea, t, sum);
                sum += no;
                assertTrue(no > 0);
                assertTrue(no < 3);
                hist[no - 1]++;
            }
            assertEquals(T, hist[0] + hist[1]);
            assertEquals(0.5d, (double) hist[0] / T, 1.0E-1);
            assertEquals(0.5d, (double) hist[1] / T, 1.0E-1);
        }
    }
}