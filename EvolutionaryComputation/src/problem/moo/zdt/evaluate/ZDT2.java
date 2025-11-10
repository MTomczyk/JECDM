package problem.moo.zdt.evaluate;

/**
 * Evaluates specimens as imposed by the ZDT2 benchmark.
 *
 * @author MTomczyk
 */
public class ZDT2 extends AbstractZDTEvaluate
{
    /**
     * Default constructor (sets the number of decision variables to 30).
     */
    public ZDT2()
    {
        super(30);
    }

    /**
     * Supportive method for calculating the objective vector.
     *
     * @param x decision vector
     * @return objective vector
     */
    @Override
    protected double[] evaluate(double[] x)
    {
        double sum = 0.0d;
        for (int i = 1; i < x.length; i++) sum += x[i];
        double g = 1.0d + _cMul * sum;
        double h = 1.0d - Math.pow(x[0] / g, 2.0d);
        return new double[]{x[0], h * g};
    }
}
