package emo.aposteriori.nsga;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.AbstractEMOBundle;
import os.IOSChangeListener;
import space.distance.Euclidean;
import space.distance.IDistance;
import space.normalization.builder.StandardLinearBuilder;

/**
 * Bundle (container) of necessary fields for the NSGA algorithm.
 *
 * @author MTomczyk
 */
public class NSGABundle extends AbstractEMOBundle
{
    /**
     * Params container for the bundle getter.
     */
    public static class Params extends AbstractEMOBundle.Params
    {
        /**
         * Distance function used when calculating niche counts
         */
        public IDistance _distance = new Euclidean();

        /**
         * Distance threshold for the niche count procedure
         */
        public double _th = 0.1d;

        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         */
        public Params(Criteria criteria)
        {
            super("NSGA", criteria);
        }
    }

    /**
     * Reference to the NSGA sorting phase.
     */
    protected NSGASort _nsgaSort;

    /**
     * Constructs the bundle of fields aiding in instantiating the NSGA algorithm.
     *
     * @param p params container
     */
    public NSGABundle(Params p)
    {
        super(p);
    }

    /**
     * Auxiliary method instantiating the NSGA-dedicated "sort" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateSortPhase(AbstractEABundle.Params p)
    {
        Params pp = (Params) p;
        _nsgaSort = new NSGASort(p._criteria, pp._distance, pp._th);
        _phasesBundle._sort = _nsgaSort;
    }


    /**
     * Auxiliary method for retrieving the NSGA-dedicated OS changed listener.
     * @param p params container
     * @return OS changed listeners.
     */
    @Override
    protected IOSChangeListener[] getOSChangedListeners(AbstractEABundle.Params p)
    {
        NSGAOSChangeListener l = new NSGAOSChangeListener(_nsgaSort, new StandardLinearBuilder());
        return new IOSChangeListener[]{l};
    }

    /**
     * Provides initial normalization data to selected objects.
     *
     * @param p params container
     */
    @Override
    protected void registerInitialNormalizations(AbstractEABundle.Params p)
    {
        if (p._initialNormalizations != null) _nsgaSort.updateNormalizations(p._initialNormalizations);
    }
}
