package phase;

/**
 * Abstract class realizing the "Update OS" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractUpdateOSPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "UPDATE_OS").
     */
    public AbstractUpdateOSPhase()
    {
        super("UPDATE_OS");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractUpdateOSPhase(String name)
    {
        super(name);
    }
}
