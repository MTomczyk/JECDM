package reproduction.operators.crossover;

import random.IRandom;

/**
 * An implementation of {@link ICrossoverMO}. It serves as a simple wrapper to {@link ICrossover} that is run a desired
 * number of times to generate an expected number of offspring solutions.
 *
 * @author MTomczyk
 */
public class DoubleWrappingCrossover implements ICrossoverMO
{
    /**
     * No. offspring to construct (min 1; capped at 1)./
     */
    private final int _no;

    /**
     * Wrapped crossover.
     */
    private final ICrossover _c;

    /**
     * Parameterized constructor.
     *
     * @param no no. offspring to construct (min 1; capped at 1)
     * @param c  wrapped crossover
     */
    public DoubleWrappingCrossover(int no, ICrossover c)
    {
        _no = Math.max(1, no);
        _c = c;
    }

    /**
     * Method declaration for reproducing (applying crossover) two parents' double decision vectors.
     *
     * @param p1 decision vector of the first parent
     * @param p2 decision vector of the second parent
     * @param R  random number generator
     * @return an arbitrary (but constant and pre-defined) number of decision vectors(wrapped via DoubleResult class)
     */
    @Override
    public DoubleResult crossover(double[] p1, double[] p2, IRandom R)
    {
        double[][] off = new double[_no][];
        for (int i = 0; i < _no; i++)
            off[i] = _c.crossover(p1, p2, R)._o;
        return new DoubleResult(off);
    }

    /**
     * This method informs about the number of offspring solutions the reproducer will construct using two parents. The
     * number is constant.
     *
     * @return the number of offspring solutions the reproducer will construct using two parents
     */
    public int getNoOffspring()
    {
        return _no;
    }
}
