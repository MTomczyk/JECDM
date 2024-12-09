package problem.moo.dtlz.evaluate;

import phase.IEvaluate;

/**
 * Evaluates specimens as imposed by the DTLZ7 benchmark.
 *
 * @author MTomczyk
 */


public class DTLZ7 extends DTLZEvaluate implements IEvaluate
{

    /**
     * Parameterized constructor.
     *
     * @param M the number of objectives
     * @param D the number of distance-related parameters
     */
    public DTLZ7(int M, int D)
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
            g += x[i] / (double) _D;

        g = 1.0d + 9.0d * g;

        double[] f = new double[_P + 1];

        System.arraycopy(x, 0, f, 0, _P);

        double h = _P + 1;
        for (int i = 0; i < _P; i++)
        {
            double p1 = f[i] / (1.0d + g);
            double p2 = (1.0d + Math.sin(3.0d * Math.PI * f[i]));
            h -= (p1 * p2);
        }
        //System.out.println("t   " + t);
        f[_P] = (1.0d + g) * h;

        return f;
    }
}
