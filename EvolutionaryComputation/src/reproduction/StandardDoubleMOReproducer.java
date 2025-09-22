package reproduction;

import random.IRandom;
import reproduction.operators.crossover.ICrossoverMO;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Class assisting in performing standard reproduction using two parents' decision vectors (of doubles; see the
 * {@link StandardDoubleMOReproducer#reproduce(double[], double[], IRandom)} method). First, it constructs an arbitrary
 * number, but constant and pre-defined, of offspring vectors using the reproduction operator. If the mutation operator
 * is supplied, it is used then to mutate the offspring vectors. Lastly, if the value check object is supplied, it is
 * applied to the offspring vectors.
 *
 * @author MTomczyk
 */
public class StandardDoubleMOReproducer extends AbstractStandardMOReproducer
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
    public StandardDoubleMOReproducer(ICrossoverMO c, IMutate m, IValueCheck vc, double vLowerBound, double vUpperBound)
    {
        super(c, m, vc);
        _vLowerBound = vLowerBound;
        _vUpperBound = vUpperBound;
    }

    /**
     * Performs the reproduction using two parents' decision vectors (of doubles). First, it constructs an arbitrary
     * number, but constant and pre-defined, of offspring vectors using the reproduction operator. If the mutation
     * operator is supplied, it is used then to mutate the offspring vector. Lastly, if the value check object is
     * supplied, it is applied to the offspring vector.
     *
     * @param p1 the first parent's decision vector (reference; do not modify it)
     * @param p2 the second parent's decision vector (reference; do not modify it)
     * @param R  random number generator
     * @return offspring's decision vector
     */
    public double[][] reproduce(double[] p1, double[] p2, IRandom R)
    {
        double[][] o = _c.crossover(p1, p2, R)._o;
        if (_m != null)
            for (double[] doubles : o) _m.mutate(doubles, R);

        if (_vc != null)
        {
            for (double[] doubles : o)
                _vc.checkAndCorrect(doubles, _vLowerBound, _vUpperBound);
        }
        return o;
    }
}
