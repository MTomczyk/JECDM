package thread;

import org.junit.jupiter.api.Test;
import print.PrintUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Several tests for the {@link Partitioner} class.
 *
 * @author MTomczyk
 */
class PartitionerTest
{
    /**
     * Test #1.
     */
    @Test
    void generateOffsetsEnds1()
    {
        int t = 4;
        int[] o = Partitioner.generatePartitions(4, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 1, 1, 2, 2, 3, 3, 4};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(4, Partitioner.getValidNoThreads(o, t));
    }

    /**
     * Test #2.
     */
    @Test
    void generateOffsetsEnds2()
    {
        int t = 1;
        int[] o = Partitioner.generatePartitions(4, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 4};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(1, Partitioner.getValidNoThreads(o, t));
    }

    /**
     * Test #3.
     */
    @Test
    void generateOffsetsEnds3()
    {
        int t = 8;
        int[] o = Partitioner.generatePartitions(4, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 1, 1, 2, 2, 3, 3, 4, -1, -1, -1, -1, -1, -1, -1, -1};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(4, Partitioner.getValidNoThreads(o, t));
    }


    /**
     * Test #4.
     */
    @Test
    void generateOffsetsEnds4()
    {
        int t = 3;
        int[] o = Partitioner.generatePartitions(2, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 1, 1, 2, -1, -1};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(2, Partitioner.getValidNoThreads(o, t));
    }

    /**
     * Test #5.
     */
    @Test
    void generateOffsetsEnds5()
    {
        int t = 3;
        int[] o = Partitioner.generatePartitions(13, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 4, 4, 9, 9, 13};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(3, Partitioner.getValidNoThreads(o, t));
    }


    /**
     * Test #6.
     */
    @Test
    void generateOffsetsEnds6()
    {
        int t = 5;
        int[] o = Partitioner.generatePartitions(31, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 6, 6, 12, 12, 19, 19, 25, 25, 31};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(5, Partitioner.getValidNoThreads(o, t));
    }

    /**
     * Test #7.
     */
    @Test
    void generateOffsetsEnds7()
    {
        int t = 1;
        int[] o = Partitioner.generatePartitions(30, t);
        PrintUtils.printVectorOfIntegers(o);
        int[] exp = new int[]{0, 30};
        assertEquals(t * 2, o.length);
        for (int i = 0; i < o.length; i++) assertEquals(exp[i], o[i]);
        assertEquals(1, Partitioner.getValidNoThreads(o, t));
    }


    /**
     * Test #8.
     */
    @Test
    void generateOffsetsEnds8Massive()
    {
        int[] T = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[] L = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 100, 1000, 10000};

        for (int t : T)
        {
            for (int l : L)
            {
                int[] o = Partitioner.generatePartitions(l, t);
                assertEquals(o.length, t * 2);
                int[] cover = new int[l];
                for (int i = 0; i < t; i++)
                {
                    if (o[i * 2] == -1) continue;
                    for (int j = o[i * 2]; j < o[i * 2 + 1]; j++)
                        cover[j]++;
                }
                for (Integer i : cover) assertEquals(1, i);
            }
        }

    }

}