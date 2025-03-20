package plot.heatmap;

import drmanager.DisplayRangesManager;
import plot.heatmap.utils.Coords;
import plot.heatmap.utils.HeatmapDataProcessor;
import swing.swingworkerqueue.QueuedSwingWorker;

import java.util.concurrent.ExecutionException;


/**
 * Swing worker object responsible for updating the heatmap data.
 *
 * @author MTomczyk
 */
class HeatmapUpdater extends QueuedSwingWorker<Void, Void>
{
    /**
     * Heatmap layer model.
     */
    private final HeatmapLayerModel _layerModel;

    /**
     * No. dimensions. Use 2 for 2D visualization and 3 for 3D.
     */
    private final int _dimensions;

    /**
     * No. divisions on the X-axis.
     */
    private final int _xDiv;

    /**
     * No. divisions on the Y-axis.
     */
    private final int _yDiv;

    /**
     * No. divisions on the Z-axis.
     */
    private final int _zDiv;

    /**
     * Data to be processed.
     */
    private final double[][][] _data;

    /**
     * If true, the additional preprocessing is done: the buckets are sorted in ascending order of their values; imposes higher preprocessing time but improves filtering efficiency.
     */
    private final boolean _requiresPresorting;

    /**
     * Data points (buckets) represented as an array sorted based on bucket values. It is assumed that either
     * regular data matrix or this array is not null. If this array is not null, it is used for processing.
     * The use of this array is motivated by enabling more efficient data processing (but at a higher preprocessing cost, though).
     */
    private final Coords[] _sortedCoords;

    /**
     * Buckets' values sorted and stored as an array. Optionally used with ``sorted coords'' field.
     */
    private final double[] _sortedValues;

    /**
     * Parameterized constructor for 2D processing.
     *
     * @param layerModel         heatmap layer model
     * @param data               data to be processed
     * @param xDiv               no. divisions on the X-axis
     * @param yDiv               no. divisions on the Y-axis
     * @param requiresPresorting if true, the additional preprocessing is done: the buckets are sorted in ascending order of their values; imposes higher preprocessing time but improves filtering efficiency
     */
    public HeatmapUpdater(HeatmapLayerModel layerModel,
                          double[][] data,
                          int xDiv,
                          int yDiv,
                          boolean requiresPresorting)
    {
        _layerModel = layerModel;
        _dimensions = 2;
        _xDiv = xDiv;
        _yDiv = yDiv;
        _zDiv = 1;
        _data = new double[][][]{data};
        _requiresPresorting = requiresPresorting;
        _sortedCoords = null;
        _sortedValues = null;
    }

    /**
     * Parameterized constructor for 3D processing.
     *
     * @param layerModel         heatmap layer model
     * @param data               data to be processed
     * @param xDiv               no. divisions on the X-axis
     * @param yDiv               no. divisions on the Y-axis
     * @param zDiv               no. divisions on the Z-axis
     * @param requiresPresorting if true, the additional preprocessing is done: the buckets are sorted in ascending order of their values; imposes higher preprocessing time but improves filtering efficiency
     */
    public HeatmapUpdater(HeatmapLayerModel layerModel,
                          double[][][] data,
                          int xDiv,
                          int yDiv,
                          int zDiv,
                          boolean requiresPresorting)
    {
        _layerModel = layerModel;
        _dimensions = 3;
        _xDiv = xDiv;
        _yDiv = yDiv;
        _zDiv = zDiv;
        _data = data;
        _requiresPresorting = requiresPresorting;
        _sortedCoords = null;
        _sortedValues = null;
    }

    /**
     * Parameterized constructor capturing presorted buckets.
     *
     * @param layerModel   heatmap layer model
     * @param dimensions   heatmap dimensionality
     * @param xDiv         no. divisions on the X-axis
     * @param yDiv         no. divisions on the Y-axis
     * @param zDiv         no. divisions on the Z-axis
     * @param sortedCoords data points (buckets) represented as an array sorted based on bucket values; it is assumed that either regular data matrix or this array is not null; if this array is not null, it is used for processing; the use of this array is motivated by enabling more efficient data processing (but at a higher preprocessing cost, though)
     * @param sortedValues buckets' values sorted and stored as an array; optionally used with ``sorted coords'' field
     */
    public HeatmapUpdater(HeatmapLayerModel layerModel,
                          int dimensions,
                          int xDiv,
                          int yDiv,
                          int zDiv,
                          Coords[] sortedCoords,
                          double[] sortedValues)
    {
        _layerModel = layerModel;
        _dimensions = dimensions;
        _xDiv = xDiv;
        _yDiv = yDiv;
        _zDiv = zDiv;
        _data = null;
        _requiresPresorting = false;
        _sortedCoords = sortedCoords;
        _sortedValues = sortedValues;
    }

    /**
     * Method executed in the background to update display ranges.
     *
     * @return Report on the executed display ranges update.
     */
    @Override
    protected Void doInBackground()
    {
        if (!((_data != null) || ((!_requiresPresorting) && (_sortedCoords != null) && (_sortedValues != null)))) return null;
        //assert ((_data != null) || ((!_requiresPresorting) && (_sortedCoords != null) && (_sortedValues != null)));
        assert (_dimensions == 2) || (_dimensions == 3);

        DisplayRangesManager heatmapProcessedDRM = _layerModel.getHeatmapDRM().getClone();
        if (_data != null)
            for (double[][] m : _data) heatmapProcessedDRM.updateSingleDisplayRange(m, _dimensions, true); // raw data
        else heatmapProcessedDRM.updateSingleDisplayRange(new double[][]{_sortedValues}, _dimensions, true); // pre sorted

        _layerModel.getHeatmapDRM().overwriteWithValuesStoredIn(heatmapProcessedDRM);
        _layerModel.setRawData(_data);

        if (_requiresPresorting)
        {
            HeatmapDataProcessor HDP = new HeatmapDataProcessor();
            Coords[] sc;
            if (_dimensions == 2) sc = HDP.getCoords2D(_xDiv, _yDiv, _data[0]);
            else sc = HDP.getCoords3D(_xDiv, _yDiv, _zDiv, _data);
            double[] sv = HDP.getSortedValues(sc, _dimensions, false)._sortedValues;
            _layerModel.setSortedCoords(sc);
            _layerModel.setSortedValues(sv);
            _layerModel.setSortedMode(true);
        }
        else
        {
            _layerModel.setSortedMode(_sortedCoords != null);
            _layerModel.setSortedCoords(_sortedCoords);
            _layerModel.setSortedValues(_sortedValues);
        }

        notifyTermination();
        return null;
    }

    /**
     * Catches exceptions.
     */
    @Override
    public void done()
    {
        if (isCancelled()) return;
        try
        {
            get();
        } catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}
