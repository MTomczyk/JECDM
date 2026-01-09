package model.internals.value.scalarizing;

import alternative.Alternative;
import model.internals.IInternalModel;
import model.internals.value.AbstractValueInternalModel;
import preference.indirect.PairwiseComparison;
import space.Vector;
import space.normalization.INormalization;
import space.scalarfunction.LNorm;

import java.util.LinkedList;

/**
 * Preference model based on the preference cone concepts proposed by M. Kadziński, M. Tomczyk, and R. Słowiński
 * (<a href="https://doi.org/10.1016/j.swevo.2019.100602">publication</a>). Important note: When the normalizations are
 * not provided, it is assumed that all criteria are cost-type and 0 is the utopia solution. When the normalizations
 * are used, a similar assumption is made for the normalized performance vector. If the resulting normalized vector is a
 * zero vector, it is transformed into a weight vector composed of equal weights. Also, the preference direction of
 * this model is to minimize. The zero value is transformed into a weight vector composed of equal weights. For each
 * incompatible pairwise comparison, the score is increased by 1.
 *
 * @author MTomczyk
 */
public class KTSCone extends AbstractValueInternalModel implements IInternalModel
{
    /**
     * List of pairwise comparisons used to establish the cones.
     */
    private final LinkedList<PairwiseComparison> _PCs;

    /**
     * Normalizers used to rescale alternatives' evaluations.
     */
    private INormalization[] _normalizations;

    /**
     * Reference L-norm used as a Chebyshev function.
     */
    private final LNorm _lnorm = new LNorm(Double.POSITIVE_INFINITY);

    /**
     * Default constructor.
     *
     * @param PCs list of pairwise comparisons used to establish the cones
     */
    public KTSCone(LinkedList<PairwiseComparison> PCs)
    {
        this(PCs, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param PCs            list of pairwise comparisons used to establish the cones
     * @param normalizations normalizers used to rescale alternatives' evaluations
     */
    public KTSCone(LinkedList<PairwiseComparison> PCs, INormalization[] normalizations)
    {
        super("KTSCone");
        _PCs = PCs;
        _normalizations = normalizations;
        _lnorm.setNormalizations(_normalizations);
    }

    /**
     * The main method for evaluating an alternative.
     *
     * @param alternative alternative to be evaluated
     * @return attained score
     */
    @Override
    public double evaluate(Alternative alternative)
    {
        if (_PCs == null) return 0.0d;
        if (_PCs.isEmpty()) return 0.0d;

        double[] e = alternative.getPerformanceVector().clone();
        if (_normalizations != null)
        {

            for (int i = 0; i < Math.min(e.length, _normalizations.length); i++)
                if (_normalizations[i] != null)
                    e[i] = _normalizations[i].getNormalized(e[i]);
        }

        double[] w = getWeightVector(e);
        _lnorm.setWeights(w);


        int score = 0;
        for (PairwiseComparison PC : _PCs)
        {
            double v1 = _lnorm.evaluate(PC.getPreferredAlternative().getPerformanceVector());
            double v2 = _lnorm.evaluate(PC.getNotPreferredAlternative().getPerformanceVector());
            if (Double.compare(v1, v2) >= 0) score++;
        }

        return score;
    }

    /**
     * Auxiliary method for creating a weight vector given the input solution's evaluations.
     *
     * @param e performance vector
     * @return corresponding weight vector
     */
    protected double[] getWeightVector(double[] e)
    {
        double[] w;

        if (Vector.isZeroVector(e))
        {
            double value = 1.0d / e.length;
            w = Vector.getVectorWithEqualComponents(e.length, value);
        }
        else
        {
            w = new double[e.length];
            double[] wp = new double[e.length];
            boolean infinity = false;
            for (int i = 0; i < e.length; i++)
            {
                if (Double.compare(e[i], 0.0d) == 0)
                {
                    wp[i] = Double.POSITIVE_INFINITY;
                    infinity = true;
                }
                else wp[i] = 1.0d / e[i];
            }

            if (infinity)
            {
                for (int i = 0; i < e.length; i++)
                {
                    if (Double.compare(wp[i], Double.POSITIVE_INFINITY) == 0) wp[i] = 1.0d;
                    else wp[i] = 0.0d;
                }
            }

            double sum = 0.0d;
            for (double s : wp) sum += s;
            for (int i = 0; i < e.length; i++) w[i] = wp[i] / sum;
        }
        return w;
    }

    /**
     * The main method for setting new normalizations (used to rescale alternative evaluations given the considered
     * criteria).
     *
     * @param normalizations normalizations used to rescale the dimensions
     */
    @Override
    public void setNormalizations(INormalization[] normalizations)
    {
        _normalizations = normalizations;
        _lnorm.setNormalizations(normalizations);
    }


}
