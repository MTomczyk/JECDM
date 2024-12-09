package scenario;

import exception.GlobalException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various tests for {@link Scenarios}.
 *
 * @author MTomczyk
 */
class ScenariosTest
{
    /**
     * Test 1.
     */
    @Test
    void getScenariosThatMatch1()
    {
        String[] keys = new String[]{"k1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "C"},
                {"1", "2", "3"},
                {"X", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];

        String msg = null;
        for (int i = 0; i < 3; i++)
        {
            try
            {
                kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        Scenarios scenarios = null;
        try
        {
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        {
            KeyValue [] pattern = new KeyValue[]
                    {
                      new KeyValue(kvs[0].getKey(), kvs[0].getValues()[1])
                    };

            Scenario [] mathing = scenarios.getScenariosThatMatch(pattern);
            assertEquals(9, mathing.length);

            String [] exp = new String[]{
                    "K1_B_K2_1_K3_X",
                    "K1_B_K2_1_K3_Y",
                    "K1_B_K2_1_K3_Z",
                    "K1_B_K2_2_K3_X",
                    "K1_B_K2_2_K3_Y",
                    "K1_B_K2_2_K3_Z",
                    "K1_B_K2_3_K3_X",
                    "K1_B_K2_3_K3_Y",
                    "K1_B_K2_3_K3_Z",
            };

            Set<String> expS = new HashSet<>();
            Collections.addAll(expS, exp);
            for (Scenario s: mathing)
                assertTrue(expS.contains(s.toString()));
        }
    }

    /**
     * Test 2.
     */
    @Test
    void getScenariosThatMatch2()
    {
        String[] keys = new String[]{"k1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "C"},
                {"1", "2", "3"},
                {"X", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];

        String msg = null;
        for (int i = 0; i < 3; i++)
        {
            try
            {
                kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        Scenarios scenarios = null;
        try
        {
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        {
            KeyValue [] pattern = new KeyValue[]
                    {
                            new KeyValue(kvs[0].getKey(), kvs[0].getValues()[1]),
                                    new KeyValue(kvs[1].getKey(), kvs[1].getValues()[0])
                    };

            Scenario [] mathing = scenarios.getScenariosThatMatch(pattern);
            assertEquals(3, mathing.length);

            String [] exp = new String[]{
                    "K1_B_K2_1_K3_X",
                    "K1_B_K2_1_K3_Y",
                    "K1_B_K2_1_K3_Z",
            };

            Set<String> expS = new HashSet<>();
            Collections.addAll(expS, exp);
            for (Scenario s: mathing)
                assertTrue(expS.contains(s.toString()));
        }
    }

    /**
     * Test 3.
     */
    @Test
    void getScenariosThatMatch3()
    {
        String[] keys = new String[]{"k1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "C"},
                {"1", "2", "3"},
                {"X", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];

        String msg = null;
        for (int i = 0; i < 3; i++)
        {
            try
            {
                kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        Scenarios scenarios = null;
        try
        {
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        {
            KeyValue [] pattern = new KeyValue[]
                    {
                            new KeyValue(kvs[0].getKey(), kvs[0].getValues()[1]),
                            new KeyValue(kvs[1].getKey(), kvs[1].getValues()[0]),
                            new KeyValue(kvs[2].getKey(), kvs[2].getValues()[2])
                    };

            Scenario [] mathing = scenarios.getScenariosThatMatch(pattern);
            assertEquals(1, mathing.length);

            String [] exp = new String[]{
                    "K1_B_K2_1_K3_Z",
            };

            Set<String> expS = new HashSet<>();
            Collections.addAll(expS, exp);
            for (Scenario s: mathing)
                assertTrue(expS.contains(s.toString()));
        }
    }


    /**
     * Test 4.
     */
    @Test
    void getScenariosThatMatch4()
    {
        String[] keys = new String[]{"k1", "k2", "k3"};
        String[][] values = new String[][]{
                {"A", "B", "C"},
                {"1", "2", "3"},
                {"X", "Y", "Z"}
        };
        KeyValues[] kvs = new KeyValues[3];

        String msg = null;
        for (int i = 0; i < 3; i++)
        {
            try
            {
                kvs[i] = KeyValues.getInstance(keys[i], keys[i], values[i], null);
            } catch (GlobalException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        Scenarios scenarios = null;
        try
        {
            scenarios = ScenariosGenerator.getScenarios(kvs, null, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(scenarios);

        {
            KeyValue [] pattern = new KeyValue[]
                    {
                            new KeyValue(kvs[0].getKey(), kvs[1].getValues()[0]), // invalid index causes 0
                    };

            Scenario [] mathing = scenarios.getScenariosThatMatch(pattern);
            assertEquals(0, mathing.length);
        }
    }
}