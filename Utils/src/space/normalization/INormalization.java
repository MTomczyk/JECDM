package space.normalization;

/**
 * Interface for classes responsible for normalizing doubles.
 *
 * @author MTomczyk
 */
public interface INormalization
{
    /**
     * Normalizes input value.
     *
     * @param value input value
     * @return normalized value
     */
    double getNormalized(double value);
}
