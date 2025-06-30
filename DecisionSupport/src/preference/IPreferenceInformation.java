package preference;

import preference.indirect.PairwiseComparison;

/**
 * General interface for classes responsible for simulating the decision maker's preference examples (preference
 * information provided).
 *
 * @author MTomczyk
 */
public interface IPreferenceInformation
{
    /**
     * Method for returning the preference information viewed as pairwise comparisons. Should return null if the
     * specific form cannot be translated into this form.
     *
     * @return preference example viewed as a series of pairwise comparisons (null if not possible)
     */
    PairwiseComparison[] getPairwiseComparisons();

    /**
     * Method for returning the number of pieces of preference that can be viewed as pairwise comparisons.
     *
     * @return the number of pieces of preference that can be viewed as pairwise comparisons.
     */
    int getNoPairwiseComparisons();

    /**
     * The implemented method should return false if the preference example is direct (explicit, e.g., provided
     * weights), true otherwise (indirect, holistic form, e.g., pairwise comparison).
     *
     * @return false if the preference example is direct, true otherwise (holistic)
     */
    boolean isIndirect();

    /**
     * To string method should be overwritten.
     *
     * @return string representation
     */
    String toString();

    /**
     * To string method should be overwritten (returns the string representation).
     *
     * @return string representation
     */
    String getStringRepresentation();

    /**
     * Returns the constant associated with the form of the preference information represented by the object instance.
     *
     * @return form.
     */
    Form getForm();

    /**
     * Supposed to return a copy of the object. The copy should be deep, at least at this object's level (i.e., the
     * implementation does not need to guarantee that the bottom objects -- e.g., alternatives -- will be cloned).
     *
     * @return copy of the object
     */
    IPreferenceInformation getClone();
}
