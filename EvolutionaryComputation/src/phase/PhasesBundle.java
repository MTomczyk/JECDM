package phase;

import ea.EA;

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
     * Supportive method that sets phases in {@link EA.Params} as imposed by the bundle object.
     *
     * @param p {@link EA.Params} object to be passed to EA's constructor
     * @param B instantiated phases
     */
    public static void copyPhasesFromBundleToEA(EA.Params p, PhasesBundle B)
    {
        p._initStarts = B._initStarts;
        p._constructInitialPopulation = B._constructInitialPopulation;
        p._assignSpecimenIDs = B._assignSpecimenIDs;
        p._evaluate = B._evaluate;
        p._sort = B._sort;
        p._initEnds = B._initEnds;
        p._prepareStep = B._prepareStep;
        p._constructMatingPool = B._constructMatingPool;
        p._selectParents = B._selectParents;
        p._reproduce = B._reproduce;
        p._merge = B._merge;
        p._remove = B._remove;
        p._finalizeStep = B._finalizeStep;
        p._updateOS = B._updateOS;
    }

    /**
     * Creates a bundle with nulled entries.
     * @return phases bundle with nulled entries
     */
    public static PhasesBundle createNulledPhases()
    {
        PhasesBundle p = new PhasesBundle();
        p._initStarts = null;
        p._constructInitialPopulation = null;
        p._assignSpecimenIDs = null;
        p._evaluate = null;
        p._sort = null;
        p._initEnds = null;
        p._prepareStep = null;
        p._constructMatingPool = null;
        p._selectParents = null;
        p._reproduce = null;
        p._merge = null;
        p._remove = null;
        p._finalizeStep = null;
        p._updateOS = null;
        return p;
    }
}
