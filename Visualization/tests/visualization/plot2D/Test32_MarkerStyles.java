package visualization.plot2D;

import color.gradient.Color;
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
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test32_MarkerStyles
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = true;
        pP._drawAuxGridlines = false;
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(
                new Range(0.0d, 1.0d), false, false,
                new Range(0.0d, 1.0d), true, false
        );


        Plot2D plot = new Plot2D(pP);
        DrawingArea2D d2d = (DrawingArea2D) plot.getComponentsContainer().getDrawingArea();
        d2d.getMainGrid().getHorizontalTicksDataGetter().setNoTicks(12);
        plot.getComponentsContainer().getAxes()[1].getTicksDataGetter().setNoTicks(12);

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        plot.getModel().notifyDisplayRangesChangedListeners();
        ArrayList<IDataSet> dss = new ArrayList<>();

        float s = 5.0f;
        float r = 0.5f;

        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.SQUARE, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("SQUARE", new double[][]{{0.75d, 0.0d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.CIRCLE, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("CIRCLE", new double[][]{{0.75d, 0.25d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.TRIANGLE_UP, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("T-UP", new double[][]{{0.75d, 0.5d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.TRIANGLE_DOWN, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("T-DOWN", new double[][]{{0.75d, 0.75d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.TRIANGLE_LEFT, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("T-LEFT", new double[][]{{0.75d, 1.0d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.TRIANGLE_RIGHT, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("T-RIGHT", new double[][]{{0.75d, 1.25d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.PENTAGON, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("PENTAGON", new double[][]{{0.75d, 1.5d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.STAR, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("STAR", new double[][]{{0.75d, 1.75d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.HEXAGON_HOR, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("H-HOR", new double[][]{{0.75d, 2.0d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.HEXAGON_VERT, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("H-VERT", new double[][]{{0.75d, 2.25d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.DIAMOND_HOR, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("D-HOR", new double[][]{{0.75d, 2.5d}}, ms));
        }
        {
            MarkerStyle ms = new MarkerStyle(s, Color.RED, Marker.DIAMOND_VERT, new LineStyle(r, Color.BLACK));
            dss.add(DataSet.getFor2D("D-VERT", new double[][]{{0.75d, 2.75d}}, ms));
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        plot.getModel().setDataSets(dss, true);

        frame.setVisible(true);
    }
}
