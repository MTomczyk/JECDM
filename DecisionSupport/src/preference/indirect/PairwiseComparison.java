package preference.indirect;

import alternative.Alternative;
import preference.Form;
import preference.IPreferenceInformation;
import relation.Relations;


/**
 * Class representing a pairwise comparison.
 *
 * @author MTomczyk
 */
public class PairwiseComparison extends AbstractIndirectForm implements IPreferenceInformation
{

    /**
     * Parameterized constructor.
     *
     * @param first    the first alternative
     * @param second   the second alternative
     * @param relation relation between the alternatives
     */
    protected PairwiseComparison(Alternative first, Alternative second, Relations relation)
    {
        super("Pairwise comparison: " + first.getName() + " " + relation.name() + " " + second.getName(), relation);
        _alternatives = new Alternative[]{first, second};
    }

    /**
     * Object constructor for the case A preferred over B.
     *
     * @param preferred    the preferred alternative
     * @param notPreferred the not preferred alternative
     * @return object instance
     */
    public static PairwiseComparison getPreference(Alternative preferred, Alternative notPreferred)
    {
        return new PairwiseComparison(preferred, notPreferred, Relations.PREFERENCE);
    }

    /**
     * Object constructor for the case A indifferent to B.
     *
     * @param A one of the indifferent alternatives
     * @param B the other indifferent alternative
     * @return object instance
     */
    public static PairwiseComparison getIndifference(Alternative A, Alternative B)
    {
        return new PairwiseComparison(A, B, Relations.INDIFFERENCE);
    }

    /**
     * Object constructor for the case A incomparable to B.
     *
     * @param A one of the indifferent alternatives
     * @param B the other indifferent alternative
     * @return object instance
     */
    public static PairwiseComparison getIncomparable(Alternative A, Alternative B)
    {
        return new PairwiseComparison(A, B, Relations.INCOMPARABILITY);
    }


    /**
     * The method returns the preferred alternative. If relation is not PREFERENCE, the method returns null.
     *
     * @return the preferred alternative (or null, if they are indifferent)
     */
    public Alternative getPreferredAlternative()
    {
        if (_relations[0].equals(Relations.PREFERENCE)) return _alternatives[0];
        else return null;
    }

    /**
     * The method returns the not preferred alternative. If relation is not PREFERENCE, the method returns null.
     *
     * @return the outperformed alternative (or null, if they are indifferent)
     */
    public Alternative getNotPreferredAlternative()
    {
        if (_relations[0].equals(Relations.PREFERENCE)) return _alternatives[1];
        else return null;
    }

    /**
     * Returns the relation between the alternatives.
     *
     * @return relation between the alternatives
     */
    public Relations getRelation()
    {
        return _relations[0];
    }

    /**
     * Getter for the first (left) alternative stored.
     *
     * @return the first alternative stored
     */
    public Alternative getFirstAlternative()
    {
        return _alternatives[0];
    }

    /**
     * Getter for the second (right) alternative stored.
     *
     * @return the second alternative stored
     */
    public Alternative getSecondAlternative()
    {
        return _alternatives[1];
    }


    /**
     * Method for returning the preference information viewed as pairwise comparisons.Returns itself, wrapped in array.
     *
     * @return preference example viewed as a series of pairwise comparisons
     */
    @Override
    public PairwiseComparison[] getPairwiseComparisons()
    {
        return new PairwiseComparison[]{this};
    }

    /**
     * Method for returning the number of pieces of preference that can be viewed as pairwise comparisons.
     *
     * @return the number of pieces of preference that can be viewed as pairwise comparisons.
     */
    @Override
    public int getNoPairwiseComparisons()
    {
        return 1;
    }

    /**
     * Returns the constant associated with the form of the preference information represented by the object instance.
     *
     * @return form.
     */
    @Override
    public Form getForm()
    {
        return Form.PAIRWISE_COMPARISON;
    }

    /**
     * Returns a copy of the object.  The copy should be deep, at least at this object's level (i.e., the implementation
     * does not need to guarantee that the bottom objects -- e.g., alternatives -- will be cloned).
     *
     * @return deep copy of the object
     */
    @Override
    public IPreferenceInformation getClone()
    {
        return new PairwiseComparison(_alternatives[0], _alternatives[1], _relations[0]);
    }
}
