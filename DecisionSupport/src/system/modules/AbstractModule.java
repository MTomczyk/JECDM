package system.modules;

import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.ModuleException;
import system.dm.DM;
import system.dm.DecisionMakerSystem;


/**
 * Abstract module class.
 *
 * @author MTomczyk
 */


public abstract class AbstractModule
{
    /**
     * Params container
     */
    public static class Params
    {
        /**
         * Module's name.
         */
        protected final String _name;

        /**
         * The DMs identifiers.
         */
        public DM [] _DMs;

        /**
         * Decision Makers' systems.
         */
        public DecisionMakerSystem[] _DMSs;

        /**
         * Parameterized constructor.
         *
         * @param name module name
         */
        protected Params(String name)
        {
            _name = name;
        }
    }


    /**
     * Module's name.
     */
    protected final String _name;

    /**
     * The DMs identifiers.
     */
    protected final DM [] _DMs;

    /**
     * Decision Makers' systems.
     */
    protected final DecisionMakerSystem[] _DMSs;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractModule(Params p)
    {
        _name = p._name;
        _DMs = p._DMs;
        _DMSs = p._DMSs;
    }

    /**
     * Returns a string representation (module's name).
     *
     * @return string representation
     */
    @Override
    public String toString()
    {
        return _name;
    }

    /**
     * Auxiliary method for performing data validation.
     *
     * @throws ModuleException the exception will be thrown if the validation fails
     */
    public void validate() throws ModuleException
    {
        if (_DMSs == null)
            throw new ModuleException("The decision maker system(s) is (are) not provided (the array is null)", this.getClass());
        if (_DMSs.length == 0)
            throw new ModuleException("The decision maker system(s) is (are) not provided (the array is empty)", this.getClass());
        for (DecisionMakerSystem dms: _DMSs)
            if (dms == null)
                throw new ModuleException("One of the provided decision maker systems is null", this.getClass());
        if (_DMs == null)
            throw new ModuleException("The decision maker identifier(s) is (are) not provided (the array is null)", this.getClass());
        if (_DMs.length == 0)
            throw new ModuleException("The decision maker identifier(s) is (are) not provided (the array is empty)", this.getClass());
        for (DM dm: _DMs)
            if (dm == null)
                throw new ModuleException("One of the provided decision maker identifiers is null", this.getClass());


        boolean mismatch = false;
        if (_DMs.length != _DMSs.length) mismatch = true;
        else
        {
            for (int i = 0; i < _DMs.length; i++)
            {
                if (!_DMSs[i].getDM().equals(_DMs[i]))
                {
                    mismatch = true;
                    break;
                }
            }
        }
        if (mismatch)
            throw new ModuleException("The decision maker system(s) does (do) not match the decision maker identifier (s)", this.getClass());

    }

    /**
     * Auxiliary method for registering the current decision-making context.
     *
     * @param dmContext current decision-making context
     * @throws ModuleException the exception can be thrown 
     */
    protected void registerDecisionMakingContext(DMContext dmContext) throws ModuleException
    {
        if (dmContext == null)
            throw new ModuleException("The current decision-making context is not provided", this.getClass());

        for (DecisionMakerSystem dms : _DMSs)
        {
            try
            {
                dms.registerDecisionMakingContext(dmContext);
            } catch (DecisionMakerSystemException e)
            {
                throw new ModuleException("The exception occurred when registering the current decision-making context for the decision maker = " + dms.getDM().getName()
                        + " " + e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }
    }



    /**
     * Auxiliary method for unregistering the current decision-making context
     *
     * @throws ModuleException the exception can be thrown 
     */
    protected void unregisterDecisionMakingContext() throws ModuleException
    {
        for (DecisionMakerSystem dms : _DMSs)
        {
            try
            {
                dms.unregisterDecisionMakingContext();
            } catch (DecisionMakerSystemException e)
            {
                throw new ModuleException("The exception occurred for the decision maker = " + dms.getDM().getName() +
                        " when unregistering the current decision-making context " + e.getDetailedReasonMessage(), this.getClass(), e);
            }
        }
    }

}
