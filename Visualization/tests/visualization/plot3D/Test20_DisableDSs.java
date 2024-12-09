package visualization.plot3D;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test20_DisableDSs
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._zAxisTitle = "Z-Axis Test (j)";

        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;
        pP._drawLegend = true;
        pP._useAlphaChannel = false;
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.01f);

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(new Range(0.0d, 1.25d), false, false,
                Range.getNormalRange(), true, false, Range.getNormalRange(), false, false);

        Plot3D plot = new Plot3D(pP);
        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        ArrayList<IDataSet> dss = new ArrayList<>();
        // no fill
        {
            MarkerStyle ms = new MarkerStyle(4.0f, null, Marker.POINT_3D, 1.0f);
            dss.add(DataSet.getFor3D("POINT", new double[][]{{0.25d, 0.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.CUBE_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("CUBE", new double[][]{{0.25d, 0.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.TETRAHEDRON_UP_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-UP", new double[][]{{0.25d, 0.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.TETRAHEDRON_DOWN_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-DOWN", new double[][]{{0.25d, 0.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.TETRAHEDRON_LEFT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-LEFT", new double[][]{{0.25d, 1.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.TETRAHEDRON_RIGHT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-RIGHT", new double[][]{{0.25d, 1.25d, 0.5d}}, ms, null));
            dss.get(dss.size()-1).setDisplayableOnLegend(false);
            dss.get(dss.size()-1).setSkipRendering(true);
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.TETRAHEDRON_FRONT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-FRONT", new double[][]{{0.25d, 1.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.TETRAHEDRON_BACK_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-BACK", new double[][]{{0.25d, 1.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.SPHERE_LOW_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_LOW", new double[][]{{0.25d, 2.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.SPHERE_MEDIUM_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_MEDIUM", new double[][]{{0.25d, 2.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, null, Marker.SPHERE_HIGH_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_HIGH", new double[][]{{0.25d, 2.5d, 0.5d}}, ms, null));
        }


        // fill red
        {
            MarkerStyle ms = new MarkerStyle(4.0f, Color.RED, Marker.POINT_3D, 1.0f);
            dss.add(DataSet.getFor3D("POINT", new double[][]{{0.5d, 0.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.CUBE_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("CUBE", new double[][]{{0.5d, 0.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_UP_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-UP", new double[][]{{0.5d, 0.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_DOWN_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-DOWN", new double[][]{{0.5d, 0.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_LEFT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-LEFT", new double[][]{{0.5d, 1.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_RIGHT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-RIGHT", new double[][]{{0.5d, 1.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_FRONT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-FRONT", new double[][]{{0.5d, 1.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_BACK_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-BACK", new double[][]{{0.5d, 1.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_LOW_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_LOW", new double[][]{{0.5d, 2.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_MEDIUM_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_MEDIUM", new double[][]{{0.5d, 2.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_HIGH_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_HIGH", new double[][]{{0.5d, 2.5d, 0.5d}}, ms, null));
        }

        // gradient fill

        {
            MarkerStyle ms = new MarkerStyle(4.0f, Gradient.getViridisGradient(), 1, Marker.POINT_3D, 1.0f);
            dss.add(DataSet.getFor3D("POINT", new double[][]{{0.75d, 0.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.CUBE_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("CUBE", new double[][]{{0.75d, 0.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.TETRAHEDRON_UP_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-UP", new double[][]{{0.75d, 0.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.TETRAHEDRON_DOWN_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-DOWN", new double[][]{{0.75d, 0.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.TETRAHEDRON_LEFT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-LEFT", new double[][]{{0.75d, 1.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.TETRAHEDRON_RIGHT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-RIGHT", new double[][]{{0.75d, 1.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.TETRAHEDRON_FRONT_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-FRONT", new double[][]{{0.75d, 1.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.TETRAHEDRON_BACK_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-BACK", new double[][]{{0.75d, 1.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.SPHERE_LOW_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_LOW", new double[][]{{0.75d, 2.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.SPHERE_MEDIUM_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_MEDIUM", new double[][]{{0.75d, 2.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getViridisGradient(), 1, Marker.SPHERE_HIGH_POLY_3D, new LineStyle(1.0f, Color.BLACK, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_HIGH", new double[][]{{0.75d, 2.5d, 0.5d}}, ms, null));
        }

        // gradient edges

        {
            MarkerStyle ms = new MarkerStyle(4.0f, Color.RED, Marker.POINT_3D, 1.0f);
            dss.add(DataSet.getFor3D("POINT", new double[][]{{1.0d, 0.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.CUBE_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("CUBE", new double[][]{{1.0d, 0.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_UP_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-UP", new double[][]{{1.0d, 0.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_DOWN_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-DOWN", new double[][]{{1.0d, 0.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_LEFT_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-LEFT", new double[][]{{1.0d, 1.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_RIGHT_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-RIGHT", new double[][]{{1.0d, 1.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_FRONT_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-FRONT", new double[][]{{1.0d, 1.5d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.TETRAHEDRON_BACK_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("T-BACK", new double[][]{{1.0d, 1.75d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_LOW_POLY_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_LOW", new double[][]{{1.0d, 2.0d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_MEDIUM_POLY_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_MEDIUM", new double[][]{{1.0d, 2.25d, 0.5d}}, ms, null));
        }
        {
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.SPHERE_HIGH_POLY_3D, new LineStyle(5.0f, Gradient.getPlasmaGradient(), 1, 0.1f), 1.0f);
            dss.add(DataSet.getFor3D("SPHERE_HIGH", new double[][]{{1.0d, 2.5d, 0.5d}}, ms, null));
        }
        plot.getModel().setDataSets(dss, true);

        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);
    }
}
