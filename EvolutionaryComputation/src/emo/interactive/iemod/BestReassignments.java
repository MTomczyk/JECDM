package emo.interactive.iemod;

import ea.IEA;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import population.SpecimensContainer;
import system.ds.Report;

/**
 * Default strategy that assumes assigning best-performing specimen to each goal.
 */
public class BestReassignments implements IGoalsUpdateReassignmentStrategy
{
    /**
     * The main method. Calls for {@link MOEADGoalsManager#makeBestAssignments(SpecimensContainer)}.
     *
     * @param report            report on the execution of the decision support system
     * @param ea                the evolutionary algorithm
     * @param moeadGoalsManager goals manager
     * @throws PhaseException phase exception
     */
    @Override
    public void update(Report report, IEA ea, MOEADGoalsManager moeadGoalsManager) throws PhaseException
    {
        moeadGoalsManager.makeBestAssignments(ea.getSpecimensContainer());
    }
}
