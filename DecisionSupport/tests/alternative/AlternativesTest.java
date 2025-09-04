package alternative;

import org.junit.jupiter.api.Test;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Alternatives}.
 *
 * @author MTomczyk
 */
class AlternativesTest
{
    /**
     * Test 1 (tests factory-like classes).
     */
    @Test
    void getAlternativeArray()
    {
        assertNull(Alternatives.getAlternativeArray("A", -1, -1));
        assertNull(Alternatives.getAlternativeArray("A", 5, -1));
        assertNull(Alternatives.getAlternativeArray("A", -1, 5));
        {
            Alternatives alternatives = Alternatives.getAlternativeArray(null, 2, 3);
            assertNotNull(alternatives);
            assertEquals(2, alternatives.size());
            assertEquals("null0", alternatives.get(0).getName());
            assertEquals("null1", alternatives.get(1).getName());
            TestUtils.assertEquals(new double[3], alternatives.get(0).getPerformanceVector(), 1.0E-5);
            TestUtils.assertEquals(new double[3], alternatives.get(1).getPerformanceVector(), 1.0E-5);
        }
        {
            Alternatives alternatives = Alternatives.getAlternativeArray("A", 2, 3);
            assertNotNull(alternatives);
            assertEquals(2, alternatives.size());
            assertEquals("A0", alternatives.get(0).getName());
            assertEquals("A1", alternatives.get(1).getName());
            TestUtils.assertEquals(new double[3], alternatives.get(0).getPerformanceVector(), 1.0E-5);
            TestUtils.assertEquals(new double[3], alternatives.get(1).getPerformanceVector(), 1.0E-5);
        }
        {
            Alternatives alternatives = Alternatives.getAlternativeArray("A", 2, 3, 4);
            assertNotNull(alternatives);
            assertEquals(2, alternatives.size());
            assertEquals("A4", alternatives.get(0).getName());
            assertEquals("A5", alternatives.get(1).getName());
            TestUtils.assertEquals(new double[3], alternatives.get(0).getPerformanceVector(), 1.0E-5);
            TestUtils.assertEquals(new double[3], alternatives.get(1).getPerformanceVector(), 1.0E-5);
        }
        assertNull(Alternatives.getAlternativeArray("A", null));
        assertNull(Alternatives.getAlternativeArray("A", new double[][]{null}));
        {
            Alternatives alternatives = Alternatives.getAlternativeArray(null,
                    new double[][]{{1.0d, 2.0d, 3.0d}, {3.0d, 4.0d, 5.0d}});
            assertNotNull(alternatives);
            assertEquals(2, alternatives.size());
            assertEquals("null0", alternatives.get(0).getName());
            assertEquals("null1", alternatives.get(1).getName());
            TestUtils.assertEquals(new double[]{1.0d, 2.0d, 3.0d}, alternatives.get(0).getPerformanceVector(), 1.0E-5);
            TestUtils.assertEquals(new double[]{3.0d, 4.0d, 5.0d}, alternatives.get(1).getPerformanceVector(), 1.0E-5);
        }
        {
            Alternatives alternatives = Alternatives.getAlternativeArray("A",
                    new double[][]{{1.0d, 2.0d, 3.0d}, {3.0d, 4.0d, 5.0d}});
            assertNotNull(alternatives);
            assertEquals(2, alternatives.size());
            assertEquals("A0", alternatives.get(0).getName());
            assertEquals("A1", alternatives.get(1).getName());
            TestUtils.assertEquals(new double[]{1.0d, 2.0d, 3.0d}, alternatives.get(0).getPerformanceVector(), 1.0E-5);
            TestUtils.assertEquals(new double[]{3.0d, 4.0d, 5.0d}, alternatives.get(1).getPerformanceVector(), 1.0E-5);
        }
        {
            Alternatives alternatives = Alternatives.getAlternativeArray("A",
                    new double[][]{{1.0d, 2.0d, 3.0d}, {3.0d, 4.0d, 5.0d}}, 2);
            assertNotNull(alternatives);
            assertEquals(2, alternatives.size());
            assertEquals("A2", alternatives.get(0).getName());
            assertEquals("A3", alternatives.get(1).getName());
            TestUtils.assertEquals(new double[]{1.0d, 2.0d, 3.0d}, alternatives.get(0).getPerformanceVector(), 1.0E-5);
            TestUtils.assertEquals(new double[]{3.0d, 4.0d, 5.0d}, alternatives.get(1).getPerformanceVector(), 1.0E-5);
        }
    }
}