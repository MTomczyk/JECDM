package statistics;

/**
 * Implementation of {@link IStatistic} that derives the L-norm value from an input double array. i.e., statistic =
 * [sum(v^power)]^(1/power) (the input v-values are used as they are, i.e., absolute values are not derived).
 *
 * @author MTomczyk
 */
public class LNorm extends AbstractStatistic implements IStatistic
{
    /**
     * Statistic function name.
     */
    public static final String _name = "LNORM";

    /**
     * Alpha-value (power) for the L-norm (at least 1).
     */
    public final double _alpha;

    /**
     * Inverse alpha-value (power).
     */
    public final double _invAlpha;

    /**
     * Default constructor.
     *
     * @param alpha alpha-value (power) for the L-norm (at least 1; use Double.POSITIVE_INFINITY to establish a
     *              Chebyshev function)
     */
    public LNorm(double alpha)
    {
        super(_name);
        _alpha = Math.max(1.0d, alpha);
        if ((Double.compare(_alpha, 1.0) != 0) && (Double.compare(_alpha, Double.POSITIVE_INFINITY) != 0))
            _invAlpha = 1.0d / _alpha;
        else _invAlpha = 1.0d;
    }


    /**
     * Calculates the statistics (l-norm value). Returns 0 if no data is provided. Note that the input v-values are used
     * as they are, i.e., absolute values are not derived.
     *
     * @param v input array
     * @return statistic
     */
    @Override
    public double calculate(double[] v)
    {
        if (v.length == 0) return 0.0f;
        double s = 0.0d;
        if (Double.compare(_alpha, 1.0d) == 0)
        {
            for (double vv : v) s += vv;
            return s;
        } else if (Double.compare(_alpha, Double.POSITIVE_INFINITY) == 0)
        {
            double max = Double.NEGATIVE_INFINITY;
            for (double vv : v)
                if (Double.compare(vv, max) > 0) max = vv;
            return max;

        } else
        {
            for (double vv : v) s += Math.pow(vv, _alpha);
            return Math.pow(s, _invAlpha);
        }
    }
}
