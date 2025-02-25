package emo.aposteriori.nsga;

import criterion.Criteria;
import ea.EA;
import emo.utils.density.NicheCount;
import emo.utils.front.FNDSorting;
import exception.PhaseException;
import org.apache.commons.math4.legacy.stat.StatUtils;
import phase.AbstractSortPhase;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;
import population.Specimens;
import relation.dominance.Dominance;
import space.distance.IDistance;
import space.normalization.INormalization;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of NSGA sorting procedure. Note that this phase overwrites the
 * current population. Hence, there is no need to call the "remove" phase.
 *
 * @author MTomczyk
 */

public class NSGASort extends AbstractSortPhase implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     * @param distance distance function used when calculating niche counts
     * @param th       distance threshold for the niche count procedure
     */
    public NSGASort(Criteria criteria, IDistance distance, double th)
    {
        super("NSGA: Sort");
        _FND = new FNDSorting(new Dominance(criteria));
        _NC = new NicheCount(distance, th);
    }

    /**
     * Normalizations used when calculating niche counts.
     */
    private INormalization[] _normalizations = null;

    /**
     * Object responsible for identifying non-dominated fronts.
     */
    private final FNDSorting _FND;

    /**
     * Supportive object for calculating niche counts.
     */
    private final NicheCount _NC;

    /**
     * Phase's main action.
     *
     * @param ea     evolutionary algorithm
     * @param report report on the executed action (to be filled)
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public void action(EA ea, PhaseReport report) throws PhaseException
    {
        // Instantiate new population
        ArrayList<Specimen> newPopulation = new ArrayList<>(ea.getPopulationSize());

        // Identify the required number of non-dominated fronts
        LinkedList<LinkedList<Integer>> fronts = _FND.getFrontAssignments(
                new Specimens(ea.getSpecimensContainer().getPopulation()), ea.getPopulationSize());

        // Pass as many fronts as possible and determine the ambiguous front
        // Note that the procedure sets specimens' aux scores: defined as the front level (starting from zero)
        FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation,
                ea.getSpecimensContainer().getPopulation(), fronts, ea.getPopulationSize());

        // only if there is an ambiguous front
        if (aFront != null)
        {
            // construct the ambiguous population
            ArrayList<Specimen> lastFront = new ArrayList<>(ea.getPopulationSize() - aFront._passedMembers);

            // there is a need to calculate niche counts.
            double[] nc = _NC.getNicheCountInFront(aFront._front, ea.getSpecimensContainer().getPopulation(), _normalizations);
            double div = StatUtils.max(nc);

            int num = 0;
            for (Integer idx : aFront._front)
            {
                lastFront.add(ea.getSpecimensContainer().getPopulation().get(idx));
                lastFront.get(num).setAuxScore(aFront._passedFronts + (nc[num] / div));
                num++;
            }

            sort.Sort.sortByAuxValue(lastFront, true);
            for (int i = 0; i < ea.getPopulationSize() - aFront._passedMembers; i++)
                newPopulation.add(lastFront.get(i));
        }

        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }

    /**
     * Updates normalization used when calculating niche counts.
     *
     * @param normalizations normalizations used for scaling objective values
     */
    protected void updateNormalizations(INormalization[] normalizations)
    {
        _normalizations = normalizations;
    }


    /**
     * Setter for niche count threshold parameter.
     *
     * @param th niche count threshold
     */
    protected void setNicheCountThreshold(double th)
    {
        _NC.setNicheCountThreshold(th);
    }


    /**
     * Getter for the current niche count threshold.
     *
     * @return current niche count threshold
     */
    protected double getNicheCountThreshold()
    {
        return _NC.getNicheCountThreshold();
    }
}
