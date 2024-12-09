package phase;

/**
 * Abstract class realizing the "Construct Mating Pool" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractConstructMatingPoolPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Construct Mating Pool").
     */
    public AbstractConstructMatingPoolPhase()
    {
        super("Construct Mating Pool", PhasesIDs.PHASE_CONSTRUCT_MATING_POOL);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractConstructMatingPoolPhase(String name)
    {
        super(name, PhasesIDs.PHASE_CONSTRUCT_MATING_POOL);
    }
}
