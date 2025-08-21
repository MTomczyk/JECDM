package emo.aposteriori.moead;

import ea.AbstractPhasesEA;
import ea.EA;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import phase.FinalizeStep;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;


/**
 * Default MOEA/D "finalize step" phase.
 * It updates specimen-goals assignments.
 *
 * @author MTomczyk
 */

public class MOEADFinalizeStep extends FinalizeStep implements IPhase
{
    /**
     * MOEA/D goals manager.
     */
    private final MOEADGoalsManager _goalManager;

    /**
     * Parameterized constructor.
     *
     * @param goalManager MOEA/D goal manager
     */
    public MOEADFinalizeStep(MOEADGoalsManager goalManager)
    {
        super("MOEA/D: Finalize step");
        _goalManager = goalManager;
    }


    /**
     * Phase's main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        super.action(ea, report);
        GoalID GL = _goalManager.getCurrentGoalToBeUpdated(ea.getCurrentSteadyStateRepeat());
        Specimen O = ea.getSpecimensContainer().getOffspring().get(0);
        _goalManager.executeUpdate(O, GL, ea.getSpecimensContainer());
    }
}
