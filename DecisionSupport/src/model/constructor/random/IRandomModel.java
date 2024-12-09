package model.constructor.random;

import model.internals.AbstractInternalModel;
import random.IRandom;
import space.normalization.INormalization;

/**
 * Auxiliary interface for objects responsible for generating random model instances.
 *
 * @author MTomczyk
 */
public interface IRandomModel <T extends AbstractInternalModel>
{
    /**
     * Constructs random model instance.
     * @param R random number generator
     * @return random model instance
     */
    T generateModel(IRandom R);

    /**
     * Auxiliary method for setting new normalizations (used to rescale alternative evaluations given the considered criteria).
     *
     * @param normalizations normalizations used to rescale the dimensions
     */
    void setNormalizations(INormalization [] normalizations);
}
