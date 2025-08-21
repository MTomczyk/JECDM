package phase;

/**
 * Abstract class realizing the "Init starts" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractInitStartsPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "INIT_STARTS").
     */
    public AbstractInitStartsPhase()
    {
        super("INIT_STARTS");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractInitStartsPhase(String name)
    {
        super(name);
    }
}
