package emo.interactive.ktscone.cdemo;

import criterion.Criteria;
import ea.AbstractPhasesEA;
import emo.aposteriori.nsgaii.NSGAIISort;
import emo.utils.front.FNDSorting;
import exception.PhaseException;
import exeption.PreferenceModelException;
import model.definitions.KTSCone;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;
import population.Specimens;
import system.dm.DecisionMakerSystem;
import system.ds.DecisionSupportSystem;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of CDEMO sorting procedure. Note that this phase overwrites the
 * current population. Hence, there is no need to call the "remove" phase.
 *
 * @author MTomczyk
 */

public class CDEMOSort extends NSGAIISort implements IPhase
{

    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     * @param DSS      reference to DSS
     */
    public CDEMOSort(Criteria criteria, DecisionSupportSystem DSS)
    {
        super("CDEMO: Sort", criteria);
        _DMS = DSS.getDecisionMakersSystems()[0];
        _coneModel = (KTSCone) _DMS.getModelSystems()[0].getPreferenceModel();
    }

    /**
     * Decision maker's system.
     */
    protected final DecisionMakerSystem _DMS;

    /**
     * Reference to the cone model.
     */
    protected final KTSCone _coneModel;

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
        if (_DMS.getHistory().getNoPreferenceExamples() == 0)
        {
            super.action(ea, report); //delegate to nsga-ii
            return;
        }

        // Instantiate new population
        ArrayList<Specimen> newPopulation = new ArrayList<>(ea.getPopulationSize());
        double[] evals;
        try
        {
            evals = _coneModel.evaluateAlternatives(new Specimens(ea.getSpecimensContainer().getPopulation()))._evaluations;
        } catch (PreferenceModelException e)
        {
            throw new PhaseException("Could not evaluate alternatives " + e.getDetailedReasonMessage(), this.getClass(), e);
        }

        // Cone fronts
        LinkedList<LinkedList<Integer>> coneFronts = Utils.getConeFronts(evals);

        // Pass as many fronts as possible and determine the ambiguous front
        FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation,
                ea.getSpecimensContainer().getPopulation(), coneFronts, ea.getPopulationSize());

        // only if there is an ambiguous front
        if (aFront != null)
        {
            ArrayList<Specimen> toPartition = new ArrayList<>(aFront._front.size());
            for (Integer idx : aFront._front) toPartition.add(ea.getSpecimensContainer().getPopulation().get(idx));
            LinkedList<LinkedList<Integer>> fronts = _FND.getFrontAssignments(new Specimens(toPartition));

            // pass the remaining alternatives arbitrarily
            int remaining = ea.getPopulationSize() - aFront._passedMembers;
            int aux = 1;
            for (LinkedList<Integer> front : fronts)
            {
                for (Integer index : front)
                {
                    Specimen specimen = toPartition.get(index);
                    specimen.setAuxScore(aFront._passedFronts + aux);
                    newPopulation.add(specimen);
                    remaining--;
                    if (remaining == 0) break;
                }
                aux++;
                if (remaining == 0) break;
            }

        }


        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }
}
