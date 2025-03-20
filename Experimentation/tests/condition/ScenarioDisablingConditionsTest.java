package condition;

import exception.GlobalException;
import org.junit.jupiter.api.Test;
import scenario.KeyValue;
import scenario.KeyValues;
import scenario.Scenario;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Various tests for {@link ScenarioDisablingConditions}.
 *
 * @author MTomczyk
 */
class ScenarioDisablingConditionsTest
{
    /**
     * Test 1.
     */
    @Test
    void shouldBeDisabled1()
    {
        ScenarioDisablingConditions SDC = new ScenarioDisablingConditions("P", "1");
        String msg = "";
        KeyValues kvs = null;
        try
        {
            kvs = KeyValues.getInstance("P", "P", new String[]{"2"}, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertEquals("", msg);
        assertNotNull(kvs);
        assertEquals(1, kvs.getValues().length);
        Scenario scenario = new Scenario(new KeyValue[]{new KeyValue(kvs.getKey(), kvs.getValues()[0])}, 0);
        assertFalse(SDC.shouldBeDisabled(scenario));
    }

    /**
     * Test 2.
     */
    @Test
    void shouldBeDisabled2()
    {
        ScenarioDisablingConditions SDC = new ScenarioDisablingConditions("P", "1");
        String msg = "";
        KeyValues kvs = null;
        try
        {
            kvs = KeyValues.getInstance("P", "P", new String[]{"1"}, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertEquals("", msg);
        assertNotNull(kvs);
        assertEquals(1, kvs.getValues().length);
        Scenario scenario = new Scenario(new KeyValue[]{new KeyValue(kvs.getKey(), kvs.getValues()[0])}, 0);
        assertTrue(SDC.shouldBeDisabled(scenario));
    }


    /**
     * Test 3.
     */
    @Test
    void shouldBeDisabled3()
    {
        ScenarioDisablingConditions SDC = new ScenarioDisablingConditions("P", "1");
        String msg = null;
        KeyValues kvs = null;
        try
        {
            kvs = KeyValues.getInstance("P", "P", new String[]{"3", "1"}, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(kvs);
        assertEquals(2, kvs.getValues().length);
        Scenario scenario = new Scenario(new KeyValue[]{new KeyValue(kvs.getKey(), kvs.getValues()[0]),
                new KeyValue(kvs.getKey(), kvs.getValues()[1])}, 0);
        assertTrue(SDC.shouldBeDisabled(scenario));
    }
}