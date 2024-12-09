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
         * @param noOffspring           no. offspring solutions to be generated
         */
        public Params(int noParentsPerOffspring, int noOffspring)
        {
            super(noParentsPerOffspring, noOffspring);
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
     * Parameterized constructor.
     *
     * @param noParentsPerOffspring no parents per offspring (to be selected)
     * @param noOffspring           no. offspring solutions to be generated
     */
    public Random(int noParentsPerOffspring, int noOffspring)
    {
        this(new Params(noParentsPerOffspring, noOffspring));
    }


    /**
     * Execute random parent selection.
     *
     * @param matingPool mating pool
     * @param R          random number generator
     * @return selected parents
     */
    @Override
    public ArrayList<Parents> selectParents(ArrayList<Specimen> matingPool, IRandom R)
    {
        ArrayList<Parents> parents = new ArrayList<>(_noOffspring);
        for (int i = 0; i < _noOffspring; i++)
        {
            ArrayList<Specimen> specimens = new ArrayList<>(_noParentsPerOffspring);
            for (int j = 0; j < _noParentsPerOffspring; j++)
                specimens.add(matingPool.get(R.nextInt(matingPool.size())));
            Parents p = new Parents(specimens);
            parents.add(p);
        }
        return parents;
    }
}
