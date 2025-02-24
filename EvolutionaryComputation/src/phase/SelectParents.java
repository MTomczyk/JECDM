package phase;

import ea.EA;
import exception.PhaseException;
import population.SpecimensContainer;
import selection.ISelect;

import java.util.ArrayList;

/**
 * Default "select parents" phase.
 * The default (implicit) assumptions are as follows:
 * - The selection is delegated to {@link ISelect}.
 * - The specimens will be selected from the ones contained in {@link SpecimensContainer#getMatingPool()}.
 * - The parents will be stored as {@link SpecimensContainer#setParents(ArrayList)}.
 *
 * @author MTomczyk
 */


public class SelectParents extends AbstractSelectParentsPhase implements IPhase
{
    /**
     * Object responsible for selecting parents from the mating pool.
     */
    protected final ISelect _select;

    /**
     * Parameterized constructor.
     *
     * @param select object responsible for selecting parents
     */
    public SelectParents(ISelect select)
    {
        this("Select", select);
    }

    /**
     * Parameterized constructor.
     *
     * @param name   name of the phase
     * @param select object responsible for selecting parents
     */
    public SelectParents(String name, ISelect select)
    {
        super(name);
        _select = select;
    }

    /**
     * Phase main action.
     * The default (implicit) assumptions are as follows:
     * - The selection is delegated to {@link ISelect}.
     * - The specimens will be selected from the ones contained in {@link SpecimensContainer#getMatingPool()}.
     * - The parents will be stored as {@link SpecimensContainer#setParents(ArrayList)}.
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        ea.getSpecimensContainer().setParents(_select.selectParents(ea));
    }
}
