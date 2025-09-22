package reproduction;

import random.IRandom;
import reproduction.operators.crossover.ICrossoverTO;
import reproduction.operators.mutation.IMutate;

/**
 * Class assisting in performing standard reproduction using two parents' decision vectors (of booleans; see the
 * {@link StandardBoolTOReproducer#reproduce(boolean[], boolean[], IRandom)} method). First, it constructs two
 * offspring vectors using the reproduction operator. If the mutation operator is supplied, it is used then to mutate
 * the offspring vectors.
 *
 * @author MTomczyk
 */
public class StandardBoolTOReproducer extends AbstractStandardTOReproducer
{
    /**
     * Parameterized constructor.
     *
     * @param c crossover operator (designed to construct two offspring from two parents)
     * @param m mutation operator (can be null; not used then)
     */
    public StandardBoolTOReproducer(ICrossoverTO c, IMutate m)
    {
        super(c, m, null);
    }

    /**
     * Performs the reproduction using two parents' decision vectors (of doubles). First, it constructs two offspring
     * vectors using the reproduction operator. If the mutation operator is supplied, it is used then to
     * mutate the offspring vectors. Lastly, if the value check object is supplied, it is applied to the offspring
     * vectors.
     *
     * @param p1 the first parent's decision vector (reference; do not modify it)
     * @param p2 the second parent's decision vector (reference; do not modify it)
     * @param R  random number generator
     * @return offsprings' decision vectors packed into 2D matrix (the first element is the first offspring; the second
     * element is the second offspring)
     */
    public boolean[][] reproduce(boolean[] p1, boolean[] p2, IRandom R)
    {
        boolean[] o1 = _c.crossover(p1, p2, R)._o1;
        boolean[] o2 = _c.crossover(p1, p2, R)._o2;
        if (_m != null)
        {
            _m.mutate(o1, R);
            _m.mutate(o2, R);
        }
        return new boolean[][]{o1, o2};
    }
}
