package phase;

/**
 * Abstract class realizing the "Sort" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractSortPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Sort").
     */
    public AbstractSortPhase()
    {
        super("Sort", PhasesIDs.PHASE_SORT);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractSortPhase(String name)
    {
        super(name, PhasesIDs.PHASE_SORT);
    }
}
