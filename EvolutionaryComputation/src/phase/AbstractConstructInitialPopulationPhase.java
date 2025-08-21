package phase;

/**
 * Abstract class realizing the "Construct initial population" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractConstructInitialPopulationPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "CONSTRUCT_INITIAL_POPULATION").
     */
    public AbstractConstructInitialPopulationPhase()
    {
        super("CONSTRUCT_INITIAL_POPULATION");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractConstructInitialPopulationPhase(String name)
    {
        super(name);
    }
}
