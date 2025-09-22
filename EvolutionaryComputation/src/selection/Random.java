package selection;

import population.Parents;
import population.Specimen;
import random.IRandom;

import java.util.ArrayList;

/**
 * Implementation of the random selection procedure.
 *
 * @author MTomczyk
 */
public class Random extends AbstractSelect implements ISelect
{
    /**
     * Params container.
     */
    public static class Params extends AbstractSelect.Params
    {
        /**
         * Default constructor.
         */
        public Params()
        {

        }

        /**
         * Parameterized constructor.
         *
         * @param noParentsPerOffspring no. parents per offspring (to be selected)
         */
        public Params(int noParentsPerOffspring)
        {
            super(noParentsPerOffspring);
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public Random(Params p)
    {
        super(p);
    }

    /**
     * Default constructor. Assumes that there are always two parents to be selected per one offspring to be
     * constructed.
     */
    public Random()
    {
        this(2);
    }

    /**
     * Parameterized constructor.
     *
     * @param noParentsPerOffspring no parents per offspring (to be selected)
     */
    public Random(int noParentsPerOffspring)
    {
        this(new Params(noParentsPerOffspring));
    }

    /**
     * Auxiliary method signature for selecting one Parents object from an input specimens array. Random selection.
     * Sets the expected number of offspring to generate to 1.
     *
     * @param specimens input specimens array
     * @param R         random number generator
     * @return parents object
     */
    @Override
    public Parents selectParents(ArrayList<Specimen> specimens, IRandom R)
    {
        return super.selectParents(specimens, R);
    }
}
