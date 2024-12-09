package phase;

/**
 * Abstract class realizing the "Reproduce" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractReproducePhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Reproduce").
     */
    public AbstractReproducePhase()
    {
        super("Reproduce", PhasesIDs.PHASE_REPRODUCE);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractReproducePhase(String name)
    {
        super(name, PhasesIDs.PHASE_REPRODUCE);
    }
}
