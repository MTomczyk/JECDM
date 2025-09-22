package emo;

import ea.AbstractEABuilder;
import ea.EA;
import emo.aposteriori.nsgaiii.NSGAIIIBuilder;
import emo.utils.decomposition.goal.IGoal;
import exception.EAException;
import random.IRandom;

/**
 * Auxiliary class assisting in instantiating decomposition-based EMO algorithms
 * (see, e.g., {@link emo.aposteriori.moead.MOEADBuilder#getInstance()}).
 *
 * @author MTomczyk
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractDecompositionEMOABuilder <T extends EA> extends AbstractEMOABuilder <T>
{
    /**
     * Parameterized constructor.
     *
     * @param R the random number generator
     */
    public AbstractDecompositionEMOABuilder(IRandom R)
    {
        super(R);
    }

    /**
     * Optimization goals used to steer the evolution. They also determine the population size (thus, overwrite the
     * indications of {@link AbstractEABuilder#_populationSize}).
     */
    private IGoal[] _goals = null;

    /**
     * Setter for the optimization goals used to steer the evolution. They also determine the population size (thus,
     * overwrite the indications of {@link AbstractEABuilder#_populationSize}).
     *
     * @param goals optimization goals
     * @return NSGA-III builder being parameterized
     */
    public AbstractDecompositionEMOABuilder<T> setGoals(IGoal[] goals)
    {
        _goals = goals;
        if (goals == null) _populationSize = 0;
        else _populationSize = goals.length;
        return this;
    }

    /**
     * Getter for the optimization goals used to steer the evolution.
     *
     * @return goals
     */
    public IGoal[] getGoals()
    {
        return _goals;
    }

    /**
     * Auxiliary method that can be overwritten to perform simple data validation. It is called by default by
     * {@link NSGAIIIBuilder#getInstance()} prior to initialization of the algorithm.
     *
     * @throws EAException an exception can be thrown and propagated higher.
     */
    @Override
    public void validate() throws EAException
    {
        super.validate();
        if (_goals == null)
            throw EAException.getInstanceWithSource("The optimization goals are not provided " +
                    "(the array is null)", this.getClass());
        if (_goals.length == 0)
            throw EAException.getInstanceWithSource("The optimization goals are not provided " +
                    "(the array is empty)", this.getClass());
        if (_goals.length != _populationSize)
            throw EAException.getInstanceWithSource("The number of optimization goals provided is different than " +
                    "the expected population size (" + _goals.length + " vs " + _populationSize + ")", this.getClass());
    }
}
