package t1_10.t4_decision_support_module.t4_decision_support_system.t2_use_case;

import exeption.HistoryException;
import history.History;
import history.PreferenceInformationWrapper;
import preference.indirect.PairwiseComparison;
import relation.Relations;
import updater.AbstractSource;
import updater.IDataSource;

import java.util.LinkedList;

/**
 * Creates the data set using all pairs of alternatives compared by the decision maker.
 * Pairs are stored in subsequent rows in the returned set.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class HistorySource extends AbstractSource implements IDataSource
{
    /**
     * Reference to related preference elicitation history.
     */
    private final History _history;

    /**
     * Parameterized constructor.
     *
     * @param history reference to related preference elicitation history.
     */
    public HistorySource(History history)
    {
        _history = history;
    }

    /**
     * Should create a new data and returns it.
     *
     * @return new data
     */
    @Override
    public double[][] createData()
    {
        try
        {
            LinkedList<PreferenceInformationWrapper> copy = _history.getPreferenceInformationCopy();
            if (copy.isEmpty()) return null;

            // pairs of rows = preferred alternative and not; columns = f1, f2, f3, not-relevant, [0, 1]
            // (0 = preferred, 1 = not).
            double[][] data = new double[2 * copy.size()][5];

            int off = 0;
            for (PreferenceInformationWrapper pi : copy)
            {
                PairwiseComparison PC = pi._preferenceInformation.getPairwiseComparisons()[0];
                if (PC.getRelation() != Relations.PREFERENCE)
                {
                    // disable data point
                    data[off] = null;
                    data[off + 1] = null;
                    data[off + 2] = null;
                }
                else
                {
                    // First row = preferred alternative's performance vector (first three elements):
                    for (int i = 0; i < 3; i++) data[off][i] = PC.getPreferredAlternative().getPerformanceVector()[i];
                    // Second row = not preferred alternative's performance vector (first three elements):
                    for (int i = 0; i < 3; i++)
                        data[off + 1][i] = PC.getNotPreferredAlternative().getPerformanceVector()[i];

                    // not use index of 3 (reserved for relevance)

                    // 0 = preferred; 1 = not
                    data[off][4] = 0.0d;
                    data[off + 1][4] = 1.0d;
                }
                off += 2;
            }

            return data;

        } catch (HistoryException e)
        {
            return null;
        }
    }
}
