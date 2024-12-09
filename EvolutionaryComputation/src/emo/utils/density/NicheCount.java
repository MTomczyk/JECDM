package emo.utils.density;

import population.Specimen;
import space.distance.IDistance;
import space.normalization.INormalization;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class for calculating the niche count (used, e.g., by the original NSGA algorithm).
 *
 * @author MTomczyk
 */


public class NicheCount
{
    /**
     * Distance function used.
     */
    private final IDistance _distance;

    /**
     * Distance threshold (if the distance exceeds it, the NC value is considered 1).
     */
    private final double _th;

    /**
     * Parameterized constructor.
     * @param distance distance function used
     * @param th distance threshold (if the distance exceeds it, the NC value is considered 0)
     */
    public NicheCount(IDistance distance, double th)
    {
        _distance = distance;
        _th = th;
    }

    /**
     * Calculates the niche count by considering only those specimens that are in the given front.
     * @param front non-dominated front of specimens for which the niche counts will be calculated (indices pointing to the specimen-array elements)
     * @param population current population
     * @param normalizations normalizations used to scale dimensions
     * @return niche count array; the array is of front length, and each front element (index) corresponds to each element of the returned array (niche count)
     *
     */
    public double[] getNicheCountInFront(LinkedList<Integer> front,
                                         ArrayList<Specimen> population,
                                         INormalization[] normalizations)
    {
        _distance.setNormalizations(normalizations);

        double [] nc = new double[front.size()];

        double [][] dm = new double[front.size()][front.size()]; // distance matrix
        double [] ae;
        double [] be;
        int i = -1;

        for (Integer a: front)
        {
            i++;
            int j = -1;
            ae = population.get(a).getEvaluations();

            for (Integer b: front)
            {
                j++;
                if (i == j) continue;
                be = population.get(b).getEvaluations();

                double d = _distance.getDistance(ae, be);
                if (d < _th) dm[i][j] = (1 - Math.pow(d / _th, 2.0d));
            }
        }

        for (i = 0; i < front.size(); i++)
        {
            nc[i] = 1.0d;
            for (int j = 0; j < front.size(); j++) nc[i] += dm[i][j];
        }

        return nc;
    }
}
