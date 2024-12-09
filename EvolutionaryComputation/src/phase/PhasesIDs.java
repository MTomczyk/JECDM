package phase;

/**
 * Phases IDs
 *
 * @author MTomczyk
 */

public class PhasesIDs
{
    /**
     * "Init starts" phase.
     */
    public static final int PHASE_INIT_STARTS = 0;

    /**
     * "Construct initial population" phase.
     */
    public static final int PHASE_CONSTRUCT_INITIAL_POPULATION = 1;

    /**
     * "Assign specimens' IDs" phase.
     */
    public static final int PHASE_ASSIGN_SPECIMENS_IDS = 2;

    /**
     * "Evaluate" phase.
     */
    public static final int PHASE_EVALUATE = 3;

    /**
     * "Sort" phase.
     */
    public static final int PHASE_SORT = 4;

    /**
     * "Init ends" phase.
     */
    public static final int PHASE_INIT_ENDS = 5;

    /**
     * "Prepare step" phase.
     */
    public static final int PHASE_PREPARE_STEP = 6;

    /**
     * "Construct mating pool" phase.
     */
    public static final int PHASE_CONSTRUCT_MATING_POOL = 7;

    /**
     * "Select parents" phase.
     */
    public static final int PHASE_SELECT_PARENTS = 8;

    /**
     * "Reproduce" phase.
     */
    public static final int PHASE_REPRODUCE = 9;

    /**
     * "Merge" phase.
     */
    public static final int PHASE_MERGE = 10;

    /**
     * "Remove" phase.
     */
    public static final int PHASE_REMOVE = 11;

    /**
     * "Finalize step" phase.
     */
    public static final int PHASE_FINALIZE_STEP = 12;

    /**
     * "Update objective space" phase.
     */
    public static final int PHASE_UPDATE_OS = 13;

    /**
     * Array of phases' names.
     */
    public static final String[] _phaseNames = new String[]{
            "INIT_STARTS",
            "CONSTRUCT_INITIAL_POPULATION",
            "ASSIGN_SPECIMENS_IDS",
            "EVALUATE",
            "SORT",
            "INIT_ENDS",
            "PREPARE_STEP",
            "CONSTRUCT_MATING_POOL",
            "SELECT_PARENTS",
            "REPRODUCE",
            "MERGE",
            "REMOVE",
            "FINALIZE_STEP",
            "UPDATE_OS"};
}
