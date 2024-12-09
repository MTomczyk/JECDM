package visualization.parallelcoordinate2D;

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
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;


/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link ParallelCoordinatePlot2D}) on a frame.
 * Test parallel coordinate plot.
 *
 * @author MTomczyk
 */
public class Test5_1DIM
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int dims = 1;
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(dims);
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "Objectives";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = true;
        pP._axesTitles = new String[dims];
        for (int i = 0; i < dims; i++) pP._axesTitles[i] = "D" + (i + 1);
        pP._scheme = new WhiteScheme();
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(dims, new Range(-3.0d, 4.0d), false, false);

        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        int lines = 2000;

        double[][] data = new double[lines][dims];
        IRandom R = new MersenneTwister64(1);
        double[] means = new double[dims];
        double[] std = new double[dims];
        for (int i = 0; i < dims; i++)
        {
            means[i] = R.nextDouble();
            std[i] = 1.0d - means[i];
        }

        for (int i = 0; i < lines; i++)
            for (int j = 0; j < dims; j++)
                data[i][j] = means[j] + R.nextGaussian() * std[j];

        LineStyle ls = new LineStyle(0.25f, Gradient.getPlasmaGradient(), 0);
        IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 1", dims, data, new MarkerStyle(4.0f, Gradient.getViridisGradient(), 0, Marker.CIRCLE), ls);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);


        plot.getModel().setDataSet(ds, true);
        frame.setVisible(true);

    }
}
