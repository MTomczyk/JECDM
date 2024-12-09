package t1_10.t1_visualization_module.t1_plot2d.t11;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;

/**
 * This tutorial provides basics on gradients.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial11
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        // Let's first create a display range manager (params container). The lines below establish three display
        // ranges: for the X axis, for the Y axis, and an auxiliary display range associated with the data point
        // distance to the (0, 0) coordinate. While the first two are fixed and set to [-1, 1] ranges, the last is
        // dynamic and initially nulled.
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true);

        pP._xAxisTitle = "X-coordinate";
        pP._yAxisTitle = "Y-coordinate";

        // We need to establish a gradient object before instantiating a colorbar.
        Gradient gradient = Gradient.getViridisGradient();
        // Use the below lines if you want to have a discretized gradient.
        //float [][] colors = Viridis.colors;
        //Gradient gradient = Gradient.getGradientFromColorMatrix(colors, "gradient", 8, false);

        // You can also use gradient with the alpha channel (transparency):
        //float [][] colors = Viridis.colors_alpha_increasing;
        //Gradient gradient = Gradient.getGradientFromColorMatrix(colors, "gradient", 256, false);

        // A colorbar object requires providing an auxiliary object that implements an ITicksDataGetter interface
        // responsible for creating ticks (and their positions and associated values). The implementation below can be
        // considered binding between the colorbar axis and a selected display range (the third one).
        ITicksDataGetter tdg = new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 10);

        // This line provides a colorbar to the plot params container (gradient object, title, and the ticks data getter).
        pP._colorbar = new Colorbar(gradient, "Distance", tdg);

        pP._scheme = new WhiteScheme();
        // We need to extend the margin so that the colorbar can fit.
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        // The lines below create data consisting of a selected number of data points. Each data point has three
        // attributes: random x and y coordinates (drawn randomly from a Gaussian distribution with a standard
        // deviation of 0.25) and a distance from the drawn coordinates to the [0,0] coordinate.
        int noSamples = 100000;
        IRandom R = new MersenneTwister64(0);
        double[][] data = new double[noSamples][3];
        for (int i = 0; i < noSamples; i++)
        {
            double x = R.nextGaussian() * 0.25d;
            double y = R.nextGaussian() * 0.25d;
            double d = Math.sqrt(x * x + y * y);
            data[i][0] = x;
            data[i][1] = y;
            data[i][2] = d;
        }

        // The marker style definition binds the constructed gradient object with the third display range
        // (associated with the data points' distances to the plot center).
        MarkerStyle ms = new MarkerStyle(1.0f, gradient, 2, Marker.CIRCLE);
        IDataSet dataSet = DataSet.getFor2D("Random points", data, ms);

        plot.getModel().setDataSet(dataSet, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
