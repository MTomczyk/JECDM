package emo.interactive.nemo.nemo0;

import criterion.Criteria;
import ea.AbstractPhasesEA;
import emo.aposteriori.nsgaii.NSGAIISort;
import emo.utils.front.FNDSorting;
import exception.PhaseException;
import exeption.PreferenceModelException;
import model.IPreferenceModel;
import model.internals.AbstractInternalModel;
import org.apache.commons.math4.legacy.stat.StatUtils;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;
import population.Specimens;
import system.dm.DecisionMakerSystem;
import system.ds.DecisionSupportSystem;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Sorting procedure for the NEMO-0 algorithm.
 *
 * @author MTomczyk
 */

public class NEMO0Sort extends NSGAIISort implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     * @param DSS      reference to DSS
     */
    public NEMO0Sort(Criteria criteria, DecisionSupportSystem DSS)
    {
        super("NEMO-0: Sort", criteria);
        _DMS = DSS.getDecisionMakersSystems()[0];
        _preferenceModel = _DMS.getModelSystems()[0].getPreferenceModel();
    }

    /**
     * Decision maker's system.
     */
    protected final DecisionMakerSystem _DMS;

    /**
     * Reference to the cone model.
     */
    protected final IPreferenceModel<? extends AbstractInternalModel> _preferenceModel;

    /**
     * Phase's main action.
     *
     * @param ea     evolutionary algorithm
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

        // do nemo-0

        // Instantiate new population
        ArrayList<Specimen> newPopulation = new ArrayList<>(ea.getPopulationSize());

        // Identify the required number of non-dominated fronts
        LinkedList<LinkedList<Integer>> fronts = _FND.getFrontAssignments(
                new Specimens(ea.getSpecimensContainer().getPopulation()), ea.getPopulationSize());

        // Pass as many fronts as possible and determine the ambiguous front
        FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation,
                ea.getSpecimensContainer().getPopulation(), fronts, ea.getPopulationSize());

        // only if there is an ambiguous front
        if (aFront != null)
        {
            // construct the ambiguous population
            ArrayList<Specimen> lastFront = new ArrayList<>(ea.getPopulationSize() - aFront._passedMembers);

            double[] eval = new double[aFront._front.size()];
            int t = 0;
            for (Integer idx : aFront._front)
            {
                Specimen candidate = ea.getSpecimensContainer().getPopulation().get(idx);
                lastFront.add(candidate);
                try
                {
                    eval[t++] = _preferenceModel.evaluate(candidate.getAlternative());
                } catch (PreferenceModelException e)
                {
                    throw new PhaseException("Error occurred when evaluating an alternative " + e.getDetailedReasonMessage(), this.getClass(), e);
                }
            }
            double maxEval = StatUtils.max(eval);
            if (Double.compare(maxEval, 0.0d) == 0) maxEval = 1.0;
            t = 0;
            if (_preferenceModel.isLessPreferred())
            {
                for (Integer idx : aFront._front)
                {
                    Specimen candidate = ea.getSpecimensContainer().getPopulation().get(idx);
                    candidate.setAuxScore(aFront._passedFronts + eval[t++] / maxEval);
                }
            }
            else
            {
                for (Integer idx : aFront._front)
                {
                    Specimen candidate = ea.getSpecimensContainer().getPopulation().get(idx);
                    candidate.setAuxScore(aFront._passedFronts + 1.0d - (eval[t++] / maxEval));
                }
            }

            sort.Sort.sortByAuxValue(lastFront, true);
            for (int i = 0; i < ea.getPopulationSize() - aFront._passedMembers; i++)
                newPopulation.add(lastFront.get(i));
        }

        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }

}
