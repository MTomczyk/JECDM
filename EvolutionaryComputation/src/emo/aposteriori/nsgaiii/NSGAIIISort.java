package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import ea.EA;
import emo.utils.decomposition.goal.Assignment;
import emo.utils.decomposition.nsgaiii.IAssignmentResolveTie;
import emo.utils.decomposition.nsgaiii.ISpecimenResolveTie;
import emo.utils.decomposition.nsgaiii.NSGAIIIGoalsManager;
import emo.utils.front.FNDSorting;
import exception.PhaseException;
import phase.AbstractSortPhase;
import phase.IPhase;
import phase.PhaseReport;
import population.Specimen;
import population.Specimens;
import print.PrintUtils;
import relation.dominance.Dominance;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Implementation of NSGA-III sorting procedure. Note that this phase overwrites the
 * current population. Hence, there is no need to call the "remove" phase.
 *
 * @author MTomczyk
 */

public class NSGAIIISort extends AbstractSortPhase implements IPhase
{
    /**
     * Parameterized constructor.
     *
     * @param criteria             considered criteria
     * @param goalsManager         NSGA-III goals manager
     * @param assignmentResolveTie auxiliary object used when resolving a tie during a selection of a goal (assignment) with a minimal niche count
     * @param specimenResolveTie   auxiliary object used when resolving a tie during a selection of a specimen when the associated assignment object has a niche count value greater than one
     */
    public NSGAIIISort(Criteria criteria,
                       NSGAIIIGoalsManager goalsManager,
                       IAssignmentResolveTie assignmentResolveTie,
                       ISpecimenResolveTie specimenResolveTie)
    {
        super("NSGAIII: Sort");
        _goalsManager = goalsManager;
        _assignmentResolveTie = assignmentResolveTie;
        _specimenResolveTie = specimenResolveTie;
        _FND = new FNDSorting(new Dominance(criteria));
    }

    /**
     * Goals manager steering the evolutionary process.
     */
    public NSGAIIIGoalsManager _goalsManager;

    /**
     * Object responsible for identifying non-dominated fronts.
     */
    private final FNDSorting _FND;

    /**
     * Object used for resolving a tie when selecting an assignment with a minimal niche count
     */
    private final IAssignmentResolveTie _assignmentResolveTie;

    /**
     * Object used when resolving a tie during a selection of a specimen when the associated assignment object has a niche count value greater than one.
     */
    private final ISpecimenResolveTie _specimenResolveTie;

    /**
     * Can be used to print the state.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final boolean _debug = false;

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
        if (_debug)
        {
            System.out.println("Generation = " + ea.getCurrentGeneration());
            System.out.println("Current population = ");
            for (Specimen s : ea.getSpecimensContainer().getPopulation())
            {
                System.out.print(s.getID().toString() + " ");
                System.out.println(PrintUtils.getVectorOfDoubles(s.getEvaluations(), 4));
            }
        }

        // Instantiate new population
        ArrayList<Specimen> newPopulation = new ArrayList<>(ea.getPopulationSize());

        // Identify the required number of non-dominated fronts
        LinkedList<LinkedList<Integer>> fronts = _FND.getFrontAssignments(
                new Specimens(ea.getSpecimensContainer().getPopulation()), ea.getPopulationSize());

        if (_debug)
        {
            System.out.println("Derived fronts: " + fronts.size());
            int idx = 0;
            for (LinkedList<Integer> front : fronts)
                System.out.println("Front no = " + (idx++) + " has size = " + front.size());
        }

        // Pass as many fronts as possible and determine the ambiguous front
        FNDSorting.AmbiguousFront aFront = FNDSorting.fillNewPopulationWithCertainFronts(newPopulation,
                ea.getSpecimensContainer().getPopulation(), fronts, ea.getPopulationSize());

        // only if there is an ambiguous front
        if (aFront != null)
        {
            if (_debug)
            {
                System.out.println("Ambiguous front:");
                System.out.println("Already passed fronts = " + aFront._passedFronts);
                System.out.println("Already passed members = " + aFront._passedMembers);
                System.out.println("Ambiguous front size = " + aFront._front.size());
                System.out.println("To pass = " + (ea.getPopulationSize() - aFront._passedMembers));
                System.out.println("Excess in ambiguous front = " + (aFront._front.size() - (ea.getPopulationSize() - aFront._passedMembers)));
            }

            // Execute assignment step (assign each solution to its closest goal)
            // the procedure also calculates initial niching (used the solutions that must be passed to the next generation)
            _goalsManager.executeAssignmentStep(ea.getSpecimensContainer().getPopulation(), fronts);

            // repeat the required number of times
            for (int i = 0; i < ea.getPopulationSize() - aFront._passedMembers; i++)
            {
                // find goal (assignment) with the minimal niche count
                LinkedList<Assignment> assignments = _goalsManager.getAssignmentsWithMinimalNicheCount(true);

                Assignment assignment;
                if (assignments.size() > 1) assignment = _assignmentResolveTie.resolveTie(assignments, ea.getR());
                else assignment = assignments.getFirst();

                int NC = (int) (assignment.getNicheCount() + 0.5d); // to avoid numerical errors

                int specimenIndex = 0; // the best

                // note that empty assignments are already ignored
                if (NC != 0) specimenIndex = _specimenResolveTie.getSpecimenIndex(assignment.getSpecimens(), ea.getR());

                // assign some solution using the policy
                Specimen specimen = assignment.getSpecimenAndRemoveFromLists(specimenIndex);
                specimen.setAuxScore(aFront._passedFronts + 0.5d);
                newPopulation.add(specimen);
                assignment.incrementNicheCount();
            }
        }

        if (_debug)
        {
            System.out.println("New population = ");
            for (Specimen s : newPopulation)
            {
                System.out.print(s.getID().toString() + " ");
                System.out.println(PrintUtils.getVectorOfDoubles(s.getEvaluations(), 4));
            }
        }

        // overwrite the population
        ea.getSpecimensContainer().setPopulation(newPopulation);
    }

}
