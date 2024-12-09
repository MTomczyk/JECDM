package t1_10.t1_visualization_module.t2_plot3d.t5;

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
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import scheme.WhiteScheme;
import scheme.enums.Align;
import space.Dimension;
import space.Range;

import java.util.ArrayList;

/**
 * This tutorial focuses on creating and displaying a basic 3D plot (overview of marker styles).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial5
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();

        // Adjust display ranges
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-0.25f, 5.25f), false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-0.25f, 0.25f), false);
        pP._zProjectionBound = new Dimension(-0.25f, 0.5f);

        pP._scheme = WhiteScheme.getForPlot3D();

        // Disable cube
        pP._drawCube = false;
        // Disable panes
        pP._paneAlignments = new Align[]{};
        // Disable axes
        pP._axesAlignments = new Align[]{};

        // Enabling this flag permits using the alpha channel in OpenGL.
        pP._useAlphaChannel = true;

        Plot3D plot = new Plot3D(pP);

        // First, let's wrap all available marker shapes (3D) in the array.
        Marker[] markers = new Marker[]{
                Marker.POINT_3D,
                Marker.TETRAHEDRON_FRONT_3D, Marker.TETRAHEDRON_BACK_3D,
                Marker.TETRAHEDRON_DOWN_3D, Marker.TETRAHEDRON_UP_3D,
                Marker.TETRAHEDRON_LEFT_3D, Marker.TETRAHEDRON_RIGHT_3D,
                Marker.SPHERE_LOW_POLY_3D,
                Marker.SPHERE_MEDIUM_POLY_3D,
                Marker.SPHERE_HIGH_POLY_3D
        };


        ArrayList<IDataSet> dataSets = new ArrayList<>(4);

        // No marker fill; black edges.
        int index = 0;
        for (int i = 0; i < markers.length; i++)
        {
            float size = 0.1f;
            if (markers[i] == Marker.POINT_3D) size = 1.0f;
            dataSets.add(DataSet.getFor3D(String.valueOf(index++), new double[][]{{i, 0.0d, 0.0d}},
                    new MarkerStyle(size, null, markers[i], new LineStyle(1.0f, Color.BLACK))));
        }

        // Red marker fill; no edges.
        for (int i = 0; i < markers.length; i++)
        {
            float size = 0.1f;
            if (markers[i] == Marker.POINT_3D) size = 1.0f;
            dataSets.add(DataSet.getFor3D(String.valueOf(index++), new double[][]{{i, 1.0d, 0.0d}},
                    new MarkerStyle(size, Color.RED, markers[i])));
        }

        // Red marker fill (transparent: additional flag in DataSet.getFor3D must be set); black edges.
        for (int i = 0; i < markers.length; i++)
        {
            float size = 0.1f;
            if (markers[i] == Marker.POINT_3D) size = 1.0f;
            dataSets.add(DataSet.getFor3D(String.valueOf(index++), new double[][]{{i, 2.0d, 0.0d}},
                    new MarkerStyle(size, new Color(1.0f, 0.0f, 0.0f, 0.5f), markers[i]), true));
        }

        // Gradient marker fill; black edges.
        for (int i = 0; i < markers.length; i++)
        {
            float size = 0.1f;
            if (markers[i] == Marker.POINT_3D) size = 1.0f;
            dataSets.add(DataSet.getFor3D(String.valueOf(index++), new double[][]{{i, 3.0d, 0.0d}},
                    new MarkerStyle(size, Gradient.getViridisGradient(), 0, markers[i],
                            new LineStyle(1.0f, Color.BLACK))));
        }

        // Gradient marker fill; Gradient edges.
        for (int i = 0; i < markers.length; i++)
        {
            float size = 0.1f;
            if (markers[i] == Marker.POINT_3D) size = 1.0f;
            dataSets.add(DataSet.getFor3D(String.valueOf(index++), new double[][]{{i, 4.0d, 0.0d}},
                    new MarkerStyle(size, Gradient.getViridisGradient(), 0, markers[i],
                            new LineStyle(1.0f, Gradient.getRedBlueGradient(), 0))));
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 1400, 1200);
        plot.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
