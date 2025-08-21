package indicator;

import ea.IEA;
import population.Specimen;

import java.util.ArrayList;

/**
 * Default abstract implementation of {@link IPerformanceIndicator}.
 * It assumes that the EA is assessed based on the population.
 * The population is passed to the auxiliary method, which can be overwritten.
 *
 * @author MTomczyk
 */


public abstract class AbstractPerformanceIndicator implements IPerformanceIndicator
{
    /**
     * Determines the preference direction (false = to maximize; true = to minimize).
     */
    protected final boolean _lessIsPreferred;

    /**
     * Parameterized constructor.
     *
     * @param lessIsPreferred determines the preference direction (false = to maximize; true = to minimize)
     */
    public AbstractPerformanceIndicator(boolean lessIsPreferred)
    {
        _lessIsPreferred = lessIsPreferred;
    }

    /**
     * Main method for evaluating EA's performance
     *
     * @param ea evolutionary algorithm
     * @return performance value (0 if the specimen container is null)
     */
    @Override
    public double evaluate(IEA ea)
    {
        if (ea.getSpecimensContainer().getPopulation() == null) return 0.0d;
        return evaluate(ea.getSpecimensContainer().getPopulation());
    }

    /**
     * Auxiliary method for assessing the performance of EA based on its population.
     * The method is to be overwritten.
     *
     * @param population input population
     * @return performance value
     */
    protected double evaluate(ArrayList<Specimen> population)
    {
        return 0.0d;
    }

    /**
     * Method for identifying preference direction.
     *
     * @return true - less is preferred
     */
    @Override
    public boolean isLessPreferred()
    {
        return _lessIsPreferred;
    }

    /**
     * The implementation must overwrite the toString() method.
     */
    @Override
    public String toString()
    {
        return "ABSTRACT_PERFORMANCE_INDICATOR";
    }
}
