package emo.aposteriori.moead;

import ea.EA;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import phase.AbstractConstructMatingPoolPhase;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;

import java.util.ArrayList;

/**
 * Default MOEA/D "construct mating pool" phase.
 * It establishes the mating pool based on the current goal and its neighborhood.
 *
 * @author MTomczyk
 */

public class MOEADConstructMatingPool extends AbstractConstructMatingPoolPhase implements IPhase
{
    /**
     * MOEA/D goal manager.
     */
    private final MOEADGoalsManager _goalManager;

    /**
     * Parameterized constructor.
     *
     * @param goalManager MOEA/D goal manager
     */
    public MOEADConstructMatingPool(MOEADGoalsManager goalManager)
    {
        super("MOEA/D: Construct mating pool");
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
        GoalID GL = _goalManager.getCurrentGoalToBeUpdated(ea.getCurrentSteadyStateRepeat());
        ArrayList<Specimen> MP = _goalManager.createMatingPool(GL);
        ea.getSpecimensContainer().setMatingPool(MP);
    }
}
