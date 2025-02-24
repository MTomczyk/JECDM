package selection;

import ea.EA;
import population.Parents;
import population.Specimen;
import population.SpecimensContainer;
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
     * Parameterized constructor.
     *
     * @param noParentsPerOffspring no parents per offspring (to be selected)
     */
    public Random(int noParentsPerOffspring)
    {
        this(new Params(noParentsPerOffspring));
    }


    /**
     * Constructs array of parents (one element for one offspring to generate).
     * The default (implicit) assumptions are as follows:
     * - The number of parents to construct ({@link Parents}) equals the offspring size ({@link EA#getOffspringSize()}).
     * - The parents are selected from the current mating pool in {@link SpecimensContainer#getMatingPool()}.
     *
     * @param ea evolutionary algorithm
     * @return selected parents
     */
    @Override
    public ArrayList<Parents> selectParents(EA ea)
    {
        ArrayList<Parents> parents = new ArrayList<>(ea.getOffspringSize());
        ArrayList<Specimen> matingPool = ea.getSpecimensContainer().getMatingPool();
        IRandom R = ea.getR();

        for (int i = 0; i < ea.getOffspringSize(); i++)
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
