package emo.interactive.utils.dmcontext;

import alternative.AbstractAlternatives;
import ea.IEA;
import population.Specimens;

/**
 * Implementation of {@link IAlternativesProvider}. Treats the current population as the superset of alternatives.
 *
 * @author MTomczyk
 */
public class FromPopulation implements IAlternativesProvider
{
    /**
     * The main method.
     *
     * @param ea evolutionary algorithm
     * @return the current population
     */
    @Override
    public AbstractAlternatives<?> getAlternatives(IEA ea)
    {
        return new Specimens(ea.getSpecimensContainer().getPopulation());
    }
}
