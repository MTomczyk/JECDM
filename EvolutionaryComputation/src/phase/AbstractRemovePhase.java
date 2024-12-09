package phase;

/**
 * Abstract class realizing the "Remove" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractRemovePhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Remove").
     */
    public AbstractRemovePhase()
    {
        super("Remove", PhasesIDs.PHASE_REMOVE);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractRemovePhase(String name)
    {
        super(name, PhasesIDs.PHASE_REMOVE);
    }
}
