package criterion;

import org.junit.jupiter.api.Test;
import utils.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link Criteria}.
 *
 * @author MTomczyk
 */
class CriteriaTest
{

    /**
     * Test 1 (tests factory-like classes).
     */
    @Test
    void constructCriterion()
    {
        {
            Criteria criteria = Criteria.constructCriterion(null, false);
            assertNotNull(criteria);
            assertEquals(1, criteria._no);
            assertEquals(1, criteria._c.length);
            assertEquals(1, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{false}, criteria.getCriteriaTypes());
            assertNull(criteria._c[0].getName());
            assertFalse(criteria._c[0].isGain());
            assertEquals("null", criteria.toString());
            assertEquals("null", criteria.getStringRepresentation());
        }
        {
            Criteria criteria = Criteria.constructCriterion("C", true);
            assertNotNull(criteria);
            assertEquals(1, criteria._no);
            assertEquals(1, criteria._c.length);
            assertEquals(1, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{true}, criteria.getCriteriaTypes());
            assertEquals("C", criteria._c[0].getName());
            assertTrue(criteria._c[0].isGain());
            assertEquals("C", criteria.toString());
            assertEquals("C", criteria.getStringRepresentation());
        }
    }

    /**
     * Test 2 (tests factory-like classes).
     */
    @Test
    void constructCriteria1()
    {
        assertNull(Criteria.constructCriteria("C", -1, false));
        //noinspection DataFlowIssue
        assertEquals(0, Criteria.constructCriteria("C", 0, false)._no);
        {
            Criteria criteria = Criteria.constructCriteria("C", 2, false);
            assertNotNull(criteria);
            assertEquals(2, criteria._no);
            assertEquals(2, criteria._c.length);
            assertEquals(2, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{false, false}, criteria.getCriteriaTypes());
            assertEquals("C0", criteria._c[0].getName());
            assertEquals("C1", criteria._c[1].getName());
            assertFalse(criteria._c[0].isGain());
            assertFalse(criteria._c[1].isGain());
        }
        {
            Criteria criteria = Criteria.constructCriteria("C", 2, true);
            assertNotNull(criteria);
            assertEquals(2, criteria._no);
            assertEquals(2, criteria._c.length);
            assertEquals(2, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{true, true}, criteria.getCriteriaTypes());
            assertEquals("C0", criteria._c[0].getName());
            assertEquals("C1", criteria._c[1].getName());
            assertTrue(criteria._c[0].isGain());
            assertTrue(criteria._c[1].isGain());
        }
        {
            Criteria criteria = Criteria.constructCriteria("C", 2, true, 2);
            assertNotNull(criteria);
            assertEquals(2, criteria._no);
            assertEquals(2, criteria._c.length);
            assertEquals(2, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{true, true}, criteria.getCriteriaTypes());
            assertEquals("C2", criteria._c[0].getName());
            assertEquals("C3", criteria._c[1].getName());
            assertTrue(criteria._c[0].isGain());
            assertTrue(criteria._c[1].isGain());
        }
    }


    /**
     * Test 3 (tests factory-like classes).
     */
    @Test
    void constructCriteria2()
    {
        assertNull(Criteria.constructCriteria("C", null));
        assertEquals(0, Criteria.constructCriteria("C", new boolean[0])._no);
        {
            Criteria criteria = Criteria.constructCriteria("C", new boolean[]{true, false});
            assertNotNull(criteria);
            assertEquals(2, criteria._no);
            assertEquals(2, criteria._c.length);
            assertEquals(2, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{true, false}, criteria.getCriteriaTypes());
            assertEquals("C0", criteria._c[0].getName());
            assertEquals("C1", criteria._c[1].getName());
            assertTrue(criteria._c[0].isGain());
            assertFalse(criteria._c[1].isGain());
        }
        {
            Criteria criteria = Criteria.constructCriteria("C", new boolean[]{false, true}, 2);
            assertNotNull(criteria);
            assertEquals(2, criteria._no);
            assertEquals(2, criteria._c.length);
            assertEquals(2, criteria.getCriteriaTypes().length);
            TestUtils.assertEquals(new boolean[]{false, true}, criteria.getCriteriaTypes());
            assertEquals("C2", criteria._c[0].getName());
            assertEquals("C3", criteria._c[1].getName());
            assertFalse(criteria._c[0].isGain());
            assertTrue(criteria._c[1].isGain());
        }
    }
}