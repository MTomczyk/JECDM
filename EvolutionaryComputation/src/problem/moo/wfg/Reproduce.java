package problem.moo.wfg;

import population.Chromosome;
import population.Specimen;
import random.IRandom;
import reproduction.AbstractReproduce;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

import java.util.ArrayList;

/**
 * Creates offspring for the WFG problem (default implementation).
 *
 * @author MTomczyk
 */


public class Reproduce extends AbstractReproduce implements IReproduce
{
    /**
     * The number of position-related parameters.
     */
    protected final int _k;

    /**
     * The number of distance-related parameters.
     */
    protected final int _l;

    /**
     * Parameterized constructor.
     *
     * @param M         the number of objectives
     * @param k         the number of position-related parameters
     * @param l         the number of distance-related parameters
     * @param crossover crossover operator.
     * @param mutate    mutation operator.
     */
    public Reproduce(int M, int k, int l, ICrossover crossover, IMutate mutate)
    {
        super(crossover, mutate, M);
        _k = k;
        _l = l;
    }

    /**
     * Supportive method for constructing one offspring.
     *
     * @param parents parents array
     * @param R       random number generator
     * @return constructed offspring
     */
    @Override
    protected Specimen createOffspring(ArrayList<Specimen> parents, IRandom R)
    {
        double[] p1 = parents.get(0).getDoubleDecisionVector().clone();
        double[] p2 = parents.get(1).getDoubleDecisionVector().clone();

        for (int i = 0; i < _k + _l; i++) // normalize first
        {
            p1[i] /= (2.0d * (i + 1));
            p2[i] /= (2.0d * (i + 1));
        }

        double[] o = _crossover.crossover(p1, p2, R)._o;
        _mutate.mutate(o, R);

        // rescale then
        for (int i = 0; i < _k + _l; i++) o[i] *= (2.0d * (i + 1));
        Chromosome c = new Chromosome(o);
        Specimen S = new Specimen(_criteria);
        S.setChromosome(c);
        return S;
    }
}
