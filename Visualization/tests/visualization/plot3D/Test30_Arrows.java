package visualization.plot3D;

import color.gradient.Color;
import color.gradient.Gradient;
import color.gradient.gradients.Inferno;
import dataset.DSFactory3D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import scheme.WhiteScheme;
import space.Range;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Test drawing arrows.
 *
 * @author MTomczyk
 */
public class Test30_Arrows
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        pP._scheme = WhiteScheme.getForPlot3D();
        pP._scheme.setAllFontsTo("Times New Roman");
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(),
                Range.getNormalRange(), Range.getNormalRange());
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";
        pP._useAlphaChannel = true;
        Plot3D plot3D = new Plot3D(pP);

        Frame frame = new Frame(plot3D, 0.5f);

        ArrayList<IDataSet> DSs = new ArrayList<>();

        {    // easy
            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(new double[][]
                    {
                            {0.1d, 0.1d, 0.1d},
                            null,
                            {0.1d, 0.3d, 0.1d},
                            {0.3d, 0.3d, 0.1d},
                            null,
                            null,
                            {0.3d, 0.3d, 0.4d},
                            {0.3d, 1.0d, 0.4d}
                    });
            LineStyle ls = new LineStyle(0.01f, new Color(0.0f, 0.0f, 0.0f, 0.8f), Line.POLY_OCTO);
            ArrowStyles as = new ArrowStyles(null,
                    new ArrowStyle(0.01f, 0.01f, Gradient.getBlueRedGradient(), 0, Arrow.TRIANGULAR_REVERSED_3D));
            DataSet ds = DSFactory3D.getDS("Arrows 1", lData, null, ls, as, false, true, 0.005f);
            DSs.add(ds);
        }

        {

            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(new double[][]
                    {
                            {0.3d, 0.3d, 0.5d},
                            {0.2d, 0.4d, 0.5d},
                            {0.2d, 0.4d, 0.7d},
                            {0.5d, 0.4d, 0.7d},
                            {0.7d, 0.4d, 0.7d},
                            {0.7d, 0.4d, 0.4d},
                            {0.7d, 0.5d, 0.4d},
                    });

            MarkerStyle ms = new MarkerStyle(0.02f, Color.BLUE, Marker.SPHERE_LOW_POLY_3D);
            float [][] gd = Inferno.colors_alpha_decreasing;
            LineStyle ls = new LineStyle(2.0f, Gradient.getGradientFromColorMatrix(gd, "IA", 255,
                    false), 0);
            DataSet ds = DSFactory3D.getDS("Arrows 2", lData, ms, ls, null, false, true, 0.005f);
            DSs.add(ds);
        }


        {
            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(new double[][]
                    {
                            {0.5d, 0.2d, 0.1d},
                            {0.6d, 0.2d, 0.1d},
                            null,
                            {0.6d, 0.3d, 0.1d},
                            null,
                            {0.6d, 0.3d, 0.2d},
                            {0.8d, 0.3d, 0.2d},
                            null,
                            {0.8d, 0.3d, 0.4d},
                            {0.8d, 0.5d, 0.4d},
                    });

            MarkerStyle ms = new MarkerStyle(0.02f, Color.BLACK, Marker.TETRAHEDRON_FRONT_3D);
            LineStyle ls = new LineStyle(0.03f, new Color(1.0f, 0.0f, 0.0f, 0.5f), Line.POLY_OCTO);
            DataSet ds = DSFactory3D.getDS("Arrows 3", lData, ms, ls, null, false, true, 0.005f);
            DSs.add(ds);
        }

        {

            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(new double[][]
                    {
                            {0.1d, 0.7d, 0.7d},
                            {0.3d, 0.7d, 0.7d},
                            {0.5d, 0.7d, 0.7d},
                            {0.8d, 0.7d, 0.7d},
                    });

            MarkerStyle ms = new MarkerStyle(0.02f, Color.BLACK, Marker.SPHERE_LOW_POLY_3D);
            float [][] gd = Inferno.colors_alpha_increasing;
            LineStyle ls = new LineStyle(0.03f, Gradient.getGradientFromColorMatrix(gd, "gd", 255, false), 0, Line.POLY_OCTO);
            DataSet ds = DSFactory3D.getDS("Arrows 4", lData, ms, ls, null, false, true, 0.005f);
            DSs.add(ds);
        }

        {

            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(new double[][]
                    {
                            {0.8d, 0.1d, 0.5d},
                            {0.8d, 0.3d, 0.5d},
                            {0.8d, 0.5d, 0.5d},
                            {0.8d, 0.9d, 0.5d},
                    });

            MarkerStyle ms = new MarkerStyle(0.02f, Color.BLACK, Marker.SPHERE_LOW_POLY_3D);
            LineStyle ls = new LineStyle(0.03f, new Color(1.0f, 0.0f, 0.0f, 0.5f), Line.POLY_QUAD);
            DataSet ds = DSFactory3D.getDS("Arrows 5", lData, ms, ls, null, true, true, 0.005f);
            DSs.add(ds);
        }


        {

            LinkedList<double[][]> lData = new LinkedList<>();
            lData.add(new double[][]
                    {
                            {0.8d, 0.1d, 0.1d},
                            {0.8d, 0.3d, 0.1d},
                            {0.8d, 0.5d, 0.1d},
                            {0.8d, 0.9d, 0.1d},
                    });

            MarkerStyle ms = new MarkerStyle(0.02f, Color.BLACK, Marker.SPHERE_LOW_POLY_3D);
            float [][] gd = Inferno.colors_alpha_increasing;
            LineStyle ls = new LineStyle(0.03f, Gradient.getGradientFromColorMatrix(gd, "gd", 255, false), 1, Line.POLY_QUAD);
            DataSet ds = DSFactory3D.getDS("Arrows 6", lData, ms, ls, null, true, true, 0.005f);
            DSs.add(ds);
        }

        frame.setVisible(true);
        plot3D.getModel().setDataSets(DSs, true);

    }
}
