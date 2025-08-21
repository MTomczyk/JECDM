package phase;

import population.SpecimensContainer;

/**
 * Sorts specimens stored in {@link SpecimensContainer#getPopulation()} according to their first performance values.
 *
 * @author MTomczyk
 */
public class SortByPerformanceValue extends Sort implements IPhase
{
    /**
     * Parameterized constructor (sets the name to "SORT_BY_PERFORMANCE_VALUE").
     *
     * @param ascendingOrder if true, specimens are sorted in ascending order according to their first performance values;
     *                       false: in descending order.
     */
    public SortByPerformanceValue(boolean ascendingOrder)
    {
        this("SORT_BY_PERFORMANCE_VALUE", ascendingOrder);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     * @param ascendingOrder if true, specimens are sorted in ascending order according to their first performance values;
     *                       false: in descending order.
     */
    public SortByPerformanceValue(String name, boolean ascendingOrder)
    {
        super(name, specimens -> sort.Sort.sortByPerformanceValue(specimens, ascendingOrder));
    }
}
