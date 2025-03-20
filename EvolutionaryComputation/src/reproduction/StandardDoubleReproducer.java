package reproduction;

import random.IRandom;
import reproduction.operators.crossover.ICrossover;
import reproduction.operators.mutation.IMutate;
import reproduction.valuecheck.IValueCheck;

/**
 * Class assisting in performing standard reproduction using two parents' decision vectors (of doubles; see the
 * {@link StandardDoubleReproducer#reproduce(double[], double[], IRandom)} method). First, it constructs the offspring
 * vector using the reproduction operator. If the mutation operator is supplied, it is used then to mutate the offspring
 * vector. Lastly, if the value check object is supplied, it is applied to the offspring vector.
 *
 * @author MTomczyk
 */
public class StandardDoubleReproducer extends AbstractStandardReproducer
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
     * @param c           crossover operator
     * @param m           mutation operator (can be null; not used then)
     * @param vc          object for checking if the resulting variable are in valid bounds (can be null; not used then)
     * @param vLowerBound lower bound for the variables' values
     * @param vUpperBound upper bound for the variables' values
     */
    public StandardDoubleReproducer(ICrossover c, IMutate m, IValueCheck vc, double vLowerBound, double vUpperBound)
    {
        super(c, m, vc);
        _vLowerBound = vLowerBound;
        _vUpperBound = vUpperBound;
    }

    /**
     * Performs the reproduction using two parents' decision vectors (of doubles). First, the offspring vector is
     * constructed using the reproduction operator. If the mutation operator is supplied, it is used then to mutate
     * the offspring vector. Lastly, if the value check object is supplied, it is applied to the offspring vector.
     *
     * @param p1 the first parent's decision vector
     * @param p2 the second parent's decision vector
     * @param R  random number generator
     * @return offspring's decision vector
     */
    public double[] reproduce(double[] p1, double[] p2, IRandom R)
    {
        double[] o = _c.crossover(p1, p2, R);
        if (_m != null) _m.mutate(o, R);
        if (_vc != null) _vc.checkAndCorrect(o, _vLowerBound, _vUpperBound);
        return o;
    }
}
