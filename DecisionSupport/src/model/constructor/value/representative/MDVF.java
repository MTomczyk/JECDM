package model.constructor.value.representative;

import compatibility.CompatibilityAnalyzer;
import history.PreferenceInformationWrapper;
import model.internals.value.AbstractValueInternalModel;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This implementation of {@link IRepresentativeValueModelSelector} selects a model that is the most discriminative,
 * i.e., maximizes the model's minimal compatibility with the preference information.
 *
 * @author MTomczyk
 */
public class MDVF<T extends AbstractValueInternalModel> implements IRepresentativeValueModelSelector<T>
{
    /**
     * Compatibility analyzer.
     */
    private final CompatibilityAnalyzer _CA;

    /**
     * Parameterized constructor.
     */
    public MDVF()
    {
        this(null);
    }

    /**
     * Parameterized constructor.
     *
     * @param CA compatibility analyzer (if null, the default object {@link CompatibilityAnalyzer} will be used)
     */
    public MDVF(CompatibilityAnalyzer CA)
    {
        if (CA == null) _CA = new CompatibilityAnalyzer();
        else _CA = CA;
    }

    /**
     * The main method. It selects a model that is the most discriminative, i.e., maximizes the model's minimal
     * compatibility with the preference information.
     *
     * @param models                candidate model instances
     * @param preferenceInformation collected preference examples
     * @return the most discriminative model
     */
    @Override
    public T selectModel(ArrayList<T> models, LinkedList<PreferenceInformationWrapper> preferenceInformation)
    {
        T incumbent = null;
        double bv = Double.NEGATIVE_INFINITY;

        for (T m : models)
        {
            Double mc = _CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, m);
            if (mc == null) return null;

            if (Double.compare(mc, bv) > 0)
            {
                bv = mc;
                incumbent = m;
            }
        }

        return incumbent;
    }
}
