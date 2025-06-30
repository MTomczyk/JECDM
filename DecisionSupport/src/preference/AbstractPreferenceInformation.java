package preference;

import preference.indirect.PairwiseComparison;


/**
 * Abstract implementation of {@link IPreferenceInformation}. Provides common fields, functionalities.
 *
 * @author MTomczyk
 */
public class AbstractPreferenceInformation implements IPreferenceInformation
{
    /**
     * Name of the preference information (string representation).
     */
    protected final String _name;

    /**
     * Parameterized constructor.
     *
     * @param name name of the preference information (string representation)
     */
    public AbstractPreferenceInformation(String name)
    {
        _name = name;
    }

    /**
     * Method for returning the number of pieces of preference that can be viewed as pairwise comparisons.
     *
     * @return  the number of pieces of preference that can be viewed as pairwise comparisons.
     */
    @Override
    public int getNoPairwiseComparisons()
    {
        return 0;
    }

    /**
     * Method for returning the preference information viewed as pairwise comparisons. Should return null if the
     * specific form cannot be translated into this form.
     *
     * @return preference example viewed as a series of pairwise comparisons (null if not possible)
     */
    @Override
    public PairwiseComparison[] getPairwiseComparisons()
    {
        return null;
    }

    /**
     * The implemented method should return false if the preference example is direct (explicit, e.g., provided
     * weights), true otherwise (indirect, holistic form, e.g., pairwise comparison).
     *
     * @return false if the preference example is direct, true otherwise (holistic)
     */
    @Override
    public boolean isIndirect()
    {
        return false;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }

    /**
     * Returns the string representation.
     *
     * @return string representation
     */
    @Override
    public String getStringRepresentation()
    {
        return _name;
    }

    /**
     * Returns the constant associated with the form of the preference information represented by the object instance.
     *
     * @return form.
     */
    @Override
    public Form getForm()
    {
        return null;
    }

    /**
     * Supposed to return a copy of the object. The copy should be deep, at least at this object's level (i.e., it is
     * not guaranteed that the bottom objects are cloned, e.g., alternatives). Returns null for the abstract
     * implementation.
     *
     * @return null
     */
    @Override
    public IPreferenceInformation getClone()
    {
        return null;
    }

}
