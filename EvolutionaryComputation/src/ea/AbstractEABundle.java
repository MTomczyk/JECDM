package ea;

import criterion.Criteria;
import os.IOSChangeListener;
import os.ObjectiveSpaceManager;
import phase.*;
import reproduction.IReproduce;
import selection.ISelect;
import space.normalization.INormalization;
import space.normalization.builder.INormalizationBuilder;
import space.normalization.builder.StandardLinearBuilder;

import java.util.Objects;

/**
 * Abstract class supporting the parameterization of EA.
 *
 * @author MTomczyk
 */
public abstract class AbstractEABundle
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Name of the EA.
         */
        public String _name = null;

        /**
         * Considered criteria.
         */
        public Criteria _criteria;

        /**
         * Constructs initial specimens.
         */
        public IConstruct _construct;

        /**
         * Constructs offspring.
         */
        public IReproduce _reproduce;

        /**
         * Evaluates new solutions.
         */
        public IEvaluate _evaluate;

        /**
         * Parents selection procedure.
         */
        public ISelect _select;

        /**
         * Objective space manager.
         */
        public ObjectiveSpaceManager _osManager;

        /**
         * Auxiliary object constructing normalization functions using data on the current known bounds on the relevant
         * part of the objective space. Primarily used in the context of evolutionary multi-objective optimization.
         * It is recommended to use {@link StandardLinearBuilder} (default field value). If null,
         * {@link StandardLinearBuilder} will be used.
         */
        public INormalizationBuilder _normalizationBuilder = new StandardLinearBuilder();


        /**
         * Auxiliary initial normalizations.
         */
        public INormalization[] _initialNormalizations;

        /**
         * Parameterized constructor.
         *
         * @param name     name of the EA
         * @param criteria criteria used to assess population members
         */
        public Params(String name, Criteria criteria)
        {
            _name = name;
            _criteria = criteria;
        }

        /**
         * Default constructor to be overwritten.
         */
        protected Params()
        {

        }

        /**
         * Auxiliary method called by the constructor at the beginning to instantiate default params values (optional).
         *
         * @deprecated to be removed in future releases
         */
        @Deprecated
        protected void instantiateDefaultValues()
        {

        }
    }

    /**
     * Name of the EA.
     */
    public String _name;

    /**
     * Phases bundle.
     */
    public PhasesBundle _phasesBundle;

    /**
     * Reference to the objective space manager.
     */
    public ObjectiveSpaceManager _osManager;

    /**
     * Auxiliary object constructing normalization functions using data on the current known bounds on the relevant
     * part of the objective space. Primarily used in the context of evolutionary multi-objective optimization.
     * It is recommended to use {@link StandardLinearBuilder}. If null, {@link StandardLinearBuilder} will be used.
     */
    public INormalizationBuilder _normalizationBuilder;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    protected AbstractEABundle(Params p)
    {
        p.instantiateDefaultValues();
        _name = p._name;
        _phasesBundle = new PhasesBundle();
        _osManager = p._osManager;
        _normalizationBuilder = Objects.requireNonNullElseGet(p._normalizationBuilder, StandardLinearBuilder::new);
        instantiateInitStartsPhase(p);
        instantiateConstructInitialPopulationPhase(p);
        instantiateAssignSpecimensIDPhase(p);
        instantiateEvaluatePhase(p);
        instantiateSortPhase(p);
        instantiateInitEndsPhase(p);
        instantiatePrepareStepPhase(p);
        instantiateConstructMatingPoolPhase(p);
        instantiateSelectParentsPhase(p);
        instantiateReproducePhase(p);
        instantiateMergePhase(p);
        instantiateRemovePhase(p);
        instantiateFinalizeStepPhase(p);
        instantiateUpdateObjectiveSpacePhase(p);
        registerOSChangeListeners(p);
        registerInitialNormalizations(p);
    }

    /**
     * Auxiliary method instantiating the "init starts" phase.
     *
     * @param p params container
     */
    protected void instantiateInitStartsPhase(Params p)
    {
        _phasesBundle._initStarts = null;
    }

    /**
     * Auxiliary method instantiating the "create initial population" phase.
     *
     * @param p params container
     */
    protected void instantiateConstructInitialPopulationPhase(Params p)
    {
        _phasesBundle._constructInitialPopulation = null;
        if (p._construct != null)
        {
            _phasesBundle._constructInitialPopulation = new ConstructInitialPopulation(p._construct);
        }
    }


    /**
     * Auxiliary method instantiating the "assign specimens id" phase.
     *
     * @param p params container
     */
    protected void instantiateAssignSpecimensIDPhase(Params p)
    {
        _phasesBundle._assignSpecimenIDs = new AssignSpecimensIDs();
    }


    /**
     * Auxiliary method instantiating the "evaluate" phase.
     *
     * @param p params container
     */
    protected void instantiateEvaluatePhase(Params p)
    {
        _phasesBundle._evaluate = null;
        if (p._evaluate != null) _phasesBundle._evaluate = new Evaluate(p._evaluate);
    }

    /**
     * Auxiliary method instantiating the "sort" phase (sort by aux value, ascending order).
     *
     * @param p params container
     */
    protected void instantiateSortPhase(Params p)
    {
        _phasesBundle._sort = new SortByAuxValue(true);
    }

    /**
     * Auxiliary method instantiating the "init starts" phase.
     *
     * @param p params container
     */
    protected void instantiateInitEndsPhase(Params p)
    {
        _phasesBundle._initEnds = new InitEnds();
    }

    /**
     * Auxiliary method instantiating the "prepare" phase.
     *
     * @param p params container
     */
    protected void instantiatePrepareStepPhase(Params p)
    {
        _phasesBundle._prepareStep = null;
    }

    /**
     * Auxiliary method instantiating the "construct mating pol" phase.
     *
     * @param p params container
     */
    protected void instantiateConstructMatingPoolPhase(Params p)
    {
        _phasesBundle._constructMatingPool = new ConstructMatingPool();
    }

    /**
     * Auxiliary method instantiating the "select parents" phase.
     *
     * @param p params container
     */
    protected void instantiateSelectParentsPhase(Params p)
    {
        _phasesBundle._selectParents = null;
        if (p._select != null) _phasesBundle._selectParents = new SelectParents(p._select);
    }

    /**
     * Auxiliary method instantiating the "reproduce" phase.
     *
     * @param p params container
     */
    protected void instantiateReproducePhase(Params p)
    {
        _phasesBundle._reproduce = null;
        if (p._reproduce != null) _phasesBundle._reproduce = new Reproduce(p._reproduce);
    }

    /**
     * Auxiliary method instantiating the "merge" phase.
     *
     * @param p params container
     */
    protected void instantiateMergePhase(Params p)
    {
        _phasesBundle._merge = new Merge();
    }

    /**
     * Auxiliary method instantiating the "remove" phase.
     *
     * @param p params container
     */
    protected void instantiateRemovePhase(Params p)
    {
        _phasesBundle._remove = new Remove();
    }

    /**
     * Auxiliary method instantiating the "finalize step" phase.
     *
     * @param p params container
     */
    protected void instantiateFinalizeStepPhase(Params p)
    {
        _phasesBundle._finalizeStep = new FinalizeStep();
    }

    /**
     * Auxiliary method instantiating the "update objective space" phase.
     *
     * @param p params container
     */
    protected void instantiateUpdateObjectiveSpacePhase(Params p)
    {
        _phasesBundle._updateOS = null;
        if (p._osManager != null) _phasesBundle._updateOS = new UpdateObjectiveSpace(p._osManager);
    }

    /**
     * Auxiliary method for registering the OS change listeners.
     *
     * @param p params container
     */
    protected void registerOSChangeListeners(Params p)
    {
        IOSChangeListener[] listeners = getOSChangedListeners(p);
        if ((_osManager != null) && (listeners != null))
            _osManager.addOSChangeListeners(listeners);
    }


    /**
     * Auxiliary method for retrieving OS changed listeners (facilitates customization by the class extensions).
     *
     * @param p params container
     * @return OS changed listeners.
     */
    protected IOSChangeListener[] getOSChangedListeners(AbstractEABundle.Params p)
    {
        return null;
    }

    /**
     * This method can be overwritten to provide initial normalization data to selected objects.
     *
     * @param p params container
     */
    protected void registerInitialNormalizations(AbstractEABundle.Params p)
    {

    }
}
