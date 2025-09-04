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

    /**
     * Constructs cloned object (to be suitably implemented).
     *
     * @return cloned object
     */
    default INormalization getClone()
    {
        return null;
    }

    /**
     * Returns a clone of the input array (null entries are passed as nulls).
     *
     * @param normalizations input array
     * @return cloned array (null, if the input is null)
     */
    static INormalization[] getCloned(INormalization[] normalizations)
    {
        if (normalizations == null) return null;
        INormalization[] result = new INormalization[normalizations.length];
        for (int i = 0; i < normalizations.length; i++)
            result[i] = normalizations[i] == null ? null : normalizations[i].getClone();
        return result;
    }
}
