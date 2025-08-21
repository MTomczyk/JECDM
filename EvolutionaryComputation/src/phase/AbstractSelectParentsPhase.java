package phase;

/**
 * Abstract class realizing the "Select Parents" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractSelectParentsPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "SELECT_PARENTS").
     */
    public AbstractSelectParentsPhase()
    {
        super("SELECT_PARENTS");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractSelectParentsPhase(String name)
    {
        super(name);
    }
}
