package sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for the {@link sort.InsertionSortDouble} class.
 *
 * @author MTomczyk
 */
class InsertionSortDoubleTest
{
    /**
     * Test 1.
     */
    @Test
    public void test1()
    {
        {
            InsertionSortDouble sort = new InsertionSortDouble();
            sort.init(3, true);
            sort.add(3);
            sort.add(10);
            sort.add(5);
            sort.add(11);
            sort.add(23);
            sort.add(-4);

            assertEquals(-4.0d, sort._data[0], 0.0001d);
            assertEquals(3.0d, sort._data[1], 0.0001d);
            assertEquals(5.0d, sort._data[2], 0.0001d);
        }
    }

    /**
     * Test 2.
     */
    @Test
    public void test2()
    {
        InsertionSortDouble sort = new InsertionSortDouble();
        sort.init(3, true);
        sort.add(5);
        sort.add(2);
        sort.add(1);
        sort.add(4);
        sort.add(3);

        assertEquals(1.0d, sort._data[0], 0.0001d);
        assertEquals(2.0d, sort._data[1], 0.0001d);
        assertEquals(3.0d, sort._data[2], 0.0001d);

    }

    /**
     * Test 3.
     */
    @Test
    public void test3()
    {
        {
            InsertionSortDouble sort = new InsertionSortDouble();
            sort.init(3, false);
            sort.add(3);
            sort.add(10);
            sort.add(5);
            sort.add(11);
            sort.add(23);
            sort.add(-4);
            assertEquals(23.0d, sort._data[0], 0.0001d);
            assertEquals(11.0d, sort._data[1], 0.0001d);
            assertEquals(10.0d, sort._data[2], 0.0001d);
        }
    }

    /**
     * Test 4.
     */
    @Test
    public void test4()
    {
        InsertionSortDouble sort = new InsertionSortDouble();
        sort.init(3, false);
        sort.add(5);
        sort.add(2);
        sort.add(1);
        sort.add(4);
        sort.add(3);
        assertEquals(5.0d, sort._data[0], 0.0001d);
        assertEquals(4.0d, sort._data[1], 0.0001d);
        assertEquals(3.0d, sort._data[2], 0.0001d);

    }
}