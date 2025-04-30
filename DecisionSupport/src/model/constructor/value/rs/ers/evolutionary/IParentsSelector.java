package model.constructor.value.rs.ers.evolutionary;

import dmcontext.DMContext;
import model.constructor.value.rs.ers.ERS;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;

/**
 * Interface for classes responsible for selecting parents for reproduction (used by {@link EvolutionaryModelConstructor}).
 *
 * @author MTomczyk
 */
public interface IParentsSelector<T extends AbstractValueInternalModel>
{
    /**
     * The main method for constructing parents for reproduction.
     *
     * @param dmContext current decision-making context
     * @param ers       top-level constructor, provides access; the parents are supposed to be sampled from {@link ERS#getModelsQueue()}
     * @return selected parents
     */
    ArrayList<SortedModel<T>> getParents(DMContext dmContext, ERS<T> ers);
}
