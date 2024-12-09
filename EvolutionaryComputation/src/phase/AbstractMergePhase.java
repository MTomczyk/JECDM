package phase;

/**
 * Abstract class realizing the "Merge" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractMergePhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Merge").
     */
    public AbstractMergePhase()
    {
        super("Merge", PhasesIDs.PHASE_MERGE);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractMergePhase(String name)
    {
        super(name, PhasesIDs.PHASE_MERGE);
    }
}
