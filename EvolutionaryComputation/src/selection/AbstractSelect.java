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
         * Number of offspring specimens that are to be generated
         */
        public int _noOffspring;

        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param noOffspring no. offspring solutions to be generated
         */
        public Params(int noOffspring)
        {
            this(2, noOffspring);
        }

        /**
         * Parameterized constructor.
         *
         * @param noParentsPerOffspring no parents per offspring (to be selected)
         * @param noOffspring           no. offspring solutions to be generated
         */
        public Params(int noParentsPerOffspring, int noOffspring)
        {
            _noParentsPerOffspring = noParentsPerOffspring;
            _noOffspring = noOffspring;
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
        _noOffspring = params._noOffspring;
    }


    /**
     * Number of parents per one offspring.
     */
    protected final int _noParentsPerOffspring;

    /**
     * Number of offspring specimens that are to be generated.
     */
    protected final int _noOffspring;
}
