package problem.moo.wfg.transformations;


/**
 * Flat region transformation.
 *
 * @author MTomczyk
 */
public class b_flat extends AbstractTransformation implements ITransformation
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
    public b_flat(double A, double B, double C, int startIdx, int stopIdx)
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
        for (int i = _startIdx; i < _stopIdx; i++) r[i] = Transformations.b_flat(r[i], _A, _B, _C);
        return r;
    }
}