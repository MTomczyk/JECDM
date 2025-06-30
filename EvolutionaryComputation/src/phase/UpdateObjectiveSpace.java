package phase;

import ea.EA;
import ea.EATimestamp;
import exception.PhaseException;
import os.ObjectiveSpaceManager;

/**
 * Default "Update OS" phase. Delegates the update to {@link ObjectiveSpaceManager}.
 *
 * @author MTomczyk
 */


public class UpdateObjectiveSpace extends AbstractUpdateOSPhase implements IPhase
{
    /**
     * Object responsible for updating knowledge on the objective space
     */
    protected final ObjectiveSpaceManager _osManager;

    /**
     * Parameterized constructor.
     *
     * @param osManager object responsible for updating knowledge on the objective space
     */
    public UpdateObjectiveSpace(ObjectiveSpaceManager osManager)
    {
        this("Update Objective Space", osManager);
    }

    /**
     * Parameterized constructor.
     *
     * @param name     name of the phase
     * @param osManager object responsible for updating knowledge on the objective space
     */
    public UpdateObjectiveSpace(String name, ObjectiveSpaceManager osManager)
    {
        super(name);
        _osManager = osManager;
    }

    /**
     * Phase main action. Delegates the update to {@link ObjectiveSpaceManager}.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        _osManager.update(ea.getSpecimensContainer(), new EATimestamp(ea.getCurrentGeneration(), ea.getCurrentSteadyStateRepeat()));
    }
}
