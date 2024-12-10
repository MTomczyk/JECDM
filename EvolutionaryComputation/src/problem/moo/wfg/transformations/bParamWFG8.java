package problem.moo.wfg.transformations;


import java.util.Arrays;

/**
 * Param dependent (WFG8) transformation.
 *
 * @author MTomczyk
 */
public class bParamWFG8 extends AbstractTransformation implements ITransformation
{

    /**
     * Parameterized constructor.
     *
     * @param A        technical parameter #1
     * @param B        technical parameter #2
     * @param C        technical parameter #3
     * @param startIdx starting index for processing
     * @param stopIdx  stopping index for processing
     */
    public bParamWFG8(double A, double B, double C, int startIdx, int stopIdx)
    {
        super(A, B, C, startIdx, stopIdx);
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
            int start = 0;
            int stop = i - 1;
            int L = stop - start;
            double[] a = new double[L];
            double[] w = new double[L];
            System.arraycopy(input, start, a, 0, L);
            Arrays.fill(w, 1.0d);
            double sum = Transformations.r_sum(a, w);
            r[i] = Transformations.b_paramDependant(r[i], sum, _A, _B, _C);
        }
        return r;
    }
}