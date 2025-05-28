package model.constructor.value.rs.ers.evolutionary;

import dmcontext.DMContext;
import model.constructor.value.rs.ers.ERS;
import model.constructor.value.rs.ers.SortedModel;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Simple implementation of a tournament selection. It selects two parents for further reproduction.
 *
 * @author MTomczyk
 */
public class Tournament<T extends AbstractValueInternalModel> implements IParentsSelector<T>
{
    /**
     * Tournament size.
     */
    private final int _ts;

    /**
     * Parameterized constructor.
     *
     * @param ts tournament size (at least 1)
     */
    public Tournament(int ts)
    {
        _ts = Math.max(ts, 1);
    }

    /**
     * The main method for constructing parents for reproduction.
     *
     * @param dmContext current decision-making context
     * @param ers       top-level constructor, provides access; the parents are supposed to be sampled from {@link ERS#getModelsQueue()}
     * @return selected parents
     */
    @Override
    public ArrayList<SortedModel<T>> getParents(DMContext dmContext, ERS<T> ers)
    {
        int l = ers.getModelsQueue().getQueue().size();

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
        ListIterator<SortedModel<T>> it = ers.getModelsQueue().getQueue().listIterator();
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

        ArrayList<SortedModel<T>> parents = new ArrayList<>(2);
        parents.add(m1);
        parents.add(m2);
        return parents;
    }
}
