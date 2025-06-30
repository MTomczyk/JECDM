package interaction.feedbackprovider.dm.artificial.value;

import exeption.FeedbackProviderException;
import interaction.reference.ReferenceSets;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;
import preference.IPreferenceInformation;

import java.util.LinkedList;

/**
 * Interface for classes responsible for providing concrete forms of preference information given the input reference sets
 * (associated with {@link ArtificialValueDM}).
 *
 * @author MTomczyk
 */
public interface IFormConstructor<T extends AbstractValueInternalModel>
{
    /**
     * Signature for the method responsible for retrieving pieces of preferences having a specified form.
     *
     * @param referenceSets input reference sets derived via reference sets constructor ({@link interaction.reference.ReferenceSetsConstructor})
     * @param model         preference model associated with the artificial decision maker ({@link ArtificialValueDM})
     * @return preference information retrieved
     * @throws FeedbackProviderException the exception can be thrown 
     */
    LinkedList<IPreferenceInformation> getFeedback(ReferenceSets referenceSets, IPreferenceModel<T> model) throws FeedbackProviderException;
}
