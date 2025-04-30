package indicator.emo;

import indicator.IPerformanceIndicator;
import population.Specimen;
import space.distance.IDistance;

import java.util.ArrayList;


/**
 * Implementation of the Inverted Generational Distance indicator. This implementation quantifies distances to the PF
 * based on the provided set of reference Pareto optimal solutions. Thus, the obtained results are approximations.
 *
 * @author MTomczyk
 */


public class IGD extends AbstractReferenceSetBased implements IPerformanceIndicator
{
    /**
     * Parameterized constructor.
     *
     * @param distance     distance function employed
     * @param referenceSet reference set employed
     */
    public IGD(IDistance distance, double[][] referenceSet)
    {
        super(distance, referenceSet);
    }

    /**
     * Method that calculated the inverted generational distance value (tobe minimized).
     *
     * @param population input population
     * @return performance value
     */
    @Override
    protected double evaluate(ArrayList<Specimen> population)
    {
        double sum = 0.0d;

        for (double[] rp : _referenceSet) // for each reference point
        {
            double bestDistance = Double.POSITIVE_INFINITY; // find the closes specimen
            for (Specimen s : population)
            {
                double distance = _distance.getDistance(s.getEvaluations(), rp);
                if (Double.compare(distance, bestDistance) < 0) bestDistance = distance;
            }
            sum += bestDistance;
        }

        return sum / _referenceSet.length;
    }

    /**
     * Returns string representation (IGD)
     *
     * @return "IGD"
     */
    @Override
    public String toString()
    {
        return "IGD";
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
        return new IGD(_distance, _referenceSet);
    }
}
