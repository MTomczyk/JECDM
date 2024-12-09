package problem.moo.wfg.transformations;

import java.util.Arrays;

/**
 * Param dependent (WFG7) transformation.
 *
 * @author MTomczyk
 */
public class b_param_WFG7 extends AbstractTransformation implements ITransformation
{
    /**
     * Technical parameter #4.
     */
    private final int _k;

    /**
     * Technical parameter #5.
     */
    private final int _l;

    /**
     * Parameterized constructor.
     *
     * @param A                 technical parameter #1
     * @param B                 technical parameter #2
     * @param C                 technical parameter #3
     * @param k                 technical parameter #4
     * @param l                 technical parameter #5
     * @param startIdx          starting index for processing
     * @param stopIdx           stopping index for processing
     */
    public b_param_WFG7(double A, double B, double C, int startIdx, int stopIdx, int k, int l)
    {
        super(A, B, C, startIdx, stopIdx);
        _k = k;
        _l = l;
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
        double[] r = input.clone();
        for (int i = _startIdx; i < _stopIdx; i++)
        {
            int stop = _k + _l;
            int L = stop - i;
            double[] a = new double[L];
            double[] w = new double[L];
            System.arraycopy(input, i, a, 0, L);
            Arrays.fill(w, 1.0d);
            double sum = Transformations.r_sum(a, w);
            r[i] = Transformations.b_paramDependant(r[i], sum, _A, _B, _C);
        }
        return r;
    }
}