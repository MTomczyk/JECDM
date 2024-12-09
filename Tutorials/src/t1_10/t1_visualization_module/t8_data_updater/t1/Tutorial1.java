package t1_10.t1_visualization_module.t8_data_updater.t1;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import plotswrapper.TwoPlotsHorizontally;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import t1_10.t1_visualization_module.t8_data_updater.shared.DistanceProcessor;
import t1_10.t1_visualization_module.t8_data_updater.shared.GaussianGenerator;
import updater.*;

/**
 * This tutorial shows how to construct and use data updater (i.e., {@link updater.DataUpdater}).
 *
 * @author MTomczyk
 */

public class Tutorial1
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Let's keep the plots in an array:
        AbstractPlot[] plots = new AbstractPlot[2];

        // Create the 3D plot first (id = 0).
        {
            Plot3D.Params pP = new Plot3D.Params();
            pP._xAxisTitle = "X-coordinate";
            pP._yAxisTitle = "Y-coordinate";
            pP._zAxisTitle = "Z-coordinate";
            pP._drawLegend = true;

            pP._scheme = WhiteScheme.getForPlot3D();

            // The display range params container is customized. Apart from the 3 display ranges linked to the X, Y, and Z
            // axes, a fourth display range associated with the distance is added.
            pP._pDisplayRangesManager = new DisplayRangesManager.Params();
            pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
            for (int i = 0; i < 4; i++)
                pP._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(null, true);
            pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};

            plots[0] = new Plot3D(pP);
        }

        // Now, it's time to create a parallel coordinate plot (id = 1)
        {
            ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(3);
            pP._axesTitles = new String[]{"D1", "D2", "D3"};
            pP._xAxisTitle = "Dimension";
            pP._yAxisTitle = "Coordinate";
            //pP._disableOverlappingTicks = true;
            pP._drawLegend = true;

            pP._scheme = WhiteScheme.getForPCP2D();

            // The display ranges manager must be customized to account for the distance-based processing.
            // The note provided in plot.parallelcoordinateplot.ParallelCoordinatePlot2D reads: ``the PCP allows using
            // custom display ranges, but they must be positioned explicitly between the first M display ranges
            // associated with the parallel coordinate lines and the last display range reserved for the X-axis.''
            // Hence, the display ranges should be defined as follows in this example:
            // - 0-2: display ranges associated with the parallel Y-axes.
            // - 3: display range linked to the distance.
            // - 4: display range reserved for the X-axis.
            //Note that no mapping in ``_attIds_to_drIDX'' points to 4 (the parallel coordinate plot painter
            // explicitly processes x-coordinates).
            pP._pDisplayRangesManager = new DisplayRangesManager.Params();
            pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[5];
            for (int i = 0; i < 3; i++)
                pP._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(new Range(-2.0d, 2.0d), true);
            pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true);
            pP._pDisplayRangesManager._DR[4] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false);
            pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};

            plots[1] = new ParallelCoordinatePlot2D(pP);
        }

        // One can use the wrapper below if (s)he wants to display the plots side by side (horizontal orientation).
        TwoPlotsHorizontally wrapper = new TwoPlotsHorizontally(plots[0], plots[1]);
        Frame frame = new Frame(wrapper, 0.5f, 0.5f);

        for (int i = 0; i < 2; i++)
        {
            RightClickPopupMenu menu = new RightClickPopupMenu();
            menu.addItem(new SaveAsImage());
            plots[i].getController().addRightClickPopupMenu(menu);
        }

        // The lines below establish the random number generator and two means for the Gaussian distributions
        // used by the two data sources.
        IRandom R = new MersenneTwister64(0);
        double[] means0 = new double[]{0.0d, 0.0d, 0.0d};
        double[] means1 = new double[]{1.0d, -1.0d, 0.5d};

        // It's time now to construct the data updater. First, we need to create a params container. Note that the plots
        // wrapper must be passed via the constructor. The data updater will access the plots from the wrapper when
        // updating data sets.
        DataUpdater.Params pP = new DataUpdater.Params(wrapper);

        //  The lines below create two data sources that provide points drawn randomly from a parameterized Gaussian
        //  distribution (see the GaussianGenerator class in the shared package).
        pP._dataSources = new IDataSource[2];
        pP._dataSources[0] = new GaussianGenerator(100, 3, means0, new double[]{0.3d, 0.2d, 0.3d}, R);
        pP._dataSources[1] = new GaussianGenerator(100, 3, means1, new double[]{0.1d, 0.5d, 0.1d}, R);

        // Next, we must create two data processors, each linked to a different data source. Even though these processors
        // perform the same operations, they must be unique to each source. The reason is that the processor may still
        // perform some source-dependent operations, as in this example. The DistanceProcessor class extends the
        // AbstractDataProcessor, which implements mechanisms that allow past data to be stored and returned along
        // with the current data via the LinkedList<double[][]> (each element is related to one data update called on
        // the processor object). This ``cumulative'' mode is explicitly enabled in the DistanceProcessor.
        pP._dataProcessors = new IDataProcessor[2];
        pP._dataProcessors[0] = new DistanceProcessor(3, means0);
        pP._dataProcessors[1] = new DistanceProcessor(3, means1);

        // The lines below establish sources-processor bindings (note that, in general, each source may be connected
        // with multiple processors, but one processor may be linked with only one source).
        pP._sourcesToProcessors = new SourceToProcessors[2];
        pP._sourcesToProcessors[0] = new SourceToProcessors(0);
        pP._sourcesToProcessors[1] = new SourceToProcessors(1);

        // The lines below establish sources-processor bindings (note that, in general, each source may be connected
        // with multiple processors, but one processor may be linked with only one source). The code below links
        // source #0 with processor #0 and source #1 with processor #1.
        pP._processorToPlots = new ProcessorToPlots[2];
        int[] plotIds = new int[]{0, 1};

        // The following two blocks of code establish processor-plots bindings. In this tutorial, each processor
        // provides the output to two plots (Plot3D and ParallelCoordinatePlot2D).
        {
            // Creating reference data sets (each for each targeted plot):
            IDataSet RDS0 = DataSet.getFor3D("DS0", new MarkerStyle(0.02f, Gradient.getViridisGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
            IDataSet RDS1 = DataSet.getForParallelCoordinatePlot2D("DS0", 3, new LineStyle(0.1f, Gradient.getViridisGradient(), 3));
            IDataSet[] referenceDataSets = new IDataSet[]{RDS0, RDS1};
            // Creating the binding (processor #0 -> two plots):
            pP._processorToPlots[0] = new ProcessorToPlots(plotIds, referenceDataSets);
        }
        {
            // Creating reference data sets (each for each targeted plot):
            IDataSet RDS0 = DataSet.getFor3D("DS1", new MarkerStyle(0.02f, Gradient.getPlasmaGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
            IDataSet RDS1 = DataSet.getForParallelCoordinatePlot2D("DS1", 3, new LineStyle(0.1f, Gradient.getPlasmaGradient(), 3));
            IDataSet[] referenceDataSets = new IDataSet[]{RDS0, RDS1};
            // Creating the binding (processor #1 -> two plots):
            pP._processorToPlots[1] = new ProcessorToPlots(plotIds, referenceDataSets);
        }

        // Let's now create the data updater object (the constructor can throw an exception when the validation of
        // provided params fails).
        DataUpdater dataUpdater;
        try
        {
            dataUpdater = new DataUpdater(pP);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Display the frame:
        frame.setVisible(true);

        // The loop shows that the calls of dataUpdater.update() may be used to update data sets sequentially (e.g., along with the optimizer being run).
        for (int i = 0; i < 20; i++)
        {
            // Uncomment to observe the sequence of updates as animation.
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            dataUpdater.update();
        }
    }
}
