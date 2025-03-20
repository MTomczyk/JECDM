package phase;

import ea.EA;
import exception.PhaseException;
import population.Specimen;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * A simple extension of {@link AbstractSortPhase} that delivers convenient means for sorting specimens stored in
 * {@link SpecimensContainer#getPopulation()}. The sorting is delegated to an object implementing an auxiliary
 * {@link ISort} interface.
 *
 * @author MTomczyk
 */
public class Sort extends AbstractSortPhase implements IPhase
{
    /**
     * Auxiliary interface for sorting specimens. The array elements are assumed to be sorted in the same object
     * (no new object should be constructed).
     */
    public interface ISort
    {
        /**
         * The main method's signature for sorting specimens. The input array elements are assumed to be sorted in the
         * same object (no new object should be constructed).
         *
         * @param specimens array to be sorted
         */
        void sort(ArrayList<Specimen> specimens);
    }

    /**
     * Object responsible for sorting specimens.
     */
    private final ISort _sorter;

    /**
     * Parameterized constructor.
     *
     * @param sorter auxiliary object responsible for sorting specimens
     */
    public Sort(ISort sorter)
    {
        this("Sort", sorter);
    }

    /**
     * Parameterized constructor.
     *
     * @param sorter auxiliary object responsible for sorting specimens
     * @param name name of the phase
     */
    public Sort(String name, ISort sorter)
    {
        super(name);
        _sorter = sorter;
    }


    /**
     * Phase main action (sorts the specimens stored in {@link SpecimensContainer#getPopulation()} by first performance values).
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        _sorter.sort(ea.getSpecimensContainer().getPopulation());
    }
}
