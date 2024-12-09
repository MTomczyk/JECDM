package tools.ivemo.heatmap;

import ea.EA;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;
import tools.ivemo.heatmap.utils.BucketData;

/**
 * Extension of {@link AbstractHeatmap} class for generating and maintaining robust heatmap-based 2D visualizations.
 * of the performance of EMOAs.
 *
 * @author MTomczyk
 */


public class Heatmap2DProcessor extends AbstractHeatmapProcessor
{
    /**
     * Params container.
     */
    public static class Params extends AbstractHeatmapProcessor.Params
    {

    }

    /**
     * Parameterized constructor.
     *
     * @param p params container.
     */
    public Heatmap2DProcessor(Params p)
    {
        super(p);
    }

    /**
     * Auxiliary method instantiating bucket coordinate object (responsible for translating input data points into bucket coordinates).
     *
     * @param p params container.
     */
    @Override
    protected void instantiateBucketCoords(AbstractHeatmapProcessor.Params p)
    {
        _BC = new BucketCoordsTransform(2, new int[]{p._xAxisDivisions, p._yAxisDivisions},
                new Range[]{p._xAxisDisplayRange, p._yAxisDisplayRange}, new LinearlyThresholded());
    }

    /**
     * Auxiliary method for instantiating data.
     */
    protected void instantiateData()
    {
        _data = new double[1][_yAxisDivisions][_xAxisDivisions];
        _dataForAggregation = new BucketData[1][_yAxisDivisions][_xAxisDivisions];

        int arraySize = (int) Math.sqrt(_trials);
        if (arraySize < 5) arraySize = 5;

        for (int y = 0; y < _yAxisDivisions; y++)
            for (int x = 0; x < _xAxisDivisions; x++)
                _dataForAggregation[0][y][x] = new BucketData(arraySize);
    }

    /**
     * Auxiliary method for instantiating (getter) trial data (to be overwritten).
     */
    protected BucketData[][][] getBucketTrialData(EA ea)
    {
        BucketData[][][] btd = new BucketData[1][_yAxisDivisions][_xAxisDivisions];
        int arraySize = (int) Math.sqrt(ea.getPopulationSize());
        if (arraySize < 5) arraySize = 5; // arbitrarily set lower threshold for the in-array size

        for (int y = 0; y < _yAxisDivisions; y++)
            for (int x = 0; x < _xAxisDivisions; x++)
                btd[0][y][x] = new BucketData(arraySize);
        return btd;
    }
}
