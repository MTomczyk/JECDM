package emo.utils.decomposition.nsgaiii;

import emo.utils.decomposition.AbstractGoalsManager;
import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.family.FamilyID;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.goal.GoalID;
import emo.utils.decomposition.goal.IGoal;
import population.Specimen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Class providing various functionalities for maintaining/processing goals/assignments in a
 * decomposition-based algorithm founded on the NSGA-III algorithm.
 *
 * @author MTomczyk
 */

public class NSGAIIIGoalsManager extends AbstractGoalsManager
{
    /**
     * Params container.
     */
    public static class Params extends AbstractGoalsManager.Params
    {

        /**
         * Parameterized constructor. Establishes one-family decomposition data.
         *
         * @param goals optimization goals (the only family)
         */
        public Params(IGoal[] goals)
        {
            this(new IGoal[][]{goals});
        }

        /**
         * Parameterized constructor. Establishes several families.
         *
         * @param goals optimization goals (matrix: each entry is a separate family)
         */
        public Params(IGoal[][] goals)
        {
            super(goals);
        }
    }

    /**
     * Delta for the equality relation when comparing niche counts.
     */
    private final double _ncDelta = 0.000001d;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public NSGAIIIGoalsManager(Params p)
    {
        super(p);
        for (Family f : _F) f.instantiateDefaultAssignments();
    }

    /**
     * Auxiliary method that restructures the families-related data based on the new goals matrix. Note that this
     * implementation also implicitly forgets assignments, etc., as new data structures are created. It also, calls
     * for {@link Family#instantiateDefaultAssignments()}.
     *
     * @param goals new goals matrix (each row correspond to a different family)
     *
     */
    @Override
    public void restructure(IGoal[][] goals)
    {
        super.restructure(goals);
        if (_F != null) for (Family f : _F) f.instantiateDefaultAssignments();
    }

    /**
     * Method for performing the assignment step. It assigns each population member that is in the already passed fronts
     * or the ambiguous front to its closest goal. Additionally, the method sets the initial niche count based on the
     * solutions that will certainly be passed to the next generation (not in the ambiguous front).
     *
     * @param population current population
     * @param fronts     passed fronts + the ambiguous front (or just the last front; if the size = 1); stored integers are indices of associated solutions in the current population array
     */
    public void executeAssignmentStep(ArrayList<Specimen> population, LinkedList<LinkedList<Integer>> fronts)
    {
        // reset assignments
        for (Family f : _F)
        {
            f.resetAssignmentsNicheCounts();
            f.resetAssignmentsLists();
        }

        boolean notLastFront;
        ListIterator<LinkedList<Integer>> listIt = fronts.listIterator();
        LinkedList<Integer> front;

        // for each specimen
        while (listIt.hasNext())
        {
            front = listIt.next();
            notLastFront = listIt.hasNext();

            for (Integer id : front)
            {
                Specimen specimen = population.get(id);
                // find the best assignment
                GoalID gID = findClosestGoal(specimen);
                // get the assignment
                Assignment assignment = _F[gID.getFamilyArrayIndex()].getAssignment(gID.getGoalArrayIndex());

                // if not in the last (ambiguous) front, increase NC
                if (notLastFront) assignment.incrementNicheCount();
                // else add a specimen to the assignment
                else assignment.insertSpecimen(specimen);
            }
        }
    }

    /**
     * Auxiliary method for finding the closest goal to a given input specimen.
     * Note that it is assumed that all goals are either to be minimized or maximized (mixing is not allowed).
     *
     * @param specimen input specimen
     * @return pointer to the closest goal
     */
    private GoalID findClosestGoal(Specimen specimen)
    {
        int fID = 0;
        int gID = 0;
        double bestEvaluation = _F[0].getGoals()[0].evaluate(specimen);
        for (int f = 0; f < _F.length; f++)
        {
            for (int g = 0; g < _F[f].getSize(); g++)
            {
                double evaluation = _F[f].getGoals()[g].evaluate(specimen);
                if (
                        ((_F[f].getGoals()[g].isLessPreferred()) && (Double.compare(evaluation, bestEvaluation) < 0)) ||
                                ((!_F[f].getGoals()[g].isLessPreferred()) && (Double.compare(evaluation, bestEvaluation) > 0)))
                {
                    bestEvaluation = evaluation;
                    fID = f;
                    gID = g;
                }
            }
        }
        return new GoalID(new FamilyID(fID), gID);
    }

    /**
     * Method for selecting an assignment with a minimal niche count.
     *
     * @param ignoreEmptyAssignments if true, assignments that have no assigned solution are skipped
     * @return assignment with a minimal niche count
     */
    public LinkedList<Assignment> getAssignmentsWithMinimalNicheCount(boolean ignoreEmptyAssignments)
    {
        LinkedList<Assignment> assignments = new LinkedList<>();

        // find minimum
        double minNC = Double.POSITIVE_INFINITY;

        for (Family f : _F)
        {
            for (Assignment a : f.getAssignments())
            {
                if (Double.compare(a.getNicheCount(), minNC) < 0)
                {
                    if ((!ignoreEmptyAssignments) || (!a.getSpecimens().isEmpty())) minNC = a.getNicheCount();
                }
            }
        }

        // gather all
        for (Family f : _F)
            for (Assignment a : f.getAssignments())
                if ((Math.abs(a.getNicheCount() - minNC) < _ncDelta)
                    && (((!ignoreEmptyAssignments) || (!a.getSpecimens().isEmpty())))) assignments.add(a);

        return assignments;
    }
}
