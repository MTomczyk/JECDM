package problem.moo.dtlz.evaluate;

import phase.IEvaluate;

/**
 * Evaluates specimens as imposed by the DTLZ4 benchmark.
 *
 * @author MTomczyk
 */


public class DTLZ4 extends DTLZEvaluate implements IEvaluate
{
    /**
     * Determines the level of bias
     */
    private final double _bias;

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public DTLZ4(int M, int D)
    {
        this(M, D, 100.0d);
    }

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     * @param bias position-related bias (default = 100.0)
     */
    public DTLZ4(int M, int D, double bias)
    {
        super(M, D);
        _bias = bias;
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
        double g = 0.0;
        for (int i = _P; i < _P + _D; i++) g += Math.pow(x[i] - 0.5d, 2.0d);

        double[] f = new double[_P + 1];

        for (int i = 0; i < _P + 1; i++)
        {
            f[i] = (1.0d + g);
            for (int j = 0; j < _P - i; j++) f[i] *= Math.cos(Math.pow(x[j], _bias) * Math.PI / 2.0d);
            if (i > 0) f[i] *= Math.sin(Math.pow(x[_P - i], _bias) * Math.PI / 2.0d);

        }
        return f;
    }
}
