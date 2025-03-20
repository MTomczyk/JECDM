package updater;

/**
 * Extension of {@link DataProcessor}. It employs an object implementing the auxiliary {@link IDataPointProcessor}
 * interface for processing input data points. This way, this class instance can be adjusted on the fly.
 *
 * @author MTomczyk
 */


public class DataPointsProcessor extends DataProcessor implements IDataProcessor
{
    /**
     * Data point processor responsible for processing input data points.
     */
    private final IDataPointProcessor _dataPointProcessor;

    /**
     * Default constructor.
     *
     * @param dataPointProcessor data point processor responsible for processing input data points
     */
    public DataPointsProcessor(IDataPointProcessor dataPointProcessor)
    {
        this(dataPointProcessor, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param dataPointProcessor data point processor responsible for processing input data points
     * @param cumulative         if true: current data captures all data collected throughout subsequent calls; false = represents only the most recently submitted data.
     */
    public DataPointsProcessor(IDataPointProcessor dataPointProcessor, boolean cumulative)
    {
        this(dataPointProcessor, cumulative, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param dataPointProcessor data point processor responsible for processing input data points
     * @param cumulative         if true: current data captures all data collected throughout subsequent calls; false =
     *                           represents only the most recently submitted data.
     * @param interlaceNulls     if true: The list elements will be interlaced by nulls (i.e., one double[][], one null,
     *                           and so on); this flag may be helpful when the intention is to illustrate line segments (nulls are considered breaks in lines)
     */
    public DataPointsProcessor(IDataPointProcessor dataPointProcessor, boolean cumulative, boolean interlaceNulls)
    {
        this(dataPointProcessor, cumulative, Integer.MAX_VALUE, interlaceNulls);
    }

    /**
     * Parameterized constructor.
     *
     * @param dataPointProcessor data point processor responsible for processing input data points
     * @param cumulative         if true: current data captures all data collected throughout subsequent calls; false =
     *                           represents only the most recently submitted data.
     * @param cumulativeLimit    limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest
     *                           entry is removed; in the case when the flag "interlace nulls" is true,
     *                           the last two oldest entries are removed (without checking if a single removal would be
     *                           enough to satisfy the threshold limit)
     */
    public DataPointsProcessor(IDataPointProcessor dataPointProcessor, boolean cumulative, int cumulativeLimit)
    {
        this(dataPointProcessor, cumulative, cumulativeLimit, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param dataPointProcessor data point processor responsible for processing input data points
     * @param cumulative         if true: current data captures all data collected throughout subsequent calls; false =
     *                           represents only the most recently submitted data.
     * @param cumulativeLimit    limit for the accumulated data arrays. In the case of exceeding the threshold, the oldest
     *                           entry is removed; in the case when the flag "interlace nulls" is true,
     *                           the last two oldest entries are removed (without checking if a single removal would be
     *                           enough to satisfy the threshold limit)
     * @param interlaceNulls     if true: The list elements will be interlaced by nulls (i.e., one double[][], one null,
     *                           and so on); this flag may be helpful when the intention is to illustrate line segments
     *                           (nulls are considered breaks in lines)
     */
    public DataPointsProcessor(IDataPointProcessor dataPointProcessor, boolean cumulative, int cumulativeLimit, boolean interlaceNulls)
    {
        super(new Params(cumulative, cumulativeLimit, interlaceNulls));
        _dataPointProcessor = dataPointProcessor;
    }

    /**
     * Custom implementation. It processes each data point in the input matrix using
     * {@link DataPointsProcessor#_dataPointProcessor} (returns immediately null if the input is null).
     *
     * @param sourceData source data
     * @return processed data
     */
    @Override
    protected double[][] getProcessedData(double[][] sourceData)
    {
        if (sourceData == null) return null;
        for (int i = 0; i < sourceData.length; i++)
            sourceData[i] = _dataPointProcessor.processPoint(sourceData[i]);
        return sourceData;
    }
}
