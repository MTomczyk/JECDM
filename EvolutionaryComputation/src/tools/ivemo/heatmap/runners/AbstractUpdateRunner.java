package tools.ivemo.heatmap.runners;

import ea.EA;
import exception.RunnerException;
import population.Specimen;
import runner.IRunner;
import runner.Runner;
import statistics.distribution.bucket.AbstractBucketCoords;
import tools.ivemo.heatmap.feature.IFeatureGetter;
import tools.ivemo.heatmap.utils.BucketData;

import java.util.ArrayList;

/**
 * Abstract runner for updating heatmap data when executing processing.
 *
 * @author MTomczyk
 */

public class AbstractUpdateRunner extends Runner implements IRunner
{
    /**
     * Params container
     */
    public static class Params extends Runner.Params
    {
        /**
         * Used to calculate feature values
         */
        public IFeatureGetter _featureGetter;

        /**
         * Bucket coords object responsible for translating input points into bucket coordinates (ids).
         */
        public AbstractBucketCoords _BC;

        /**
         * Dimensionality of the objective space.
         */
        public int _dimensions;

        /**
         * Parameterized constructor.
         *
         * @param ea                 evolutionary algorithm
         * @param steadyStateRepeats the number of steady-state iterations
         */
        public Params(EA ea, int steadyStateRepeats)
        {
            super(ea, steadyStateRepeats);
        }
    }

    /**
     * Used to calculate feature values
     */
    protected IFeatureGetter _featureGetter;

    /**
     * Reference to the currently processed trial data (one of the elements of the _trialData field).
     */
    protected BucketData[][][] _btd;

    /**
     * Bucket coords object responsible for translating input points into bucket coordinates (ids).
     */
    protected AbstractBucketCoords _BC;

    /**
     * Dimensionality of the objective space.
     */
    protected int _dimensions;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractUpdateRunner(Params p)
    {
        super(p);
        _featureGetter = p._featureGetter;
        _BC = p._BC;
        _dimensions = p._dimensions;
    }

    /**
     * Updates data using the initial population.
     *
     * @throws RunnerException exception can be captured when executing the method and propagated higher
     */
    @Override
    public void postInitPhase() throws RunnerException
    {
        try
        {
            super.postInitPhase();
            processSpecimens(_eas[0].getSpecimensContainer().getPopulation(), 0, 0);
        } catch (RunnerException e)
        {
            throw e;
        } catch (Exception e)
        {
            wrapException("post init phase", e);
        }
    }


    /**
     * Setter for the currently processed data sample (should be called before runner is executed).
     *
     * @param btd processed data sample
     */
    public void setProcessedSample(BucketData[][][] btd)
    {
        _btd = btd;
    }

    /**
     * Auxiliary method for processing specimens.
     *
     * @param specimens         specimens
     * @param generation        generation no.
     * @param steadyStateRepeat steady-state repeat no.
     */
    public void processSpecimens(ArrayList<Specimen> specimens, int generation, int steadyStateRepeat)
    {
        for (Specimen s : specimens)
        {
            double[] e = s.getEvaluations();
            int[] c = _BC.getBucketCoords(e);
            if (c == null) continue;
            double v = _featureGetter.getFeature(s, generation, steadyStateRepeat);

            if (_dimensions == 2) _btd[0][c[1]][c[0]]._LA.addValue(v);
            else if (_dimensions == 3) _btd[c[2]][c[1]][c[0]]._LA.addValue(v);
        }
    }
}
