package problem.moo.wfg.transformations;


/**
 * Deceptive transformation.
 *
 * @author MTomczyk
 */
public class s_deceptive extends AbstractTransformation implements ITransformation
{
    /**
     * Parameterized constructor.
     *
     * @param A                 technical parameter #1
     * @param B                 technical parameter #2
     * @param C                 technical parameter #3
     * @param startIdx          starting index for processing
     * @param stopIdx           stopping index for processing
     */
    public s_deceptive(double A, double B, double C, int startIdx, int stopIdx)
    {
        super(A, B, C, startIdx, stopIdx);
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
            r[i] = Transformations.s_deceptive(r[i], _A, _B, _C);
        return r;
    }
}

