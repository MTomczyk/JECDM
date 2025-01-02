package model.constructor;

import compatibility.CompatibilityAnalyzer;
import dmcontext.DMContext;
import exeption.ConstructorException;
import history.PreferenceInformationWrapper;
import model.internals.AbstractInternalModel;
import space.normalization.INormalization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Abstract implementation of {@link IConstructor}. Provides common fields and functionalities.
 *
 * @author MTomczyk
 */
public abstract class AbstractConstructor<T extends AbstractInternalModel> implements IConstructor<T>
{
    /**
     * Constructor's name.
     */
    protected String _name;

    /**
     * Current decision-making context.
     */
    protected DMContext _dmContext;

    /**
     * Models maintained by the constructor.
     */
    protected ArrayList<T> _models;

    /**
     * Most recently obtained preference information.
     */
    protected LinkedList<PreferenceInformationWrapper> _mostRecentPreferenceInformation;

    /**
     * Flag indicating whether the preference elicitation was attempted.
     */
    protected boolean _preferenceElicitationAttempted;

    /**
     * Flag indicating whether the preference elicitation failed.
     */
    protected boolean _preferenceElicitationFailed;

    /**
     * Flag indicating whether the inconsistency was triggered and the constructor is now trying to deal with it.
     */
    protected boolean _inconsistencyHandlingMode;

    /**
     * List of preference examples that were removed in the most recent attempt to reintroduce consistency (provided via wrappers).
     */
    protected LinkedList<PreferenceInformationWrapper> _removedPreferenceInformation;

    /**
     * List of preference examples that were added in the most recent attempt to reintroduce consistency (provided via wrappers).
     */
    protected LinkedList<PreferenceInformationWrapper> _addedPreferenceInformation;

    /**
     * Auxiliary flag for ensuring normalizations transfer.
     */
    protected boolean _normalizationsSuppliedAtLeastOnce;

    /**
     * Additional flag indicating whether the normalizations were updated during the most recent model bundle construction.
     */
    protected boolean _normalizationsWereUpdated = false;

    /**
     * Auxiliary object responsible for calculating the degree to which a given preference information is compatible
     * with an input preference model.
     */
    protected final CompatibilityAnalyzer _compatibilityAnalyzer;

    /**
     * Parameterized constructor.
     *
     * @param name                  constructor's name
     * @param compatibilityAnalyzer auxiliary object responsible for calculating the degree to which a given preference
     *                              information is compatible with an input preference model; if null, the default
     *                              object instance is instantiated
     */
    protected AbstractConstructor(String name, CompatibilityAnalyzer compatibilityAnalyzer)
    {
        _name = name;
        _models = null;
        _normalizationsSuppliedAtLeastOnce = false;
        _compatibilityAnalyzer = Objects.requireNonNullElseGet(compatibilityAnalyzer, CompatibilityAnalyzer::new);
    }

    /**
     * Auxiliary method that can be called to clear all stored models (can be called, e.g., when the inconsistency was detected).
     */
    @Override
    public void clearModels()
    {
        if (_models != null) _models.clear();
    }

    /**
     * Auxiliary method that can be used to register the current decision-making context {@link DMContext}.
     *
     * @param dmContext decision-making context
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void registerDecisionMakingContext(DMContext dmContext) throws ConstructorException
    {
        if (dmContext == null)
            throw new ConstructorException("The input decision-making context is null", this.getClass());
        _dmContext = dmContext;
        _preferenceElicitationAttempted = false;
        _preferenceElicitationFailed = false;
        _inconsistencyHandlingMode = false;
        _removedPreferenceInformation = null;
        _normalizationsWereUpdated = false;
    }

    /**
     * Auxiliary method that supplies the currently stored models with new normalizations objects.
     *
     * @param normalizations normalizations objects
     */
    protected void supplyModelsWithNormalizations(INormalization[] normalizations)
    {
        if (_models != null)
            for (AbstractInternalModel m : _models)
                m.setNormalizations(normalizations);
    }

