package emo.interactive.iemod;

import ea.IEA;
import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.PreferenceValueModel;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import exception.PhaseException;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;


/**
 * Auxiliary class that provides a method for updating the optimization goals maintained by IEMO/D (called when the
 * preference model was successfully updated).
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
     * Parameterized constructor.
     *
     * @param preferenceModel preference model
     * @param goalsManager    goals manager
     */
    public IEMODGoalsUpdater(IPreferenceModel<T> preferenceModel,
                             MOEADGoalsManager goalsManager)
    {
        _preferenceModel = preferenceModel;
        _goalsManager = goalsManager;
    }

    /**
     * Can be called to retrieve preference models from DSS and set them as new preference directions for the MOEA/D framework.
     *
     * @param ea     parent evolutionary algorithm
     * @throws PhaseException the exception can be called 
     */
    public void updateGoals(IEA ea) throws PhaseException
    {
        ArrayList<? extends AbstractValueInternalModel> models = _preferenceModel.getInternalModels();
        Family F = _goalsManager.getFamilies()[0];
        int no = F.getSize();
        if ((models == null) || (models.size() < no))
            throw new PhaseException("There is not enough internal models to supply IEMO/D", this.getClass());

        IGoal[] goals = new IGoal[no];
        for (int i = 0; i < no; i++) goals[i] = new PreferenceValueModel(models.get(i));
        F.replaceGoals(goals);
        _goalsManager.establishNeighborhood();
        _goalsManager.makeBestAssignments(ea.getSpecimensContainer());
        _goalsManager.updatePopulationAsImposedByAssignments(ea.getSpecimensContainer());
    }

}
