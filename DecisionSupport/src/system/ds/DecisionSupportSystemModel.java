package system.ds;

import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.DecisionSupportSystemException;
import exeption.ModelSystemException;
import exeption.ModuleException;
import model.internals.AbstractInternalModel;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.model.ModelSystem;
import system.modules.elicitation.PreferenceElicitationModule;
import system.modules.updater.ModelsUpdaterModule;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Model object for {@link DecisionMakerSystem}. It contains the essential objects and provides means for their
 * initialization and validation.
 *
 * @author MTomczyk
 */


class DecisionSupportSystemModel
{
    /**
     * Represents a consistent family of criteria.
     */
    protected Criteria _criteria;

    /**
     * Decision maker identifiers.
     */
    protected DM[] _DMs;

    /**
     * Decision maker systems.
     */
    protected DecisionMakerSystem[] _DMSs;

    /**
     * Auxiliary map that associated a decision maker (identifier) with its system.
     */
    protected HashMap<DM, DecisionMakerSystem> _mapDMSs;

    /**
     * Preference elicitation module.
     */
    protected PreferenceElicitationModule _preferenceElicitationModule;

    /**
     * Models updater module.
     */
    protected ModelsUpdaterModule _modelsUpdaterModule;

    /**
     * Date and time when {@link DecisionSupportSystem#notifySystemStarts()} was called.
     */
    protected LocalDateTime _systemStartTimestamp;

    /**
     * Flag indicating whether the system started.
     */
    protected boolean _systemStarted = false;

