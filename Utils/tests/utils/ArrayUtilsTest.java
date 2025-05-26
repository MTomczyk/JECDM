package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ArrayUtils} class.
 *
 * @author MTomczyk
 */
class ArrayUtilsTest
{
    /**
     * Tests array construction (via generics).
     */
    @Test
    void getArrayGenerics()
    {
        {
            Integer[] a = ArrayUtils.getArray(-1, 5);
            assertNull(a);
            a = ArrayUtils.getArray(2, 3);
            assertNotNull(a);
            assertEquals(2, a.length);
            assertEquals(3, a[0]);
            assertEquals(3, a[1]);
            a = ArrayUtils.getArray(0, 3);
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getArray(-1, Integer.class, i -> 2 * i);
            assertNull(a);
            a = ArrayUtils.getArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getArray(3, Integer.class, i -> 2 * i);
            assertNotNull(a);
            assertEquals(3, a.length);
            assertEquals(0, a[0]);
            assertEquals(2, a[1]);
            assertEquals(4, a[2]);
            a = ArrayUtils.getArray(0, Integer.class, i -> 2 * i);
            assertNotNull(a);
            assertEquals(0, a.length);
        }

        {
            String[] a = ArrayUtils.getArray(-1, "A");
            assertNull(a);
            a = ArrayUtils.getArray(2, "A");
            assertNotNull(a);
            assertEquals(2, a.length);
            assertEquals("A", a[0]);
            assertEquals("A", a[1]);
            a = ArrayUtils.getArray(0, "A");
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getArray(-1, String.class, i -> String.valueOf(2 * i));
            assertNull(a);
            a = ArrayUtils.getArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getArray(3, String.class, i -> String.valueOf(2 * i));
            assertNotNull(a);
            assertEquals(3, a.length);
            assertEquals("0", a[0]);
            assertEquals("2", a[1]);
            assertEquals("4", a[2]);
            a = ArrayUtils.getArray(0, String.class, i -> String.valueOf(2 * i));
            assertNotNull(a);
            assertEquals(0, a.length);
        }

        {
            Boolean[] a = ArrayUtils.getArray(-1, true);
            assertNull(a);
            a = ArrayUtils.getArray(2, true);
            assertNotNull(a);
            assertEquals(2, a.length);
            assertTrue(a[0]);
            assertTrue(a[1]);
            a = ArrayUtils.getArray(0, false);
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getArray(-1, Boolean.class, i -> i % 2 == 0);
            assertNull(a);
            a = ArrayUtils.getArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getArray(3, Boolean.class, i -> i % 2 == 0);
            assertNotNull(a);
            assertEquals(3, a.length);
            assertTrue(a[0]);
            assertFalse(a[1]);
            assertTrue(a[2]);
            a = ArrayUtils.getArray(0, Boolean.class, i -> i % 2 == 0);
            assertNotNull(a);
            assertEquals(0, a.length);
        }
    }

    /**
     * Tests array construction (int primitive).
     */
    @Test
    void getArrayInt()
    {
        {
            int[] a = ArrayUtils.getIntArray(-1, 5);
            assertNull(a);
            a = ArrayUtils.getIntArray(2, 3);
            assertNotNull(a);
            assertEquals(2, a.length);
            assertEquals(3, a[0]);
            assertEquals(3, a[1]);
            a = ArrayUtils.getIntArray(0, 3);
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getIntArray(-1, i -> 2 * i);
            assertNull(a);
            a = ArrayUtils.getIntArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getIntArray(3, i -> 2 * i);
            assertNotNull(a);
            assertEquals(3, a.length);
            assertEquals(0, a[0]);
            assertEquals(2, a[1]);
            assertEquals(4, a[2]);
            a = ArrayUtils.getIntArray(0, i -> 2 * i);
            assertNotNull(a);
            assertEquals(0, a.length);
        }
    }

    /**
     * Tests array construction (float primitive).
     */
    @Test
    void getArrayFloat()
    {
        {
            float[] a = ArrayUtils.getFloatArray(-1, 5.0f);
            assertNull(a);
            a = ArrayUtils.getFloatArray(2, 3);
            assertNotNull(a);
            assertEquals(2, a.length);
            assertEquals(3, a[0]);
            assertEquals(3, a[1]);
            a = ArrayUtils.getFloatArray(0, 3);
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getFloatArray(-1, i -> (float) (2 * i));
            assertNull(a);
            a = ArrayUtils.getFloatArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getFloatArray(3, i -> (float) 2 * i);
            assertNotNull(a);
            assertEquals(3, a.length);
            assertEquals(0, a[0], 1.0E-6);
            assertEquals(2, a[1], 1.0E-6);
            assertEquals(4, a[2], 1.0E-6);
            a = ArrayUtils.getFloatArray(0, i -> (float) 2 * i);
            assertNotNull(a);
            assertEquals(0, a.length);
        }
    }

    /**
     * Tests array construction (double primitive).
     */
    @Test
    void getArrayDouble()
    {
        {
            double[] a = ArrayUtils.getDoubleArray(-1, 5.0f);
            assertNull(a);
            a = ArrayUtils.getDoubleArray(2, 3);
            assertNotNull(a);
            assertEquals(2, a.length);
            assertEquals(3, a[0]);
            assertEquals(3, a[1]);
            a = ArrayUtils.getDoubleArray(0, 3);
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getDoubleArray(-1, i -> (double) (2 * i));
            assertNull(a);
            a = ArrayUtils.getDoubleArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getDoubleArray(3, i -> (double) 2 * i);
            assertNotNull(a);
            assertEquals(3, a.length);
            assertEquals(0, a[0], 1.0E-6);
            assertEquals(2, a[1], 1.0E-6);
            assertEquals(4, a[2], 1.0E-6);
            a = ArrayUtils.getDoubleArray(0, i -> (double) 2 * i);
            assertNotNull(a);
            assertEquals(0, a.length);
        }
    }

    /**
     * Tests array construction (boolean primitive).
     */
    @Test
    void getArrayBoolean()
    {
        {
            boolean[] a = ArrayUtils.getBooleanArray(-1, true);
            assertNull(a);
            a = ArrayUtils.getBooleanArray(2, true);
            assertNotNull(a);
            assertEquals(2, a.length);
            assertTrue(a[0]);
            assertTrue(a[1]);
            a = ArrayUtils.getBooleanArray(0, false);
            assertNotNull(a);
            assertEquals(0, a.length);

            a = ArrayUtils.getBooleanArray(-1, i -> i % 2 == 0);
            assertNull(a);
            a = ArrayUtils.getBooleanArray(-1, null);
            assertNull(a);
            a = ArrayUtils.getBooleanArray(3, i -> i % 2 == 0);
            assertNotNull(a);
            assertEquals(3, a.length);
            assertTrue(a[0]);
            assertFalse(a[1]);
            assertTrue(a[2]);
            a = ArrayUtils.getBooleanArray(0, i -> i % 2 == 0);
            assertNotNull(a);
            assertEquals(0, a.length);
        }
    }

}