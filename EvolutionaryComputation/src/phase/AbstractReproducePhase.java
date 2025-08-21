package phase;

/**
 * Abstract class realizing the "Reproduce" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractReproducePhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "REPRODUCE").
     */
    public AbstractReproducePhase()
    {
        super("REPRODUCE");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractReproducePhase(String name)
    {
        super(name);
    }
}
