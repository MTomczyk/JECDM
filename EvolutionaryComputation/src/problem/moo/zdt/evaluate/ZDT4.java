package problem.moo.zdt.evaluate;

/**
 * Evaluates specimens as imposed by the ZDT4 benchmark. Important note: ZDT4 involves decision variables with
 * different domains. However, this implementation assumes that the variables are normalized and lie in the [0, 1]
 * range; their appropriate rescaling is performed during evaluation (i.e., x' = -5 + 10x).
 *
 * @author MTomczyk
 */
public class ZDT4 extends AbstractZDTEvaluate
{
    /**
     * Default constructor (sets the number of decision variables to 30).
     */
    public ZDT4()
    {
        super(10);
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
        for (int i = 1; i < x.length; i++)
        {
            double t = -5.0d + 10.0d * x[i];
            sum += (t * t - 10.0d * Math.cos(4.0d * Math.PI * t));
        }
        double g = 91.0d + sum;
        double h = 1.0d - Math.sqrt(x[0] / g);
        return new double[]{x[0], h * g};
    }
}
