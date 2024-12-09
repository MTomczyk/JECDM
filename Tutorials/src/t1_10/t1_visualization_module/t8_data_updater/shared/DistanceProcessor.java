package t1_10.t1_visualization_module.t8_data_updater.shared;

import updater.AbstractDataProcessor;
import updater.IDataProcessor;

/**
 * Simple data processor that appends to input data points their distances to expected mean.
 *
 * @author MTomczyk
 */
public class DistanceProcessor extends AbstractDataProcessor implements IDataProcessor
{
    /**
     * Space dimensionality.
     */
    private final int _m;

    /**
     * Means (i-th element is linked to i-th dimension).
     */
    private final double[] _means;

    /**
     * Parameterized constructor.
     *
     * @param m     space dimensionality
     * @param means means (i-th element is linked to i-th dimension)
     */
    public DistanceProcessor(int m, double[] means)
    {
        super(new Params(true)); // true = use the cumulative mode
        _m = m;
        _means = means;
    }

    /**
     * Appends to input data points their distances to expected mean.
     *
     * @param sourceData source data
     * @return processed data
     */
    @Override
    protected double[][] getProcessedData(double[][] sourceData)
    {
        double[][] processed = new double[sourceData.length][_m + 1];
        double d;
        for (int i = 0; i < sourceData.length; i++)
        {
            double sum = 0.0d;
            for (int j = 0; j < _m; j++)
            {
                processed[i][j] = sourceData[i][j];
                d = sourceData[i][j] - _means[j];
                sum += d * d;
            }
            processed[i][_m] = Math.sqrt(sum);
        }
        return processed;
    }
}
