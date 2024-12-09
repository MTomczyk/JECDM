package phase;

/**
 * Abstract class realizing the "Assign specimen IDs" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractAssignSpecimenIDs extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Assign Specimen IDs").
     */
    public AbstractAssignSpecimenIDs()
    {
        super("Assign Specimen IDs", PhasesIDs.PHASE_ASSIGN_SPECIMENS_IDS);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractAssignSpecimenIDs(String name)
    {
        super(name, PhasesIDs.PHASE_ASSIGN_SPECIMENS_IDS);
    }
}
