package scheme.referencevalue;

/**
 * Interface for classes responsible for providing reference value used when calculating relative locations/dimensions/etc.
 */
public interface IReferenceValueGetter
{
    /**
     * Returns the reference value used when calculating relative locations/dimensions/etc.
     *
     * @return the reference value used when calculating relative locations/dimensions/etc.
     */
    float getReferenceValue();
}
