package phase;

import ea.EA;
import exception.PhaseException;
import exeption.DecisionSupportSystemException;
import system.ds.DecisionSupportSystem;

/**
 * Default 'init starts' phase that is used by preference-based methods. Its purpose is to start decision support
 * module by calling {@link DecisionSupportSystem#notifySystemStarts()}.
 *
 * @author MTomczyk
 */
public class InitStartsRunDSS extends AbstractInitStartsPhase implements IPhase
{
    /**
     * Reference to the decision support system.
     */
    private final DecisionSupportSystem _DSS;

    /**
     * Parameterized constructor.
     *
     * @param DSS decision support system
     */
    public InitStartsRunDSS(DecisionSupportSystem DSS)
    {
        this("Init starts (run DSS)", DSS);
    }

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     * @param DSS  decision support system
     */
    public InitStartsRunDSS(String name, DecisionSupportSystem DSS)
    {
        super(name);
        _DSS = DSS;
    }

    /**
     * Executes the main action of the phase (calls {@link DecisionSupportSystem#notifySystemStarts()}).
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        try
        {
            _DSS.notifySystemStarts();
        } catch (DecisionSupportSystemException e)
        {
            throw new PhaseException("Could not start DSS " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

}
