package reproduction;

import random.IRandom;
import reproduction.operators.crossover.ICrossoverTO;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Class assisting in performing standard reproduction using two parents' decision vectors (of doubles; see the
 * {@link StandardDoubleTOReproducer#reproduce(double[], double[], IRandom)} method).First, it constructs two
 * offspring vectors using the reproduction operator.  If the mutation operator is supplied, it is used then to mutate
 * the offspring vector. Lastly, if the value check object is supplied, it is applied to the offspring vector.
 *
 * @author MTomczyk
 */
public class StandardDoubleTOReproducer extends AbstractStandardTOReproducer
{
    /**
     * Lower bound for the variables' values.
     */
    private final double _vLowerBound;

    /**
     * Upper bound for the variables' values.
     */
    private final double _vUpperBound;

    /**
     * Parameterized constructor.
     *
     * @param c           crossover operator (designed to construct two offspring from two parents)
     * @param m           mutation operator (can be null; not used then)
     * @param vc          object for checking if the resulting variable are in valid bounds (can be null; not used then)
     * @param vLowerBound lower bound for the variables' values
     * @param vUpperBound upper bound for the variables' values
     */
    public StandardDoubleTOReproducer(ICrossoverTO c, IMutate m, IValueCheck vc, double vLowerBound, double vUpperBound)
    {
        super(c, m, vc);
        _vLowerBound = vLowerBound;
        _vUpperBound = vUpperBound;
    }

    /**
     * Performs the reproduction using two parents' decision vectors (of doubles). First, it constructs two offspring
     * vectors using the reproduction operator. If the mutation operator is supplied, it is used then to mutate
     * the offspring vector. Lastly, if the value check object is supplied, it is applied to the offspring vector.
     *
     * @param p1 the first parent's decision vector (reference; do not modify it)
     * @param p2 the second parent's decision vector (reference; do not modify it)
     * @param R  random number generator
     * @return offsprings' decision vectors packed into 2D matrix (the first element is the first offspring; the second
     * element is the second offspring)
     */
    public double[][] reproduce(double[] p1, double[] p2, IRandom R)
    {
        double[] o1 = _c.crossover(p1, p2, R)._o1;
        double[] o2 = _c.crossover(p1, p2, R)._o2;
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
        return new double[][]{o1, o2};
    }
}
