package problem.moo.zdt.evaluate;

/**
 * Evaluates specimens as imposed by the ZDT3 benchmark.
 *
 * @author MTomczyk
 */
public class ZDT3 extends AbstractZDTEvaluate
{
    /**
     * Default constructor (sets the number of decision variables to 30).
     */
    public ZDT3()
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
        double d = x[0] / g;
        double h = 1.0d - Math.sqrt(d) - (d * Math.sin(10.0d * Math.PI * x[0]));
        return new double[]{x[0], h * g};
    }
}
