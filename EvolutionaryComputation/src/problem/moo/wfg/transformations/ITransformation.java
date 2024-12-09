package problem.moo.wfg.transformations;

/**
 * Provides interfaces for transformations used in WFG tests.
 *
 * @author MTomczyk
 */
public interface ITransformation
{
    /**
     * Executes transformation.
     *
     * @param input input vector
     * @return transformed vector (new instance)
     */
    double[] applyTransformation(double[] input);
}

