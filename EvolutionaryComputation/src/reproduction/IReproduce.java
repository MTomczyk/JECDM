package reproduction;

import ea.EA;
import exception.PhaseException;
import population.Specimen;
import population.SpecimensContainer;

import java.util.ArrayList;

/**
 * Interface for classes responsible for reproducing specimens (create offspring).
 * The default (implicit) assumptions are as follows:
 * - The number of offspring to create equals the offspring size {@link EA#getOffspringSize()}.
 * - One offspring is created using one parents object ({@link population.Parents}) stored in {@link SpecimensContainer#getParents()}.
 *
 * @author MTomczyk
 */

public interface IReproduce
{
    /**
     * Creates an array of offspring specimens.
     * The default (implicit) assumptions are as follows:
     * - The number of offspring to create equals the offspring size {@link EA#getOffspringSize()}.
     * - One offspring is created using one parents object ({@link population.Parents}) stored in {@link SpecimensContainer#getParents()}.
     *
     * @param ea evolutionary algorithm
     * @return offspring specimens
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    ArrayList<Specimen> createOffspring(EA ea) throws PhaseException;
}
