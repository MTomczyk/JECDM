package emo;

import dataset.IDataSet;
import drmanager.DisplayRangesManager;
import ea.EA;
import frame.Frame;
import plot.Plot2D;
import plot.Plot3D;
import space.Range;
import updater.DataProcessor;
import updater.DataUpdater;
import updater.IDataProcessor;
import updater.IDataSource;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.EASource;

/**
 * Utils class that provides a common code for all test cases.
 *
 * @author MTomczyk
 */
public class Utils
{
    /**
     * Creates the instance of the visualization module.
     *
     * @param frame            frame containing the plot
     * @param ea               instance of the evolutionary algorithm
     * @param ds               data set
     * @param cumulativeUpdate if true -> cumulative mode is on
     * @return visualization module
     */
    public static IVisualization getVisualization(Frame frame, EA ea, IDataSet ds, boolean cumulativeUpdate)
    {
        IDataSource source = new EASource(ea, cumulativeUpdate);
        IDataProcessor processor = new DataProcessor(cumulativeUpdate);

        DataUpdater updater;
        try
        {
            updater = DataUpdater.getSimpleDataUpdater(frame.getModel().getPlotsWrapper(), source, processor, ds);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return new Visualization(frame, updater);
    }

    /**
     * Creates Plot 2D.
     *
     * @param displayRanges    initial display ranges
     * @param cumulativeUpdate true -> cumulative mode is on
     * @return plot 2D instance
     */
    public static Plot2D getPlot2D(Range[] displayRanges, boolean cumulativeUpdate)
    {
        Plot2D.Params params = new Plot2D.Params();
        params._xAxisTitle = "f1";
        params._yAxisTitle = "f2";
        if (cumulativeUpdate)
        {
            params._pDisplayRangesManager = new DisplayRangesManager.Params();
            params._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
            params._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(displayRanges[0], false, false);
            params._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(displayRanges[1], false, false);
            params._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true, false);
            params._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};
        }
        else params._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(displayRanges[0], displayRanges[1]);

        return new Plot2D(params);
    }

    /**
     * Creates Plot 3D.
     *
     * @param displayRanges    initial display ranges
     * @param cumulativeUpdate true -> cumulative mode is on
     * @return plot 2D instance
     */
    public static Plot3D getPlot3D(Range[] displayRanges, boolean cumulativeUpdate)
    {
        Plot3D.Params params = new Plot3D.Params();
        params._xAxisTitle = "f1";
        params._yAxisTitle = "f2";
        params._zAxisTitle = "f3";
        if (cumulativeUpdate)
        {
            params._pDisplayRangesManager = new DisplayRangesManager.Params();
            params._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
            params._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(displayRanges[0], false, false);
            params._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(displayRanges[1], false, false);
            params._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(displayRanges[2], false, false);
            params._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true, false);
            params._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};
        }
        else
            params._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(displayRanges[0], displayRanges[1], displayRanges[2]);

        return new Plot3D(params);
    }
}
