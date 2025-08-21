package ea;


import criterion.Criteria;
import random.IRandom;

/**
 * Base implementation of an EA. It models the evolutionary process via a series of sequentially executed functional
 * blocks, called phases ({@link phase.IPhase}).
 *
 * @author MTomczyk
 */

public class EA extends AbstractPhasesEA implements IEA
{
    /**
     * Params container.
     */
    public static class Params extends AbstractPhasesEA.Params
    {
        /**
         * Parameterized constructor.
         *
         * @param name                  name of the EA
         * @param id,                   unique ID of the EA
         * @param R                     random number generator.
         * @param computeExecutionTimes flag indicating whether the total execution time (as well as other
         *                              implementation-dependent times) are measured or not (in ms)
         * @param criteria              considered criteria
         */
        public Params(String name, int id, IRandom R, boolean computeExecutionTimes, Criteria criteria)
        {
            super(name, id, R, computeExecutionTimes, criteria);
        }

        /**
         * Parameterized constructor.
         *
         * @param R                     random number generator.
         * @param criteria              considered criteria
         */
        public Params(IRandom R, Criteria criteria)
        {
            super(null, 0, R, true, criteria);
        }

        /**
         * Parameterized constructor.
         *
         * @param name                  name of the EA
         * @param criteria              considered criteria
         */
        public Params(String name, Criteria criteria)
        {
            super(name, 0, null, false, criteria);
        }

        /**
         * Parameterized constructor.
         *
         * @param criteria considered criteria
         * @param bundle   EA bundle
         */
        public Params(Criteria criteria, AbstractEABundle bundle)
        {
            super(criteria, bundle);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public EA(Params p)
    {
        super(p);
    }
}
