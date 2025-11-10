package problem.moo.zdt;

import reproduction.AbstractReproduce;
import reproduction.IReproduce;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

/**
 * Creates offspring for the ZDT[1,2,3,4,6] problems (default implementation; two parents produce one offspring using
 * one obligatory crossover operator and one optional mutation operator). Important note: ZDT4 involves decision
 * variables with different domains. However, this implementation assumes that the variables are normalized and lie in
 * the [0, 1] range; their appropriate rescaling is performed during evaluation (i.e., x' = -5 + 10x).
 *
 * @author MTomczyk
 */
public class Reproduce12346 extends AbstractReproduce implements IReproduce
{
    /**
     * Parameterized constructor.
     *
     * @param crossover crossover operator.
     * @param mutate    mutation operator.
     */
    public Reproduce12346(ICrossover crossover, IMutate mutate)
    {
        super(crossover, mutate, 2);
    }
}
