package emo.aposteriori.moead;

import ea.EA;
import emo.AbstractDecompositionEMOABuilder;
import emo.utils.decomposition.alloc.IAlloc;
import emo.utils.decomposition.alloc.Uniform;
import emo.utils.decomposition.similarity.ISimilarity;
import exception.EAException;
import random.IRandom;

/**
 * Auxiliary abstract class assisting in instantiating the MOEA/D-based algorithms.
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractMOEADBuilder<T extends EA> extends AbstractDecompositionEMOABuilder<T>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public AbstractMOEADBuilder(IRandom R)
    {
        super(R);
        _name = "MOEA/D";
    }

    /**
     * Similarity measure used when building the goals' neighborhood.
     */
    private ISimilarity _similarity = null;

    /**
     * Neighborhood size (at least 1; note that a goal is considered to be within its own neighborhood)
     */
    private int _neighborhoodSize = 1;

    /**
     * Auxiliary object used by goals manager to determine the sequence in which the maintained goals should be updated
     * in each generation.
     */
    private IAlloc _alloc = new Uniform();

    /**
     * Setter for the similarity measure used when building the goals' neighborhood.
     *
     * @param similarity similarity measure used when building the goals' neighborhood
     * @return MOEA/D builder being parameterized
     */
    public AbstractMOEADBuilder<T> setSimilarity(ISimilarity similarity)
    {
        _similarity = similarity;
        return this;
    }

    /**
     * Getter for the similarity measure used when building the goals' neighborhood.
     *
     * @return similarity measure used when building the goals' neighborhood
     */
    public ISimilarity getSimilarity()
    {
        return _similarity;
    }

    /**
     * Setter for the neighborhood size (at least 1; note that a goal is considered to be within its own neighborhood).
     *
     * @param neighborhoodSize neighborhood size
     * @return MOEA/D builder being parameterized
     */
    public AbstractMOEADBuilder<T> setNeighborhoodSize(int neighborhoodSize)
    {
        _neighborhoodSize = neighborhoodSize;
        return this;
    }

    /**
     * Getter for the neighborhood size.
     *
     * @return neighborhood size
     */
    public int getNeighborhoodSize()
    {
        return _neighborhoodSize;
    }

    /**
     * Setter for the auxiliary object used by goals manager to determine the sequence in which the maintained goals
     * should be updated in each generation.
     *
     * @param alloc allocation procedure
     * @return MOEA/D builder being parameterized
     */
    public AbstractMOEADBuilder<T> setAlloc(IAlloc alloc)
    {
        _alloc = alloc;
        return this;
    }

    /**
     * Getter for the auxiliary object used by goals manager to determine the sequence in which the maintained goals
     * should be updated in each generation.
     *
     * @return allocation procedure
     */
    public IAlloc getAlloc()
    {
        return _alloc;
    }

    /**
     * Auxiliary method that can be overwritten to perform simple data validation. It is called by default by
     * {@link AbstractMOEADBuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (_similarity == null)
            throw EAException.getInstanceWithSource("The similarity measure is not provided", this.getClass());
        if (_neighborhoodSize < 1)
            throw EAException.getInstanceWithSource("The neighborhood size must not be less than 1 (equals = "
                    + _neighborhoodSize + ")", this.getClass());
        if (_alloc == null)
            throw EAException.getInstanceWithSource("The allocation procedure is not provided", this.getClass());
    }
}
