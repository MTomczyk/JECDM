package problem.moo.zdt.evaluate;

/**
 * Evaluates specimens as imposed by the ZDT6 benchmark.
 *
 * @author MTomczyk
 */
public class ZDT6 extends AbstractZDTEvaluate
{
    /**
     * Default constructor (sets the number of decision variables to 30).
     */
    public ZDT6()
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
        double f1 = 1.0d - Math.exp(-4.0d * x[0]) *
                Math.pow(Math.sin(6.0d * Math.PI * x[0]), 6.0d);
        double sum = 0.0d;
        for (int i = 1; i < x.length; i++) sum += x[i];
        double g = 1.0d + 9.0d * Math.pow(sum / 9.0d, 0.25d);
        double h = 1.0d - Math.pow(f1 / g, 2.0d);
        return new double[]{f1, g * h};
    }
}
