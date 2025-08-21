package phase;

/**
 * Abstract class realizing the "Finalize Step" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractFinalizeStepPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "FINALIZE_STEP").
     */
    public AbstractFinalizeStepPhase()
    {
        super("FINALIZE_STEP");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractFinalizeStepPhase(String name)
    {
        super(name);
    }
}
