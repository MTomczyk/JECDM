package t1_10.t1_visualization_module.t4_parallel_coordinate_plot_2d.t1;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;

import java.util.ArrayList;


/**
 * This tutorial focuses on creating and displaying a basic 2D parallel coordinate plot.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // The below line instantiates a params container for the parallel coordinate plot. As can be observed,
        // the constructor accepts the integer representing the space dimensionality. This number will reflect
        // the number of parallel vertical axes.
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(3);

        pP._xAxisTitle = "Objectives";
        pP._yAxisTitle = "Y-Axis Test (j)";

        // Each parallel vertical axis can be appointed a label. These labels can be passed via the below field.
        pP._axesTitles = new String[]{"D1", "D2", "D3"};

        // Use a customized white scheme
        pP._scheme = WhiteScheme.getForPCP2D();

        // This plot type uses a suitably adjusted display ranges manager. Specifically, it is assumed that n + 1 display
        // ranges are maintained, where n denotes the space dimensionality. The first n display ranges correspond to the
        // vertical axes (dimensions), and they can be freely and independently to one another parameterized (e.g.,
        // dynamic update flags set to true/false). In turn, the last display range is associated with the X-axis and is
        // fixed at the [0, 1] interval. Regarding the mapping, the array takes the following form [0,1,...n - 1]. Noticeably,
        // no attribute is mapped into the last display range. The reason is that it is explicitly reserved and exploited
        // by the painter dedicated to a parallel coordinate plot (not accessible, e.g., to gradients). The valid
        // parameterization of the display ranges params container can be obtained via ``getForParallelCoordinatePlot2D''
        // static methods.
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(3,
                Range.getNormalRange(), false);
        // Equivalent to:
        //pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(3,
        //        new Range[]{Range.getNormalRange(), Range.getNormalRange(), Range.getNormalRange()},
        //        new boolean[] {false, false, false});

        // Finally, the ParallelCoordinatePlot2D object can be instantiated.
        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);


        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        ArrayList<IDataSet> dataSets = new ArrayList<>();
        {
            // The raw data to be illustrated on the parallel coordinate plot consists of a series of n-dimensional data
            // points (n equals the number of attributes). Each data point will be rendered as a continuous line passing
            // through all parallel vertical axes at valid positions. One point is modeled as one row in the double[][]
            // matrix provided when instantiating a data set object, and it is supposed to be an n-element vector, where
            // the i-th attribute corresponds to the i-th dimension. Note that, in the example data below, the line
            // defined as {1.0d, 2.0d, 0.0d} would barely be visible if the display ranges were fixed at [0, 1] intervals
            // (i.e., the dynamic update flag was set to false).
            double[][] data = new double[][]{{0.0d, 0.0d, 1.0d}, {1.0d, 2.0d, 0.0d}, {0.5d, 0.5d, -0.5d}};
            // Notably, markers/lines can be colored using a gradient linked to one of the 1-n display ranges
            // (the last display range is not accessible).
            LineStyle ls = new LineStyle(2, Gradient.getPlasmaGradient(), 2);
            LineStyle mes = new LineStyle(1, Gradient.getBlueRedGradient(), 2);
            MarkerStyle ms = new MarkerStyle(5, Gradient.getViridisGradient(), 0, Marker.SQUARE, mes);
            // A data set object that is suitably parameterized for the parallel coordinate plot can be obtained by
            // using ``getForParallelCoordinatePlot2D'' static factory-like methods.
            dataSets.add(DataSet.getForParallelCoordinatePlot2D("DS1", 3, data, ms, ls));
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        plot.getModel().setDataSets(dataSets, true);
        frame.setVisible(true);

    }
}
