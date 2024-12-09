package space;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for {@link IntRange} class.
 *
 * @author MTomczyk
 */
class IntRangeTest
{

    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        {
            IntRange R = new IntRange(2,5);
            assertEquals(2, R.getLeft());
            assertEquals(5, R.getRight());
            assertEquals(3, R.getInterval());
        }

        {
            IntRange R = new IntRange(5,5);
            assertEquals(5, R.getLeft());
            assertEquals(5, R.getRight());
            assertEquals(0, R.getInterval());
        }

        {
            IntRange R = new IntRange(6,2, true);
            assertEquals(6, R.getLeft());
            assertEquals(2, R.getRight());
            assertEquals(-4, R.getInterval());
        }
    }
}