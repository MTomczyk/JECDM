package statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides some tests for {@link Constant}
 *
 * @author MTomczyk
 */
class ConstantTest
{
    /**
     * Test 1.
     */
    @Test
    void calculate()
    {
        Constant c = new Constant(54.0d);
        assertEquals(54.0d, c.calculate(new double[]{2.0d, 1.0d}));
    }
}