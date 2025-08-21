package phase;

import ea.AbstractPhasesEA;
import exception.PhaseException;

/**
 * Base implementation of the {@link IPhase} interface.
 *
 * @author MTomczyk
 */
public abstract class AbstractPhase implements IPhase
{
    /**
     * The name of the phase.
     */
    public final String _name;

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     */
    public AbstractPhase(String name)
    {
        _name = name;
    }

    /**
     * Executes the pre-action, main action, and post action.
     *
     * @param ea evolutionary algorithm
     * @return report on the executed action
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public PhaseReport perform(AbstractPhasesEA ea) throws PhaseException
    {
        PhaseReport report = new PhaseReport();
        preAction(ea, report);
        action(ea, report);
        postAction(ea, report);
        return report;
    }

    /**
     * Executes a step preceding the main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    public void preAction(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        if ((ea.getComputeExecutionTimes() || ea.getComputePhasesExecutionTimes()))
            report._startTime = System.nanoTime();
    }


    /**
     *  Executes the main action of the phase (to be overwritten).
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    //@Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {

    }

    /**
     * Executes a step succeeding the main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    public void postAction(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        if ((ea.getComputeExecutionTimes() || ea.getComputePhasesExecutionTimes()))
        {
            report._stopTime = System.nanoTime();
            report._elapsedTime = ((double) (report._stopTime - report._startTime) / 1000000.0);
        }
    }

    /**
     * Returns the string representation ("Phase: " + name).
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return "Phase: " + _name;
    }

    /**
     * Returns the phase name
     *
     * @return name
     */
    @Override
    public String getName()
    {
        return _name;
    }
}
