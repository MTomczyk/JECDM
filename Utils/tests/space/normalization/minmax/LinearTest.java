package space.normalization.minmax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for {@link Linear} class.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
class LinearTest
{
    /**
     * Test 1.
     */
    @Test
    void getNormalized1()
    {
        AbstractMinMaxNormalization n = new Linear(2.0d, 10.0d);
        double v = n.getNormalized(2.0d);
        assertEquals(0.0, v, 0.0001d);
        v = n.getNormalized(10.0d);
        assertEquals(1.0, v, 0.0001d);
        v = n.getNormalized(6.0d);
        assertEquals(0.5, v, 0.0001d);
        System.out.println(n);

        v = n.getUnnormalized(0.0f);
        assertEquals(2.0, v, 0.0001d);
        v = n.getUnnormalized(1.0f);
        assertEquals(10.0, v, 0.0001d);
        v = n.getUnnormalized(0.5f);
        assertEquals(6.0, v, 0.0001d);
        v = n.getUnnormalized(2.0f);
        assertEquals(18.0, v, 0.0001d);
    }

    /**
     * Test 2.
     */
    @Test
    void getNormalized2()
    {
        AbstractMinMaxNormalization n = new Linear(0.0d, 0.0d);
        double v = n.getNormalized(2.0d);
        assertEquals(0.0, v, 0.0001d);
        v = n.getUnnormalized(2.0d);
        assertEquals(0.0, v, 0.0001d);
    }
}