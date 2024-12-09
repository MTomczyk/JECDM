package emo.utils.decomposition.moead;

import emo.utils.decomposition.AbstractGoalsManager;
import emo.utils.decomposition.alloc.IAlloc;
import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.GoalWrapper;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.neighborhood.Neighborhood;
import emo.utils.decomposition.neighborhood.constructor.INeighborhoodConstructor;
import emo.utils.decomposition.neighborhood.constructor.InsertionSortConstructor;
import emo.utils.decomposition.similarity.ISimilarity;
import population.Specimen;
import population.SpecimensContainer;
import random.IRandom;
import space.normalization.INormalization;

import java.util.ArrayList;

/**
 * Class providing various functionalities for maintaining/processing goals/assignments in a
 * decomposition-based algorithm founded on the MOEA/D algorithm.
 *
 * @author MTomczyk
 */
public class MOEADGoalsManager extends AbstractGoalsManager
{
    /**
     * Params container.
     */
    public static class Params extends AbstractGoalsManager.Params
    {
        /**
         * Neighborhood size (should be at least 1).
         */
        public int _neighborhoodSize;

        /**
         * Similarity measures used to compare the goals within families (1:1 link, i.e., no. elements should equal no. row in the goals matrix).
         */
        public ISimilarity[] _similarity;

        /**
         * Used to allocate resources (determines updates of goals in subsequent steady-state repeats within one generation).
         */
        public IAlloc _alloc = null;

        /**
         * Object used to construct neighborhood.
         */
        public INeighborhoodConstructor _neighborhoodConstructor;

        /**
         * Parameterized constructor. Establishes one-family decomposition data. Insertion sort constructor {@link InsertionSortConstructor}
         * is used when building neighborhoods.
         *
         * @param goals            optimization goals (the only family)
         * @param similarity       similarity measure used to compare the goals
         * @param neighborhoodSize neighborhood size
         */
        public Params(IGoal[] goals, ISimilarity similarity, int neighborhoodSize)
        {
            this(new IGoal[][]{goals}, new ISimilarity[]{similarity}, new InsertionSortConstructor(), neighborhoodSize);
        }


        /**
         * Parameterized constructor. Establishes one-family decomposition data.
         *
         * @param goals                   optimization goals (the only family)
         * @param similarity              similarity measure used to compare the goals
         * @param neighborhoodConstructor object used to construct neighborhood
         * @param neighborhoodSize        neighborhood size
         */
        public Params(IGoal[] goals, ISimilarity similarity, INeighborhoodConstructor neighborhoodConstructor, int neighborhoodSize)
        {
            this(new IGoal[][]{goals}, new ISimilarity[]{similarity}, neighborhoodConstructor, neighborhoodSize);
        }

        /**
         * Parameterized constructor. Establishes several families.
         *
         * @param goals                   optimization goals (matrix: each entry is a separate family)
         * @param similarity              similarity measures used to compare the goals within families (1:1 link, i.e., no. elements should equal no. row in the goals' matrix)
         * @param neighborhoodConstructor object used to construct neighborhood
         * @param neighborhoodSize        neighborhood size
         */
        public Params(IGoal[][] goals, ISimilarity[] similarity, INeighborhoodConstructor neighborhoodConstructor, int neighborhoodSize)
        {
            super(goals);
            _similarity = similarity;
            _neighborhoodConstructor = neighborhoodConstructor;
            _neighborhoodSize = neighborhoodSize;
        }
    }

    /**
     * Used to allocate resources (determines updates of goals in subsequent steady-state repeats within one generation).
     */
    private final IAlloc _alloc;

    /**
     * Neighborhood size
     */
    private final int _neighborhoodSize;

    /**
     * Neighborhood (each for every family).
     */
    private Neighborhood[] _N;

    /**
     * Similarity measures are used to establish a neighborhood (each for every family).
     */
    private final ISimilarity[] _S;

    /**
     * Neighborhood constructor: establishes the neighborhood.
     */
    private final INeighborhoodConstructor _NC;

    /**
     * Sequence of goals updates obtained via {@link IAlloc} object.
     */
    private GoalID[] _updatesSequence = null;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public MOEADGoalsManager(Params p)
    {
        super(p);
        _S = p._similarity;
        _NC = p._neighborhoodConstructor;
        _neighborhoodSize = p._neighborhoodSize;
        _alloc = p._alloc;
    }


    /**
     * Can be called to construct a sequence of goal updates to be executed in subsequence steady-state repeats.
     *
     * @param R random number generator
     */
    public void determineUpdatesSequence(IRandom R)
    {
        _updatesSequence = _alloc.getAllocations(_F, R);
    }

    /**
     * Returns the current goal to be updated.
     *
     * @param steadyStateRepeat steady-state repeat no.
     * @return goal to be updated
     */
    public GoalID getCurrentGoalToBeUpdated(int steadyStateRepeat)
    {
        return _updatesSequence[steadyStateRepeat];
    }

    /**
     * Returns the current neighborhood of a goal being updated.
     *
     * @param currentGoal points to the current goal being updated
     * @return IDs of goals belonging to the current neighborhood
     */
    public GoalID[] getCurrentNeighborhood(GoalID currentGoal)
    {
        return _N[currentGoal.getFamilyArrayIndex()].getNeighborhood(currentGoal.getGoalArrayIndex());
    }

    /**
     * Method for establishing neighborhoods.
     */
    public void establishNeighborhood()
    {
        _N = new Neighborhood[_F.length];
        for (int f = 0; f < _F.length; f++)
            _N[f] = _NC.getNeighborhood(_F[f], _S[f], _neighborhoodSize);
    }


