package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t5_complex_example;

import exeption.HistoryException;
import history.History;
import history.PreferenceInformationWrapper;
import preference.indirect.PairwiseComparison;
import relation.Relations;
import updater.AbstractSource;
import updater.IDataSource;

import java.util.LinkedList;

/**
 * Creates the data set using the most recent pair of alternatives compared by the decision maker.
 * The pair is represented as two rows in the returned set.
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
        // rows = preferred alternative and not; columns = f1, f2, f3, [0, 1] (depends on whether the alternative is preferred).
        double[][] data = new double[2][4];
        try
        {
            LinkedList<PreferenceInformationWrapper> copy = _history.getPreferenceInformationCopy();
            if (copy.isEmpty()) return null;
            PairwiseComparison PC = copy.getLast()._preferenceInformation.getPairwiseComparisons()[0];
            if (PC.getRelation() != Relations.PREFERENCE) return null;
            // First row = preferred alternative's performance vector (first three elements):
            for (int i = 0; i < 3; i++) data[0][i] = PC.getPreferredAlternative().getPerformanceVector()[i];
            // Second row = not preferred alternative's performance vector (first three elements):
            for (int i = 0; i < 3; i++) data[1][i] = PC.getNotPreferredAlternative().getPerformanceVector()[i];
            // 0 = preferred; 1 = not
            data[0][3] = 0.0d;
            data[1][3] = 1.0d;

            return data;
        } catch (HistoryException e)
        {
            return null;
        }
    }
}
