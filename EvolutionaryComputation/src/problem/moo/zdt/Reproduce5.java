package problem.moo.zdt;

import population.Chromosome;
import population.Gene;
import population.Specimen;
import random.IRandom;
import reproduction.AbstractReproduce;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

import java.util.ArrayList;

/**
 * Creates offspring for the ZDT5 problem (default implementation; two parents produce one offspring using
 * one obligatory crossover operator and one optional mutation operator). Important note: the decision variables for
 * this problem are bit sequences. They are modeled as boolean arrays and stored within separate genes ({@link Gene}
 * within the top-level specimen's chromosome). The genes are crossed and mutated independently using the specified
 * Boolean-dedicated operators.
 *
 * @author MTomczyk
 */
public class Reproduce5 extends AbstractReproduce implements IReproduce
{
    /**
     * Mutation operator devoted to variables x2-x11.
     */
    private final IMutate _mutate30;

    /**
     * Parameterized constructor.
     *
     * @param crossover crossover operator
     * @param mutate1   mutation operator devoted to the first variable
     * @param mutate2   mutation operator devoted to variables x2-x11
     */
    public Reproduce5(ICrossover crossover, IMutate mutate1, IMutate mutate2)
    {
        super(crossover, mutate1, 2);
        _mutate30 = mutate2;
    }


    /**
     * Supportive method for constructing one offspring (default implementation; two parents produce one offspring using
     * one obligatory crossover operator and one optional mutation operator)
     *
     * @param parents parents array
     * @param R       random number generator
     * @return constructed offspring
     */
    @Override
    protected Specimen createOffspring(ArrayList<Specimen> parents, IRandom R)
    {
        Gene[] g1 = parents.get(0).getChromosome()._genes;
        Gene[] g2 = parents.get(1).getChromosome()._genes;
        Gene[] og = new Gene[g1.length];
        for (int i = 0; i < 11; i++)
        {
            og[i] = new Gene(_crossover.crossover(g1[i]._bv, g2[i]._bv, R)._o);
            if ((i == 0) && (_mutate != null)) _mutate.mutate(og[i]._bv, R);
            else if ((i > 0) && (_mutate30 != null)) _mutate30.mutate(og[i]._bv, R);
        }
        Chromosome c = new Chromosome(og);
        Specimen S = new Specimen(_criteria);
        S.setChromosome(c);
        return S;
    }
}
