package model.constructor.value.rs.ers.comparators;

import model.constructor.value.rs.ers.AbstractComparator;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;

/**
 * Custom comparator. It sorts a collection of {@link model.constructor.value.rs.ers.SortedModel} using the following rules:
 * 1) If both compared models are compatible, they are ranked according to their similarity to the k-nearest neighbor
 * (lesser/higher similarities are preferred - implementation dependent; the k index will be suitably truncated if
 * the neighbors are not available).
 * 2) Otherwise, the models are ranked according to their compatibility degree (higher values are preferred).
 *
 * @author MTomczyk
 */
public class MostSimilarWithTieResolving<T extends AbstractValueInternalModel> extends AbstractComparator<T>
        implements java.util.Comparator<SortedModel<T>>
{
    /**
     * Comparator implementation.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return 1 - the first element is preferred, 0 both inputs are equal, -1 - the second element is preferred
     */
    @Override
    public int compare(SortedModel<T> o1, SortedModel<T> o2)
    {
        if ((Double.compare(o1._compatibilityDegree, 0.0d) > 0) && (Double.compare(o2._compatibilityDegree, 0.0d) > 0))
        {
            int idx = 0;
            int comp = 0;
            while ((idx < o1._closestModels.getNoStoredModels()) &&
                    (idx < o2._closestModels.getNoStoredModels()) &&
                    ((comp = Double.compare(o1._closestModels._similarities[idx],
                            o2._closestModels._similarities[idx])) == 0)) idx++;
            if (_similarity.isLessMeaningCloser()) return -comp;
            else return comp;
        }
        return -Double.compare(o1._compatibilityDegree, o2._compatibilityDegree);
    }
}
