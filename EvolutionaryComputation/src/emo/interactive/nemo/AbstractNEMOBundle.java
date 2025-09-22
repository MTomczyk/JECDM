package emo.interactive.nemo;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.aposteriori.nsgaii.NSGAIIOSChangeListener;
import emo.aposteriori.nsgaii.NSGAIISort;
import emo.interactive.AbstractEMOInteractiveBundle;
import os.IOSChangeListener;
import system.ds.DecisionSupportSystem;

/**
 * Abstract bundle (container) of necessary fields for the NEMO algorithms.
 * (see <a href="https://doi.org/10.1109/TEVC.2014.2303783">link</a>).
 *
 * @author MTomczyk
 */

public abstract class AbstractNEMOBundle extends AbstractEMOInteractiveBundle
{
    /**
     * Params container for the bundle's getter.
     */
    public static class Params extends AbstractEMOInteractiveBundle.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name     algorithm name
         * @param criteria considered criteria
         * @param DSS      decision support system object
         */
        protected Params(String name,
                         Criteria criteria,
                         DecisionSupportSystem DSS)
        {
            super(name, criteria, DSS);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractNEMOBundle(Params p)
    {
        super(p);
    }


    /**
     * Auxiliary method instantiating the "prepare step" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiatePrepareStepPhase(AbstractEABundle.Params p)
    {
        AbstractNEMOBundle.Params pp = (AbstractNEMOBundle.Params) p;
        _phasesBundle._prepareStep = new NEMOInteractAndPrepareStep(_name + ": Interact and prepare step", pp._DSS, pp._dmContextParamsConstructor);
    }

    /**
     * Auxiliary method for retrieving OS changed listeners (facilitates customization by the class extensions).
     *
     * @param p params container
     * @return OS changed listeners.
     */
    @Override
    protected IOSChangeListener[] getOSChangedListeners(AbstractEABundle.Params p)
    {
        AbstractNEMOBundle.Params pp = (AbstractNEMOBundle.Params) p;
        NSGAIIOSChangeListener l = new NSGAIIOSChangeListener((NSGAIISort) _phasesBundle._sort);
        return new IOSChangeListener[]{l, pp._dmContextParamsConstructor, (IOSChangeListener) _phasesBundle._prepareStep};
    }

    /**
     * Provides initial normalization data to selected objects.
     *
     * @param p params container
     */
    @Override
    protected void registerInitialNormalizations(AbstractEABundle.Params p)
    {
        if (p._initialNormalizations != null) ((NSGAIISort) _phasesBundle._sort).updateNormalizations(p._initialNormalizations);
    }

}
