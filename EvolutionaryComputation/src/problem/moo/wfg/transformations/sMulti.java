package problem.moo.wfg.transformations;

/**
 * Multimodal transformation.
 *
 * @author MTomczyk
 */
public class sMulti extends AbstractTransformation implements ITransformation
{
    /**
     * Technical parameter #1.
     */
    private final int _Ai;

    /**
     * Parameterized constructor.
     *
     * @param Ai       technical parameter #1
     * @param B        technical parameter #2
     * @param C        technical parameter #3
     * @param startIdx starting index for processing
     * @param stopIdx  stopping index for processing
     */
    public sMulti(int Ai, double B, double C, int startIdx, int stopIdx)
    {
        super(0, B, C, startIdx, stopIdx);
        _Ai = Ai;
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
            r[i] = Transformations.s_multi(r[i], _Ai, _B, _C);
        return r;
    }
}

