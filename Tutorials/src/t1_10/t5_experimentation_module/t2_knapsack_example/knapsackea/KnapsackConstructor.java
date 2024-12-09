package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import ea.EA;
import exception.PhaseException;
import phase.IConstruct;
import population.Chromosome;
import population.Specimen;

import java.util.ArrayList;

/**
 * This implementation of the {@link IConstruct} interfaces constructs an initial specimen array of length = ea.getPopulationSize.
 * The solution representation adopted in this implementation is a boolean vector of size = the number of items; each
 * element explicitly tells which items are or aren't added to the knapsack.
 *
 * @author MTomczyk
 */


@SuppressWarnings("DuplicatedCode")
public class KnapsackConstructor implements IConstruct
{
    /**
     * Data container.
     */
    private final Data _data;

    /**
     * Parameterized constructor.
     *
     * @param data data container.
     */
    public KnapsackConstructor(Data data)
    {
        _data = data;
    }

    /**
     * Creates initial population (array of specimens) of size ``population size''.
     * The population consists of random solutions to the knapsack problem.
     *
     * @param ea evolutionary algorithm
     * @return specimen array
     * @throws PhaseException the exception can be thrown and propagated higher
     */
    @Override
    public ArrayList<Specimen> createInitialPopulation(EA ea) throws PhaseException
    {
        int ps = ea.getPopulationSize(); // number of specimens = population size
        ArrayList<Specimen> specimens = new ArrayList<>(ps); // instantiate the array
        for (int i = 0; i < ps; i++) // iterate the required number of times
        {
            boolean[] itemUsed = new boolean[_data._items]; // create random boolean vector (item used or not)
            for (int j = 0; j < _data._items; j++) itemUsed[j] = ea.getR().nextBoolean();
            Specimen s = new Specimen(2); // create specimen; 2 = the number of criteria (value; size)
            Chromosome c = new Chromosome(itemUsed); // create the chromosome (decision space; boolean vector is passed)
            s.setChromosome(c); // set specimen's chromosome
            specimens.add(s); // store the specimen
        }
        return specimens;
    }
}
