package emo.interactive;

import exception.EAException;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.internals.AbstractInternalModel;

/**
 * Extension of {@link AbstractDSSBuilder} for handling a standard interaction model that involves:<br>
 * 1) One decision-maker <br>
 * 2) One decision-maker's preference model <br>
 * 3) One preference learning procedure coupled with the decision maker's preference model <br>
 * 4) One interaction rule<br>
 * Its extensions are supposed to assist in constructing preference-based methods.
 *
 * @author MTomczyk
 */
public class StandardDSSBuilder<T extends AbstractInternalModel> extends AbstractDSSBuilder
{
    /**
     * The interaction rule (decides when to interact).
     */
    private IRule _interactionRule;

    /**
     * The object responsible for constructing reference alternatives to be evaluated by the decision-maker.
     */
    private IReferenceSetConstructor _referenceSetConstructor = null;

    /**
     * The decision maker's feedback provider. Used to construct preference examples from presented reference sets.
     */
    private IDMFeedbackProvider _dmFeedbackProvider;

    /**
     * Preference model used to model the decision-maker's preferences.
     */
    private IPreferenceModel<T> _preferenceModel = null;

    /**
     * Object responsible for learning assumed preference model instances.
     */
    private IConstructor<T> _modelConstructor = null;

    /**
     * Setter for the interaction rule (decides when to interact).
     *
     * @param interactionRule interaction rule
     */
    public void setInteractionRule(IRule interactionRule)
    {
        _interactionRule = interactionRule;
    }

    /**
     * Getter for the interaction rule (decides when to interact).
     *
     * @return interaction rule
     */
    public IRule getInteractionRule()
    {
        return _interactionRule;
    }

    /**
     * Setter for the object responsible for constructing reference alternatives to be evaluated by the decision-maker.
     *
     * @param referenceSetConstructor the object responsible for constructing reference alternatives to be evaluated by
     *                                the decision-maker
     */
    public void setReferenceSetConstructor(IReferenceSetConstructor referenceSetConstructor)
    {
        _referenceSetConstructor = referenceSetConstructor;
    }

    /**
     * Getter for the object responsible for constructing reference alternatives to be evaluated by the decision-maker.
     *
     * @return the object responsible for constructing reference alternatives to be evaluated by the decision-maker
     */
    public IReferenceSetConstructor getReferenceSetConstructor()
    {
        return _referenceSetConstructor;
    }

    /**
     * Setter for the decision maker's feedback provider. Used to construct preference examples from presented reference
     * sets.
     *
     * @param dmFeedbackProvider the decision maker's feedback provider
     */
    public void setDMFeedbackProvider(IDMFeedbackProvider dmFeedbackProvider)
    {
        _dmFeedbackProvider = dmFeedbackProvider;
    }

    /**
     * Getter for the decision maker's feedback provider. Used to construct preference examples from presented reference
     * sets.
     *
     * @return the decision maker's feedback provider
     */
    public IDMFeedbackProvider getDMFeedbackProvider()
    {
        return _dmFeedbackProvider;
    }

    /**
     * Setter for the preference model used to model the decision-maker's preferences.
     *
     * @param preferenceModel preference model used to model the decision-maker's preferences
     */
    public void setPreferenceModel(IPreferenceModel<T> preferenceModel)
    {
        _preferenceModel = preferenceModel;
    }

    /**
     * Getter for the preference model used to model the decision-maker's preferences.
     *
     * @return preference model used to model the decision-maker's preferences
     */
    public IPreferenceModel<T> getPreferenceModel()
    {
        return _preferenceModel;
    }

    /**
     * Setter for the object responsible for learning assumed preference model instances.
     *
     * @param modelConstructor the object responsible for learning assumed preference model instances
     */
    public void setModelConstructor(IConstructor<T> modelConstructor)
    {
        _modelConstructor = modelConstructor;
    }

    /**
     * Getter for the object responsible for learning assumed preference model instances.
     *
     * @return the object responsible for learning assumed preference model instances
     */
    public IConstructor<T> getModelConstructor()
    {
        return _modelConstructor;
    }


    /**
     * Auxiliary method for performing a simple data validation.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (_interactionRule == null)
            throw EAException.getInstanceWithSource("No interaction rule has been provided", this.getClass());
        if (_referenceSetConstructor == null)
            throw EAException.getInstanceWithSource("No reference set constructor has been provided", this.getClass());
        if (_dmFeedbackProvider == null)
            throw EAException.getInstanceWithSource("No decision-maker's feedback provider has been provided", this.getClass());
        if (_preferenceModel == null)
            throw EAException.getInstanceWithSource("No preference model has been provided", this.getClass());
        if (_modelConstructor == null)
            throw EAException.getInstanceWithSource("No model constructor has been provided", this.getClass());
    }
}
