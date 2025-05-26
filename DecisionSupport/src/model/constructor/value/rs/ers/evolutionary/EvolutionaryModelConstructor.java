package model.constructor.value.rs.ers.evolutionary;

import dmcontext.DMContext;
import model.constructor.value.rs.ers.ModelsQueue;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

import java.util.ListIterator;


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
     * Size of the tournament (tournament selection).
     */
    private final int _ts;

    /**
     * Parameterized constructor.
     *
     * @param offspringConstructor offspring constructor object
     * @param ts                   size of the tournament (tournament selection).
     */
    public EvolutionaryModelConstructor(IOffspringConstructor<T> offspringConstructor, int ts)
    {
        _offspringConstructor = offspringConstructor;
        _ts = Math.max(ts, 1);
    }

    /**
     * Creates a new model. The process is in the spirit of evolutionary computation.
     *
     * @param modelsQueue models queue
     * @param dmContext   current decision-making context
     * @return new model (offspring)
     */
    public T getModel(ModelsQueue<T> modelsQueue, DMContext dmContext)
    {
        int l = modelsQueue.getQueue().size();
        IRandom R = dmContext.getR();
        int p1Idx = Integer.MAX_VALUE;
        int p2Idx = Integer.MAX_VALUE;
        for (int i = 0; i < _ts; i++)
        {
            int c = R.nextInt(l);
            if (c < p1Idx) p1Idx = c;
            c = R.nextInt(l);
            if (c < p2Idx) p2Idx = c;
        }

        // swap to keep 1 < 2 (order of parents should not matter)
        if (p2Idx < p1Idx)
        {
            int tmp = p2Idx;
            p2Idx = p1Idx;
            p1Idx = tmp;
        }

        int cIdx = 0;
        ListIterator<SortedModel<T>> it = modelsQueue.getQueue().listIterator();
        SortedModel<T> model;
        SortedModel<T> m1 = null;
        SortedModel<T> m2 = null;

        while (it.hasNext())
        {
            model = it.next();
            if (cIdx == p1Idx) m1 = model;
            if (cIdx == p2Idx)
            {
                m2 = model;
                break;
            }
            cIdx++;
        }
        return _offspringConstructor.getModel(m1, m2, dmContext);
    }
}
