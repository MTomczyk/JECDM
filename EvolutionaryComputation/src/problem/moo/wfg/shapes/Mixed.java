package problem.moo.wfg.shapes;

/**
 * Mixed Pareto front shape function.
 *
 * @author MTomczyk
 */
public class Mixed extends AbstractShape implements IShape
{

    /**
     * Controls the overall shape.
     */
    private final double _alpha;

    /**
     * Controls the number of convex/concave regions.
     */
    private final int _A;


    /**
     * Parameterized constructor.
     * @param alpha controls the overall shape
     * @param A controls the number of convex/concave regions
     */
    public Mixed(double alpha, int A)
    {
        super(0, 0);
        _alpha = alpha;
        _A = A;
    }

    @Override
    public double getShape(double[] x)
    {
        double tmp = 2.0d * (double) _A * Math.PI;
        double nom = tmp * x[0] + Math.PI / 2.0d;
        double in = Math.cos(nom) / tmp;
        return 1.0d - x[0] - Math.pow(in, _alpha);
    }
}
