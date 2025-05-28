package model.constructor.value.rs.ers.evolutionary;

import dmcontext.DMContext;
import model.constructor.value.rs.ers.ERS;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;


/**
 * A simple class for constructing new model instances. It is coupled with {@link model.constructor.value.rs.ers.ERS},
 * and its performance mimics evolutionary algorithms. Specifically, when the model construction is called, the method
 * selects two models from the model queue {@link model.constructor.value.rs.ers.ModelsQueue} using a tournament
 * selection. Then, these two models are employed to construct a new one (offspring).
 *
 * @author MTomczyk
 */
public class EvolutionaryModelConstructor<T extends AbstractValueInternalModel>
{
    /**
     * Offspring constructor object.
     */
    private final IOffspringConstructor<T> _offspringConstructor;

    /**
     * Object responsible for selecting parents for reproduction.
     */
    private final IParentsSelector<T> _parentsSelector;

    /**
     * Parameterized constructor.
     *
     * @param offspringConstructor offspring constructor object
     * @param parentsSelector      object responsible for selecting parents for reproduction
     */
    public EvolutionaryModelConstructor(IOffspringConstructor<T> offspringConstructor, IParentsSelector<T> parentsSelector)
    {
        _offspringConstructor = offspringConstructor;
        _parentsSelector = parentsSelector;
    }

    /**
     * Creates a new model. The process is in the spirit of evolutionary computation.
     *
     * @param dmContext current decision-making context
     * @param ers       parent, top-level object
     * @return new model (offspring)
     */
    public T getModel(DMContext dmContext, ERS<T> ers)
    {
        ArrayList<SortedModel<T>> parents = _parentsSelector.getParents(dmContext, ers);
        return _offspringConstructor.getModel(dmContext, parents);
    }
}
