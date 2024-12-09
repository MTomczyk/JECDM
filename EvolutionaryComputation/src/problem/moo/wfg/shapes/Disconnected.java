package problem.moo.wfg.shapes;


/**
 * Disconnected Pareto front shape function.
 *
 * @author MTomczyk
 */
public class Disconnected extends AbstractShape implements IShape
{
    /**
     * Controls the number of disconnected regions.
     */
    private final int _A;

    /**
     * Controls the overall shape.
     */
    private final double _alpha;

    /**
     * Controls the locations of disconnected regions.
     */
    private final double _beta;

    /**
     * Parameterized constructor.
     *
     * @param A                 controls the number of disconnected regions
     * @param alpha             controls the overall shape
     * @param beta              controls the locations of disconnected regions
     */
    public Disconnected(int A, double alpha, double beta)
    {
        super(0, 0);
        assert A > 0;
        assert alpha > 0.0d;
        assert beta > 0.0d;

        _A = A;
        _alpha = alpha;
        _beta = beta;
    }

    /**
     * Returns the shape value.
     *
     * @param x input decision vector
     * @return shape value
     */
    @Override
    public double getShape(double[] x)
    {
        double cos = Math.cos((double) _A * Math.pow(x[0], _beta) * Math.PI);
        double pow = Math.pow(x[0], _alpha);
        return 1.0d - pow * Math.pow(cos, 2.0d);
    }
}
