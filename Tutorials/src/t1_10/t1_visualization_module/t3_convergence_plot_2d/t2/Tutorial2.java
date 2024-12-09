package t1_10.t1_visualization_module.t3_convergence_plot_2d.t2;

import color.gradient.Color;
import component.axis.ticksupdater.FromDisplayRange;
import component.drawingarea.DrawingArea2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import space.Range;
import space.normalization.minmax.Logarithmic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * This tutorial focuses on creating and displaying a basic 2D convergence plot (using a logarithmic scale).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();


        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();

        // The two lines below introduce a logarithmic scale into the Y-axis. Note that the display range was suitably
        // fixed so that no zero or negative values will be input for the logarithmic function (if so, glitches in rendering may occur).
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(0.01f, 2.0f), false);
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Logarithmic());

        pP._xAxisTitle = "Iteration";
        pP._yAxisTitle = "Performance";

        ArrayList<IDataSet> dataSets = new ArrayList<>();

        int iterations = 100;
        {
            double[][] data = new double[iterations][4];
            for (int i = 0; i < iterations; i++)
            {
                data[i][0] = i;
                data[i][1] = 0.25f + 1.0f / Math.pow(i + 1, 0.5f);
                data[i][2] = data[i][1] + 0.05f;
                data[i][3] = data[i][1] - 0.05f;
            }
            MarkerStyle ms = new MarkerStyle(2.0f, Color.BLACK, Marker.CIRCLE);
            ms._paintEvery = 10;
            ms._startPaintingFrom = 5;
            IDataSet ds = DataSet.getForConvergencePlot2D("CP1", data, ms,
                    new color.Color(0.0f, 0.0f, 1.0f, 0.25f));
            dataSets.add(ds);
        }

        {
            double[][] data = new double[iterations][4];
            for (int i = 0; i < iterations; i++)
            {
                data[i][0] = i;
                data[i][1] = 0.1f + 1.0f / (i + 1);
                data[i][2] = data[i][1] + 0.1f;
                data[i][3] = data[i][1] - 0.1f;
            }
            LineStyle ls = new LineStyle(0.2f, Color.RED);
            IDataSet ds = DataSet.getForConvergencePlot2D("CP2", data, ls,
                    new color.Color(1.0f, 0.0f, 0.0f, 0.25f));
            dataSets.add(ds);
        }

        Plot2D plot = new Plot2D(pP);

        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setForcedUnnormalizedLocations(
                new float[]{1.0f/16.0f, 1.0f/8.0f, 1.0f/4.0f, 1.0f/ 2.0f, 1.0f, 2.0f});

        NumberFormat numberFormat = new DecimalFormat();
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setNumberFormat(numberFormat);

        DrawingArea2D drawingArea = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();
        drawingArea.getMainGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 100));
        drawingArea.getMainGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{1.0f/16.0f, 1.0f/8.0f, 1.0f/4.0f, 1.0f/ 2.0f, 1.0f, 2.0f});

        drawingArea.getAuxGrid().setHorizontalTicksDataGetter(new FromDisplayRange(pP._pDisplayRangesManager._DR[1], 100));
        drawingArea.getAuxGrid().getHorizontalTicksDataGetter().setForcedUnnormalizedLocations(new float[]{
                1.0f/16.0f + 1.0f/32.0f,
                1.0f/8.0f + 1.0f/16.0f,
                1.0f/4.0f + 1.0f/8.0f,
                1.0f/ 2.0f + 1.0f / 4.0f,
                1.0f + 1.0f/2.0f,
        });


        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot,  600, 600);

        plot.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
