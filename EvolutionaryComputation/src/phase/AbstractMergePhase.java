package phase;

/**
 * Abstract class realizing the "Merge" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractMergePhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "MERGE").
     */
    public AbstractMergePhase()
    {
        super("MERGE");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractMergePhase(String name)
    {
        super(name);
    }
}
