package problem.moo.wfg.transformations;


/**
 * WFG1 final transformation.
 *
 * @author MTomczyk
 */
public class WFG1Final extends AbstractTransformation implements ITransformation
{
    /**
     * The number of objectives.
     */
    private final int _M;

    /**
     * The number of position-related parameters
     */
    private final int _k;


    /**
     * Parameterized constructor.
     *
     * @param M                 the number of objectives
     * @param k                 the number of position-related parameters
     */
    public WFG1Final(int M, int k)
    {
        super(0, 0, 0, 0, 0);
        _k = k;
        _M = M;
        assert _k % (_M - 1) == 0;
    }

    /**
     * Executes transformation.
     *
     * @param input input vector
     * @return transformed vector (new instance)
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public double[] applyTransformation(double[] input)
    {
        double[] r = new double[_M];

        double[] w = new double[input.length];
        for (int i = 1; i <= input.length; i++)
            w[i - 1] = 2.0d * i;

        for (int i = 1; i <= _M - 1; i++)
        {
            int start = (i - 1) * _k / (_M - 1);
            int stop = i * _k / (_M - 1);
            int L = stop - start;
            double[] c = new double[L];
            System.arraycopy(input, start, c, 0, L);
            double[] pw = new double[L];
            System.arraycopy(w, start, pw, 0, L);
            r[i - 1] = Transformations.r_sum(c, pw);
        }

        {
            double[] c = new double[input.length - _k];
            System.arraycopy(input, _k, c, 0, input.length - _k);
            double[] pw = new double[input.length - _k];
            System.arraycopy(w, _k, pw, 0, input.length - _k);
            r[_M - 1] = Transformations.r_sum(c, pw);
        }

        return r;
    }
}
