package statistics.incrementalcontainer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Various tests for {@link IncrementalContainerFloat} class.
 *
 * @author MTomczyk
 */
class IncrementalContainerFloatTest
{
    /**
     * Checks if two float arrays are equal.
     *
     * @param exp expected result
     * @param ans obtained result
     */
    private void compare(float[] exp, float[] ans)
    {
        assertEquals(exp.length, ans.length);
        for (int i = 0; i < exp.length; i++)
            assertEquals(exp[i], ans[i],0.0001f);
    }

    /**
     * Test #1.
     */
    @Test
    void addElement1()
    {
        IncrementalContainerFloat C = new IncrementalContainerFloat(4);
        assertEquals(4, C._data.length);

        C.addElement(2);
        assertEquals(0, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(1, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 0, 0, 0}, C._data);

        C.addElement(1);
        assertEquals(0, C.getBeginIndex());
        assertEquals(1, C.getCurrentIndex());
        assertEquals(2, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 1, 0, 0}, C._data);

        C.addElement(5);
        assertEquals(0, C.getBeginIndex());
        assertEquals(2, C.getCurrentIndex());
        assertEquals(3, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 1, 5, 0}, C._data);

        C.addElement(7);
        assertEquals(0, C.getBeginIndex());
        assertEquals(3, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 1, 5, 7}, C._data);

        C.addElement(3);
        assertEquals(1, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 1, 5, 7}, C._data);

        C.addElement(11);
        assertEquals(2, C.getBeginIndex());
        assertEquals(1, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 11, 5, 7}, C._data);

        C.addElement(-3);
        assertEquals(3, C.getBeginIndex());
        assertEquals(2, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 11, -3, 7}, C._data);

        C.addElement(77);
        assertEquals(0, C.getBeginIndex());
        assertEquals(3, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 11, -3, 77}, C._data);

        C.addElement(-101);
        assertEquals(1, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{-101, 11, -3, 77}, C._data);

        C.reset();

        assertEquals(4, C._data.length);

        C.addElement(2);
        assertEquals(0, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(1, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 0, 0, 0}, C._data);

        C.addElement(1);
        assertEquals(0, C.getBeginIndex());
        assertEquals(1, C.getCurrentIndex());
        assertEquals(2, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 1, 0, 0}, C._data);

        C.addElement(5);
        assertEquals(0, C.getBeginIndex());
        assertEquals(2, C.getCurrentIndex());
        assertEquals(3, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 1, 5, 0}, C._data);

        C.addElement(7);
        assertEquals(0, C.getBeginIndex());
        assertEquals(3, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{2, 1, 5, 7}, C._data);

        C.addElement(3);
        assertEquals(1, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 1, 5, 7}, C._data);

        C.addElement(11);
        assertEquals(2, C.getBeginIndex());
        assertEquals(1, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 11, 5, 7}, C._data);

        C.addElement(-3);
        assertEquals(3, C.getBeginIndex());
        assertEquals(2, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 11, -3, 7}, C._data);

        C.addElement(77);
        assertEquals(0, C.getBeginIndex());
        assertEquals(3, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 11, -3, 77}, C._data);

        C.addElement(-101);
        assertEquals(1, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(4, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{-101, 11, -3, 77}, C._data);
    }

    /**
     * Test #2.
     */
    @Test
    void addElement2()
    {
        IncrementalContainerFloat C = new IncrementalContainerFloat(2);
        assertEquals(2, C._data.length);

        C.addElement(1);
        assertEquals(0, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(1, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{1, 0}, C._data);

        C.addElement(2);
        assertEquals(0, C.getBeginIndex());
        assertEquals(1, C.getCurrentIndex());
        assertEquals(2, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{1, 2}, C._data);

        C.addElement(3);
        assertEquals(1, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(2, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 2}, C._data);

        C.addElement(4);
        assertEquals(0, C.getBeginIndex());
        assertEquals(1, C.getCurrentIndex());
        assertEquals(2, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{3, 4}, C._data);

        C.addElement(5);
        assertEquals(1, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(2, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{5, 4}, C._data);
    }


    /**
     * Test #3.
     */
    @Test
    void addElement3()
    {
        IncrementalContainerFloat C = new IncrementalContainerFloat(1);
        assertEquals(1, C._data.length);

        C.addElement(3);
        assertEquals(0, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(1, C.getNoStoredElements());
        assertFalse(C.cycleBegan());
        compare(new float[]{3}, C._data);

        C.addElement(4);
        assertEquals(0, C.getBeginIndex());
        assertEquals(0, C.getCurrentIndex());
        assertEquals(1, C.getNoStoredElements());
        assertTrue(C.cycleBegan());
        compare(new float[]{4}, C._data);
    }

}