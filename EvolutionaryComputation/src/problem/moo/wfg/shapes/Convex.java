package problem.moo.wfg.shapes;


/**
 * Convex Pareto front shape function.
 *
 * @author MTomczyk
 */
public class Convex extends AbstractShape implements IShape
{
    /**
     * Parameterized constructor.
     *
     * @param m                 objective function id
     * @param M                 the number of objectives
     */
    public Convex(int m, int M)
    {
        super(m, M);
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
        assert (_M - 1) == x.length;
        double r = 1.0d;
        for (int i = 0; i < _M - (_m + 1); i++) r *= (1.0d - Math.cos(x[i] * Math.PI / 2.0d));
        if (_m != 0) r *= (1.0d - Math.sin(x[_M - (_m + 1)] * Math.PI / 2.0d));
        return r;
    }
}
