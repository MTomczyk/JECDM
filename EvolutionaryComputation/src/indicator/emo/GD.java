package indicator.emo;

import indicator.IPerformanceIndicator;
import population.Specimen;
import space.distance.IDistance;
import java.util.ArrayList;
/**
 * Implementation of the Generational Distance indicator.
 *
 * @author MTomczyk
 */


public class GD extends AbstractReferenceSetBased implements IPerformanceIndicator
{
    /**
     * Parameterized constructor.
     *
     * @param distance     distance function employed
     * @param referenceSet reference set employed
     */
    public GD(IDistance distance, double[][] referenceSet)
    {
        super(distance, referenceSet);
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
            double bestDistance = Double.POSITIVE_INFINITY; // find the closes reference point
            for (double[] rp : _referenceSet)
            {
                double distance = _distance.getDistance(e, rp);
                if (Double.compare(distance, bestDistance) < 0) bestDistance = distance;
            }
            sum += bestDistance;
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
        return new GD(_distance, _referenceSet);
    }
}
