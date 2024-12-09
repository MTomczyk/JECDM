package scenario;

import exception.GlobalException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link CrossedSetting}.
 *
 * @author MTomczyk
 */
class CrossedSettingTest
{

    /**
     * Test 1.
     */
    @Test
    void instantiateSetting1()
    {
        String[] keys = new String[]{"k1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "C"},
                {"1", "2", "3"},
                {"X", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];
        Scenarios scenarios = null;

        String msg = null;

        try
        {
            for (int i = 0; i < 3; i++) kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        {
            CrossedSetting CS = new CrossedSetting(null, null, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("No keys are provided (the array is null)", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{}, null, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("No keys are provided (the array is empty)", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{null}, null, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("One of the keys provided is null", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"}, null, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("No values are provided (the array is null)", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"}, new String[][]{}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("No values are provided (the array is empty)", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"}, new String[][]{new String[]{}}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("The number of keys to be compared differs from the number of value arrays", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2", "K3", "K4"},
                    new String[][]{new String[]{}, new String[]{}, new String[]{}, new String[]{}}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("Only 1-level, 2-level, and 3-level cross-comparisons are allowed currently", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"},
                    new String[][]{null, null}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("The value array for key = K1 is not provided (the array is null)", msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"},
                    new String[][]{new String[]{}, null}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("The value array for key = K1 is not provided (the array is empty)", msg);
        }

        msg = null;
        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"},
                    new String[][]{new String[]{"A", "B", "C"},
                            new String[]{"1", "2", "3"}}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "X"},
                    new String[][]{new String[]{"A", "B", "C"},
                            new String[]{"1", "2", "3"}}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("Could not find a matching key = X in maintained scenarios", msg);
        }

        msg = null;
        {
            CrossedSetting CS = new CrossedSetting(new String[]{"K1", "K2"},
                    new String[][]{new String[]{"A", "B1", "C"},
                            new String[]{"1", "2", "3"}}, null);
            try
            {
                CS.instantiateSetting(scenarios);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertEquals("Could not find a matching value = B1 for key = K1 in maintained scenarios", msg);
        }
    }
}