    /**
     * Auxiliary method for deriving the current decision-making context object from the provided params container.
     *
     * @param pDMC params container
     * @return decision-making context object
     * @throws DecisionSupportSystemException the exception will be thrown if (1) alternatives superset is not provided,
     *                                        (2) current objective space is not provided, or (3) current iteration number is not provided.
     */
    protected DMContext deriveDecisionMakingContext(DMContext.Params pDMC) throws DecisionSupportSystemException
    {
        if (pDMC == null)
            throw new DecisionSupportSystemException("The params container is not provided", this.getClass());
        if (pDMC._currentAlternativesSuperset == null)
            throw new DecisionSupportSystemException("The current alternatives superset is not provided", this.getClass());
        if (pDMC._currentIteration == null)
            throw new DecisionSupportSystemException("The current iteration is not provided", this.getClass());

        return new DMContext(pDMC, _criteria, _systemStartTimestamp);
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    protected DecisionSupportSystemModel(DecisionSupportSystem.Params p) throws DecisionSupportSystemException
    {
        if (p == null) throw new DecisionSupportSystemException("The params object is not provided", this.getClass());
        initCriteria(p);
        initDecisionMakerIdentifiers(p);
        initDecisionMakerSystems(p);
        initDecisionMakerMaps();
        initPreferenceElicitationModule(p);
        initModelsUpdaterModule(p);
    }

    /**
     * Auxiliary method for criteria initialization.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    private void initCriteria(DecisionSupportSystem.Params p) throws DecisionSupportSystemException
    {
        if (p._criteria == null)
            throw new DecisionSupportSystemException("The criteria are not provided (the object is null)", this.getClass());
        if (p._criteria._no <= 0)
            throw new DecisionSupportSystemException("The criteria are not provided (the number of criteria is less than 1)", this.getClass());
        _criteria = p._criteria;
    }

    /**
     * Auxiliary method for decision maker identifiers initialization.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    private void initDecisionMakerIdentifiers(DecisionSupportSystem.Params p) throws DecisionSupportSystemException
    {
        if (p._dmBundles == null)
            throw new DecisionSupportSystemException("The decision maker bundles are not provided (the array is null)", this.getClass());
        if (p._dmBundles.length == 0)
            throw new DecisionSupportSystemException("The decision maker bundles are not provided (the array is empty)", this.getClass());
        for (DMBundle dmBundle : p._dmBundles)
            if (dmBundle == null)
                throw new DecisionSupportSystemException("One of the provided decision maker bundles is null", this.getClass());
            else if (dmBundle._name == null)
                throw new DecisionSupportSystemException("One of the provided decision maker bundles provides no name", this.getClass());
        Set<String> names = new HashSet<>(p._dmBundles.length);
        for (DMBundle dmBundle : p._dmBundles)
        {
            if (names.contains(dmBundle._name))
                throw new DecisionSupportSystemException("The decision maker name = " + dmBundle._name + " is not unique", this.getClass());
            names.add(dmBundle._name);
        }
        _DMs = new DM[p._dmBundles.length];
        for (int i = 0; i < p._dmBundles.length; i++) _DMs[i] = new DM(i, p._dmBundles[i]._name);
    }

    /**
     * Auxiliary method for decision maker systems initialization.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    private void initDecisionMakerSystems(DecisionSupportSystem.Params p) throws DecisionSupportSystemException
    {
        _DMSs = new DecisionMakerSystem[_DMs.length];
        int idx = -1;

        for (DM dm : _DMs)
        {
            idx++;
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dm;
            if (p._dmBundles[idx]._modelBundles == null)
                throw new DecisionSupportSystemException("The model bundle(s) for the decision maker = " +
                        p._dmBundles[idx]._name + " is (are) not provided (the array is null)", this.getClass());
            if (p._dmBundles[idx]._modelBundles.length == 0)
                throw new DecisionSupportSystemException("The model bundle(s) for the decision maker = " +
                        p._dmBundles[idx]._name + " is (are) not provided (the array is empty)", this.getClass());
            for (ModelBundle<? extends AbstractInternalModel> modelBundle : p._dmBundles[idx]._modelBundles)
                if (modelBundle == null)
                    throw new DecisionSupportSystemException("One of the model bundles provided for the decision maker = " +
                            p._dmBundles[idx]._name + " is null", this.getClass());


            ModelBundle<?>[] mBundles = p._dmBundles[idx]._modelBundles;
            pDMS._addTimestamps = true;
            pDMS._modelSystems = new ModelSystem<?>[mBundles.length];
            for (int j = 0; j < mBundles.length; j++)
            {
                ModelSystem<? extends AbstractInternalModel> modelSystem;
                try
                {
                    modelSystem = new ModelSystem<>(mBundles[j]);
                } catch (ModelSystemException e)
                {
                    throw new DecisionSupportSystemException("Could not initialize a model system for a decision maker = " +
                            p._dmBundles[idx]._name + " " + e.getDetailedReasonMessage(), this.getClass(), e);
                }
                pDMS._modelSystems[j] = modelSystem;
            }

            try
            {
                _DMSs[idx] = new DecisionMakerSystem(pDMS);
            } catch (DecisionMakerSystemException e)
            {
                throw new DecisionSupportSystemException("Could not initialize decision maker system for a decision maker = " +
                        p._dmBundles[idx]._name + " " + e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }
    }


    /**
     * Auxiliary method for initializing a map that transforms a decision maker {@link DM} into its system {@link DecisionSupportSystem}.
     */
    private void initDecisionMakerMaps()
    {
        _mapDMSs = new HashMap<>();
        for (int i = 0; i < _DMs.length; i++)
            _mapDMSs.put(_DMs[i], _DMSs[i]);
    }


    /**
     * Auxiliary method for initializing the preference elicitation module {@link system.modules.elicitation.PreferenceElicitationModule}.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    private void initPreferenceElicitationModule(DecisionSupportSystem.Params p) throws DecisionSupportSystemException
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._DMs = _DMs;
        pP._DMSs = _DMSs;
        pP._interactionTrigger = p._interactionTrigger;
        pP._refiner = p._refiner;
        pP._referenceSetsConstructor = p._referenceSetsConstructor;
        pP._feedbackProvider = p._feedbackProvider;
        pP._collectInteractionTriggerResults = p._collectInteractionTriggerResults;
        pP._collectRefinerResults = p._collectRefinerResults;
        pP._collectReferenceSetsConstructionResults = p._collectReferenceSetsConstructionResults;
        _preferenceElicitationModule = new PreferenceElicitationModule(pP);
        try
        {
            _preferenceElicitationModule.validate();
        } catch (ModuleException e)
        {
            throw new DecisionSupportSystemException("Validation of the preference elicitation module failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }


    /**
     * Auxiliary method for initializing the models updater module {@link system.modules.updater.ModelsUpdaterModule}.
     *
     * @param p params container
     * @throws DecisionSupportSystemException the exception will be thrown if the data validation fails
     */
    private void initModelsUpdaterModule(DecisionSupportSystem.Params p) throws DecisionSupportSystemException
    {
        ModelsUpdaterModule.Params pP = new ModelsUpdaterModule.Params();
        pP._collectModelsUpdatesResults = p._collectModelsUpdatesResults;
        pP._DMs = _DMs;
        pP._DMSs = _DMSs;
        _modelsUpdaterModule = new ModelsUpdaterModule(pP);
        try
        {
            _modelsUpdaterModule.validate();
        } catch (ModuleException e)
        {
            throw new DecisionSupportSystemException("Validation of the models updater module failed " + e.getDetailedReasonMessage(), this.getClass(), e);
        }
    }

}
