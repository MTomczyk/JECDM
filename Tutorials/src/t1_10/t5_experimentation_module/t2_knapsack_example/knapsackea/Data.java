package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import random.IRandom;
import space.IntRange;
import space.Range;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Data container for the considered knapsack problem
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
    public final int[] _sizes;
    /**
     * Item indices sorted according to value[item]/size[item] ratio (ascending order).
     */
    public final int[] _sortedIndices;


    /**
     * Parameterized constructor.
     *
     * @param items         the number of items
     * @param values        values (i-th index = i-th item)
     * @param sizes         sizes  (i-th index = i-th item)
     * @param sortedIndices item indices sorted according to value[item]/size[item] ratio (ascending order).
     */
    protected Data(int items, double[] values, int[] sizes, int[] sortedIndices)
    {
        _items = items;
        _values = values;
        _sizes = sizes;
        _sortedIndices = sortedIndices;
    }

    /**
     * Generates random data instance.
     *
     * @param items        the number of items
     * @param R            random number generator
     * @param valuesBounds bounds for items' values (closed interval)
     * @param sizesBounds  bounds for items' sizes (closed interval)
     * @return random problem instance
     */
    public static Data getInstance(int items, IRandom R, Range valuesBounds, IntRange sizesBounds)
    {
        double[] v = new double[items];
        int[] s = new int[items];
        LinkedList<Integer> tmpI = new LinkedList<>();
        double[] tmp = new double[items];
        for (int i = 0; i < items; i++)
        {
            v[i] = valuesBounds.getLeft() + R.nextDouble() * valuesBounds.getInterval();
            s[i] = sizesBounds.getLeft() + R.nextInt(sizesBounds.getInterval() + 1);
            tmpI.add(i);
            tmp[i] = v[i] / (double) s[i];
        }

        tmpI.sort(Comparator.comparingDouble(o -> tmp[o]));
        int[] sI = new int[tmp.length];
        int idx = 0;
        for (Integer i : tmpI) sI[idx++] = i;
        return new Data(items, v, s, sI);
    }


}
