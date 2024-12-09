package alternative;

/**
 * Simple interface for alternatives wrappers.
 *
 * @author MTomczyk
 */
public interface IAlternativeWrapper
{
    /**
     * Getter for the alternative.
     * @return alternative
     */
    Alternative getAlternative();

    /**
     * Returns alternative's name.
     * @return name
     */
    String getName();

    /**
     * Getter for the performance vector (important note: reference is returned, not a clone).
     *
     * @return performance vector (important note: reference is returned, not a clone)
     */
    double[] getPerformanceVector();
}
