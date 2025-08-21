package phase;

import population.SpecimensContainer;

/**
 * Sorts specimens stored in {@link SpecimensContainer#getPopulation()} according to their first aux values.
 *
 * @author MTomczyk
 */
public class SortByAuxValue extends Sort implements IPhase
{
    /**
     * Parameterized constructor (sets the name to "SORT_BY_AUX_VALUE").
     *
     * @param ascendingOrder if true, specimens are sorted in ascending order according to their first aux values;
     *                       false: in descending order.
     */
    public SortByAuxValue(boolean ascendingOrder)
    {
        this("SORT_BY_AUX_VALUE", ascendingOrder);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     * @param ascendingOrder if true, specimens are sorted in ascending order according to their first aux values;
     *                       false: in descending order.
     */
    public SortByAuxValue(String name, boolean ascendingOrder)
    {
        super(name, specimens -> sort.Sort.sortByAuxValue(specimens, ascendingOrder));
    }
}
