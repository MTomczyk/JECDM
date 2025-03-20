package tools.ivemo.heatmap;

import ea.EA;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;
import tools.ivemo.heatmap.utils.BucketData;

/**
 * Extension of {@link AbstractHeatmap} class for generating and maintaining robust heatmap-based 3D visualizations.
 * of the performance of EMOAs.
 *
 * @author MTomczyk
 */


public class Heatmap3DProcessor extends AbstractHeatmapProcessor
{
    /**
     * Params container.
     */
    public static class Params extends AbstractHeatmapProcessor.Params
    {
        /**
         * Z-axis display range used when dividing the objective space into buckets on the Z-axis.
         */
        public Range _zAxisDisplayRange = null;

        /**
         * Number of divisions on the Z-axis (used to construct buckets).
         */
        public int _zAxisDivisions = 100;
    }

    /**
     * Z-axis display range used when dividing the objective space into buckets on the Z-axis.
     */
    protected Range _zAxisDisplayRange;

    /**
     * Number of divisions on the Z-axis (used to construct buckets).
     */
    protected int _zAxisDivisions;

    /**
     * Parameterized constructor.
     *
     * @param p params container.
     */
    public Heatmap3DProcessor(Params p)
    {
        super(p);
        _zAxisDivisions = p._zAxisDivisions;
        _zAxisDisplayRange = p._zAxisDisplayRange;
    }

    /**
     * Auxiliary method that instantiates the dimensionality of the objective space.
     */
    @Override
    public void instantiateDimensionality()
    {
        _dimensions = 3;
    }


    /**
     * Auxiliary method instantiating bucket coordinate object (responsible for translating input data points into bucket coordinates).
     *
     * @param p params container.
     */
    @Override
    protected void instantiateBucketCoords(AbstractHeatmapProcessor.Params p)
    {
        Params pp = (Params) p;
        _BC = new BucketCoordsTransform(3, new int[]{p._xAxisDivisions, p._yAxisDivisions, pp._zAxisDivisions},
                new Range[]{p._xAxisDisplayRange, p._yAxisDisplayRange, pp._zAxisDisplayRange}, new LinearlyThresholded());
    }

    /**
     * Auxiliary method for instantiating data.
     */
    protected void instantiateData()
    {
        _data = new double[_zAxisDivisions][_yAxisDivisions][_xAxisDivisions];
        _dataForAggregation = new BucketData[_zAxisDivisions][_yAxisDivisions][_xAxisDivisions];

        int arraySize = (int) Math.sqrt(_trials);
        if (arraySize < 5) arraySize = 5;

        for (int z = 0; z < _zAxisDivisions; z++)
            for (int y = 0; y < _yAxisDivisions; y++)
                for (int x = 0; x < _xAxisDivisions; x++)
                    _dataForAggregation[z][y][x] = new BucketData(arraySize);
    }

    /**
     * Auxiliary method for instantiating (getter) trial data (to be overwritten).
     */
    @SuppressWarnings("DuplicatedCode")
    protected BucketData[][][] getBucketTrialData(EA ea)
    {
        BucketData[][][] btd = new BucketData[_zAxisDivisions][_yAxisDivisions][_xAxisDivisions];
        int arraySize = (int) Math.pow(ea.getPopulationSize(), 1.0d / 3.0d);
        if (arraySize < 5) arraySize = 5; // arbitrarily set lower threshold for the in-array size

        for (int z = 0; z < _zAxisDivisions; z++)
            for (int y = 0; y < _yAxisDivisions; y++)
                for (int x = 0; x < _xAxisDivisions; x++)
                    btd[z][y][x] = new BucketData(arraySize);
        return btd;
    }

    /**
     * Can be called after {@link #executeProcessing} to pre-process data to be stored.
     */
    @Override
    public void generateSortedInputData()
    {
        if (_data == null) return;
        _SC = _HDP.getCoords3D(_xAxisDivisions, _yAxisDivisions, _zAxisDivisions, _data);
        _SV = _HDP.getSortedValues(_SC, 3, false);
    }
}
