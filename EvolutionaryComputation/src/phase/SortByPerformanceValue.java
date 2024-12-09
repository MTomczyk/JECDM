package phase;

import ea.EA;
import exception.PhaseException;
import population.SpecimensContainer;

/**
 * Sorts specimens stored in {@link SpecimensContainer#getPopulation()} according to their first performance values.
 *
 * @author MTomczyk
 */


public class SortByPerformanceValue extends AbstractSortPhase implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param ascendingOrder if true, specimens are sorted in ascending order according to their first performance values;
     *                       false: in descending order.
     */
    public SortByPerformanceValue(boolean ascendingOrder)
    {
        this("Sort", ascendingOrder);
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
        super(name);
        _ascendingOrder = ascendingOrder;
    }


    /**
     * If true, sorts in the ascending order. False: in descending order.
     */
    private final boolean _ascendingOrder;

    /**
     * Phase main action (sorts the specimens stored in {@link SpecimensContainer#getPopulation()} by first performance values).
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        sort.Sort.sortByPerformanceValue(ea.getSpecimensContainer().getPopulation(), _ascendingOrder);
    }
}
