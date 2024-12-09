package phase;

/**
 * Abstract class realizing the "Init starts" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractInitStartsPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Init Starts").
     */
    public AbstractInitStartsPhase()
    {
        super("Init Starts", PhasesIDs.PHASE_INIT_STARTS);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractInitStartsPhase(String name)
    {
        super(name, PhasesIDs.PHASE_INIT_STARTS);
    }
}
