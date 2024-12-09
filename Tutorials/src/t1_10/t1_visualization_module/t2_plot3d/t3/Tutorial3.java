package t1_10.t1_visualization_module.t2_plot3d.t3;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;

import java.util.ArrayList;

/**
 * This tutorial focuses on creating and displaying a basic 3D plot (colorbar and scheme customization).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial3
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();

        // Let's create 4 display ranges: the first three for X, Y, and Z axes, while the fourth will be linked to the distance.
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0f, 1.0f), false);
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, false);

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";
        pP._zAxisTitle = "Z-coordinate";

        pP._scheme = WhiteScheme.getForPlot3D(0.3f); // set the right margin to 0.3 (relative multiplier)
        pP._scheme._sizes.put(SizeFields.COLORBAR_SHRINK, 0.66f); // we can reduce the colorbar height (66% of max height)

        // Setting this flag to true will make the rendering panel transparent, allowing the rendering to be saved as
        // a PNG file with a transparent background (also, this flag permits using the alpha channel in OpenGL).
        pP._useAlphaChannel = true;

        // Create the colorbar associated with the fourth (index of 3) display range.
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Distance",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 5));

        Plot3D plot = new Plot3D(pP);

        // Create a data set consisting o points defined as [x-coordinate, y-coordinate, z-coordinate, distance to 0^3 point].
        ArrayList<IDataSet> dataSets = new ArrayList<>(4);
        {
            int noSamples = 100000;
            IRandom R = new MersenneTwister64(0);
            double[][] data = new double[noSamples][4];
            for (int i = 0; i < noSamples; i++)
            {
                double x = R.nextGaussian() * 0.25d;
                double y = R.nextGaussian() * 0.25d;
                double z = R.nextGaussian() * 0.25d;
                double d = Math.sqrt(x * x + y * y + z * z);
                data[i][0] = x;
                data[i][1] = y;
                data[i][2] = z;
                data[i][3] = d;
            }

            // Specify the gradient and the display range association
            MarkerStyle ms = new MarkerStyle(0.01f, Gradient.getViridisGradient(), 3, Marker.SPHERE_LOW_POLY_3D);
            dataSets.add(DataSet.getFor3D("DS1", data, ms));
        }

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(plot, 1400, 1200);
        plot.getModel().setDataSets(dataSets, true);

        frame.setVisible(true);
    }
}
