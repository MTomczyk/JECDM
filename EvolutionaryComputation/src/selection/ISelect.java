package selection;

import ea.EA;
import ea.IEA;
import population.Parents;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * Interface for classes responsible for selecting parents from the mating pool.
 * The default (implicit) assumptions are as follows:
 * - The number of parents to construct ({@link Parents}) equals the offspring size ({@link IEA#getOffspringSize()}).
 * - The parents are selected from the passed specimen array (mating pool).
 *
 * @author MTomczyk
 */
public interface ISelect
{
    /**
     * Constructs array of parents (one element for one offspring to generate).
     * The default (implicit) assumptions are as follows:
     * - The number of parents to construct ({@link Parents}) equals the offspring size ({@link IEA#getOffspringSize()}).
     * - The parents are selected from the current mating pool in {@link SpecimensContainer#getMatingPool()}.
     *
     * @param ea evolutionary algorithm
     * @return selected parents
     */
    ArrayList<Parents> selectParents(IEA ea);
}
