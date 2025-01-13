package plot;

import dataset.IDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Auxiliary container-like class aiding data set updating.
 *
 * @author MTomczyk
 */
class DSUpdaterData
{
    /**
     * New data sets (reference is new).
     */
    public final ArrayList<IDataSet> _newDataSets;

    /**
     * If i-th element is true, i-th data set data will be disposed (IMPORTANT: this refers not to the new data
     * set object (to be set as new), but to the current data set object (to be updated with)
     */
    public final boolean[] _dispose;

    /**
     * If i-th element is true, i-th data set IDS update will be skipped.
     */
    public final boolean[] _skipIDSUpdate;

    /**
     * Parameterized constructor (prepares the data)
     *
     * @param currentDataSets    current data set
     * @param newDataSets        new data set
     * @param updateMatchingOnly if true, he method updates data sets whose names match names of those provided; if
     *                           a data set is in the input but not in the currently maintained set, it is ignored;
     *                           if a data set is not in the input but is in te currently maintained set, it remains
     *                           in the set but; if false, the method removes the currently maintained sets and uses
     *                           the input as the new ones
     */
    public DSUpdaterData(ArrayList<IDataSet> currentDataSets, ArrayList<IDataSet> newDataSets, boolean updateMatchingOnly)
    {
        if ((currentDataSets == null) && (updateMatchingOnly))
        {
            _dispose = null;
            _newDataSets = null;
            _skipIDSUpdate = null;
            return;
        }

        if (currentDataSets == null) _dispose = null;
        else _dispose = new boolean[currentDataSets.size()];

        HashMap<String, Integer> map = null;
        if (currentDataSets != null)
        {
            map = new HashMap<>(currentDataSets.size());
            for (int i = 0; i < currentDataSets.size(); i++)
                if ((currentDataSets.get(i) != null) && (currentDataSets.get(i).getName() != null))
                    map.put(currentDataSets.get(i).getName(), i);
        }

        int nDS;
        if (updateMatchingOnly)
        {
            nDS = currentDataSets.size();
            _skipIDSUpdate = new boolean[nDS];
            Arrays.fill(_skipIDSUpdate, true);
        }
        else
        {
            nDS = newDataSets.size();
            _skipIDSUpdate = null;
        }

        if ((_dispose != null) && (currentDataSets != newDataSets))
        {
            for (int i = 0; i < currentDataSets.size(); i++)
                if (currentDataSets.get(i) != null) _dispose[i] = true;
        }

        _newDataSets = new ArrayList<>(nDS);
        if (updateMatchingOnly)
        {
            Arrays.fill(_dispose, false);
            _newDataSets.addAll(currentDataSets);
            for (IDataSet newDataSet : newDataSets)
            {
                if (map.containsKey(newDataSet.getName()))
                {
                    int idx = map.get(newDataSet.getName());
                    _dispose[idx] = true;
                    _newDataSets.set(idx, newDataSet);
                    _skipIDSUpdate[idx] = false;
                }
            }
        }
        else _newDataSets.addAll(newDataSets);
    }
}
