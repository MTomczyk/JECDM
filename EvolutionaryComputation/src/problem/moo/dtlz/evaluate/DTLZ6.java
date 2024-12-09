package problem.moo.dtlz.evaluate;

import phase.IEvaluate;

/**
 * Evaluates specimens as imposed by the DTLZ6 benchmark.
 *
 * @author MTomczyk
 */


public class DTLZ6 extends DTLZEvaluate implements IEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public DTLZ6(int M, int D)
    {
        super(M, D);
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
        for (int i = _P; i < _P + _D; i++) g += Math.pow(x[i], 0.1d);

        double[] f = new double[_P + 1];
        double[] y = new double[_P];

        y[0] = x[0];
        for (int i = 1; i < _P; i++) y[i] = (1.0 + 2.0 * g * x[i]) / (4.0d * (1.0 + g));


        for (int i = 0; i < _P + 1; i++)
        {
            f[i] = (1.0d + g);

            for (int j = 0; j < _P - i; j++) f[i] *= Math.cos(y[j] * Math.PI / 2.0d);
            if (i > 0) f[i] *= Math.sin(y[_P - i] * Math.PI / 2.0d);
        }

        return f;
    }
}
