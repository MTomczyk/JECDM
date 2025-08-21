package phase;

/**
 * Abstract class realizing the "Prepare Step" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractPrepareStepPhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "PREPARE_STEP").
     */
    public AbstractPrepareStepPhase()
    {
        super("PREPARE_STEP");
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractPrepareStepPhase(String name)
    {
        super(name);
    }
}
