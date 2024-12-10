package problem.moo.wfg.transformations;

/**
 * Param-dependent transformation.
 *
 * @author MTomczyk
 */
public class bParamDependant extends AbstractTransformation implements ITransformation
{
    /**
     * Technical parameter #4.
     */
    private final double _u;

    /**
     * Parameterized constructor.
     *
     * @param A                 technical parameter #1
     * @param B                 technical parameter #2
     * @param C                 technical parameter #3
     * @param u                 technical parameter #4
     * @param startIdx          starting index for processing
     * @param stopIdx           stopping index for processing
     */
    public bParamDependant(double A, double B, double C, double u, int startIdx, int stopIdx)
    {
        super(A, B, C, startIdx, stopIdx);
        _u = u;
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
        for (int i = _startIdx; i < _stopIdx; i++) r[i] = Transformations.b_paramDependant(r[i], _A, _B, _C, _u);
        return r;
    }
}