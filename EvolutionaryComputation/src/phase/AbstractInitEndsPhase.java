package phase;

/**
 * Abstract class realizing the "Init Ends" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractInitEndsPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "INIT_ENDS").
     */
    public AbstractInitEndsPhase()
    {
        super("INIT_ENDS");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractInitEndsPhase(String name)
    {
        super(name);
    }
}
