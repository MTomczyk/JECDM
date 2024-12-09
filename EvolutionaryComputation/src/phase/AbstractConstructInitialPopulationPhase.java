package phase;

/**
 * Abstract class realizing the "Construct initial population" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractConstructInitialPopulationPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Construct Initial Population").
     */
    public AbstractConstructInitialPopulationPhase()
    {
        super("Construct Initial Population", PhasesIDs.PHASE_CONSTRUCT_INITIAL_POPULATION);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractConstructInitialPopulationPhase(String name)
    {
        super(name, PhasesIDs.PHASE_CONSTRUCT_INITIAL_POPULATION);
    }
}
