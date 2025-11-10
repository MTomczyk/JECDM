package tools.prototypes;

import ea.IEA;
import indicator.AbstractPerformanceIndicator;
import indicator.IPerformanceIndicator;
import population.Specimen;
import space.Range;
import space.distance.Euclidean;
import space.distance.IDistance;
import space.normalization.INormalization;

import java.util.ArrayList;

/**
 * PROTOTYPE: recommended not to use it; Fast implementation of GD that uses
 * {@link ClosestRPSearcher} to find the closest reference points (prototype implementation). It
 * also checks is a specimen is from a previous generation. If not, its calculated individual distance is stored in it
 * as a metadata. If the specimen is not new, its metadata is read (avoids calculating the closest distance as it should
 * not change).
 *
 * @author MTomczyk
 */
public class GDFast extends AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Searcher (shared; must be thread-safe).
     */
    private final ClosestRPSearcher _searcher;

    /**
     * Normalization functions used to normalize the reference points.
     */
    private final INormalization[] _normalizations;

    /**
     * Distance function (without normalization).
     */
    private final IDistance _D = new Euclidean();

    /**
     * Parameterized constructor.
     *
     * @param rps            normalized reference point (not-normalized)
     * @param normalizations normalization functions (used to rescale the input points; not null)
     * @param div            the number of divisions per level (at least 2; for the searcher object)
     * @param maxRPsInBucket max RPs in a bucket (if a bucket contains more RPs, it will be subdivided; for the searcher
     *                       object)
     */
    public GDFast(double[][] rps,
                  INormalization[] normalizations,
                  int div,
                  int maxRPsInBucket)
    {
        super(true);
        _normalizations = normalizations;
        int M = 0;
        if (rps.length > 0) M = rps[0].length;
        double[][] nrps = new double[rps.length][];
        for (int i = 0; i < rps.length; i++) nrps[i] = rps[i].clone();
        for (int i = 0; i < rps.length; i++)
            for (int j = 0; j < M; j++)
                nrps[i][j] = normalizations[j].getNormalized(nrps[i][j]);
        _searcher = new ClosestRPSearcher(M, div, Range.getDefaultRanges(M), maxRPsInBucket, nrps);
    }

    /**
     * Parameterized constructor.
     *
     * @param searcher       searcher
     * @param normalizations normalization functions (used to rescale the input points; not null)
     */
    protected GDFast(ClosestRPSearcher searcher, INormalization[] normalizations)
    {
        super(true);
        _normalizations = normalizations;
        _searcher = searcher;
    }

    /**
     * The main method for performance evaluation.
     *
     * @param ea instance of the evolutionary algorithm to be assessed
     * @return the assessment
     */
    @Override
    public double evaluate(IEA ea)
    {
        ArrayList<Specimen> S = ea.getSpecimensContainer().getPopulation();
        if ((S == null) || (S.isEmpty())) return Double.POSITIVE_INFINITY;
        double sum = 0.0d;
        for (Specimen s : S)
        {
            if (s.getID()._generation < ea.getCurrentGeneration()) // read from previous
                sum += s.getAlternative().getAuxScores()[s.getAlternative().getAuxScores().length - 1];
            else // calculate and set as new
            {
                double[] p = s.getEvaluations().clone();
                for (int i = 0; i < p.length; i++) p[i] = _normalizations[i].getNormalized(p[i]);
                double[] closest = _searcher.findUsingBNB(p);
                double d = _D.getDistance(closest, p); // in the normalized space
                sum += d;
                if (s.getAlternative().getAuxScores() == null)
                    s.getAlternative().setAuxScore(d);
                else
                {
                    double[] a = s.getAlternative().getAuxScores();
                    double[] na = new double[a.length + 1];
                    System.arraycopy(a, 0, na, 0, a.length);
                    na[a.length] = d;
                    s.getAlternative().setAuxScores(na);
                }
            }
        }
        return sum / S.size();
    }

    /**
     * For cloning: must be implemented to work properly with the experimentation module.
     * Deep copy must be created, but it should consider object's INITIAL STATE.
     *
     * @return cloned instance
     */
    @Override
    public IPerformanceIndicator getInstanceInInitialState()
    {
        return new GDFast(_searcher, _normalizations);
    }

    /**
     * The implementation must overwrite the toString() method.
     */
    @Override
    public String toString()
    {
        return "GDFAST";
    }
}
