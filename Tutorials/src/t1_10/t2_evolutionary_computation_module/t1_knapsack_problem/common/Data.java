package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common;

/**
 * Data container for the considered knapsack problem
 *
 * @author MTomczyk
 */
public class Data
{
    /**
     * Knapsack capacity.
     */
    public final int _capacity;

    /**
     * Parameterized constructor.
     *
     * @param capacity knapsack capacity.
     */
    public Data(int capacity)
    {
        _capacity = capacity;
    }

    /**
     * The number of items.
     */
    public int _items = 30;

    /**
     * Values (i-th index = i-th item).
     */
    public final double[] _values = new double[]{
            10.0d, 6.0d, 3.0d, 7.0d, 5.0d, 8.0d, 2.0d, 5.0d, 7.0d, 3.0d, 5.0d, 12.0d, 5.0d, 8.0d, 4.0d, 2.0d, 8.0d, 7.0d,
            15.0d, 12.0d, 10.0d, 7.0d, 3.0d, 1.0d, 5.0d, 5.0d, 3.0d, 15.0d, 5.0d, 1.0d};

    /**
     * Sizes  (i-th index = i-th item).
     */
    public final int[] _sizes = new int[]{5, 4, 2, 4, 8, 5, 3, 1, 9, 2, 8, 4, 3, 8, 7, 7, 5, 6, 10, 8, 9, 7, 4, 4, 4,
            1, 2, 14, 5, 1,};

    /**
     * Item indices sorted according to value[item]/size[item] ratio (ascending order).
     */
    public final int[] _sortedIndices = new int[]{23, 15, 14, 4, 10, 6, 22, 8, 13, 21, 28, 29, 27, 20, 17, 24, 1, 2, 9,
            18, 19, 26, 5, 16, 12, 3, 0, 11, 7, 25};
}
