package emo.interactive.nemo.nemoii;

import criterion.Criteria;
import ea.AbstractPhasesEA;
import emo.aposteriori.nsgaii.NSGAIISort;
import emo.utils.front.FNDSorting;
import emo.utils.front.POFront;
import exception.PhaseException;
import model.IPreferenceModel;
import model.internals.AbstractInternalModel;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;
import population.Specimens;
import system.dm.DecisionMakerSystem;
import system.ds.DecisionSupportSystem;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Sorting procedure for the NEMO-II algorithm.
 *
 * @author MTomczyk
 */

public class NEMOIISort extends NSGAIISort implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     * @param DSS      reference to DSS
     */
    public NEMOIISort(Criteria criteria, DecisionSupportSystem DSS)
    {
        super("NEMO-II: Sort", criteria);
        _DMS = DSS.getDecisionMakersSystems()[0];
        _preferenceModel = _DMS.getModelSystems()[0].getPreferenceModel();
        _POF = new POFront();
    }

    /**
     * Decision maker's system.
     */
    protected final DecisionMakerSystem _DMS;

    /**
     * Object responsible for generating fronts of potential optimality.
     */
    protected final POFront _POF;

    /**
     * Reference to the cone model.
     */
    protected final IPreferenceModel<? extends AbstractInternalModel> _preferenceModel;

    /**
     * Phase's main action.
     *
     * @param ea evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown 
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public void action(AbstractPhasesEA ea, PhaseReport report) throws PhaseException
    {
        // Derive models
        ArrayList<? extends AbstractInternalModel> models = _preferenceModel.getInternalModels();

        if ((_DMS.getHistory().getNoPreferenceExamples() == 0) || (models == null) || (models.isEmpty()))
        {
            super.action(ea, report); //delegate to nsga-ii
            return;
        }

        // do nemo-ii

        // Instantiate new population
        ArrayList<Specimen> newPopulation = new ArrayList<>(ea.getPopulationSize());

        // Identify the required number of non-dominated fronts
        LinkedList<LinkedList<Integer>> fronts = _POF.getFrontAssignments(
                new Specimens(ea.getSpecimensContainer().getPopulation()), models, ea.getPopulationSize());

        // Pass as many fronts as possible and determine the ambiguous front
        FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation,
                ea.getSpecimensContainer().getPopulation(), fronts, ea.getPopulationSize());

        // only if there is an ambiguous front
        if (aFront != null)
        {
            // construct the ambiguous population
            ArrayList<Specimen> lastFront =  constructLastFront(ea, aFront);
            for (int i = 0; i < ea.getPopulationSize() - aFront._passedMembers; i++)
                newPopulation.add(lastFront.get(i));
        }

        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }

}
