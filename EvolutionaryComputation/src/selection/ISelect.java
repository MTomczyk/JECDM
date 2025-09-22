package selection;

import ea.IEA;
import exception.PhaseException;
import population.Parents;
import population.SpecimensContainer;
import reproduction.ReproductionStrategy;

import java.util.ArrayList;

/**
 * Interface for classes responsible for selecting parents from the mating pool.
 *
 * @author MTomczyk
 */
public interface ISelect
{
    /**
     * This method (its implementations) should generate an array of parents selected for reproduction. This general
     * method should take into account the indications imposed by {@link ReproductionStrategy} kept by {@link IEA}.
     * For instance, the Parents object should contain the data on the expected number of offspring that should be
     * generated from them. The assumptions are as follows: <br>
     * - The total number of the expected offspring should equal {@link IEA#getOffspringSize()}. <br>
     * - The parents should be selected from {@link SpecimensContainer#getMatingPool()}. <br>
     * - If {@link ReproductionStrategy} is provided and some constraints are violated, an exception should be thrown.
     *
     * @param ea evolutionary algorithm
     * @return selected parents
     * @throws PhaseException an exception can be thrown and propagated higher
     */
    ArrayList<Parents> selectParents(IEA ea) throws PhaseException;
}
