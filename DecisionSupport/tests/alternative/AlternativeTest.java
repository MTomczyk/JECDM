package alternative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Alternative}.
 *
 * @author MTomczyk
 */
class AlternativeTest
{
    /**
     * Alternative being tested.
     */
    private Alternative alternative = null;

    /**
     * Sets things up.
     */
    @BeforeEach
    public void setUp()
    {
        alternative = new Alternative("A1", 3);

        alternative.setPerformanceAt(0.1d, 0);
        alternative.setPerformanceAt(2.1d, 1);
        alternative.setPerformanceAt(3.1d, 2);

        alternative.setAuxScore(0.5d);
    }

    /**
     * Test 1.
     */
    @Test
    public void testClone()
    {
        Alternative b = alternative.getClone();

        assertEquals(alternative.getName(), b.getName(), "Name");
        assertEquals( alternative.getPerformanceAt(0), b.getPerformanceAt(0), "C1");
        assertEquals(alternative.getPerformanceAt(1), b.getPerformanceAt(1), "C2");
        assertEquals(alternative.getPerformanceAt(2), b.getPerformanceAt(2), "C3");
    }

    /**
     * Test 2.
     */
    @Test
    public void printEvaluation()
    {
        alternative.printInfoOnAlternatives(16);
    }

    /**
     * Test 3.
     */
    @Test
    public void testGetEvaluationVector()
    {
        double[] e = alternative.getPerformanceVector();
        assertEquals(0.1d, e[0], "C1");
        assertEquals(2.1d, e[1], "C2");
        assertEquals(3.1d, e[2], "C3");
    }

    /**
     * Test 4.
     */
    @Test
    public void testSetEvaluationVector()
    {
        double[] e = {3.0d, 2.99d, 1.1d};
        alternative.setPerformanceVector(e);

        assertEquals(alternative.getPerformanceAt(0), 3.0d, "C1");
        assertEquals(alternative.getPerformanceAt(1), 2.99d, "C2");
        assertEquals(alternative.getPerformanceAt(2), 1.1d, "C3");
    }

    /**
     * Test 5.
     */
    @Test
    public void testHaveTheSamePerformanceAs()
    {
        {
            Alternative A = new Alternative("A1", new double[]{1.0d});
            assertFalse(A.haveTheSamePerformanceAs(null));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{2.0d})));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[0])));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{2.0d, 1.0d})));
            assertTrue(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{1.0d})));
        }

        {
            Alternative A = new Alternative("A1", new double[]{1.0d, 2.0d});
            assertFalse(A.haveTheSamePerformanceAs(null));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{2.0d})));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[0])));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{2.0d, 1.0d})));
            assertFalse(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{1.0d})));
            assertTrue(A.haveTheSamePerformanceAs(new Alternative("A2", new double[]{1.0d, 2.0d})));
        }
    }
}