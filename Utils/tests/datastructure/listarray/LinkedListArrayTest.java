package datastructure.listarray;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for {@link LinkedListArray} class.
 */
class LinkedListArrayTest
{
    /**
     * Used to compare two arrays.
     *
     * @param a the first array (not null)
     * @param b the second array (not null)
     */
    private void compareArrays(double[] a, double[] b)
    {
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; i++)
            assertEquals(a[i], b[i], 0.00001d);
    }

    /**
     * Test #1.
     */
    @Test
    void getLinkedListArray1()
    {
        LinkedListArray LA = new LinkedListArray(5);

        {
            LA.reset();
            LinkedList<double[]> la = LA.getLinkedListArray();
            assertEquals(1, la.size());
            assertEquals(5, la.getFirst().length);
            assertEquals(0, LA.getNoStoredElements());

            double[] toAdd = new double[]{3.0d, 5.0d, 1.0d, 2.0d, 11.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(1, la.size());
                assertEquals(5, la.getFirst().length);
                assertEquals(i + 1, LA.getNoStoredElements());

                double[] exp = new double[5];
                System.arraycopy(toAdd, 0, exp, 0, i + 1);
                compareArrays(exp, la.getFirst());
            }

            toAdd = new double[]{32.0d, 52.0d, 13.0d, 24.0d, -115.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(2, la.size());
                assertEquals(5, la.get(1).length);
                assertEquals(i + 6, LA.getNoStoredElements());

                double[] exp = new double[5];
                System.arraycopy(toAdd, 0, exp, 0, i + 1);
                compareArrays(exp, la.get(1));
            }

            toAdd = new double[]{132.0d, 152.0d, 113.0d, 124.0d, -1115.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(3, la.size());
                assertEquals(5, la.get(2).length);
                assertEquals(i + 11, LA.getNoStoredElements());

                double[] exp = new double[5];
                System.arraycopy(toAdd, 0, exp, 0, i + 1);
                compareArrays(exp, la.get(2));
            }
        }

        {
            LA.reset();
            LinkedList<double[]> la = LA.getLinkedListArray();
            assertEquals(1, la.size());
            assertEquals(5, la.getFirst().length);
            assertEquals(0, LA.getNoStoredElements());

            double[] toAdd = new double[]{3.0d, 5.0d, 1.0d, 2.0d, 11.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(1, la.size());
                assertEquals(5, la.getFirst().length);
                assertEquals(i + 1, LA.getNoStoredElements());

                double[] exp = new double[5];
                System.arraycopy(toAdd, 0, exp, 0, i + 1);
                compareArrays(exp, la.getFirst());
            }

            toAdd = new double[]{32.0d, 52.0d, 13.0d, 24.0d, -115.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(2, la.size());
                assertEquals(5, la.get(1).length);
                assertEquals(i + 6, LA.getNoStoredElements());

                double[] exp = new double[5];
                System.arraycopy(toAdd, 0, exp, 0, i + 1);
                compareArrays(exp, la.get(1));
            }

            toAdd = new double[]{132.0d, 152.0d, 113.0d, 124.0d, -1115.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(3, la.size());
                assertEquals(5, la.get(2).length);
                assertEquals(i + 11, LA.getNoStoredElements());

                double[] exp = new double[5];
                System.arraycopy(toAdd, 0, exp, 0, i + 1);
                compareArrays(exp, la.get(2));
            }

            double[] arr = LA.getTransformedToArray();
            double[] exp = new double[]{3.0d, 5.0d, 1.0d, 2.0d, 11.0d, 32.0d, 52.0d, 13.0d, 24.0d, -115.0d, 132.0d, 152.0d, 113.0d, 124.0d, -1115.0d};
            compareArrays(exp, arr);
        }
    }


    /**
     * Test #2.
     */
    @Test
    void getLinkedListArray2()
    {
        LinkedListArray LA = new LinkedListArray(1);

        {
            LA.reset();
            LinkedList<double[]> la = LA.getLinkedListArray();
            assertEquals(1, la.size());
            assertEquals(1, la.getFirst().length);
            assertEquals(0, LA.getNoStoredElements());

            double[] toAdd = new double[]{3.0d, 5.0d, 1.0d, 2.0d, 11.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(1 + i, la.size());
                assertEquals(1, la.get(i).length);
                assertEquals(i + 1, LA.getNoStoredElements());

                double[] exp = new double[]{toAdd[i]};
                compareArrays(exp, la.get(i));
            }
        }

        {
            LA.reset();
            LinkedList<double[]> la = LA.getLinkedListArray();
            assertEquals(1, la.size());
            assertEquals(1, la.getFirst().length);
            assertEquals(0, LA.getNoStoredElements());

            double[] toAdd = new double[]{3.0d, 5.0d, 1.0d, 2.0d, 11.0d};
            for (int i = 0; i < 5; i++)
            {
                LA.addValue(toAdd[i]);
                la = LA.getLinkedListArray();
                assertEquals(1 + i, la.size());
                assertEquals(1, la.get(i).length);
                assertEquals(i + 1, LA.getNoStoredElements());

                double[] exp = new double[]{toAdd[i]};
                compareArrays(exp, la.get(i));
            }

            double[] arr = LA.getTransformedToArray();
            double[] exp = new double[]{3.0d, 5.0d, 1.0d, 2.0d, 11.0d};
            compareArrays(exp, arr);
        }
    }

    /**
     * Test #3.
     */
    @Test
    void getLinkedListArray3()
    {
        LinkedListArray LA = new LinkedListArray(5);

        {
            LA.reset();
            LA.addValue(3.0d);
            LA.addValue(1.0d);
            LA.addValue(2.0d);

            double[] arr = LA.getTransformedToArray();
            assertEquals(3, arr.length);
            assertEquals(3.0d, arr[0], 0.001d);
            assertEquals(1.0d, arr[1], 0.001d);
            assertEquals(2.0d, arr[2], 0.001d);
        }
    }

    /**
     * Test #4.
     */
    @Test
    void getLinkedListArray4()
    {
        LinkedListArray LA = new LinkedListArray(5);

        {
            LA.reset();
            double[] arr = LA.getTransformedToArray();
            assertEquals(0, arr.length);
        }
    }

    /**
     * Test #5.
     */
    @Test
    void getLinkedListArray5()
    {
        LinkedListArray LA = new LinkedListArray(2);

        {
            LA.reset();
            LA.addValue(1.0d);
            LA.addValue(2.0d);
            LA.addValue(3.0d);
            double[] arr = LA.getTransformedToArray();
            assertEquals(3, arr.length);

            assertEquals(1.0d, arr[0], 0.001d);
            assertEquals(2.0d, arr[1], 0.001d);
            assertEquals(3.0d, arr[2], 0.001d);
        }
    }
}