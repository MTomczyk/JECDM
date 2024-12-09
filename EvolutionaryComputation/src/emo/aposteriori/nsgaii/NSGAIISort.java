package emo.aposteriori.nsgaii;

import criterion.Criteria;
import ea.EA;
import emo.utils.density.CrowdingDistance;
import emo.utils.front.FNDSorting;
import exception.PhaseException;
import phase.AbstractSortPhase;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;
import population.Specimens;
import relation.dominance.Dominance;
import space.normalization.INormalization;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of NSGA-II sorting procedure. Note that this phase overwrites the
 * current population. Hence, there is no need to call the "remove" phase.
 *
 * @author MTomczyk
 */
public class NSGAIISort extends AbstractSortPhase implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param criteria considered criteria
     */
    public NSGAIISort(Criteria criteria)
    {
        this("NSGAII: Sort", criteria);
    }

    /**
     * Parameterized constructor.
     *
     * @param name     phase name
     * @param criteria considered criteria
     */
    public NSGAIISort(String name, Criteria criteria)
    {
        super(name);
        _FND = new FNDSorting(new Dominance(criteria));
        _CD = new CrowdingDistance(criteria._no);
    }

    /**
     * Normalizations used when calculating crowding distances.
     */
    protected INormalization[] _normalizations = null;

    /**
     * Object responsible for identifying non-dominated fronts.
     */
    protected final FNDSorting _FND;

    /**
     * Supportive object for calculating crowding-distances.
     */
    protected final CrowdingDistance _CD;

    /**
     * Phase's main action.
     *
     * @param ea evolutionary algorithm
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
        FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation,
                ea.getSpecimensContainer().getPopulation(), fronts, ea.getPopulationSize());

        // only if there is an ambiguous front
        if (aFront != null)
        {
            // construct the ambiguous population
            ArrayList<Specimen> lastFront = constructLastFront(ea, aFront);
            for (int i = 0; i < ea.getPopulationSize() - aFront._passedMembers; i++)
                newPopulation.add(lastFront.get(i));
        }

        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }

    /**
     * Auxiliary method called by {@link NSGAIISort#action(EA, PhaseReport)} to construct the last front to be added to surviving population
     * (applied crowding distance).
     *
     * @param ea     evolutionary algorithm
     * @param aFront ambiguous front
     * @return last front.
     */
    protected ArrayList<Specimen> constructLastFront(EA ea, FNDSorting.AmbiguousFront aFront)
    {
        // construct the ambiguous population
        ArrayList<Specimen> lastFront = new ArrayList<>(ea.getPopulationSize() - aFront._passedMembers);

        // there is a need to calculate crowding distances.
        double[] cd = _CD.calculateCrowdingDistanceInFront(aFront._front, ea.getSpecimensContainer().getPopulation(),
                _normalizations, Double.POSITIVE_INFINITY);

        // Find the proper divider to normalize CDs
        double divider = CrowdingDistance.identifyDivider(cd);

        int num = 0;
        for (Integer idx : aFront._front)
        {
            lastFront.add(ea.getSpecimensContainer().getPopulation().get(idx));
            if (Double.compare(cd[num], Double.POSITIVE_INFINITY) == 0)
                lastFront.get(num).setAuxScore(aFront._passedFronts);
            else lastFront.get(num).setAuxScore(aFront._passedFronts + (1.0d - cd[num] / divider));
            num++;
        }

        sort.Sort.sortByAuxValue(lastFront, true);
        return lastFront;
    }


    /**
     * Updates normalization used when calculating crowding-distances.
     *
     * @param normalizations normalizations used for scaling objective values
     */
    public void updateNormalizations(INormalization[] normalizations)
    {
        _normalizations = normalizations;
    }
}
