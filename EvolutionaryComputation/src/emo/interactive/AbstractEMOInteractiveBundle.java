package emo.interactive;

import criterion.Criteria;
import ea.AbstractEABundle;
import emo.AbstractEMOBundle;
import emo.interactive.utils.dmcontext.AbstractDMCParamsConstructor;
import emo.interactive.utils.dmcontext.DefaultConstructor;
import emo.interactive.utils.dmcontext.FromPopulation;
import phase.InitStartsRunDSS;
import system.ds.DecisionSupportSystem;


/**
 * Container (abstract) class for interactive EMOAs.
 *
 * @author MTomczyk
 */

public abstract class AbstractEMOInteractiveBundle extends AbstractEMOBundle
{
    /**
     * Params container.
     */
    public static class Params extends AbstractEMOBundle.Params
    {
        /**
         * Decision support system object.
         */
        public final DecisionSupportSystem _DSS;

        /**
         * Decision-making context params constructor. If null, the default
         * {@link emo.interactive.utils.dmcontext.DefaultConstructor} will be used.
         */
        public AbstractDMCParamsConstructor _dmContextParamsConstructor = new DefaultConstructor(new FromPopulation());

        /**
         * Parameterized constructor
         *
         * @param name     name of the EA
         * @param criteria considered criteria
         * @param DSS      decision support system
         */
        protected Params(String name, Criteria criteria, DecisionSupportSystem DSS)
        {
            super(name, criteria);
            _DSS = DSS;
        }

        /**
         * Auxiliary method called by the constructor at the beginning to instantiate default params values (optional).
         *
         * @deprecated to be removed in future releases
         */
        @Deprecated
        @Override
        protected void instantiateDefaultValues()
        {

        }
    }

    /**
     * Reference to the DSS.
     */
    protected final DecisionSupportSystem _DSS;

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractEMOInteractiveBundle(Params p)
    {
        super(p);
        _DSS = p._DSS;
    }

    /**
     * Auxiliary method instantiating the "init starts" phase.
     *
     * @param p params container
     */
    @Override
    protected void instantiateInitStartsPhase(AbstractEABundle.Params p)
    {
        AbstractEMOInteractiveBundle.Params pp = (Params) p;
        _phasesBundle._initStarts = new InitStartsRunDSS(pp._DSS);
    }

    /**
     * Getter for the decision support system.
     *
     * @return decision support system
     */
    public DecisionSupportSystem getDSS()
    {
        return _DSS;
    }
}
