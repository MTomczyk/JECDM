package updater;

/**
 * Simple interface for processing a single data point by {@link AbstractDataProcessor}.
 *
 * @author MTomczyk
 */
public interface IDataPointProcessor
{
    /**
     * Signature of the main method for processing a data point.
     *
     * @param p input data point
     * @return output data point
     */
    double[] processPoint(double[] p);
}
