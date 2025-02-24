package selection;

/**
 * Abstract class for the {@link selection.ISelect} interface.
 *
 * @author MTomczyk
 */


public abstract class AbstractSelect implements ISelect
{

    /**
     * Supportive inner class for passing various parameters to the main class.
     */
    public static class Params
    {

        /**
         * Number of parents per one offspring.
         */
        public int _noParentsPerOffspring = 2;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param noParentsPerOffspring no parents per offspring (to be selected)
         */
        public Params(int noParentsPerOffspring)
        {
            _noParentsPerOffspring = noParentsPerOffspring;
        }
    }

    /**
     * Parameterized constructor (to be called using "super"). Instantiates basic fields.
     *
     * @param params params container
     */
    public AbstractSelect(Params params)
    {
        _noParentsPerOffspring = params._noParentsPerOffspring;
    }


    /**
     * Number of parents per one offspring.
     */
    protected final int _noParentsPerOffspring;

}
