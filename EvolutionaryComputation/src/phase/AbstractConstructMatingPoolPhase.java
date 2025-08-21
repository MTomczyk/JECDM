package phase;

/**
 * Abstract class realizing the "Construct Mating Pool" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractConstructMatingPoolPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "CONSTRUCT_MATING_POOL").
     */
    public AbstractConstructMatingPoolPhase()
    {
        super("CONSTRUCT_MATING_POOL");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractConstructMatingPoolPhase(String name)
    {
        super(name);
    }
}
