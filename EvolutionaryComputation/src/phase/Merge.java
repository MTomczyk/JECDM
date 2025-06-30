package phase;

import ea.EA;
import exception.PhaseException;
import population.SpecimensContainer;

/**
 * Default "merge" phase.
 * The default (implicit) assumptions are as follows:
 * - It adds the specimens stored in {@link SpecimensContainer#getOffspring()} to the ones stored in
 * {@link SpecimensContainer#getPopulation()} (see {@link SpecimensContainer#mergeOffspringAndPopulation()}).
 * - Merged population size will equal population size + offspring size.
 *
 * @author MTomczyk
 */


public class Merge extends AbstractMergePhase implements IPhase
{
    /**
     * Default constructor.
     */
    public Merge()
    {
        this("Merge");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public Merge(String name)
    {
        super(name);
    }

    /**
     * Phase main action (merges offspring and population).
     * The default (implicit) assumptions are as follows:
     * - It adds the specimens stored in {@link SpecimensContainer#getOffspring()} to the ones stored in
     * {@link SpecimensContainer#getPopulation()} (see {@link SpecimensContainer#mergeOffspringAndPopulation()}).
     * - Merged population size will equal population size + offspring size.
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        ea.getSpecimensContainer().mergeOffspringAndPopulation();
    }
}
