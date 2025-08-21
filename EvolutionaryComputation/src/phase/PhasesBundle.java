package phase;

/**
 * Used to store phases and pass between different objects.
 *
 * @author MTomczyk
 */
public class PhasesBundle
{
    /**
     * "Init starts" phase.
     */
    public AbstractInitStartsPhase _initStarts;

    /**
     * "Construct initial population" phase.
     */
    public AbstractConstructInitialPopulationPhase _constructInitialPopulation;

    /**
     * "Assign Specimens IDs" phase.
     */
    public AbstractAssignSpecimenIDs _assignSpecimenIDs = new AssignSpecimensIDs();

    /**
     * "Evaluate" phase.
     */
    public AbstractEvaluatePhase _evaluate;

    /**
     * "Sort" phase.
     */
    public AbstractSortPhase _sort;

    /**
     * "Init ends" phase.
     */
    public AbstractInitEndsPhase _initEnds = new InitEnds();

    /**
     * "Prepare step" phase.
     */
    public AbstractPrepareStepPhase _prepareStep = null;

    /**
     * "Construct mating pool" phase.
     */
    public AbstractConstructMatingPoolPhase _constructMatingPool = new ConstructMatingPool();

    /**
     * "Select parents" phase.
     */
    public AbstractSelectParentsPhase _selectParents;

    /**
     * "Reproduce" phase.
     */
    public AbstractReproducePhase _reproduce;

    /**
     * "Merge" phase.
     */
    public AbstractMergePhase _merge = new Merge();

    /**
     * "Remove" phase.
     */
    public AbstractRemovePhase _remove = new Remove();

    /**
     * "Finalize step" phase.
     */
    public AbstractFinalizeStepPhase _finalizeStep = new FinalizeStep();

    /**
     * "Update objective space" phase.
     */
    public AbstractUpdateOSPhase _updateOS = null;

    /**
     * Creates and returns a bundle instance with nulled phases.
     *
     * @return phases bundle
     */
    public static PhasesBundle getNulledInstance()
    {
        PhasesBundle pb = new PhasesBundle();
        pb._initStarts = null;
        pb._constructInitialPopulation = null;
        pb._assignSpecimenIDs = null;
        pb._evaluate = null;
        pb._sort = null;
        pb._initEnds = null;
        pb._prepareStep = null;
        pb._constructMatingPool = null;
        pb._selectParents = null;
        pb._reproduce = null;
        pb._merge = null;
        pb._remove = null;
        pb._finalizeStep = null;
        pb._updateOS = null;
        return pb;
    }

    /**
     * Creates and returns a bundle instance with most commonly used phases instantiated by default (see the code
     * for details).
     *
     * @return phases bundle
     */
    public static PhasesBundle getDefaultInstance()
    {
        PhasesBundle pb = PhasesBundle.getNulledInstance();
        pb._assignSpecimenIDs = new AssignSpecimensIDs();
        pb._initEnds = new InitEnds();
        pb._constructMatingPool = new ConstructMatingPool();
        pb._merge = new Merge();
        pb._remove = new Remove();
        pb._finalizeStep = new FinalizeStep();
        return pb;
    }

    /**
     * Creates the default phase assignments array using phases stored in the bundle. Specifically, the assignments are
     * as follows (in the given order): <br>
     * Init: init starts, construct initial population, assign specimen IDs, evaluate, update OS, sort, init ends; <br>
     * Step: prepare step, construct mating pool, select parents, reproduce, assign specimen IDs, evaluate, update OS,
     * merge, sort, remove, finalize step
     *
     * @param B phases bundle
     * @return phase assignments
     */
    public static PhaseAssignment[] getPhasesAssignmentsFromBundle(PhasesBundle B)
    {
        PhaseAssignment[] phaseAssignments = new PhaseAssignment[18];
        // Init:
        phaseAssignments[0] = new PhaseAssignment(B._initStarts, PhaseAssignment.Assignment.INIT);
        phaseAssignments[1] = new PhaseAssignment(B._constructInitialPopulation, PhaseAssignment.Assignment.INIT);
        phaseAssignments[2] = new PhaseAssignment(B._assignSpecimenIDs, PhaseAssignment.Assignment.INIT);
        phaseAssignments[3] = new PhaseAssignment(B._evaluate, PhaseAssignment.Assignment.INIT);
        phaseAssignments[4] = new PhaseAssignment(B._updateOS, PhaseAssignment.Assignment.INIT);
        phaseAssignments[5] = new PhaseAssignment(B._sort, PhaseAssignment.Assignment.INIT);
        phaseAssignments[6] = new PhaseAssignment(B._initEnds, PhaseAssignment.Assignment.INIT);
        // Step:
        phaseAssignments[7] = new PhaseAssignment(B._prepareStep, PhaseAssignment.Assignment.STEP);
        phaseAssignments[8] = new PhaseAssignment(B._constructMatingPool, PhaseAssignment.Assignment.STEP);
        phaseAssignments[9] = new PhaseAssignment(B._selectParents, PhaseAssignment.Assignment.STEP);
        phaseAssignments[10] = new PhaseAssignment(B._reproduce, PhaseAssignment.Assignment.STEP);
        phaseAssignments[11] = new PhaseAssignment(B._assignSpecimenIDs, PhaseAssignment.Assignment.STEP);
        phaseAssignments[12] = new PhaseAssignment(B._evaluate, PhaseAssignment.Assignment.STEP);
        phaseAssignments[13] = new PhaseAssignment(B._updateOS, PhaseAssignment.Assignment.STEP);
        phaseAssignments[14] = new PhaseAssignment(B._merge, PhaseAssignment.Assignment.STEP);
        phaseAssignments[15] = new PhaseAssignment(B._sort, PhaseAssignment.Assignment.STEP);
        phaseAssignments[16] = new PhaseAssignment(B._remove, PhaseAssignment.Assignment.STEP);
        phaseAssignments[17] = new PhaseAssignment(B._finalizeStep, PhaseAssignment.Assignment.STEP);
        return phaseAssignments;
    }

}
