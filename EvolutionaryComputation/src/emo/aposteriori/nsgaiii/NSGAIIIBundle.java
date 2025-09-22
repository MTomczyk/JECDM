package emo.aposteriori.nsgaiii;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.AbstractEMOBundle;
import emo.utils.decomposition.nsgaiii.IAssignmentResolveTie;
import emo.utils.decomposition.nsgaiii.ISpecimenResolveTie;
import emo.utils.decomposition.nsgaiii.NSGAIIIGoalsManager;
import os.IOSChangeListener;

/**
 * Bundle (container) of necessary fields for the NSGA-III algorithm.
 *
 * @author MTomczyk
 */

public class NSGAIIIBundle extends AbstractEMOBundle
{
    /**
     * Auxiliary interface for classes that can be used to adjust the params container being processed.
     */
    public interface IParamsAdjuster
    {
        /**
         * The main method for adjusting the params container.
         *
         * @param p params container being instantiated
         */
        void adjust(Params p);
    }

    /**
     * Params container for the bundle's getter.
     */
    public static class Params extends AbstractEMOBundle.Params
    {
        /**
         * Goals manager steering the evolutionary process.
         */
        public final NSGAIIIGoalsManager _goalsManager;

        /**
         * Object used for resolving a tie when selecting an assignment with a minimal niche count
         */
        public final IAssignmentResolveTie _assignmentResolveTie;

        /**
         * Object used when resolving a tie during a selection of a specimen when the associated assignment object has a
         * niche count value greater than one
         */
        public final ISpecimenResolveTie _specimenResolveTie;

        /**
         * Parameterized constructor.
         *
         * @param criteria             considered criteria
         * @param goalsManager         NSGA-III goals manager
         * @param assignmentResolveTie object used for resolving a tie when selecting an assignment with a minimal niche
         *                             count
         * @param specimenResolveTie   object responsible for resolving a tia when selecting from multiple solutions
         *                             assigned to a goal with niche count > 1
         */
        public Params(Criteria criteria, NSGAIIIGoalsManager goalsManager,
                      IAssignmentResolveTie assignmentResolveTie,
                      ISpecimenResolveTie specimenResolveTie)
        {
            super("NSGA-III", criteria);
            _goalsManager = goalsManager;
            _assignmentResolveTie = assignmentResolveTie;
            _specimenResolveTie = specimenResolveTie;
        }
    }

    /**
     * Constructs the bundle of fields aiding in instantiating the NSGA-III algorithm.
     *
     * @param p params container
     */
    public NSGAIIIBundle(Params p)
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
        _phasesBundle._sort = new NSGAIIISort(p._criteria,
                ((Params) p)._goalsManager,
                ((Params) p)._assignmentResolveTie,
                ((Params) p)._specimenResolveTie);
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
        NSGAIIIOSChangeListener l = new NSGAIIIOSChangeListener(((Params) p)._goalsManager);
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
        if (p._initialNormalizations != null) ((Params) p)._goalsManager.updateNormalizations(p._initialNormalizations);
    }
}
