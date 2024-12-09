package statistics.distribution;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for {@link DiscreteDistribution} class.
 *
 * @author MTomczyk
 */
class DiscreteDistributionTest
{

    /**
     * Test 1.
     */
    @Test
    void add()
    {
        DiscreteDistribution D = new DiscreteDistribution();
        D.init(10, 10.0d, 20.0d);
        D.add(10.0d);
        D.add(20.0d);
        D.add(11.0d);
        D.add(10.5d);
        D.add(19.5d);
        D.add(19.0d);
        {
            int [] d = D.getDistribution();
            int [] exp = new int[]{2, 1, 0, 0, 0, 0, 0, 0, 0, 3};
            for (int i = 0; i < 10; i++) assertEquals(d[i], exp[i]);
        }
        {
            double [] d = D.getNormalizedDistribution();
            double [] exp = new double[]{2/6.0d, 1/6.0d, 0, 0, 0, 0, 0, 0, 0, 3/6.0d};
            for (int i = 0; i < 10; i++) assertEquals(d[i], exp[i], 0.00001d);
        }
    }
}