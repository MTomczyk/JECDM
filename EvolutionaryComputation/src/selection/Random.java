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
     * Default constructor. Assumes that there are always two parents to be selected per one offspring to be constructed.
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
            parents.add(selectParents(matingPool, R));
        return parents;
    }

    /**
     * Auxiliary method signature for selecting one Parents object from an input specimens array.
     *
     * @param specimens input specimens array
     * @param R         random number generator
     * @return parents object
     */
    public Parents selectParents(ArrayList<Specimen> specimens, IRandom R)
    {
        ArrayList<Specimen> parents = new ArrayList<>(_noParentsPerOffspring);
        for (int i = 0; i < _noParentsPerOffspring; i++)
            parents.add(specimens.get(R.nextInt(specimens.size())));
        return new Parents(parents);
    }
}
