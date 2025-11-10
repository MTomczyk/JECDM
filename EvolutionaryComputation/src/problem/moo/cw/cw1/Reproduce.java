package problem.moo.cw.cw1;

import reproduction.AbstractReproduce;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

/**
 * Creates offspring for the Crash-worthiness problem (Liao, X., Li, Q., Yang, X. et al. Multiobjective
 * optimization for crash safety design of vehicles using stepwise regression model. Struct Multidisc Optim 35, 561–569
 * (2008). <a href="https://doi.org/10.1007/s00158-007-0163-x">.LINK</a>; default implementation; two parents produce
 * one offspring using one obligatory crossover operator and one optional mutation operator). Note that the problem
 * assumes that there are 5 continuous decision variables in [1;3] bounds. This implementation assumes using normalized
 * variables in [0; 1] bounds. The linear rescaling is done when evaluating the solutions (see {@link Evaluate}).
 *
 * @author MTomczyk
 */
public class Reproduce extends AbstractReproduce implements IReproduce
{
    /**
     * Parameterized constructor.
     *
     * @param crossover crossover operator.
     * @param mutate    mutation operator.
     */
    public Reproduce(ICrossover crossover, IMutate mutate)
    {
        super(crossover, mutate, 3);
    }
}
