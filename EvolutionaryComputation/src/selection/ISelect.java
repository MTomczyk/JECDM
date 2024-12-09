package selection;

import ea.EA;
import population.Parents;
import population.Specimen;
import random.IRandom;

import java.util.ArrayList;

/**
 * Interface for classes responsible for selecting parents from the mating pool.
 * The default (implicit) assumptions are as follows:
 * - The number of parents to construct ({@link Parents}) equals the offspring size ({@link EA#getOffspringSize()}).
 * - The parents are selected from the passed specimen array (mating pool).
 *
 * @author MTomczyk
 */
public interface ISelect
{
    /**
     * Constructs array of parents (one element for one offspring to generate).
     * The default (implicit) assumptions are as follows:
     * - The number of parents to construct ({@link Parents}) equals the offspring size ({@link EA#getOffspringSize()}).
     * - The parents are selected from the passed specimen array (mating pool).
     *
     * @param matingPool mating pool
     * @param R          random number generator
     * @return selected parents
     */
    ArrayList<Parents> selectParents(ArrayList<Specimen> matingPool, IRandom R);
}
