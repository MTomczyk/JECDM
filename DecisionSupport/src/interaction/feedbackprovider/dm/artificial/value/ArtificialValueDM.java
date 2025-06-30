package interaction.feedbackprovider.dm.artificial.value;

import exeption.FeedbackProviderException;
import interaction.feedbackprovider.dm.AbstractDMFeedbackProvider;
import interaction.feedbackprovider.dm.DMResult;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.reference.ReferenceSets;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;
import preference.IPreferenceInformation;

import java.util.LinkedList;


/**
 * Implementation of an artificial DM for providing a feedback. It is assumed that the internal model employed for
 * simulating the evaluation is a value model.
 *
 * @author MTomczyk
 */


public class ArtificialValueDM<T extends AbstractValueInternalModel> extends AbstractDMFeedbackProvider implements IDMFeedbackProvider
{
    /**
     * Preference model (value model) used to simulate the decision maker's preferences.
     */
    private final IPreferenceModel<T> _model;

    /**
     * Form constructors used by the artificial decision maker. These objects transform reference sets into
     * concrete answers (e.g., pairwise comparisons).
     */
    private final LinkedList<IFormConstructor<T>> _formConstructors;

    /**
     * Parameterized constructor. Uses {@link PairwiseComparisons} with no indifference threshold to transform input
     * reference sets into pairwise comparisons.
     *
     * @param model preference model used to simulate the decision maker's preferences
     */
    public ArtificialValueDM(IPreferenceModel<T> model)
    {
        _model = model;
        _formConstructors = new LinkedList<>();
        _formConstructors.add(new PairwiseComparisons<>(true, Double.NEGATIVE_INFINITY));

    }


    /**
     * Parameterized constructor.
     *
     * @param model           preference model used to simulate the decision maker's preferences
     * @param formConstructor form constructor used by the artificial decision maker;
     *                        these objects transform reference sets into concrete answers (e.g., pairwise comparisons)
     */
    public ArtificialValueDM(IPreferenceModel<T> model, IFormConstructor<T> formConstructor)
    {
        _model = model;
        if (formConstructor == null) _formConstructors = null;
        else
        {
            _formConstructors = new LinkedList<>();
            _formConstructors.add(formConstructor);
        }
    }


    /**
     * Parameterized constructor.
     *
     * @param model            preference model used to simulate the decision maker's preferences
     * @param formConstructors form constructors used by the artificial decision maker;
     *                         these objects transform reference sets into concrete answers (e.g., pairwise comparisons)
     */
    public ArtificialValueDM(IPreferenceModel<T> model, LinkedList<IFormConstructor<T>> formConstructors)
    {
        _model = model;
        _formConstructors = formConstructors;
    }


    /**
     * Auxiliary validation method.
     *
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public void validate() throws FeedbackProviderException
    {
        if (_formConstructors == null)
            throw new FeedbackProviderException("The form constructors are not specified (the array is null)", this.getClass());
        if (_formConstructors.isEmpty())
            throw new FeedbackProviderException("The form constructors are not specified (the array is empty)", this.getClass());
        for (IFormConstructor<T> fc : _formConstructors)
            if (fc == null)
                throw new FeedbackProviderException("One of the provided form constructors is null", this.getClass());
    }


    /**
     * The main method for providing the feedback (wrapped via {@link DMResult}).
     *
     * @param referenceSets reference sets that are the base for evaluation
     * @return feedback (wrapped via {@link DMResult}).
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public DMResult getFeedback(ReferenceSets referenceSets) throws FeedbackProviderException
    {
        if (referenceSets == null)
            throw new FeedbackProviderException("The reference sets are not provided (the input is null)", this.getClass());

        DMResult result = new DMResult(_dmContext, _dm);
        long startTime = System.nanoTime();
        result._feedback = new LinkedList<>();

        for (IFormConstructor<T> fc : _formConstructors)
        {
            LinkedList<IPreferenceInformation> pi = fc.getFeedback(referenceSets, _model);
            if ((pi != null) && (!pi.isEmpty())) result._feedback.addAll(pi);
        }

        result._processingTime = (double) (System.nanoTime() - startTime) / 1000000.0d;
        return result;
    }
}
