package phase;

import ea.EA;
import exception.PhaseException;
import population.SpecimensContainer;

/**
 * Sorts specimens stored in {@link SpecimensContainer#getPopulation()} according to their first aux values.
 *
 * @author MTomczyk
 */


public class SortByAuxValue extends AbstractSortPhase implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param ascendingOrder if true, specimens are sorted in ascending order according to their first aux values;
     *                       false: in descending order.
     */
    public SortByAuxValue(boolean ascendingOrder)
    {
        this("Sort", ascendingOrder);
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
        sort.Sort.sortByAuxValue(ea.getSpecimensContainer().getPopulation(), _ascendingOrder);
    }
}