    /**
     * Method for notifying that the preference elicitation begins.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyPreferenceElicitationBegins() throws ConstructorException
    {
        _preferenceElicitationAttempted = true;
    }

    /**
     * Method for notifying that the preference elicitation failed (e.g., when there were no reasonable alternatives for comparison).
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    public void notifyPreferenceElicitationFailed() throws ConstructorException
    {
        _preferenceElicitationFailed = true;
    }

    /**
     * Method for notifying on the most recent preference information provided by the decision maker.
     *
     * @param preferenceInformation preference information (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyAboutMostRecentPreferenceInformation(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        _mostRecentPreferenceInformation = preferenceInformation;
    }

    /**
     * Method for notifying that the preference elicitation ends.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyPreferenceElicitationEnds() throws ConstructorException
    {

    }

    /**
     * Method for notifying that the internal models construction begins.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyModelsConstructionBegins() throws ConstructorException
    {

    }

    /**
     * The main method for constructing internal models (array) given the decision maker's preference information.
     * This method is divided into three phases (executed sequentially):
     * pre ({@link AbstractConstructor#postConstructModels(Report, LinkedList)}),
     * main ({@link AbstractConstructor#mainConstructModels(Report, LinkedList)}), and
     * post ({@link AbstractConstructor#postConstructModels(Report, LinkedList)}).
     *
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return constructed models (bundle object that also provides additional data)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public Report<T> constructModels(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        Report<T> bundle = preConstructModels(preferenceInformation);
        mainConstructModels(bundle, preferenceInformation);
        postConstructModels(bundle, preferenceInformation);
        return bundle;
    }

    /**
     * The pre-construct models phase. This default implementation creates the bundle and fills some of its fields.
     *
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @return bundle result object to be filled
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    protected Report<T> preConstructModels(LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        Report<T> bundle = getReportInstance();
        bundle._constructionStartTime = System.nanoTime();
        return bundle;
    }

    /**
     * Auxiliary method for creating report instance.
     * @return report instance
     */
    protected Report<T> getReportInstance()
    {
        return new Report<>(_dmContext);
    }

    /**
     * The main-construct models phase (to be overwritten).
     * The concrete extension should provide the constructed models via the bundle object.
     *
     * @param bundle                bundle result object to be filled (provided via wrappers)
     * @param preferenceInformation the decision maker's preference information stored
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    protected void mainConstructModels(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {

    }

    /**
     * The post-construct models phase. This default implementation fills some auxiliary bundle fields.
     *
     * @param bundle                bundle result object to be filled
     * @param preferenceInformation the decision maker's preference information stored (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    protected void postConstructModels(Report<T> bundle, LinkedList<PreferenceInformationWrapper> preferenceInformation) throws ConstructorException
    {
        _normalizationsWereUpdated = false;
        bundle._constructionStopTime = System.nanoTime();
        bundle._constructionElapsedTime = (bundle._constructionStopTime - bundle._constructionStartTime) / 1000000;
    }


    /**
     * Method for notifying that the internal models construction ends.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyModelsConstructionEnds() throws ConstructorException
    {

    }

    /**
     * Method for notifying that the consistency reintroduction begins.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionBegins() throws ConstructorException
    {
        _inconsistencyHandlingMode = true;
        _removedPreferenceInformation = null;
        _addedPreferenceInformation = null;
    }

    /**
     * Method for notifying about the beginning of an attempt to reintroduce consistency (one construction process is called in one attempt)
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionAttemptBegins() throws ConstructorException
    {
        _removedPreferenceInformation = null;
        _addedPreferenceInformation = null;
    }


    /**
     * Method for notifying about preference examples removed from the initial set when attempting to reinstate consistency.
     *
     * @param removedPreferenceInformation removed preference examples (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyAboutRemovedPreferenceInformation(LinkedList<PreferenceInformationWrapper> removedPreferenceInformation) throws ConstructorException
    {
        _removedPreferenceInformation = removedPreferenceInformation;
    }


    /**
     * Method for notifying about preference examples added from the preference examples set when attempting to reinstate consistency.
     *
     * @param addedPreferenceInformation added preference examples (provided via wrappers)
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyAboutAddedPreferenceInformation(LinkedList<PreferenceInformationWrapper> addedPreferenceInformation) throws ConstructorException
    {
        _addedPreferenceInformation = addedPreferenceInformation;
    }

    /**
     * Method for notifying about the ending of an attempt to reintroduce consistency (one construction process is called in one attempt)
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionAttemptEnds() throws ConstructorException
    {
        _removedPreferenceInformation = null;
        _addedPreferenceInformation = null;
    }

    /**
     * Method for notifying that the consistency reintroduction ends.
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void notifyConsistencyReintroductionEnds() throws ConstructorException
    {
        _inconsistencyHandlingMode = false;
    }


    /**
     * Unregisters the decision-making context
     *
     * @throws ConstructorException the exception can be thrown and propagated higher
     */
    @Override
    public void unregisterDecisionMakingContext() throws ConstructorException
    {
        _dmContext = null;
    }

    /**
     * Getter for compatibility analyzer.
     *
     * @return compatibility analyzer
     */
    @Override
    public CompatibilityAnalyzer getCompatibilityAnalyzer()
    {
        return _compatibilityAnalyzer;
    }
}
