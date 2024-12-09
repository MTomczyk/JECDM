package scenario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link KeyValues} class.
 *
 * @author MTomczyk
 */
class KeyValuesTest
{
    /**
     * Test 1.
     */
    @Test
    void getInstance1()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance(null, null, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("No key is provided", msg);
    }

    /**
     * Test 2.
     */
    @Test
    void getInstance2()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("", null, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key is an empty string", msg);
    }

    /**
     * Test 3.
     */
    @Test
    void getInstance3()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", null, null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("No key abbreviation is provided", msg);
    }


    /**
     * Test 4.
     */
    @Test
    void getInstance4()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "", null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The key abbreviation is an empty string", msg);
    }

    /**
     * Test 5.
     */
    @Test
    void getInstance5()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "TEST", null, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The values for the key = TEST_KEY are not provided (the array is null)", msg);
    }

    /**
     * Test 6.
     */
    @Test
    void getInstance6()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "TEST", new String[]{}, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The values for the key = TEST_KEY are not provided (the array is empty)", msg);
    }

    /**
     * Test 7.
     */
    @Test
    void getInstance7()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "TEST", new String[]{null, "ABC"}, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The value for the key = TEST_KEY is null", msg);
    }


    /**
     * Test 8.
     */
    @Test
    void getInstance8()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "TEST", new String[]{"ABC", ""}, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The value for the key = TEST_KEY is an empty string", msg);
    }

    /**
     * Test 9.
     */
    @Test
    void getInstance9()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "TEST", new String[]{"ABC", "123"}, new boolean[]{});
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertEquals("The disable flags array for the key = TEST_KEY is of different length than the values array", msg);
    }


    /**
     * Test 10.
     */
    @Test
    void getInstance10()
    {
        String msg = null;
        try
        {
            KeyValues.getInstance("TEST_KEY", "TEST", new String[]{"ABC", "123"}, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }

    /**
     * Test 11.
     */
    @Test
    void getInstance11()
    {
        String msg = null;
        KeyValues kv = null;
        try
        {
            kv = KeyValues.getInstance("test_key", "test", new String[]{"abc", "xyz123"}, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        assertNotNull(kv);
        assertEquals("TEST_KEY", kv.getKey().getLabel());
        assertEquals("TEST_KEY", kv.getKey().toString());
        assertEquals("TEST", kv.getKey().getAbbreviation());
        assertEquals("ABC", kv.getValues()[0].getValue());
        assertEquals("XYZ123", kv.getValues()[1].getValue());
        assertEquals("TEST_KEY_ABC_XYZ123", kv.toString());
    }

    /**
     * Test 12.
     */
    @Test
    void getInstance12()
    {
        String msg = null;
        KeyValues[] kv = new KeyValues[6];
        try
        {
            kv[0] = KeyValues.getInstance("test_key", "test", new String[]{"abc", "xyz123"}, null);
            kv[1] = KeyValues.getInstance("test_key", "test1", new String[]{"abc", "xyz123"}, null);
            kv[2] = KeyValues.getInstance("test_key2", "test", new String[]{"abc", "xyz123"}, null);
            kv[3] = KeyValues.getInstance("test_key", "test", new String[]{"xyz123"}, null);
            kv[4] = KeyValues.getInstance("test_key", "test", new String[]{"abc"}, null);
            kv[5] = KeyValues.getInstance("teSt_key", "tesT", new String[]{"ABC", "xyz123"}, null);
        } catch (Exception e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                if ((i == j) || ((i == 0) && (j == 5)) || ((i == 5) && (j == 0)) || ((i == 0) && (j == 1))
                        || ((i == 1) && (j == 0)) || ((i == 1) && (j == 5)) || ((i == 5) && (j == 1)))
                    assertEquals(kv[i], kv[j]);
                else assertNotEquals(kv[i], kv[j]);
            }
        }

        for (int i = 0; i < 6; i++) kv[i].getKey().setOrder(i);
        for (int i = 0; i < 6; i++)
        {
            assertEquals(i, kv[i].hashCode());
            assertEquals(i, kv[i].getKey().hashCode());
        }
    }
}