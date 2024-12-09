package emo.utils.decomposition.goal;

import emo.utils.decomposition.family.FamilyID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * Provides various tests for {@link GoalID}
 *
 * @author MTomczyk
 */
class GoalIDTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {

        FamilyID f1 = new FamilyID(0);
        GoalID g1 = new GoalID(f1, 0);
        //noinspection EqualsWithItself
        assertEquals(g1, g1);
        GoalID g2 = new GoalID(f1, 0);
        assertEquals(g1, g2);
        GoalID g3 = new GoalID(new FamilyID(0), 0);
        assertEquals(g1, g3);
        assertEquals(g2, g3);
        GoalID g4 = new GoalID(f1, 1);
        assertNotEquals(g1, g4);
        assertNotEquals(g2, g4);
        assertNotEquals(g3, g4);
        GoalID g5 = new GoalID(new FamilyID(2), 0);
        assertNotEquals(g1, g5);
        assertNotEquals(g2, g5);
        assertNotEquals(g3, g5);
        assertNotEquals(g4, g5);
        GoalID g6 = new GoalID(new FamilyID(2), 1);
        assertNotEquals(g1, g6);
        assertNotEquals(g2, g6);
        assertNotEquals(g3, g6);
        assertNotEquals(g4, g6);
        assertNotEquals(g5, g6);
    }
}