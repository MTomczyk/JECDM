package t1_10.t1_visualization_module.t1_plot2d.t13;

import color.gradient.Color;
import color.gradient.Gradient;
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

import java.util.ArrayList;

/**
 * This tutorial overviews all marker style configurations.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial13
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();
        pP._xAxisTitle = "Marker shape";
        pP._yAxisTitle = "Configuration";

        pP._clipDrawingArea = false;

        Plot2D plot = new Plot2D(pP);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 600, 600);

        ArrayList<IDataSet> dataSets = new ArrayList<>();

        // First, let's wrap all available marker shapes in the array.
        Marker[] markers = new Marker[]{
                Marker.CIRCLE, Marker.SQUARE, Marker.TRIANGLE_UP, Marker.TRIANGLE_DOWN, Marker.TRIANGLE_LEFT, Marker.TRIANGLE_RIGHT,
                Marker.DIAMOND_HOR, Marker.DIAMOND_VERT, Marker.HEXAGON_HOR, Marker.HEXAGON_VERT, Marker.PENTAGON, Marker.STAR
        };


        // The below lines construct data sets (each for a different marker shape).
        // The markers are drawn without edges. The fill color is set to red.
        int index = 0;
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 0.0d}},
                    new MarkerStyle(5.0f, Color.RED, markers[i])));

        // The below lines construct data sets (each for a different marker shape).
        // The markers are drawn without edges. The fill color is set to red
        // (with an alpha channel linearly transiting from near 0 to 1)
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 1.0d}},
                    new MarkerStyle(5.0f, new Color(1.0f, 0.0f, 0.0f, (i + 1)/(float) (markers.length)),
                            markers[i])));

        // The below lines construct data sets (each for a different marker shape).
        // The markers are drawn without edges.
        // The fill color is set to a Viridis gradient (color is determined based on the X-coordinate).
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 2.0d}},
                    new MarkerStyle(5.0f, Gradient.getViridisGradient(), 0, markers[i])));

        // The below lines construct data sets (each for a different marker shape).
        // The edge color is set to black. The fill color is set to red.
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 3.0d}},
                    new MarkerStyle(5.0f, Color.RED, markers[i], new LineStyle(1.0f, Color.BLACK))));

        // The below lines construct data sets (each for a different marker shape).
        // The edge color is set to black.
        // The fill color is set to a Viridis gradient (color is determined based on the X-coordinate).
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 4.0d}},
                    new MarkerStyle(5.0f, Gradient.getViridisGradient(), 0, markers[i], new LineStyle(1.0f, Color.BLACK))));

        // The below lines construct data sets (each for a different marker shape).
        // The edge color is set to a Red-Blue gradient (color is determined based on the X-coordinate).
        // The fill color is set to a Viridis gradient (color is determined based on the X-coordinate).
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 5.0d}},
                    new MarkerStyle(5.0f, Gradient.getViridisGradient(), 0, markers[i],
                            new LineStyle(1.0f, Gradient.getRedBlueGradient(), 0))));

        // The below lines construct data sets (each for a different marker shape).
        // The edge color is set to a Red-Blue gradient (color is determined based on the X-coordinate).
        // The fill color is not specified (null = no fill).
        for (int i = 0; i < markers.length; i++)
            dataSets.add(DataSet.getFor2D(String.valueOf(index++), new double[][]{{i, 6.0d}},
                    new MarkerStyle(5.0f, null, 0, markers[i],
                            new LineStyle(1.0f, Gradient.getRedBlueGradient(), 0))));

        plot.getModel().setDataSets(dataSets, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
