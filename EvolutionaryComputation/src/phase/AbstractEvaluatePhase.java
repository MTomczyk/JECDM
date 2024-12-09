package phase;

/**
 * Abstract class realizing the "Evaluate" phase.
 *
 * @author MTomczyk
 */
public abstract class AbstractEvaluatePhase extends AbstractPhase implements IPhase
{
    /**
     * Default constructor (sets the name to "Evaluate").
     */
    public AbstractEvaluatePhase()
    {
        super("Evaluate", PhasesIDs.PHASE_EVALUATE);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractEvaluatePhase(String name)
    {
        super(name, PhasesIDs.PHASE_EVALUATE);
    }
}
