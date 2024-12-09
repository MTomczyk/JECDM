package phase;

import ea.EA;
import exception.PhaseException;
import reproduction.IReproduce;

/**
 * Default "reproduce" phase.
 * The default (implicit) assumptions are as follows:
 * - Offspring creation is delegated to {@link IReproduce}.
 * - The number of offspring to create equals the offspring size {@link EA#getOffspringSize()}.
 *
 * @author MTomczyk
 */


public class Reproduce extends AbstractReproducePhase implements IPhase
{
    /**
     * Object constructing new offspring solutions from parents.
     */
    protected final IReproduce _reproduce;

    /**
     * Parameterized constructor.
     *
     * @param reproduce object responsible for constructing offspring
     */
    public Reproduce(IReproduce reproduce)
    {
        this("Reproduce", reproduce);
    }

    /**
     * Parameterized constructor.
     *
     * @param name      name of the phase
     * @param reproduce object responsible for constructing offspring
     */
    public Reproduce(String name, IReproduce reproduce)
    {
        super(name);
        _reproduce = reproduce;
    }

    /**
     * Phase main action.
     * The default (implicit) assumptions are as follows:
     * - Offspring creation is delegated to {@link IReproduce}.
     * - The number of offspring to create equals the offspring size {@link EA#getOffspringSize()}.
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        ea.getSpecimensContainer().setOffspring(_reproduce.createOffspring(ea));
        ea.getSpecimensContainer().setOffspringRequiresEvaluation(true);
        ea.getSpecimensContainer().setOffspringRequiresIDAssignment(true);
    }
}
