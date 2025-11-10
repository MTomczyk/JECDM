package problem.moo.cw.cw1;

import phase.IEvaluate;
import problem.moo.dtlz.evaluate.DTLZEvaluate;

/**
 * Evaluates specimens as imposed by the Crash-worthiness problem (Liao, X., Li, Q., Yang, X. et al. Multiobjective
 * optimization for crash safety design of vehicles using stepwise regression model. Struct Multidisc Optim 35, 561–569
 * (2008). <a href="https://doi.org/10.1007/s00158-007-0163-x">.LINK</a>). Note that the problem assumes that there are
 * 5 continuous decision variables in [1;3] bounds. This implementation assumes using normalized variables in [0; 1]
 * bounds. The linear rescaling is done when evaluating the solutions by an instance of this class.
 *
 * @author MTomczyk
 */
public class Evaluate extends DTLZEvaluate implements IEvaluate
{

    /**
     * Default constructor.
     */
    public Evaluate()
    {
        super(3, 5); // 5 is treated as the total number of variables
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
        double x1 = (2.0d * x[0] + 1.0d);
        double x2 = (2.0d * x[1] + 1.0d);
        double x3 = (2.0d * x[2] + 1.0d);
        double x4 = (2.0d * x[3] + 1.0d);
        double x5 = (2.0d * x[4] + 1.0d);
        double[] f = new double[3];


        f[0] = 1640.2823
                + 2.3573285 * x1
                + 2.3220035 * x2
                + 4.5688768 * x3
                + 7.7213633 * x4
                + 4.4559504 * x5;

        f[1] = 6.5856
                + 1.15 * x1
                - 1.0427 * x2
                + 0.9738 * x3
                + 0.8364 * x4

                - 0.3695 * x1 * x4
                + 0.0861 * x1 * x5
                + 0.3628 * x2 * x4

                - 0.1106 * x1 * x1
                - 0.3437 * x3 * x3
                + 0.1764 * x4 * x4;


        f[2] = -0.0551
                + 0.0181 * x1
                + 0.1024 * x2
                + 0.0421 * x3

                - 0.0073 * x1 * x2
                + 0.024 * x2 * x3
                - 0.0118 * x2 * x4
                - 0.0204 * x3 * x4
                - 0.008 * x3 * x5

                - 0.0241 * x2 * x2
                + 0.0109 * x4 * x4;

        return f;
    }
}
