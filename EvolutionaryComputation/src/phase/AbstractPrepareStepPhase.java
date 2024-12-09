package phase;

/**
 * Abstract class realizing the "Prepare Step" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractPrepareStepPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Prepare Step").
     */
    public AbstractPrepareStepPhase()
    {
        super("Prepare Step", PhasesIDs.PHASE_PREPARE_STEP);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractPrepareStepPhase(String name)
    {
        super(name, PhasesIDs.PHASE_PREPARE_STEP);
    }
}
