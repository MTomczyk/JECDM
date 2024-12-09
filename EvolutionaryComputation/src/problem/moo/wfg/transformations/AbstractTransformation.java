package problem.moo.wfg.transformations;

/**
 * Provides common fields/functions for implementations of {@link ITransformation}.
 *
 * @author MTomczyk
 */


public abstract class AbstractTransformation implements ITransformation
{
    /**
     * Technical parameter #1.
     */
    protected final double _A;

    /**
     * Technical parameter #2.
     */
    protected final double _B;

    /**
     * Technical parameter #3.
     */
    protected final double _C;

    /**
     * Starting index for processing.
     */
    protected final int _startIdx;

    /**
     * Stopping index for processing.
     */
    protected final int _stopIdx;


    /**
     * Parameterized constructor.
     * @param A technical parameter #1
     * @param B technical parameter #2
     * @param C technical parameter #3
     * @param startIdx starting index for processing
     * @param stopIdx stopping index for processing
     */
    public AbstractTransformation(double A, double B, double C, int startIdx, int stopIdx)
    {
        _A = A;
        _B = B;
        _C = C;
        _startIdx = startIdx;
        _stopIdx = stopIdx;
    }

    /**
     * Executes transformation (to be implemented).
     *
     * @param input input vector
     * @return transformed vector (new instance)
     */
    @Override
    public double[] applyTransformation(double[] input)
    {
        return new double[0];
    }
}
