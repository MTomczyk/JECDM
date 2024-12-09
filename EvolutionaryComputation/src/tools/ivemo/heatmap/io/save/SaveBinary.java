package tools.ivemo.heatmap.io.save;

import plot.heatmap.utils.Coords;
import tools.ivemo.heatmap.AbstractHeatmapProcessor;
import tools.ivemo.heatmap.io.ISave;
import tools.ivemo.heatmap.io.params.PlotParams;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Generates binary data files for heatmap processing.
 *
 * @author MTomczyk
 */

public class SaveBinary implements ISave
{
    /**
     * If true, prints processing status.
     */
    protected boolean _notify;

    /**
     * Plot params.
     */
    protected PlotParams[] _plotParams;

    /**
     * Heatmap processors (it is assumed that they finished processing and all the data is available).
     */
    protected AbstractHeatmapProcessor[] _heatmapProcessors;

    /**
     * Parameterized constructor.
     *
     * @param plotParams       plot params
     * @param heatmapProcessor heatmap processor
     */
    public SaveBinary(PlotParams plotParams, AbstractHeatmapProcessor heatmapProcessor)
    {
        this(new PlotParams[]{plotParams}, new AbstractHeatmapProcessor[]{heatmapProcessor}, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param plotParams       plot params
     * @param heatmapProcessor heatmap processor
     * @param notify           if true, prints processing status
     */
    public SaveBinary(PlotParams plotParams, AbstractHeatmapProcessor heatmapProcessor, boolean notify)
    {
        this(new PlotParams[]{plotParams}, new AbstractHeatmapProcessor[]{heatmapProcessor}, notify);
    }

    /**
     * Parameterized constructor.
     *
     * @param plotParams        plot params
     * @param heatmapProcessors heatmap processors
     */
    public SaveBinary(PlotParams[] plotParams, AbstractHeatmapProcessor[] heatmapProcessors)
    {
        this(plotParams, heatmapProcessors, false);
    }

    /**
     * Parameterized constructor.
     *
     * @param plotParams        plot params
     * @param heatmapProcessors heatmap processors
     * @param notify            if true, prints processing status
     */
    public SaveBinary(PlotParams[] plotParams, AbstractHeatmapProcessor[] heatmapProcessors, boolean notify)
    {
        _plotParams = plotParams;
        _heatmapProcessors = heatmapProcessors;
        _notify = notify;
    }

    /**
     * Can be called to save the XML init file.
     *
     * @param path path (relative to the jar)
     * @throws Exception exception
     */
    @Override
    public void save(String path) throws Exception
    {
        if (_plotParams == null) throw new Exception("No plot params provided");
        if (_heatmapProcessors == null) throw new Exception("No heatmap processors provided");
        if (_plotParams.length != _heatmapProcessors.length)
            throw new Exception("No plot params differs from no heatmap processors");

        if (_notify) System.out.println("Saving started");

        for (int i = 0; i < _plotParams.length; i++)
        {
            if (_notify) System.out.println("Processing #" + i + " data set");
            if (_plotParams[i] == null) throw new Exception("Plot params no. #" + (i + 1) + " is null");
            if (_heatmapProcessors[i] == null) throw new Exception("Heatmap processor no. #" + (i + 1) + " is null");
            processDataSet(path, i);
        }
    }

    /**
     * Auxiliary method processing a single data set.
     *
     * @param path path (relative to the jar)
     * @param i    data set index
     * @throws Exception exception
     */
    @SuppressWarnings("ExtractMethodRecommender")
    private void processDataSet(String path, int i) throws Exception
    {
        if ((_plotParams[i]._dimensions != 2) && (_plotParams[i]._dimensions != 3))
            throw new Exception("No. dimensions for #" + (i + 1) + " data set is invalid");

        // COORDS AND SORTED VALUES
        Coords[] coords = _heatmapProcessors[i].getSortedCoords();
        double[] sortedValues = _heatmapProcessors[i].getSortedValues()._sortedValues;

        if (coords == null) throw new Exception("No. coords are constructed");
        if (sortedValues == null) throw new Exception("No. sorted values are constructed");
        if (coords.length != sortedValues.length) throw new Exception("No. coords does not equal no. sorted values");

        int noEntries = coords.length;
        int noBytes;

        if (_plotParams[i]._dimensions == 2)
        {
            // 1 byte for no. dimensions
            // 4 bytes for the noEntries
            // (4 (bytes for int) * 2 (dimensions) + 8 bytes for double (value))  * noEntries = 16 bytes * noEntries
            noBytes = 5 + 16 * noEntries;
        }
        else
        {
            // 1 byte for no. dimensions
            // 4 bytes for the noEntries
            // (4 (bytes for int) * 3 (dimensions) + 8 bytes for double (value))  * noEntries = 20 bytes * noEntries
            noBytes = 5 + 20 * noEntries;
        }

        // byte order

        byte[] bytes = new byte[noBytes];

        if (_notify) System.out.println("No dimensions = " + _plotParams[i]._dimensions);
        if (_notify) System.out.println("No entries = " + noEntries);
        if (_notify) System.out.println("No bytes = " + noBytes);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.put((byte) (_plotParams[i]._dimensions & 0xff));
        buffer.putInt(noEntries);

        for (Coords c : coords)
        {
            buffer.putInt(c._x);
            buffer.putInt(c._y);
            if (_plotParams[i]._dimensions == 3) buffer.putInt(c._z);
            buffer.putDouble(c.getValue());
        }

        // Save to file
        FileOutputStream fos = new FileOutputStream(path + _plotParams[i]._fileName + ".hm");
        fos.write(bytes);
        fos.close();
    }


}

