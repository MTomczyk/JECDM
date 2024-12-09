package phase;

/**
 * Abstract class realizing the "Update OS" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractUpdateOSPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Update OS").
     */
    public AbstractUpdateOSPhase()
    {
        super("Update OS", PhasesIDs.PHASE_UPDATE_OS);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractUpdateOSPhase(String name)
    {
        super(name, PhasesIDs.PHASE_UPDATE_OS);
    }
}
