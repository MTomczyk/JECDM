package problem.moo.wfg.shapes;

/**
 * Linear Pareto front shape function.
 *
 * @author MTomczyk
 */
public class Linear extends AbstractShape implements IShape
{
    /**
     * Parameterized constructor.
     *
     * @param m                 objective function id
     * @param M                 the number of objectives
     */
    public Linear(int m, int M)
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
        if (_m == _M - 1) return 1.0d - x[0];
        double r = 1.0d;
        if (_m == 0) for (int i = 0; i < _M - 1; i++) r *= x[i];
        else
        {
            for (int i = 0; i < _M - _m - 1; i++) r *= x[i];
            r *= (1 - x[_M - _m - 1]);
        }
        return r;
    }
}
