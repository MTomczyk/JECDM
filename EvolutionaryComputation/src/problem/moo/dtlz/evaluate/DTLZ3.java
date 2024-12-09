package problem.moo.dtlz.evaluate;

import phase.IEvaluate;

/**
 * Evaluates specimens as imposed by the DTLZ3 benchmark.
 *
 * @author MTomczyk
 */


public class DTLZ3 extends DTLZEvaluate implements IEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public DTLZ3(int M, int D)
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
        for (int i = _P; i < _P + _D; i++)
            g += (Math.pow(x[i] - 0.5d, 2.0d) - Math.cos(20.0d * Math.PI * (x[i] - 0.5d)));
        g = 100.0d * (_D + g);

        double[] f = new double[_P + 1];

        for (int i = 0; i < _P + 1; i++)
        {
            f[i] = (1.0d + g);
            for (int j = 0; j < _P - i; j++) f[i] *= Math.cos(x[j] * Math.PI / 2.0d);
            if (i > 0) f[i] *= Math.sin(x[_P - i] * Math.PI / 2.0d);
        }

        return f;
    }
}