    /**
     * Can be called to make initial (arbitrary) specimen->goals assignment (these are kept in family containers).
     * The assignment order follows the process imposed by the update sequence ({@link MOEADGoalsManager#_updatesSequence}),
     * i.e., i-th specimen is assigned to order(i)-th goal.
     *
     * @param specimensContainer specimen container storing the initial population
     */
    public void makeArbitraryAssignments(SpecimensContainer specimensContainer)
    {
        for (int s = 0; s < specimensContainer.getPopulation().size(); s++)
        {
            GoalID goalID = _updatesSequence[s];
            Family family = _F[goalID.getFamilyArrayIndex()];
            IGoal goal = family.getGoal(goalID.getGoalArrayIndex());
            Specimen specimen = specimensContainer.getPopulation().get(s);

            Assignment GA = new Assignment(goal);
            GA.setFirstSpecimen(specimen);
            GA.setFirstSpecimenEvaluation(goal.evaluate(specimen));
            family.setAssignment(goalID.getGoalArrayIndex(), GA);
        }
    }

    /**
     * Can be called to make the post assignments, i.e., each goal will be assigned the best performer in the population.
     * Note that one specimen may be allocated this way to multiple goals, thus diminishing the variability of solutions.
     *
     * @param specimensContainer current specimens container
     */
    public void makeBestAssignments(SpecimensContainer specimensContainer)
    {
        for (Family F : _F)
        {
            for (GoalWrapper G : F.getGoals())
            {
                double bestValue = Double.POSITIVE_INFINITY;
                if (!G.isLessPreferred()) bestValue = Double.NEGATIVE_INFINITY;
                int bestIndex = 0;
                for (int s = 0; s < specimensContainer.getPopulation().size(); s++)
                {
                    double v = G.evaluate(specimensContainer.getPopulation().get(s));
                    if (((G.isLessPreferred()) && (Double.compare(v, bestValue) < 0))
                            || ((!G.isLessPreferred()) && (Double.compare(v, bestValue) > 0)))
                    {
                        bestValue = v;
                        bestIndex = s;
                    }
                }
                Assignment assignment = new Assignment(G);
                assignment.setFirstSpecimen(specimensContainer.getPopulation().get(bestIndex));
                assignment.setFirstSpecimenEvaluation(bestValue);
                F.setAssignment(G.getID().getGoalArrayIndex(), assignment);
            }
        }

        // update population
        for (Family F : _F)
        {
            for (GoalWrapper G : F.getGoals())
            {
                Assignment assignment = F.getAssignment(G.getID().getGoalArrayIndex());
                Specimen specimen = assignment.getSpecimens().get(0);
                int index = _goalToSpecimenArrayIndex[F.getID().getArrayIndex()][G.getID().getGoalArrayIndex()];
                specimensContainer.getPopulation().set(index, specimen);
            }
        }
    }


    /**
     * Creates the mating pool associated with the current goal.
     *
     * @param currentGoal currently updated goal
     * @return mating pool (array os specimens to neighbors of the current goal).
     */
    public ArrayList<Specimen> createMatingPool(GoalID currentGoal)
    {
        ArrayList<Specimen> matingPool = new ArrayList<>(_neighborhoodSize);
        GoalID[] nb = getCurrentNeighborhood(currentGoal);
        for (GoalID gl : nb)
            matingPool.add(_F[gl.getFamilyArrayIndex()].getAssignment(gl.getGoalArrayIndex()).getFirstSpecimen());
        return matingPool;
    }

    /**
     * Can be called to replace specimens assigned to goals in the current neighborhood with the offspring (if proves better).
     *
     * @param offspring          offspring solution
     * @param currentGoal        current goal
     * @param specimensContainer current population (offspring can overwrite some members)
     */
    public void executeUpdate(Specimen offspring, GoalID currentGoal, SpecimensContainer specimensContainer)
    {
        GoalID[] nb = getCurrentNeighborhood(currentGoal);

        for (GoalID gl : nb)
        {
            Family family = _F[gl.getFamilyArrayIndex()];
            IGoal goal = family.getGoal(gl.getGoalArrayIndex());
            double offspringEvaluation = goal.evaluate(offspring);

            Assignment currentAssignment = family.getAssignment(gl.getGoalArrayIndex());
            if ((goal.isLessPreferred()) && (Double.compare(offspringEvaluation,
                    currentAssignment.getFirstSpecimenEvaluation()) >= 0)) continue;
            if ((!goal.isLessPreferred()) && (Double.compare(offspringEvaluation,
                    currentAssignment.getFirstSpecimenEvaluation()) <= 0)) continue;

            // alter the assignment
            currentAssignment.setFirstSpecimenEvaluation(offspringEvaluation);
            currentAssignment.setFirstSpecimen(offspring);

            int populationID = _goalToSpecimenArrayIndex[gl.getFamilyArrayIndex()][gl.getGoalArrayIndex()];
            specimensContainer.getPopulation().set(populationID, offspring);
        }
    }

    /**
     * Can be called to update normalizations maintained by the goals. It also requires reevaluating the scores linked
     * to their assigned solutions.
     *
     * @param normalizations new normalizations used to rescale objective function values
     */
    @Override
    public void updateNormalizations(INormalization[] normalizations)
    {
        super.updateNormalizations(normalizations);

        for (Family F : _F)
        {
            if (F.getAssignments() == null) continue;
            for (Assignment ga : F.getAssignments())
            {
                if (ga == null) continue;
                if (ga.getFirstSpecimen() == null) continue;
                if (ga.getGoal() == null) continue;
                ga.revaluateFirstSpecimen();
            }
        }

    }
}
