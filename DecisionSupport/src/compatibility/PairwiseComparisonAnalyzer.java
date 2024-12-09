package compatibility;

import model.internals.value.AbstractValueInternalModel;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import relation.Relations;

/**
 * The implementation of {@link IAnalyzer} for analyzing pairwise comparisons {@link PairwiseComparison}.
 * The compatibility degree is calculated as the difference between the performance of the preferred alternative and
 * the not preferred, given the input preference model (or the opposite if the model's preference direction is to minimize).
 *
 * @author MTomczyk
 */
public class PairwiseComparisonAnalyzer implements IAnalyzer
{
    /**
     * The method for calculating the compatibility degree. It immediately returns null if the inputs are invalid
     * (i.e., the preference information is not a pairwise comparison, or any input is null).
     *
     * @param preferenceInformation analyzed preference information
     * @param model                 the preference model
     * @return compatibility degree.
     */
    @Override
    public Double calculateCompatibilityDegreeWithValueModel(IPreferenceInformation preferenceInformation, AbstractValueInternalModel model)
    {
        if (preferenceInformation == null) return null;
        if (model == null) return null;
        if (!(preferenceInformation instanceof PairwiseComparison PC)) return null;
        if (PC.getRelation().equals(Relations.INCOMPARABILITY)) return null;
        if (PC.getRelation().equals(Relations.INDIFFERENCE))
            return -Math.abs(model.evaluate(PC.getFirstAlternative()) - model.evaluate(PC.getSecondAlternative()));
        double P = model.evaluate(PC.getPreferredAlternative());
        double O = model.evaluate(PC.getNotPreferredAlternative());
        if (model.isLessPreferred()) return O - P;
        else return P - O;
    }
}
