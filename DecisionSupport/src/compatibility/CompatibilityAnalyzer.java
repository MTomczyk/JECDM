package compatibility;

import history.PreferenceInformationWrapper;
import model.internals.value.AbstractValueInternalModel;
import preference.Form;
import preference.IPreferenceInformation;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The analyzer object that is responsible for calculating preference information's compatibility degree with an input
 * value model. Each form of preference information (see {@link Form}) is assigned one dedicated auxiliary analyzer (delegate).
 *
 * @author MTomczyk
 */
public class CompatibilityAnalyzer implements IAnalyzer
{
    /**
     * Params container.
     */
    public static class Params
    {
        /**
         * Analyzer object that is dedicated to pairwise comparisons.
         */
        public IAnalyzer _pairwiseComparisonsAnalyzer = new PairwiseComparisonAnalyzer();
    }

    /**
     * Map associating various forms of preference information with supportive analyzers.
     */
    private final HashMap<Form, IAnalyzer> _analyzers;

    /**
     * Default constructor (uses default parameterization).
     */
    public CompatibilityAnalyzer()
    {
        this(new Params());
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public CompatibilityAnalyzer(Params p)
    {
        _analyzers = new HashMap<>();
        if (p._pairwiseComparisonsAnalyzer != null)
            _analyzers.put(Form.PAIRWISE_COMPARISON, p._pairwiseComparisonsAnalyzer);
    }

    /**
     * The main method for calculating the compatibility degree to which the input preference information is compatible
     * with the given value model. It is assumed that positive values reflect compatibility, 0 reflects the boundary
     * case (treated as incompatible), and negative values represent incompatibility.
     *
     * @param preferenceInformation analyzed preference information
     * @param model                 the preference model
     * @return compatibility degree
     */
    @Override
    public Double calculateCompatibilityDegreeWithValueModel(IPreferenceInformation preferenceInformation, AbstractValueInternalModel model)
    {
        IAnalyzer analyzer = _analyzers.get(preferenceInformation.getForm());
        if (analyzer == null) return null;
        return analyzer.calculateCompatibilityDegreeWithValueModel(preferenceInformation, model);
    }

    /**
     * Auxiliary method that calculates the most discriminative (minimum) degree to which preference examples are compatible
     * with a given preference model.
     *
     * @param preferenceInformation preference examples (wrapped)
     * @param model                 analyzed model
     * @return compatibility degree (null if the model is not provided, smallest possible positive number is the preference information is not provided)
     */
    public Double calculateTheMostDiscriminativeCompatibilityWithValueModel(
            LinkedList<PreferenceInformationWrapper> preferenceInformation,
            AbstractValueInternalModel model)
    {
        if (model == null) return null;
        if ((preferenceInformation == null) || (preferenceInformation.isEmpty())) return Double.MIN_VALUE;

        double minCompatibility = Double.POSITIVE_INFINITY;
        for (PreferenceInformationWrapper pi : preferenceInformation)
        {
            Double c = calculateCompatibilityDegreeWithValueModel(pi._preferenceInformation, model);
            if (c == null) return null;
            if (Double.compare(c, minCompatibility) < 0) minCompatibility = c;
        }
        return minCompatibility;
    }

}
