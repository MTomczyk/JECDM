package phase;

import ea.EA;
import exception.PhaseException;
import population.Specimen;

import java.util.ArrayList;

/**
 * Interface for classes responsible for constructing initial populations (delegates).
 * The default (implicit) assumptions are as follows:
 * - The method is supposed to create a specimen array of size {@link EA#getPopulationSize()}.
 *
 * @author MTomczyk
 */

public interface IConstruct
{
    /**
     * Creates initial population (array of specimens).
     * The default (implicit) assumptions are as follows:
     * - The method is supposed to create a specimen array of size {@link EA#getPopulationSize()}.
     *
     * @param ea evolutionary algorithm
     * @return specimen array
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    ArrayList<Specimen> createInitialPopulation(EA ea) throws PhaseException;
}
