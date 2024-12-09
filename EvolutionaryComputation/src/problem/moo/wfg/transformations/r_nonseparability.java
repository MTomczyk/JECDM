package problem.moo.wfg.transformations;


/**
 * Non-separable transformation.
 *
 * @author MTomczyk
 */
public class r_nonseparability extends AbstractTransformation implements ITransformation
{
    /**
     * Technical parameter #1.
     */
    private final int _Ai;

    /**
     * Technical parameter #2.
     */
    private final int _k;

    /**
     * Parameterized constructor.
     *
     * @param Ai                 technical parameter #1
     * @param k                 technical parameter #2
     * @param startIdx          starting index for processing
     * @param stopIdx           stopping index for processing
     */
    public r_nonseparability(int Ai, int k, int startIdx, int stopIdx)
    {
        super(0, 0, 0, startIdx, stopIdx);
        _Ai = Ai;
        _k = k;
    }

    /**
     * Executes transformation.
     *
     * @param input input vector
     * @return transformed vector (new instance)
     */
    @Override
    public double[] applyTransformation(double[] input)
    {
        double[] r = input.clone();
        for (int i = _startIdx; i < _stopIdx; i++)
        {
            int left = _k + 2 * (i - _k);
            int right = left + 1;
            double[] v = { input[left], input[right] };
            r[i] = Transformations.r_nonseparability(v, _Ai);
        }

        return r;
    }
}