package problem.moo.wfg.shapes;

/**
 * Interface for classes returning a shape of the WFG Pareto front.
 *
 * @author MTomczyk
 */
public interface IShape
{
    /**
     * Returns the shape value.
     *
     * @param x input decision vector
     * @return shape value
     */
    double getShape(double[] x);
}
