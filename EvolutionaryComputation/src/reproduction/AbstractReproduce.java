package reproduction;

import ea.EA;
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
     * @param C crossover operator
     * @param M mutation operator
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
     * @param ea evolutionary algorithm
     * @return offspring specimens
     */
    @Override
    public ArrayList<Specimen> createOffspring(EA ea)
    {
        ArrayList<Specimen> offspring = new ArrayList<>(ea.getOffspringSize());
        for (int i = 0; i < ea.getOffspringSize(); i++)
        {
            Specimen o = createOffspring(ea.getSpecimensContainer().getParents().get(i)._parents, ea.getR());
            offspring.add(o);
        }
        return offspring;
    }

    /**
     * Supportive method for constructing one offspring.
     * @param parents parents array
     * @param R random number generator
     * @return constructed offspring
     */
    protected Specimen createOffspring(ArrayList<Specimen> parents, IRandom R)
    {
        return null;
    }
}
