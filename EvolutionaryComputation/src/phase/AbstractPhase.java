package phase;

import ea.EA;
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
     * Unique ID number.
     */
    protected final int _id;

    /**
     * Parameterized constructor.
     *
     * @param name name of the phase
     * @param id   phase id
     */
    public AbstractPhase(String name, int id)
    {
        _name = name;
        _id = id;
    }

    /**
     * Executes the pre-action, main action, and post action.
     *
     * @param ea evolutionary algorithm
     * @return report on the executed action
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public PhaseReport perform(EA ea) throws PhaseException
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
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    public void preAction(EA ea, PhaseReport report) throws PhaseException
    {
        if ((ea.getComputeExecutionTimes() || ea.getComputePhasesExecutionTimes()))
            report._startTime = System.nanoTime();
    }


    /**
     *  Executes the main action of the phase (to be overwritten).
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    //@Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {

    }

    /**
     * Executes a step succeeding the main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    public void postAction(EA ea, PhaseReport report) throws PhaseException
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

    /**
     * Returns phase unique identifier (see {@link PhasesIDs}).
     *
     * @return ID
     */
    @Override
    public int getID()
    {
        return _id;
    }
}
