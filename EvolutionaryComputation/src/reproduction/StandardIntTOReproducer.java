package reproduction;

import random.IRandom;
import reproduction.operators.crossover.ICrossoverTO;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Class assisting in performing standard reproduction using two parents' decision vectors (of integers; see the
 * {@link StandardIntTOReproducer#reproduce(int[], int[], IRandom)} method). First, it constructs two offspring
 * vectors using the reproduction operator. If the mutation operator is supplied, it is used then to mutate the
 * offspring vectors. Lastly, if the value check object is supplied, it is applied to the offspring vectors.
 *
 * @author MTomczyk
 */
public class StandardIntTOReproducer extends AbstractStandardTOReproducer
{
    /**
     * Lower bound for the variables' values.
     */
    private final int _vLowerBound;

    /**
     * Upper bound for the variables' values.
     */
    private final int _vUpperBound;

    /**
     * Parameterized constructor.
     *
     * @param c           crossover operator (designed to construct two offspring from two parents)
     * @param m           mutation operator (can be null; not used then)
     * @param vc          object for checking if the resulting variable are in valid bounds (can be null; not used then)
     * @param vLowerBound lower bound for the variables' values
     * @param vUpperBound upper bound for the variables' values
     */
    public StandardIntTOReproducer(ICrossoverTO c, IMutate m, IValueCheck vc, int vLowerBound, int vUpperBound)
    {
        super(c, m, vc);
        _vLowerBound = vLowerBound;
        _vUpperBound = vUpperBound;
    }

    /**
     * Performs the reproduction using two parents' decision vectors (of doubles). First, it constructs two offspring
     * vectors using the reproduction operator. If the mutation operator is supplied, it is used then to mutate
     * the offspring vectors. Lastly, if the value check object is supplied, it is applied to the offspring vectors.
     *
     * @param p1 the first parent's decision vector (reference; do not modify it)
     * @param p2 the second parent's decision vector (reference; do not modify it)
     * @param R  random number generator
     * @return offspring's decision vector
     */
    public int[][] reproduce(int[] p1, int[] p2, IRandom R)
    {
        int[] o1 = _c.crossover(p1, p2, R)._o1;
        int[] o2 = _c.crossover(p1, p2, R)._o2;
        if (_m != null)
        {
            _m.mutate(o1, R);
            _m.mutate(o2, R);
        }
        if (_vc != null)
        {
            _vc.checkAndCorrect(o1, _vLowerBound, _vUpperBound);
            _vc.checkAndCorrect(o2, _vLowerBound, _vUpperBound);
        }
        return new int[][]{o1, o2};
    }
}
