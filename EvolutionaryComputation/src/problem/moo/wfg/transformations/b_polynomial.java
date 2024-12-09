package problem.moo.wfg.transformations;


/**
 * Polynomial bias transformation.
 *
 * @author MTomczyk
 */
public class b_polynomial extends AbstractTransformation implements ITransformation
{

    /**
     * Parameterized constructor.
     *
     * @param A                 technical parameter #1
     * @param startIdx          starting index for processing
     * @param stopIdx           stopping index for processing
     */
    public b_polynomial(double A, int startIdx, int stopIdx)
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
        for (int i = _startIdx; i < _stopIdx; i++) r[i] = Transformations.b_poly(r[i], _A);
        return r;
    }
}