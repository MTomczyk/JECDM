package problem.moo.dtlz.evaluate;

import phase.IEvaluate;

/**
 * Evaluates specimens as imposed by the DTLZ2 benchmark.
 *
 * @author MTomczyk
 */


public class DTLZ2 extends DTLZEvaluate implements IEvaluate
{
    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public DTLZ2(int M, int D)
    {
        super(M, D, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param M       the number of objectives
     * @param D       the number of distance-related parameters
     * @param rescale Optional rescaling vector for testing (should be null if the original DTLZ problem is to be used). If used, every i-th objective function value is rescaled by i-th scaling factor provided.
     */
    public DTLZ2(int M, int D, double[] rescale)
    {
        super(M, D, rescale);
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
            for (int j = 0; j < _P - i; j++) f[i] *= Math.cos(x[j] * Math.PI / 2.0d);
            if (i > 0) f[i] *= Math.sin(x[_P - i] * Math.PI / 2.0d);
        }

        if (_rescale != null)
        {
            for (int i = 0; i < f.length; i++)
                f[i] *= _rescale[i];
        }

        return f;
    }
}
