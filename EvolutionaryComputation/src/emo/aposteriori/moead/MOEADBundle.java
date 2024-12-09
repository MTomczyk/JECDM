package emo.aposteriori.moead;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.AbstractEMOBundle;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import os.IOSChangeListener;
import space.normalization.builder.StandardLinearBuilder;

/**
 * Bundle (container) of necessary fields for the MOEA/D algorithm.
 *
 * @author MTomczyk
 */

public class MOEADBundle extends AbstractEMOBundle
{
    /**
     * Params container for the bundle's getter.
     */
    public static class Params extends AbstractEMOBundle.Params
    {
        /**
         * Goals manager steering the evolutionary process.
         */
        public final MOEADGoalsManager _goalsManager;

        /**
         * Parameterized constructor.
         *
         * @param criteria     considered criteria
         * @param goalsManager goals manager
         */
        public Params(Criteria criteria, MOEADGoalsManager goalsManager)
        {
            super("MOEA/D", criteria);
            _goalsManager = goalsManager;
        }
    }

    /**
     * Goals manager steering the evolutionary process.
     */
    protected MOEADGoalsManager _goalsManager;

    /**
     * Constructs the bundle of fields aiding in instantiating the MOEA/D algorithm.
     *
     * @param p params container
     */
    public MOEADBundle(MOEADBundle.Params p)
    {
        super(p);
        _goalsManager = p._goalsManager;
    }

    /**
     * Auxiliary method instantiating the "finalize step" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateFinalizeStepPhase(AbstractEABundle.Params p)
    {
        Params pp = (Params) p;
        _phasesBundle._finalizeStep = new MOEADFinalizeStep(pp._goalsManager);
    }


    /**
     * Auxiliary method instantiating the "construct mating pool" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateConstructMatingPoolPhase(AbstractEABundle.Params p)
    {
        Params pp = (Params) p;
        _phasesBundle._constructMatingPool = new MOEADConstructMatingPool(pp._goalsManager);
    }

    /**
     * Auxiliary method instantiating the "prepare step" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiatePrepareStepPhase(AbstractEABundle.Params p)
    {
        Params pp = (Params) p;
        _phasesBundle._prepareStep = new MOEADPrepareStep(pp._goalsManager);
    }

    /**
     * Auxiliary method instantiating the "init ends" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateInitEndsPhase(AbstractEABundle.Params p)
    {
        Params pp = (Params) p;
        _phasesBundle._initEnds = new MOEADInitEnds(pp._goalsManager);
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
        Params pp = (Params) p;
        IOSChangeListener l = new MOEADOSChangeListener(pp._goalsManager, new StandardLinearBuilder());
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
        Params pp = (Params) p;
        if (p._initialNormalizations != null) pp._goalsManager.updateNormalizations(p._initialNormalizations);
    }

    /**
     * Do not use the "merge" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateMergePhase(AbstractEABundle.Params p)
    {
        _phasesBundle._merge = null;
    }

}
