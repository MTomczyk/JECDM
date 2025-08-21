package emo.aposteriori.moead;

import ea.AbstractPhasesEA;
import ea.EA;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import phase.AbstractInitEndsPhase;
import phase.IPhase;
import phase.PhaseReport;

/**
 * Default MOEA/D "init ends" phase (after the initial population is constructed).
 * It establishes the neighborhood structure, constructs goals updates sequence, and performs specimens->goals assignments.
 *
 * @author MTomczyk
 */

public class MOEADInitEnds extends AbstractInitEndsPhase implements IPhase
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
    public MOEADInitEnds(MOEADGoalsManager goalManager)
    {
        super("MOEA/D: Init ends");
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
        _goalManager.establishNeighborhood();
        _goalManager.determineUpdatesSequence(ea.getR());
        _goalManager.makeArbitraryAssignments(ea.getSpecimensContainer());
    }
}
