package emo.interactive.iemod;

import ea.IEA;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import system.ds.Report;

/**
 * Auxiliary interface for solution assignment strategies triggered after updating the optimization goals and
 * re-establishing the neighborhood. The updates might have been triggered due to executing preference learning or due
 * to updating the algorithm's internal data on the objective space bounds. It is expected that the implementation will
 * suitably adjust/create the {@link emo.utils.decomposition.goal.Assignment} objects maintained by
 * {@link MOEADGoalsManager}.
 */
public interface IGoalsUpdateReassignmentStrategy
{
    /**
     * The main method for updating the assignments.
     *
     * @param report            report on the most recent model update call in the decision support system
     * @param ea                the reference to the evolutionary algorithm
     * @param moeadGoalsManager MOEA/D goals manager
     * @throws PhaseException an exception can be thrown and propagated higher
     */
    void update(Report report, IEA ea, MOEADGoalsManager moeadGoalsManager) throws PhaseException;
}
