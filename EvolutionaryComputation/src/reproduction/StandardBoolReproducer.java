package reproduction;

import random.IRandom;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;

/**
 * Class assisting in performing standard reproduction using two parents' decision vectors (of booleans; see the
 * {@link StandardBoolReproducer#reproduce(boolean[], boolean[], IRandom)} method). First, it constructs the offspring
 * vector using the reproduction operator. If the mutation operator is supplied, it is used then to mutate the offspring
 * vector.
 *
 * @author MTomczyk
 */
public class StandardBoolReproducer extends AbstractStandardSOReproducer
{
    /**
     * Parameterized constructor.
     *
     * @param c crossover operator (designed to construct one offspring from two parents)
     * @param m mutation operator (can be null; not used then)
     */
    public StandardBoolReproducer(ICrossover c, IMutate m)
    {
        super(c, m, null);
    }

    /**
     * Performs the reproduction using two parents' decision vectors (of doubles). First, the offspring vector is
     * constructed using the reproduction operator. If the mutation operator is supplied, it is used then to mutate
     * the offspring vector. Lastly, if the value check object is supplied, it is applied to the offspring vector.
     *
     * @param p1 the first parent's decision vector (reference; do not modify it)
     * @param p2 the second parent's decision vector (reference; do not modify it)
     * @param R  random number generator
     * @return offspring's decision vector
     */
    public boolean[] reproduce(boolean[] p1, boolean[] p2, IRandom R)
    {
        boolean[] o = _c.crossover(p1, p2, R)._o;
        if (_m != null) _m.mutate(o, R);
        return o;
    }
}
