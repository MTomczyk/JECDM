package phase;

import ea.EA;
import exception.PhaseException;
import population.SpecimensContainer;

/**
 * Default "Create Mating Pool" phase.
 * The default (implicit) assumptions are as follows:
 * - The population stored in {@link SpecimensContainer#getPopulation()} is considered a mating pool.
 *
 * @author MTomczyk
 */


public class ConstructMatingPool extends AbstractConstructMatingPoolPhase implements IPhase
{
    /**
     * Default constructor.
     */
    public ConstructMatingPool()
    {
        this("Construct Mating Pool");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public ConstructMatingPool(String name)
    {
        super(name);
    }

    /**
     * Phase main action. Creates the mating pool.
     * The default (implicit) assumptions are as follows:
     * - The population stored in {@link SpecimensContainer#getPopulation()} is considered a mating pool.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        ea.getSpecimensContainer().setMatingPool(ea.getSpecimensContainer().getPopulation());
    }

}
