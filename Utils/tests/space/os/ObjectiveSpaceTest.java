package space.os;

import org.junit.jupiter.api.Test;
import space.Range;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link ObjectiveSpace}.
 *
 * @author MTomczyk
 */
class ObjectiveSpaceTest
{
    /**
     * Tests 1.
     */
    @Test
    void test()
    {
        assertNull(ObjectiveSpace.getOSMaximallySpanned(null));
        {
            ObjectiveSpace os = ObjectiveSpace.getOSMaximallySpanned(new boolean[]{false, false});
            assertNotNull(os._criteriaTypes);
            assertEquals(2, os._criteriaTypes.length);
            assertFalse(os._criteriaTypes[0]);
            assertFalse(os._criteriaTypes[1]);
            TestUtils.assertEquals(new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}, os._utopia, 1.0E-6);
            TestUtils.assertEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, os._nadir, 1.0E-6);
            assertNotNull(os._ranges);
            assertEquals(2, os._ranges.length);
            assertEquals(Double.NEGATIVE_INFINITY, os._ranges[0].getLeft());
            assertEquals(Double.POSITIVE_INFINITY, os._ranges[0].getRight());
            assertEquals(Double.NEGATIVE_INFINITY, os._ranges[1].getLeft());
            assertEquals(Double.POSITIVE_INFINITY, os._ranges[1].getRight());
            assertTrue(os.isEqual(
                    new ObjectiveSpace(new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                            new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY})));
            assertTrue(os.isEqual(os.getClone()));
        }
        {
            ObjectiveSpace os = ObjectiveSpace.getOSMaximallySpanned(new boolean[]{false, false}, true);
            assertNotNull(os._criteriaTypes);
            assertEquals(2, os._criteriaTypes.length);
            assertFalse(os._criteriaTypes[0]);
            assertFalse(os._criteriaTypes[1]);
            TestUtils.assertEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, os._utopia, 1.0E-6);
            TestUtils.assertEquals(new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}, os._nadir, 1.0E-6);
            assertNotNull(os._ranges);
            assertEquals(2, os._ranges.length);
            assertEquals(Double.NEGATIVE_INFINITY, os._ranges[0].getLeft());
            assertEquals(Double.POSITIVE_INFINITY, os._ranges[0].getRight());
            assertEquals(Double.NEGATIVE_INFINITY, os._ranges[1].getLeft());
            assertEquals(Double.POSITIVE_INFINITY, os._ranges[1].getRight());
            assertTrue(os.isEqual(new ObjectiveSpace(
                    new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                    new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                    new Range[]{
                            new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                            new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)},
                    new boolean[]{false, false}
            )));
            assertTrue(os.isEqual(os.getClone()));
        }
        {
            ObjectiveSpace os = ObjectiveSpace.getOSMaximallySpanned(new boolean[]{false, true}, true);
            assertNotNull(os._criteriaTypes);
            assertEquals(2, os._criteriaTypes.length);
            assertFalse(os._criteriaTypes[0]);
            assertTrue(os._criteriaTypes[1]);
            TestUtils.assertEquals(new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}, os._utopia, 1.0E-6);
            TestUtils.assertEquals(new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}, os._nadir, 1.0E-6);
            assertNotNull(os._ranges);
            assertEquals(2, os._ranges.length);
            assertEquals(Double.NEGATIVE_INFINITY, os._ranges[0].getLeft());
            assertEquals(Double.POSITIVE_INFINITY, os._ranges[0].getRight());
            assertEquals(Double.NEGATIVE_INFINITY, os._ranges[1].getLeft());
            assertEquals(Double.POSITIVE_INFINITY, os._ranges[1].getRight());
            assertTrue(os.isEqual(new ObjectiveSpace(
                    new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY},
                    new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY},
                    new Range[]{
                            new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
                            new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)},
                    new boolean[]{false, true}
            )));
            assertTrue(os.isEqual(os.getClone()));
        }
    }

}