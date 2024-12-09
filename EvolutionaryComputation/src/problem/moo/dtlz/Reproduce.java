package problem.moo.dtlz;

import population.Chromosome;
import population.Specimen;
import random.IRandom;
import reproduction.AbstractReproduce;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

import java.util.ArrayList;

/**
 * Creates offspring for the DTLZ problem (default implementation).
 *
 * @author MTomczyk
 */
public class Reproduce extends AbstractReproduce implements IReproduce
{
    /**
     * The number of position-related variables (the number of objectives = _P + 1)
     */
    protected final int _P;

    /**
     * The number of distance-related variables.
     */
    protected final int _D;


    /**
     * Parameterized constructor.
     *
     * @param M         the number of objectives
     * @param D         the number of distance-related parameters
     * @param crossover crossover operator.
     * @param mutate    mutation operator.
     */
    public Reproduce(int M, int D, ICrossover crossover, IMutate mutate)
    {
        super(crossover, mutate, M);
        _P = M - 1;
        _D = D;
    }

    /**
     * Supportive method for constructing one offspring.
     *
     * @param parents parents array
     * @param R random number generator
     * @return constructed offspring
     */
    @Override
    protected Specimen createOffspring(ArrayList<Specimen> parents, IRandom R)
    {
        double[] p1 = parents.get(0).getDoubleDecisionVector();
        double[] p2 = parents.get(1).getDoubleDecisionVector();
        double[] o = _crossover.crossover(p1, p2, R);
        _mutate.mutate(o, R);
        Chromosome c = new Chromosome(o);
        Specimen S = new Specimen(_criteria);
        S.setChromosome(c);
        return S;
    }
}
