package problem.moo.wfg.shapes;

/**
 * Abstract Pareto front shape function.
 *
 * @author MTomczyk
 */
public abstract class AbstractShape implements IShape
{
    /**
     * Objective function id.
     */
    protected final int _m;

    /**
     * The number of objectives.
     */
    protected final int _M;

    /**
     * Parameterized constructor.
     *
     * @param m                 objective function id
     * @param M                 the number of objectives
     */
    public AbstractShape(int m, int M)
    {
        _m = m;
        _M = M;
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
        return 0.0d;
    }
}
