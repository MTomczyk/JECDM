package t1_10.t1_visualization_module.t2_plot3d.t2;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import space.Range;

import java.util.ArrayList;

/**
 * This tutorial focuses on creating and displaying a basic 3D plot (data set providing).
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
        Plot3D.Params pP = new Plot3D.Params();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(new Range(-1.0f, 1.0f),
                new Range(-1.0f, 1.0f), new Range(-1.0f, 1.0f));

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._scheme = WhiteScheme.getForPlot3D();

        // Let's use the legend.
        pP._drawLegend = true;

        Plot3D plot = new Plot3D(pP);

        // The blocks of code below create three data sets in a way similar to how data sets for plot 2D are created.
        // Naturally, data points now consist of three attributes. Note that DataSet.getFor3D method is used now to
        // create a suitable DataSet object.
        ArrayList<IDataSet> dataSets = new ArrayList<>(4);
        {
            // The data sets consists of 5 points.
            // They are to be displayed using red cubes with black edges (marker style with an edge style specified).
            double [][] data = new double[][]
                    {
                            {-1.0f, -1.0f, -1.0f},
                            {-0.5f, -0.5f, -0.5f},
                            {0.0f, 0.0f, 0.0f},
                            {0.5f, 0.5f, 0.5f},
                            {1.0f, 1.0f, 1.0f}
                    };

            //Important note: sizes typically reflect the size in the rendering space 1:1 in the 3D mode.
            // Hence, a centrally located cube with a size of 1.0 (100% for 3D rendering) will cover all the drawing area.
            // The exception is when the 3D shape is drawn using GL_LINES or GL_POINTS options. Then, the OpenGL
            // determines the size based on the raster size. These modes are used in the following situations:
            // - When drawing a marker edge (GL_LINES)
            // - When a line style is specified for a data set and Line.REGULAR style is used (default option; GL_LINES).
            // - When a marker style is specified and a Marker.POINT_3D shape is used (GL_POINTS).
            // Consequently, when different styles are mixed, the size scale may seem inconsistent, and preferred size
            // values must be determined manually (this is the case for the three data sets). Note that, however,
            // this may cause glitches in legend rendering as the marker/lines drawn for legend entries have sizes
            // determined relative to one another (hence, if one data set has a size 100 larger than the size of
            // another data set, it legend entry will also be 100 times bigger). To resolve this issue, an additional
            // parameter can be passed via marker/line style object: legend size. This size will be used when rendering
            // legend entries instead of the dynamically determined one if provided.

            // Edges are drawn in the GL_LINES mode, hence the line size scale differs from the marker size scale
            LineStyle ls = new LineStyle(1.0f, Color.BLACK, 0.05f); // bypass legend entry size
            MarkerStyle ms = new MarkerStyle(0.05f, Color.RED, Marker.CUBE_3D, ls);
            dataSets.add(DataSet.getFor3D("DS1", data, ms));
        }

        {
            // 5 data points drawn using a marker with a gradient fill color specified (without edges and line connections)
            double [][] data = new double[][]
                    {
                            {-1.0f, 0.25f, 0.25f},
                            {-0.5f, 0.25f, 0.25f},
                            {0.0f, 0.25f, 0.25f},
                            {0.5f, 0.25f, 0.25f},
                            {1.0f, 0.25f, 0.25f},
                    };
            // Assuming the style is set to Marker.POINT_3D, the marker size scale will be different.
            MarkerStyle ms = new MarkerStyle(0.05f, Gradient.getPlasmaGradient(), 0, Marker.SPHERE_HIGH_POLY_3D);
            dataSets.add(DataSet.getFor3D("DS2", data, ms));
        }

        {
            // Line determined by 3 points.
            double [][] data = new double[][]
                    {
                            {-1.0f, -1.0f, 1.0f},
                            {0.0f, -1.0f, -0.25f},
                            {1.0f, 0.5f, -0.5f},
                    };
            // The commented line uses a default Line.REGULAR shape, which uses a different line width scale.
            //LineStyle ls = new LineStyle(0.05f, Gradient.getViridisGradient(), 2);
            LineStyle ls = new LineStyle(0.05f, Gradient.getViridisGradient(), 2, Line.POLY_OCTO);
            dataSets.add(DataSet.getFor3D("DS3", data, ls));
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 1200, 1200);

        plot.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
