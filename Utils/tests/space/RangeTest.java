package space;

import org.apache.commons.math4.legacy.stat.StatUtils;
import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Some tests for {@link Range}.
 *
 * @author MTomczyk
 */
class RangeTest
{
    /**
     * Test 1.
     */
    @Test
    void isInRange()
    {
        int T1 = 1000;
        int T2 = 1000;

        IRandom R = new MersenneTwister64(1);

        for (int t = 0; t < T1; t++)
        {
            double [] p = new double[]{R.nextDouble(), R.nextDouble()};
            double left = StatUtils.min(p);
            double right = StatUtils.max(p);
            if (Double.compare(left, right) != 0)
            {
                Range r = new Range(left, right);
                assertEquals(right - left, r.getInterval(), 0.0000001d);

                assertTrue(r.isInRange(left, true));
                assertTrue(r.isInRange(right, true));
                assertFalse(r.isInRange(left, false));
                assertFalse(r.isInRange(right, false));

                for (int t2 = 0; t2 < T2; t2++)
                {
                    double s = R.nextDouble();
                    if (Double.compare(s, left) < 0)
                    {
                        assertFalse(r.isInRange(s, true));
                        assertFalse(r.isInRange(s, false));
                    }
                    else if (Double.compare(s, left) == 0)
                    {
                        assertTrue(r.isInRange(s, true));
                        assertFalse(r.isInRange(s, false));
                    }
                    else if (Double.compare(s, right) > 0)
                    {
                        assertFalse(r.isInRange(s, true));
                        assertFalse(r.isInRange(s, false));
                    }
                    else if (Double.compare(s, right) == 0)
                    {
                        assertTrue(r.isInRange(s, true));
                        assertFalse(r.isInRange(s, false));
                    }
                    else
                    {
                        assertTrue(r.isInRange(s, true));
                        assertTrue(r.isInRange(s, false));
                    }
                }
            }
        }
    }
}