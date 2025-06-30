package interaction.feedbackprovider.dm.artificial.value;

import alternative.Alternative;
import exeption.FeedbackProviderException;
import exeption.PreferenceModelException;
import interaction.reference.ReferenceSet;
import interaction.reference.ReferenceSets;
import model.IPreferenceModel;
import model.internals.value.AbstractValueInternalModel;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;

import java.util.LinkedList;

/**
 * Implementation of {@link IFormConstructor} for constructing pairwise comparisons from the input reference sets.
 *
 * @author MTomczyk
 */


public class PairwiseComparisons<T extends AbstractValueInternalModel> implements IFormConstructor<T>
{
    /**
     * If true, the reference sets of size 2 are only examined; false otherwise (size must be greater or equal to 2).
     */
    private final boolean _considerOnlyTwoElementSets;

    /**
     * Auxiliary threshold for the indifference relation. If the value difference attained by two alternatives is smaller
     * than the threshold, they are considered indifferent.
     */
    private final double _indifferenceThreshold;


    /**
     * Default constructor (only 2-element reference sets are examined, no indifference threshold).
     *
     */
    public PairwiseComparisons()
    {
        this(true, Double.NEGATIVE_INFINITY);
    }

    /**
     * Parameterized constructor (no indifference threshold).
     *
     * @param considerOnlyTwoElementSets if true, the reference sets of size 2 are only examined; false otherwise (size must be greater or equal to 2)
     */
    public PairwiseComparisons(boolean considerOnlyTwoElementSets)
    {
        this(considerOnlyTwoElementSets, Double.NEGATIVE_INFINITY);
    }


    /**
     * Parameterized constructor.
     *
     * @param considerOnlyTwoElementSets if true, the reference sets of size 2 are only examined; false otherwise (size must be greater or equal to 2)
     * @param indifferenceThreshold auxiliary threshold for the indifference relation. If the value difference attained by two alternatives is smaller than the threshold, they are considered indifferent

     */
    public PairwiseComparisons(boolean considerOnlyTwoElementSets, double indifferenceThreshold)
    {
        _considerOnlyTwoElementSets = considerOnlyTwoElementSets;
        _indifferenceThreshold = indifferenceThreshold;
    }

    /**
     * Method for retrieving pairwise comparisons from the input reference sets.
     *
     * @param referenceSets input reference sets derived via reference sets constructor ({@link interaction.reference.ReferenceSetsConstructor})
     * @param model         preference model associated with the artificial decision maker ({@link ArtificialValueDM})
     * @return preference information retrieved
     * @throws FeedbackProviderException the exception can be thrown 
     */
    @Override
    public LinkedList<IPreferenceInformation> getFeedback(ReferenceSets referenceSets, IPreferenceModel<T> model) throws FeedbackProviderException
    {
        LinkedList<IPreferenceInformation> feedback = new LinkedList<>();

        int[] u;
        if (_considerOnlyTwoElementSets) u = new int[]{2};
        else u = referenceSets.getUniqueSizes();

        for (int size : u)
        {
            if (size < 2) continue;
            LinkedList<ReferenceSet> rss = referenceSets.getReferenceSets().get(size);
            if (rss == null) continue;
            for (ReferenceSet rs : rss)
            {
                for (int i = 0; i < rs.getSize(); i++)
                {
                    Alternative A1 = rs.getAlternatives().get(i);
                    double e1;
                    try
                    {
                        e1 = model.evaluate(A1);
                    } catch (PreferenceModelException e)
                    {
                        throw new FeedbackProviderException("Could not evaluate an alternative " + e.getDetailedReasonMessage(), this.getClass(), e);
                    }

                    for (int j = i + 1; j < rs.getSize(); j++)
                    {
                        Alternative A2 = rs.getAlternatives().get(j);

                        double e2;
                        try
                        {
                            e2 = model.evaluate(A2);
                        } catch (PreferenceModelException e)
                        {
                            throw new FeedbackProviderException("Could not evaluate an alternative " + e.getDetailedReasonMessage(), this.getClass(), e);
                        }

                        int c = Double.compare(e1, e2);
                        if (Double.compare(Math.abs(e1 - e2), _indifferenceThreshold) < 0)
                            feedback.add(PairwiseComparison.getIndifference(A1, A2));
                        else
                        {
                            if (model.isLessPreferred())
                            {
                                if (c < 0) feedback.add(PairwiseComparison.getPreference(A1, A2));
                                else feedback.add(PairwiseComparison.getPreference(A2, A1));
                            }
                            else
                            {
                                if (c < 0) feedback.add(PairwiseComparison.getPreference(A2, A1));
                                else feedback.add(PairwiseComparison.getPreference(A1, A2));
                            }
                        }


                    }
                }
            }

        }
        return feedback;
    }
}
