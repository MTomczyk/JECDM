package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import random.IRandom;
import space.IntRange;
import space.Range;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Data container for the considered knapsack problem. It is assumed that values and sizes can be expressed as doubles.
 *
 * @author MTomczyk
 */
public class Data
{
    /**
     * The number of items.
     */
    public int _items;

    /**
     * Values (i-th index = i-th item).
     */
    public final double[] _values;

    /**
     * Sizes  (i-th index = i-th item).
     */
    public final double[] _sizes;
    /**
     * Item indices sorted according to value[item]/size[item] ratio (ascending order).
     */
    public final int[] _sortedIndices;

    /**
     * Parameterized constructor.
     *
     * @param items  the number of items
     * @param values values (i-th index = i-th item)
     * @param sizes  sizes  (i-th index = i-th item)
     */
    protected Data(int items, double[] values, double[] sizes)
    {
        this(items, values, sizes, getSortedIndices(items, values, sizes));
    }

    /**
     * Parameterized constructor.
     *
     * @param items         the number of items
     * @param values        values (i-th index = i-th item)
     * @param sizes         sizes  (i-th index = i-th item)
     * @param sortedIndices item indices sorted according to value[item]/size[item] ratio (ascending order).
     */
    protected Data(int items, double[] values, double[] sizes, int[] sortedIndices)
    {
        _items = items;
        _values = values;
        _sizes = sizes;
        _sortedIndices = sortedIndices;
    }

    /**
     * Generates random data instance. This instance getter instantiates sizes as integers (stored in the double array though).
     *
     * @param items        the number of items
     * @param R            random number generator
     * @param valuesBounds bounds for items' values (closed interval)
     * @param sizesBounds  bounds for items' sizes (closed interval)
     * @return random problem instance
     */
    public static Data getInstance(int items, IRandom R, Range valuesBounds, IntRange sizesBounds)
    {
        double[] s = new double[items];
        for (int i = 0; i < items; i++)
            s[i] = sizesBounds.getLeft() + R.nextInt(sizesBounds.getInterval() + 1);
        return getInstance(items, R, valuesBounds, s);
    }

    /**
     * Generates random data instance.
     *
     * @param items        the number of items
     * @param R            random number generator
     * @param valuesBounds bounds for items' values (closed interval)
     * @param sizes        pre-instantiated sizes array
     * @return random problem instance
     */
    private static Data getInstance(int items, IRandom R, Range valuesBounds, double[] sizes)
    {
        double[] v = new double[items];
        double[] s = new double[items];
        for (int i = 0; i < items; i++)
        {
            v[i] = valuesBounds.getLeft() + R.nextDouble() * valuesBounds.getInterval();
            s[i] = sizes[i];
        }
        return new Data(items, v, s, getSortedIndices(items, v, s));
    }

    /**
     * Auxiliary method for creating an array of sorted indices that points to items in the ascending order of their value/size score.
     *
     * @param items no. items
     * @param v     items' values
     * @param s     items' sizes
     * @return array
     */
    private static int[] getSortedIndices(int items, double[] v, double[] s)
    {
        double[] tmp = new double[items];
        LinkedList<Integer> tmpI = new LinkedList<>();

        for (int i = 0; i < items; i++)
        {
            tmp[i] = v[i] / s[i];
            tmpI.add(i);
        }
        tmpI.sort(Comparator.comparingDouble(o -> tmp[o]));
        int[] sI = new int[tmp.length];
        int idx = 0;
        for (Integer i : tmpI) sI[idx++] = i;
        return sI;
    }
}
