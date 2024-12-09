package y2024.gecco2024.case19.save;

import phase.IEvaluate;
import problem.moo.dtlz.evaluate.DTLZEvaluate;

/**
 * Evaluates specimens as imposed by DTLZ2 benchmark.
 *
 * @author MTomczyk
 */


public class DTLZ2_BiasedDistribution extends DTLZEvaluate implements IEvaluate
{
    /**
     * Bias level
     */
    private final double _bias;

    /**
     * Shift for position-related variables
     */
    private final int _shift;

    /**
     * Parameterized constructor.
     *
     * @param M     the number of objectives
     * @param D     the number of distance-related parameters
     * @param bias  bias level
     * @param shift shift for position-related variables
     */
    public DTLZ2_BiasedDistribution(int M, int D, double bias, int shift)
    {
        super(M, D, null);
        _bias = bias;
        _shift = shift;
    }

    /**
     * Supportive method for calculating objective vector.
     *
     * @param x decision vector
     * @return objective vector
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    protected double[] evaluate(double[] x)
    {
        double[] cx = x.clone();
        double tmp = 0.0d;
        for (int i = 0; i < _shift + 1; i++) tmp += Math.pow(x[i], _bias);
        tmp /= (_shift + 1.0d);
        x[0] = tmp;

        double g = 0.0;
        for (int i = _P + _shift; i < _P + _D; i++) g += Math.pow(cx[i] - 0.5d, 2.0d);

        double[] f = new double[_P + 1];

        for (int i = 0; i < _P + 1; i++)
        {
            f[i] = (1.0d + g);
            for (int j = 0; j < _P - i; j++) f[i] *= Math.cos(cx[j] * Math.PI / 2.0d);
            if (i > 0) f[i] *= Math.sin(cx[_P - i] * Math.PI / 2.0d);
        }

        if (_rescale != null)
        {
            for (int i = 0; i < f.length; i++)
                f[i] *= _rescale[i];
        }

        return f;
    }
}
