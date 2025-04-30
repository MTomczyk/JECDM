package indicator.emo;

import indicator.IPerformanceIndicator;
import population.Specimen;
import space.distance.IDistance;

import java.util.ArrayList;

/**
 * Implementation of the Generational Distance indicator. This implementation calculates true GD-values.
 * The assumption is that the shape of the PF is a standard concave sphere with an origin in 0-vector,
 * radius of 1, and the feasible objective bounds take non-negative values (as, e.g., in DTLZ2 test problem).
 * The indicator can work well if the shape is linearly rescaled on each dimension (as, e.g., in WFG4 test problem).
 * To do so, suitable normalization objects must be supplied along with the provided distance function definition.
 * Overall, the distances involved in calculating the GD are quantified as the solution's objective vector distance
 * to the 0-vector (in the normalized space) subtracted by 1. Important note: this class is appointed the
 * to string representation as {@link GD}.
 *
 * @author MTomczyk
 */
public class GDConcaveSpherical extends AbstractReferenceSetBased implements IPerformanceIndicator
{
    /**
     * Objective space dimensionality.
     */
    private final int _M;

    /**
     * M-dimensional zero-vector.
     */
    private final double[] _zv;

    /**
     * Parameterized constructor.
     *
     * @param distance distance function employed
     * @param M        objective space dimensionality
     */
    public GDConcaveSpherical(IDistance distance, int M)
    {
        super(distance, null);
        _M = M;
        _zv = new double[M];
    }

    /**
     * Method that calculated the generational distance value.
     *
     * @param population input population
     * @return performance value
     */
    @Override
    protected double evaluate(ArrayList<Specimen> population)
    {
        double sum = 0.0d;

        for (Specimen s : population) // for each solution
        {
            double[] e = s.getEvaluations();
            double distance = _distance.getDistance(e, _zv);
            sum += (distance - 1.0d);
        }
        return sum / population.size();
    }


    /**
     * Returns string representation (GD)
     *
     * @return "GD"
     */
    @Override
    public String toString()
    {
        return "GD";
    }

    /**
     * Creates a cloned object in an initial state.
     * The distance function and the reference sets are not copied (references are passed)
     *
     * @return cloning
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        return new GDConcaveSpherical(_distance, _M);
    }
}
