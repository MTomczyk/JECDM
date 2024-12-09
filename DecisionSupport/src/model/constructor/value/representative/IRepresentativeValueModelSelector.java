package model.constructor.value.representative;

import history.PreferenceInformationWrapper;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Interfaces for classes responsible for determining a representative model instance from a given set of generated
 * model instances.
 *
 * @author MTomczyk
 */
public interface IRepresentativeValueModelSelector<T extends AbstractValueInternalModel>
{
    /**
     * Signature for the main method.
     *
     * @param models                candidate model instances
     * @param preferenceInformation collected preference examples
     * @return model selected from the input set
     */
    T selectModel(ArrayList<T> models, LinkedList<PreferenceInformationWrapper> preferenceInformation);
}
