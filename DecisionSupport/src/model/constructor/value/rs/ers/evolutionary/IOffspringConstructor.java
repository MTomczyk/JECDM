package model.constructor.value.rs.ers.evolutionary;

import dmcontext.DMContext;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;

/**
 * Simple interface employed by {@link EvolutionaryModelConstructor} when constructing a model.
 * The underlying process is assumed to be in the spirit of evolutionary algorithms.
 *
 * @author MTomczyk
 */
public interface IOffspringConstructor<T extends AbstractValueInternalModel>
{
    /**
     * Signature for the main method responsible for producing a new (offspring) model given two selected ones (parents).
     * The parents are wrapped via {@link model.constructor.value.rs.ers.SortedModel}
     * (derived from {@link model.constructor.value.rs.ers.ModelsQueue}).
     *
     * @param p1        the first model (parent 1)
     * @param p2        the second model (parent 2)
     * @param dmContext current decision-making context
     * @return new model (offspring)
     */
    T getModel(SortedModel<T> p1, SortedModel<T> p2, DMContext dmContext);
}
