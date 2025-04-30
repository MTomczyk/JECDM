package model.constructor.value.rs.ers;

import model.similarity.ISimilarity;
import model.internals.value.AbstractValueInternalModel;

/**
 * Abstract custom comparator used by {@link ModelsQueue} to sort models.
 *
 * @author MTomczyk
 */
public abstract class AbstractComparator<T extends AbstractValueInternalModel> implements java.util.Comparator<SortedModel<T>>
{
    /**
     * Similarity measure used.
     */
    protected ISimilarity<T> _similarity;

    /**
     * Setter for the similarity measure.
     *
     * @param similarity similarity measure
     */
    protected void setSimilarity(ISimilarity<T> similarity)
    {
        _similarity = similarity;
    }
}
