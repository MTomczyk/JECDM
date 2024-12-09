package compatibility;

import model.internals.value.AbstractValueInternalModel;
import preference.IPreferenceInformation;

/**
 * Interface for analyzers responsible for calculating the degree to which the given preference information is
 * compatible with a preference model. It is assumed that positive values reflect compatibility, 0 reflects the
 * boundary case, and negative values represent incompatibility.
 *
 * @author MTomczyk
 */
public interface IAnalyzer
{
    /**
     * The signature for the main method for calculating te compatibility degree.
     * @param preferenceInformation analyzed preference information
     * @param model the preference model
     * @return compatibility degree
     */
    Double calculateCompatibilityDegreeWithValueModel(IPreferenceInformation preferenceInformation, AbstractValueInternalModel model);
}
