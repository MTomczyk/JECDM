package statistics;

import org.apache.commons.math4.legacy.stat.StatUtils;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for {@link statistics.Max} class.
 *
 * @author MTomczyk
 */
class MaxTest
{

    /**
     * Test #1.
     */
    @Test
    void calculate()
    {
        IRandom R = new MersenneTwister64(0);
        int T = 100;

        IStatistic S = new Max();

        for (int t = 0; t < T; t++)
        {
            double [] arr = new double[100];
            for (int i = 0; i < 100; i++) arr[i] = R.nextDouble();
            double r = S.calculate(arr);
            assertEquals(StatUtils.max(arr), r, 0.000000001);
        }
    }
}