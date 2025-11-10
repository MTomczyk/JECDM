package reproduction;

import ea.IEA;
import population.Chromosome;
import population.Parents;
import population.Specimen;
import random.IRandom;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

import java.util.ArrayList;

/**
 * Abstract, default implementation of {@link reproduction.IReproduce} interface.
 *
 * @author MTomczyk
 */
public abstract class AbstractReproduce implements IReproduce
{
    /**
     * Crossover operator.
     */
    protected final ICrossover _crossover;

    /**
     * Mutation operator.
     */
    protected final IMutate _mutate;

    /**
     * The number of considered criteria.
     */
    protected final int _criteria;

    /**
     * Parameterized constructor.
     *
     * @param C        crossover operator
     * @param M        mutation operator
     * @param criteria the number of considered criteria
     */
    public AbstractReproduce(ICrossover C, IMutate M, int criteria)
    {
        _crossover = C;
        _mutate = M;
        _criteria = criteria;
    }

    /**
     * Creates an array of offspring specimens from selected parents.
     *
     * @param ea evolutionary algorithm
     * @return offspring specimens
     */
    @Override
    public ArrayList<Specimen> createOffspring(IEA ea)
    {
        ArrayList<Specimen> offspring = new ArrayList<>(ea.getSpecimensContainer().getNoExpectedOffspringToConstruct());
        for (Parents p : ea.getSpecimensContainer().getParents())
        {
            Specimen o = createOffspring(p._parents, ea.getR());
            offspring.add(o);
        }
        return offspring;
    }


    /**
     * Supportive method for constructing one offspring (default implementation; two parents produce one offspring using
     * one obligatory crossover operator and one optional mutation operator)
     *
     * @param parents parents array
     * @param R       random number generator
     * @return constructed offspring
     */
    protected Specimen createOffspring(ArrayList<Specimen> parents, IRandom R)
    {
        double[] p1 = parents.get(0).getDoubleDecisionVector();
        double[] p2 = parents.get(1).getDoubleDecisionVector();
        double[] o = _crossover.crossover(p1, p2, R)._o;
        if (_mutate != null) _mutate.mutate(o, R);
        Chromosome c = new Chromosome(o);
        Specimen S = new Specimen(_criteria);
        S.setChromosome(c);
        return S;
    }
}
