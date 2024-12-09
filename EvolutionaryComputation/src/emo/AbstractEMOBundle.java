package emo;

import criterion.Criteria;
import ea.AbstractEABundle;



/**
 * Container (abstract) class for EMOAs.
 *
 * @author MTomczyk
 */

public abstract class AbstractEMOBundle extends AbstractEABundle
{
    /**
     * Params container.
     */
    public static class Params extends AbstractEABundle.Params
    {
        /**
         * Parameterized constructor
         *
         * @param name name of the EA
         * @param criteria considered criteria
         */
        public Params(String name, Criteria criteria)
        {
            super(name, criteria);
        }
    }


    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public AbstractEMOBundle(Params p)
    {
        super(p);
    }

    /**
     * Sets the sorting phase (default) to null.
     *
     * @param p params container
     */
    @Override
    protected void instantiateSortPhase(AbstractEABundle.Params p)
    {
        _phasesBundle._sort = null;
    }

    /**
     *  Sets the remove phase (default) to null.
     *
     * @param p params container
     */
    @Override
    protected void instantiateRemovePhase(AbstractEABundle.Params p)
    {
        _phasesBundle._remove = null;
    }
}
