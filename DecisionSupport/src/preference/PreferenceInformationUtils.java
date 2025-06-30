package preference;

import alternative.Alternative;
import model.internals.value.AbstractValueInternalModel;
import preference.indirect.PairwiseComparison;

/**
 * Provides auxiliary functionalities related to preference information.
 *
 * @author MTomczyk
 */
public class PreferenceInformationUtils
{
    /**
     * Auxiliary method for constructing a pairwise comparison from two alternatives that are assumed to be evaluated
     * using the input value model. In the case of attaining equal scores, pairwise comparison with the INDIFFERENCE
     * flag is returned.
     *
     * @param model preference model used to evaluate alternatives
     * @param A1    the first alternative
     * @param A2    the second alternative
     * @return pairwise comparison
     */
    public static PairwiseComparison getPairwiseComparison(AbstractValueInternalModel model, Alternative A1, Alternative A2)
    {
        double e1 = model.evaluate(A1);
        double e2 = model.evaluate(A2);
        int comp = Double.compare(e1, e2);
        if (comp == 0) return PairwiseComparison.getIndifference(A1, A2);
        if (((model.isLessPreferred()) && (comp < 0)) || ((!model.isLessPreferred()) && (comp > 0)))
            return PairwiseComparison.getPreference(A1, A2);
        else return PairwiseComparison.getPreference(A2, A1);
    }

}
