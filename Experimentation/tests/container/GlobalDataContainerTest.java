package container;

import container.global.GlobalDataContainer;
import exception.GlobalException;
import org.junit.jupiter.api.Test;
import scenario.KeyValues;
import scenario.Keys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Contains various tests for {@link GlobalDataContainer} class.
 *
 * @author MTomczyk
 */
class GlobalDataContainerTest
{
    /**
     * Test 1
     */
    @Test
    void test1()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._RNGI = null;
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The random number generator initializer is not provided", msg);
    }

    /**
     * Test 2
     */
    @Test
    void test2()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("Scenario keys are not provided (the array is null)", msg);
    }

    /**
     * Test 3
     */
    @Test
    void test3()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[0];
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("Scenario keys are not provided (the array is empty)", msg);
    }

    /**
     * Test 4
     */
    @Test
    void test4()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("Scenario values are not provided (the array is null)", msg);
    }

    /**
     * Test 5
     */
    @Test
    void test5()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[2][];
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The number of scenario keys differs from the length of the array containing scenario values", msg);
    }

    /**
     * Test 6
     */
    @Test
    void test6()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = -1;
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The number of trials should be not less than 1", msg);
    }

    /**
     * Test 7
     */
    @Test
    void test7()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = 100;
        p._noThreads = -1;
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The number of threads should be not less than 1", msg);
    }

    /**
     * Test 8
     */
    @Test
    void test8()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = 100;
        p._noThreads = 4;
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The main path is not provided (is null)", msg);
    }

    /**
     * Test 9
     */
    @Test
    void test9()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = 100;
        p._noThreads = 4;
        p._mainPath = "path";
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 10
     */
    @Test
    void test10()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = 100;
        p._noThreads = 4;
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        GDC.storeInteger("A", 10);
        GDC.storeDouble("A", 5.0d);
        GDC.storeString("B", "ABC");
        String msg = null;

        KeyValues kv = null;
        try
        {
            kv = KeyValues.getInstance("KEY", "K", new String[]{"A", "B"}, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        GDC.storeObject("TEST", kv);

        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The main path is not provided (is null)", msg);

        assertEquals(10, GDC.getInteger("A"));
        assertEquals(5.0d, GDC.getDouble("A"));
        assertEquals("ABC", GDC.getString("B"));
        assertEquals(kv, GDC.getObject("TEST"));
        GDC.dispose();
    }

    /**
     * Test 11
     */
    @Test
    void test11()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = 100;
        p._noThreads = 4;
        p._mainPath = "path";
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        GDC.storeInteger("A", 10);
        GDC.storeDouble("A", 5.0d);
        GDC.storeString("B", "ABC");
        String msg = null;

        KeyValues kv = null;
        try
        {
            kv = KeyValues.getInstance("KEY", "K", new String[]{"A", "B"}, null);
        } catch (GlobalException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        GDC.storeObject("TEST", kv);

        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        assertEquals(10, GDC.getInteger("A"));
        assertEquals(5.0d, GDC.getDouble("A"));
        assertEquals("ABC", GDC.getString("B"));
        assertEquals(kv, GDC.getObject("TEST"));
        GDC.dispose();
    }


    /**
     * Test 12
     */
    @Test
    void test12()
    {
        GlobalDataContainer.Params p = new GlobalDataContainer.Params();
        p._scenarioKeys = new String[]{Keys.KEY_PROBLEM};
        p._scenarioValues = new String[][]{{"DTLZ2"}};
        p._noTrials = 100;
        p._noThreads = 4;
        p._mainPath = "path";
        p._useMonitorThread = true;
        p._monitorReportingInterval = -1;
        GlobalDataContainer GDC = new GlobalDataContainer(p);
        String msg = null;
        try
        {
            GDC.instantiateData(null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The reporting interval should not be negative if the monitor thread is used", msg);
    }
}