package emo.aposteriori.moead;

import ea.EA;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import phase.AbstractPrepareStepPhase;
import phase.IPhase;
import phase.PhaseReport;

/**
 * Default MOEA/D "prepare step" phase.
 * It constructs goals update sequence.
 *
 * @author MTomczyk
 */

public class MOEADPrepareStep extends AbstractPrepareStepPhase implements IPhase
{
    /**
     * MOEA/D goal manager.
     */
    protected final MOEADGoalsManager _goalManager;

    /**
     * Parameterized constructor.
     *
     * @param goalManager MOEA/D goal manager
     */
    public MOEADPrepareStep(MOEADGoalsManager goalManager)
    {
        super("MOEA/D: Prepare step");
        _goalManager = goalManager;
    }


    /**
     * Phase's main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        if (ea.getCurrentSteadyStateRepeat() == 0) // only in the first steady-state repeat
            _goalManager.determineUpdatesSequence(ea.getR());
    }
}
