package t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea;

import ea.AbstractEA;
import ea.EA;
import ea.IEA;
import population.SpecimensContainer;

/**
 * Simple extension of {@link EA}.
 *
 * @author MTomczyk
 */
public class KnapsackEA extends AbstractEA implements IEA
{
    /**
     * Parameterized constructor.
     *
     * @param p params container
     */
    public KnapsackEA(Params p)
    {
        super(p);
    }

    /**
     * Auxiliary method for adjusting the population size. It also suitably alters the offspring size (should equal
     * population size). Use with caution. It should not be invoked when executing an initialization or a generation
     * but between these steps. The method does not explicitly extend the population array in
     * {@link SpecimensContainer#getPopulation()} nor truncate it. However, the default implementation of phases allows
     * for automatically adapting to new population sizes during evolution.
     *
     * @param populationSize new population size (set to 1 if the input is lesser)
     */
    public void adjustPopulationSize(int populationSize)
    {
        setPopulationSize(populationSize);
        setOffspringSize(populationSize);
    }
}
