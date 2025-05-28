package model.constructor.value.rs.ers.evolutionary;

import dmcontext.DMContext;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;

/**
 * Simple interface employed by {@link EvolutionaryModelConstructor} when constructing a model.
 * The underlying process is assumed to be in the spirit of evolutionary algorithms.
 *
 * @author MTomczyk
 */
public interface IOffspringConstructor<T extends AbstractValueInternalModel>
{
    /**
     * Signature for the main method responsible for producing a new (offspring) model given selected ones (parents).
     * The parents are wrapped via {@link model.constructor.value.rs.ers.SortedModel}
     * (derived from {@link model.constructor.value.rs.ers.ModelsQueue}).
     *
     * @param dmContext current decision-making context
     * @param parents   selected parents
     * @return new model (offspring)
     */
    T getModel(DMContext dmContext, ArrayList<SortedModel<T>> parents);
}
