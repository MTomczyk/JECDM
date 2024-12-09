package phase;

/**
 * Abstract class realizing the "Init Ends" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractInitEndsPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Init Ends").
     */
    public AbstractInitEndsPhase()
    {
        super("Init Ends", PhasesIDs.PHASE_INIT_ENDS);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractInitEndsPhase(String name)
    {
        super(name, PhasesIDs.PHASE_INIT_ENDS);
    }
}
