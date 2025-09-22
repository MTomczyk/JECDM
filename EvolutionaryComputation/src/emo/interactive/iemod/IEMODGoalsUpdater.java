package emo.interactive.iemod;

import ea.IEA;
import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.PreferenceValueModel;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;
import system.ds.Report;

import java.util.ArrayList;


/**
 * Auxiliary class that provides a method for updating the optimization goals maintained by IEMO/D (called when the
 * preference model was successfully updated or there was a change in the objective space).
 *
 * @author MTomczyk
 */
public class IEMODGoalsUpdater<T extends AbstractValueInternalModel>
{

    /**
     * Reference to the preference model
     */
    private final IPreferenceModel<T> _preferenceModel;

    /**
     * Reference to the "goals manager".
     */
    private final MOEADGoalsManager _goalsManager;

    /**
     * Solution assignment strategy triggered after updating the optimization goals and re-establishing the
     * neighborhood. The updates might have been triggered due to executing preference learning or due to updating the
     * algorithm's internal data on the objective space bounds. The implementation is expected to suitably adjust/create
     * the {@link emo.utils.decomposition.goal.Assignment} objects maintained by {@link MOEADGoalsManager}.
     */
    private final IGoalsUpdateReassignmentStrategy _reassignmentStrategy;


    /**
     * Parameterized constructor.
     *
     * @param preferenceModel preference model
     * @param goalsManager    goals manager
     */
    @Deprecated
    public IEMODGoalsUpdater(IPreferenceModel<T> preferenceModel,
                             MOEADGoalsManager goalsManager)
    {
        this(preferenceModel, goalsManager, new BestReassignments());
    }


    /**
     * Parameterized constructor.
     *
     * @param preferenceModel      preference model
     * @param goalsManager         goals manager
     * @param reassignmentStrategy goals updater reassignment strategy
     */
    public IEMODGoalsUpdater(IPreferenceModel<T> preferenceModel,
                             MOEADGoalsManager goalsManager,
                             IGoalsUpdateReassignmentStrategy reassignmentStrategy)
    {
        _preferenceModel = preferenceModel;
        _goalsManager = goalsManager;
        _reassignmentStrategy = reassignmentStrategy;
    }

    /**
     * Can be called to retrieve preference models from DSS and set them as new preference directions for the MOEA/D
     * framework.
     *
     * @param report report on the last execution of the decision support system
     * @param ea     parent evolutionary algorithm
     * @throws PhaseException the exception can be called
     */
    public void updateGoals(Report report, IEA ea) throws PhaseException
    {
        ArrayList<? extends AbstractValueInternalModel> models = _preferenceModel.getInternalModels();
        Family F = _goalsManager.getFamilies()[0];
        int no = F.getSize();
        if ((models == null) || (models.size() < no))
            throw PhaseException.getInstanceWithSource("There is not enough internal models to supply IEMO/D",
                    this.getClass());

        IGoal[] goals = new IGoal[no];
        for (int i = 0; i < no; i++) goals[i] = new PreferenceValueModel(models.get(i));
        F.replaceGoals(goals);
        _goalsManager.establishNeighborhood();
        _reassignmentStrategy.update(report, ea, _goalsManager); // make assignments
        _goalsManager.updatePopulationAsImposedByAssignments(ea.getSpecimensContainer());
    }

}
