package ea;

import org.junit.jupiter.api.Test;
import random.IRandom;
import random.XoRoShiRo128PP;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link EATimestamp}.
 *
 * @author MTomczyk
 */
class EATimestampTest
{
    /**
     * Test 1.
     */
    @Test
    void getClone()
    {
        IRandom R = new XoRoShiRo128PP(0);
        for (int t = 0; t < 1000; t++)
        {
            int g = R.nextInt(1000);
            int s = R.nextInt(1000);
            EATimestamp eaTimestamp = new EATimestamp(g, s);
            assertEquals(g, eaTimestamp._generation);
            assertEquals(s, eaTimestamp._steadyStateRepeat);
            EATimestamp clone = eaTimestamp.getClone();
            assertEquals(g, clone._generation);
            assertEquals(s, clone._steadyStateRepeat);
            assertEquals(eaTimestamp, clone);
        }
        {
            int g = -1;
            EATimestamp eaTimestamp = new EATimestamp(g, 0);
            assertEquals(0, eaTimestamp._generation);
        }
        {
            int s = -1;
            EATimestamp eaTimestamp = new EATimestamp(0, s);
            assertEquals(0, eaTimestamp._steadyStateRepeat);
        }
    }
}