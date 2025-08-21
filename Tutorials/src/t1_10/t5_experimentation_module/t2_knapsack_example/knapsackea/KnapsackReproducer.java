package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import ea.IEA;
import population.Chromosome;
import population.Parents;
import population.Specimen;
import reproduction.IReproduce;
import reproduction.operators.crossover.SinglePointCrossover;
import reproduction.operators.mutation.Flip;

import java.util.ArrayList;

/**
 * The implementation of IReproduce interface. It is assumed that the constructed size equals ea.getOffspringSize().
 * Further, the number of parents per offspring is two, and the number of parent objects stored in the specimen container
 * equals the offspring size. The offspring specimens are constructed by applying a single point crossover to parents'
 * boolean decision vectors, followed by applying flip mutation (with probability of 1 / the number of items).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class KnapsackReproducer implements IReproduce
{
    /**
     * Single point crossover operator.
     */
    private final SinglePointCrossover _SPC;

    /**
     * Flip mutation operator.
     */
    private final Flip _PM;

    /**
     * Parameterized constructor.
     *
     * @param data data container.
     */
    public KnapsackReproducer(Data data)
    {
        // instantiate the crossover and mutation operators
        _SPC = new SinglePointCrossover();
        _PM = new Flip(new Flip.Params(1.0d / (double) data._items));
    }

    /**
     * Creates an array of offspring specimens (of size equal to ``offspring size'').
     *
     * @param ea evolutionary algorithm
     * @return offspring specimens
     */
    @Override
    public ArrayList<Specimen> createOffspring(IEA ea)
    {
        int os = ea.getOffspringSize(); // get the offspring size
        ArrayList<Specimen> offspring = new ArrayList<>(os);

        for (int i = 0; i < os; i++)
        {
            // it is assumed that no. parents = offspring size; and there will be 2 parents per one offspring
            Parents parents = ea.getSpecimensContainer().getParents().get(i);
            Specimen p1 = parents._parents.get(0);
            Specimen p2 = parents._parents.get(1);
            boolean[] dv1 = p1.getBooleanDecisionVector();
            boolean[] dv2 = p2.getBooleanDecisionVector();

            // do crossover
            boolean[] ob = _SPC.crossover(dv1, dv2, ea.getR())._o; // apply the crossover operator

            // do mutation
            _PM.mutate(ob, ea.getR());

            // create the specimen
            Chromosome chromosome = new Chromosome(ob);
            Specimen specimen = new Specimen(2);
            specimen.setChromosome(chromosome);
            offspring.add(specimen);
        }

        return offspring;
    }
}
