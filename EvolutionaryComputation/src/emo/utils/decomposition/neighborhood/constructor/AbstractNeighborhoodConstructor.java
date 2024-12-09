package emo.utils.decomposition.neighborhood.constructor;

import emo.utils.decomposition.family.Family;
import emo.utils.decomposition.neighborhood.Neighborhood;
import emo.utils.decomposition.similarity.ISimilarity;

/**
 * Abstract, general implementation of the {@link INeighborhoodConstructor} interface.
 *
 * @author MTomczyk
 */

public abstract class AbstractNeighborhoodConstructor implements INeighborhoodConstructor
{
    /**
     * Constructs and returns a neighborhood structure for a given family of goals.
     *
     * @param family     goals family
     * @param similarity similarity measure
     * @param size       neighborhood size (should be smaller/equal the family size)
     * @return constructed neighborhood
     */
    @Override
    public Neighborhood getNeighborhood(Family family, ISimilarity similarity, int size)
    {
        return getNeighborhood(family, similarity, size, new Family[]{family});
    }

    /**
     * Constructs and returns a neighborhood structure for a given family of goals (to be overwritten).
     *
     * @param family     goals family
     * @param similarity similarity measure
     * @param size       neighborhood size (should be smaller/equal the total number of goals stored in the scope)
     * @param scope      if provided, neighbors are supposed to be determined using the provided families (it can be a subset of all families, stored in an arbitrary order)
     * @return constructed neighborhood
     */
    @Override
    public Neighborhood getNeighborhood(Family family, ISimilarity similarity, int size, Family[] scope)
    {
        return null;
    }

}
