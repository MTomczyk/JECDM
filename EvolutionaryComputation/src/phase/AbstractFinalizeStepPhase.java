package phase;

/**
 * Abstract class realizing the "Finalize Step" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractFinalizeStepPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Finalize Step").
     */
    public AbstractFinalizeStepPhase()
    {
        super("Finalize Step", PhasesIDs.PHASE_FINALIZE_STEP);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractFinalizeStepPhase(String name)
    {
        super(name, PhasesIDs.PHASE_FINALIZE_STEP);
    }
}
