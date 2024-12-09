package problem.moo.wfg.transformations;


/**
 * Linear transformation.
 *
 * @author MTomczyk
 */
public class s_linear extends AbstractTransformation implements ITransformation
{

    /**
     * Parameterized constructor.
     *
     * @param A                 technical parameter #1
     * @param startIdx          starting index for processing
     * @param stopIdx           stopping index for processing
     */
    public s_linear(double A, int startIdx, int stopIdx)
    {
        super(A, 0, 0, startIdx, stopIdx);
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
        for (int i = _startIdx; i < _stopIdx; i++) r[i] = Transformations.s_linear(r[i], _A);
        return r;
    }
}

