package emo.aposteriori.nsgaii;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.AbstractEMOBundle;
import os.IOSChangeListener;
import space.normalization.builder.StandardLinearBuilder;

/**
 * Bundle (container) of necessary fields for the NSGA-II algorithm.
 *
 * @author MTomczyk
 */

public class NSGAIIBundle extends AbstractEMOBundle
{
    /**
     * Params container for the bundle.
     */
    public static class Params extends AbstractEMOBundle.Params
    {

        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         */
        public Params(Criteria criteria)
        {
            super("NSGA-II", criteria);
        }
    }

    /**
     * Reference to the NSGA-II sorting procedure.
     */
    public NSGAIISort _nsgaiiSort = null;

    /**
     * Constructs the bundle of fields aiding in instantiating the NSGA-II algorithm.
     *
     * @param p params container
     */
    public NSGAIIBundle(Params p)
    {
        super(p);
    }

    /**
     * Auxiliary method instantiating the "sort" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateSortPhase(AbstractEABundle.Params p)
    {
        _nsgaiiSort = new NSGAIISort(p._criteria);
        _phasesBundle._sort = _nsgaiiSort;
    }

    /**
     * Auxiliary method for retrieving OS changed listeners (facilitates customization by the class extensions).
     * @param p params container
     * @return OS changed listeners.
     */
    @Override
    protected IOSChangeListener[] getOSChangedListeners(AbstractEABundle.Params p)
    {
        NSGAIIOSChangeListener l = new NSGAIIOSChangeListener(_nsgaiiSort, new StandardLinearBuilder());
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
        if (p._initialNormalizations != null) _nsgaiiSort.updateNormalizations(p._initialNormalizations);
    }
}
