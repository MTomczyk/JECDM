package utils;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.MersenneTwister64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ReferencePointsOnPareto}
 *
 * @author MTomczyk
 */
class ReferencePointsOnParetoTest
{

    /**
     * Tests {@link ReferencePointsOnPareto#getReferencePointsOnPF(String, int)}
     */
    @Test
    void getReferencePointsOnPF()
    {
        IRandom R = new MersenneTwister64(0);
        {
            ReferencePointsOnPareto RPS = ReferencePointsOnPareto.getDefault(0, 0,0, R);
            assertNull(RPS);
        }
        {
            ReferencePointsOnPareto RPS = ReferencePointsOnPareto.getDefault(10, 0,0, R);
            assertNull(RPS);
        }
        {
            ReferencePointsOnPareto RPS = ReferencePointsOnPareto.getDefault(10, 2,0, R);
            assertNull(RPS);
        }
        {
            ReferencePointsOnPareto RPS = ReferencePointsOnPareto.getDefault(10, 2,6, R);
            assertNotNull(RPS);
            String [] keys = new String[]{
                    "DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7",
                    "WFG1",  "WFG1ALPHA02", "WFG1ALPHA025", "WFG1ALPHA05", "WFG2", "WFG3", "WFG4", "WFG5", "WFG6",
                    "WFG7", "WFG8", "WFG9"
            };

            for (String k: keys)
            {
                for (int m = 2; m < 6; m++) assertNotNull(RPS.getReferencePointsOnPF(k, m));
                assertNull(RPS.getReferencePointsOnPF(k, 1));
                assertNull(RPS.getReferencePointsOnPF(k, 6));
            }
            assertNull(RPS.getReferencePointsOnPF("ABCD", 2));
        }

    }
}