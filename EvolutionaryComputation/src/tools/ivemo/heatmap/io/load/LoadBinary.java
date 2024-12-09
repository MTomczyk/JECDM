package tools.ivemo.heatmap.io.load;

import plot.heatmap.utils.Coords;
import tools.ivemo.heatmap.io.ILoad;
import tools.ivemo.heatmap.io.params.PlotParams;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Loads binary data files for heatmap processing.
 *
 * @author MTomczyk
 */

public class LoadBinary implements ILoad
{
    /**
     * Loaded heatmap data (field is instantiated after executing the main loading method).
     * The first dimension refers to data linked to different plots (data sets).
     * The second dimension is an array of coordinate-data (heatmap entry points) that are assumed to be sorted in
     * ascending order of their values.
     */
    public Coords[][] _loadedHeatmapData = null;

    /**
     * If true, prints processing status.
     */
    protected boolean _notify;

    /**
     * Plot params.
     */
    protected PlotParams[] _plotParams;


    /**
     * Parameterized constructor.
     *
     * @param plotParams plot params
     */
    public LoadBinary(PlotParams plotParams)
    {
        this(new PlotParams[]{plotParams});
    }

    /**
     * Parameterized constructor.
     *
     * @param plotParams plot params
     * @param notify     if true, prints processing status
     */
    public LoadBinary(PlotParams plotParams, boolean notify)
    {
        this(new PlotParams[]{plotParams}, notify);
    }

    /**
     * Parameterized constructor.
     *
     * @param plotParams plot params
     */
    public LoadBinary(PlotParams[] plotParams)
    {
        this(plotParams, false);
    }


    /**
     * Parameterized constructor.
     *
     * @param plotParams plot params
     * @param notify     if true, prints processing status
     */
    public LoadBinary(PlotParams[] plotParams, boolean notify)
    {
        _plotParams = plotParams;
        _notify = notify;
    }


    /**
     * Can be called to save the XML init file.
     *
     * @param path path (relative to the jar)
     * @throws Exception exception
     */
    @Override
    public void load(String path) throws Exception
    {
        if (_plotParams == null) throw new Exception("No plot params provided");

        if (_notify) System.out.println("Loading started");

        _loadedHeatmapData = new Coords[_plotParams.length][];

        for (int i = 0; i < _plotParams.length; i++)
        {
            if (_notify) System.out.println("Processing #" + i + " data set");

            if (_plotParams[i] == null) throw new Exception("Plot params no. #" + (i + 1) + " is null");
            Coords[] loadedData = processDataSet(path, i);
            _loadedHeatmapData[i] = loadedData;
        }
    }

    /**
     * Auxiliary method processing a single data set.
     *
     * @param path path (relative to the jar)
     * @param i    data set index
     * @return buckets coords data
     * @throws Exception exception
     */
    private Coords[] processDataSet(String path, int i) throws Exception
    {
        if ((_plotParams[i]._dimensions != 2) && (_plotParams[i]._dimensions != 3))
            throw new Exception("No. dimensions for #" + (i + 1) + " data set is invalid");

        File file = new File(path + _plotParams[i]._fileName + ".hm");
        FileInputStream in = new FileInputStream(file);
        byte[] data = in.readAllBytes();
        if (_notify) System.out.println("No bytes = " + data.length);

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.BIG_ENDIAN);

        byte dimensions = buffer.get();
        if (_notify) System.out.println("No dimensions = " + dimensions);

        if ((dimensions != 2) && (dimensions != 3))
            throw new Exception("No. dimensions stored in the binary file for #" + (i + 1) + " data set is invalid");

        int noEntries = buffer.getInt();
        if (_notify) System.out.println("No entries = " + noEntries);

        Coords[] coords = new Coords[noEntries];

        for (int e = 0; e < noEntries; e++)
        {
            int x = buffer.getInt();
            if ((x < 0) || (x >= _plotParams[i]._xAxisDivisions))
                throw new Exception("Heatmap data point x-coordinate in the binary file for #" + (i + 1) + " data set is out of bounds of [0;" + (_plotParams[i]._xAxisDivisions - 1) + "]");

            int y = buffer.getInt();
            if ((y < 0) || (y >= _plotParams[i]._yAxisDivisions))
                throw new Exception("Heatmap data point y-coordinate in the binary file for #" + (i + 1) + " data set is out of bounds of [0;" + (_plotParams[i]._yAxisDivisions - 1) + "]");

            int z = 0;
            if (dimensions == 3)
            {
                z = buffer.getInt();
                if ((z < 0) || (z >= _plotParams[i]._zAxisDivisions))
                    throw new Exception("Heatmap data point z-coordinate in the binary file for #" + (i + 1) + " data set is out of bounds of [0;" + (_plotParams[i]._zAxisDivisions - 1) + "]");

            }
            double v = buffer.getDouble();
            if ((v < _plotParams[i]._heatmapDisplayRange.getLeft()) || (v > _plotParams[i]._heatmapDisplayRange.getRight()))
                throw new Exception("Heatmap data point value in the binary file for #" + (i + 1) + " data set is out of bounds of [" + _plotParams[i]._heatmapDisplayRange.getLeft() + ", " + (_plotParams[i]._heatmapDisplayRange.getRight()) + "]");

            coords[e] = new Coords(x, y, z, v);
        }

        in.close();

        return coords;
    }


}

