package condition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Various tests for {@link TrialDisablingConditions}.
 *
 * @author MTomczyk
 */
class TrialDisablingConditionsTest
{
    /**
     * Test 1.
     */
    @Test
    void isTrialDisabled1()
    {
        {
            TrialDisablingConditions TDC = new TrialDisablingConditions(10);
            assertNotNull(TDC);

            boolean [] expected = new boolean[10];
            for (int i = 0; i < 10; i++) assertEquals(expected[i], TDC.isTrialDisabled(i));
            expected[3] = true;
            expected[4] = true;
            expected[5] = true;
            expected[8] = true;
            TDC.disableTrials(3, 5);
            TDC.disableTrial(8);
            for (int i = 0; i < 10; i++) assertEquals(expected[i], TDC.isTrialDisabled(i));
            assertFalse(TDC.isTrialDisabled(-1));
            assertFalse(TDC.isTrialDisabled(10));
            TDC.enableTrial(8);
            expected[8] = false;
            for (int i = 0; i < 10; i++) assertEquals(expected[i], TDC.isTrialDisabled(i));
            TDC.enableTrials(0, 9);
            expected[3] = false;
            expected[4] = false;
            expected[5] = false;
            for (int i = 0; i < 10; i++) assertEquals(expected[i], TDC.isTrialDisabled(i));
        }
    }
}