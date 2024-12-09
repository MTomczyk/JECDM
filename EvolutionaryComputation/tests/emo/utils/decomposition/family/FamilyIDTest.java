package emo.utils.decomposition.family;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * Provides various tests for {@link FamilyID}.
 *
 * @author MTomczyk
 */
class FamilyIDTest
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        FamilyID id1 = new FamilyID(0);
        //noinspection EqualsWithItself
        assertEquals(id1, id1);
        FamilyID id2 = new FamilyID(0);
        assertEquals(id1, id2);
        FamilyID id3 = new FamilyID(1);
        assertNotEquals(id1, id3);
        assertNotEquals(id2, id3);
    }
}