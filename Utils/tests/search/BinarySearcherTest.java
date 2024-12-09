package search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for {@link BinarySearcher} class.
 *
 * @author MTomczyk
 */
class BinarySearcherTest
{
    /**
     * Test #1.
     */
    @Test
    void generateIndex1()
    {
        BinarySearcher BS = new BinarySearcher(0, 0);
        assertEquals(0, BS.generateIndex());
    }

    /**
     * Test #2.
     */
    @Test
    void generateIndex2()
    {
        BinarySearcher BS = new BinarySearcher(0, 1);
        assertEquals(0, BS.generateIndex());
        BS.goLeft();
        assertEquals(0, BS.generateIndex());
        BS.goRight();
        assertEquals(0, BS.generateIndex());
    }

    /**
     * Test #3.
     */
    @Test
    void generateIndex3()
    {
        BinarySearcher BS = new BinarySearcher(0, 1);
        assertEquals(0, BS.generateIndex());
        BS.goRight();
        assertEquals(1, BS.generateIndex());
        BS.goLeft();
        assertEquals(1, BS.generateIndex());
    }

    /**
     * Test #4.
     */
    @Test
    void generateIndex4()
    {
        BinarySearcher BS = new BinarySearcher(0, 2);
        assertEquals(1, BS.generateIndex());
        BS.goLeft();
        assertEquals(0, BS.generateIndex());
    }

    /**
     * Test #5.
     */
    @Test
    void generateIndex5()
    {
        BinarySearcher BS = new BinarySearcher(0, 2);
        assertEquals(1, BS.generateIndex());
        BS.goRight();
        assertEquals(2, BS.generateIndex());
    }

    /**
     * Test #6.
     */
    @Test
    void generateIndex6()
    {
        BinarySearcher BS = new BinarySearcher(0, 3);
        assertEquals(1, BS.generateIndex());
        BS.goLeft();
        assertEquals(0, BS.generateIndex());
        BS.goRight();
        assertEquals(0, BS.generateIndex());
    }

    /**
     * Test #7.
     */
    @Test
    void generateIndex7()
    {
        BinarySearcher BS = new BinarySearcher(0, 3);
        assertEquals(1, BS.generateIndex());
        BS.goRight();
        assertEquals(2, BS.generateIndex());
        BS.goRight();
        assertEquals(3, BS.generateIndex());
    }

    /**
     * Test #8.
     */
    @Test
    void generateIndex8()
    {
        BinarySearcher BS = new BinarySearcher(0, 3);
        assertEquals(1, BS.generateIndex());
        BS.goRight();
        assertEquals(2, BS.generateIndex());
        BS.goLeft();
        assertEquals(2, BS.generateIndex());
    }

    /**
     * Test #9.
     */
    @Test
    void generateIndex9()
    {
        BinarySearcher BS = new BinarySearcher(0, 11);
        assertEquals(5, BS.generateIndex());
        BS.goRight();
        assertEquals(8, BS.generateIndex());
        BS.goRight();
        assertEquals(10, BS.generateIndex());
        BS.goRight();
        assertEquals(11, BS.generateIndex());
    }
}