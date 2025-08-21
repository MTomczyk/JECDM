package phase;

/**
 * Abstract class realizing the "Assign specimen IDs" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractAssignSpecimenIDs extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "ASSIGN_SPECIMENS_IDS").
     */
    public AbstractAssignSpecimenIDs()
    {
        super("ASSIGN_SPECIMENS_IDS");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractAssignSpecimenIDs(String name)
    {
        super(name);
    }
}
