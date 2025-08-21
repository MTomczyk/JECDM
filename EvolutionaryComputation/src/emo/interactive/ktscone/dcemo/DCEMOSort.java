package emo.interactive.ktscone.dcemo;

import criterion.Criteria;
import ea.AbstractPhasesEA;
import emo.aposteriori.nsgaii.NSGAIISort;
import emo.utils.front.FNDSorting;
import exception.PhaseException;
import exeption.PreferenceModelException;
import model.definitions.KTSCone;
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
 * Implementation of DCEMO sorting procedure. Note that this phase overwrites the
 * current population. Hence, there is no need to call the "remove" phase.
 *
 * @author MTomczyk
 */

public class DCEMOSort extends NSGAIISort implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     * @param DSS      reference to DSS
     */
    public DCEMOSort(Criteria criteria, DecisionSupportSystem DSS)
    {
        super("DCEMO: Sort", criteria);
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

            double[] cone = new double[aFront._front.size()];
            int index = 0;
            for (Integer idx : aFront._front)
            {
                try
                {
                    Specimen specimen = ea.getSpecimensContainer().getPopulation().get(idx);
                    double eval = _coneModel.evaluate(specimen.getAlternative());
                    cone[index++] = eval;
                } catch (PreferenceModelException e)
                {
                    throw new PhaseException("Error occurred when evaluating an alternative " + e.getDetailedReasonMessage(), this.getClass(), e);
                }
            }

            double divider = StatUtils.max(cone);
            boolean dividerIsZero = Double.compare(divider, 0.0d) == 0;
            double score;

            index = 0;

            for (Integer idx : aFront._front)
            {
                Specimen specimen = ea.getSpecimensContainer().getPopulation().get(idx);
                lastFront.add(specimen);
                if (dividerIsZero) score = 0.5d;
                else score = 0.25d + 0.5d * cone[index] / divider;
                specimen.setAuxScore(aFront._passedFronts + score);
                index++;
            }

            sort.Sort.sortByAuxValue(lastFront, true);
            for (int i = 0; i < ea.getPopulationSize() - aFront._passedMembers; i++)
                newPopulation.add(lastFront.get(i));

        }

        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }
}
