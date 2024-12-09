package t1_10.t1_visualization_module.t1_plot2d.t10;

import color.Color;
import color.ColorAssignment;
import color.gradient.Gradient;
import color.gradient.gradients.Viridis;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
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
 * This tutorial provides basics on gradients.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial10
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";

        pP._drawLegend = true;

        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);

        Plot2D plot = new Plot2D(pP);

        Frame frame = new Frame(plot, 0.5f);
        //Frame frame = new Frame(plot, 600, 600);

        ArrayList<IDataSet> dataSets = new ArrayList<>(2);

        // The gradient colors for pre-defined gradients are tabularized (see classes in color.gradient.gradients).
        // The below line retrieves colors for the Viridis gradient (256 uniformly distributed colors defined as
        // [0-1] RGB tuples). This array will be used later to construct custom gradients.
        float [][] colors = Viridis.colors;

        {

            double[][] data = new double[][]
                    {
                            {0.0d, 0.6d},
                            {1.0d, 0.6d},
                    };


            // A gradient object consists of two primary fields: associations between the normalized [0-1] value and
            // a color (see color.gradient.ColorAssignment; the normalized values are kept sorted in ascending order);
            // and an interpolation flag. The interpolation flag is used when retrieving a color for which the input
            // normalized value is not stored. If the flag is true, the nearest normalized values to the requested one
            // are identified (smaller/equal and greater/equal; binary search is used) and their associated colors.
            // Then, a new color (object) whose RGB values are linearly interpolated is constructed and returned.
            // However, if the flag is false, the already kept object, a color whose value is the closest to the
            // requested one, is returned. The former option is not recommended as it may end with constructing
            // an enormous number of new objects on the fly. The latter object is preferred, but the number of
            // pre-defined colors stored in the gradient must be reasonably large (e.g., 256).

            // The below line constructs a gradient that consists of  256 colors derived from the input color table
            // and with an interpolation flag set to false. It is the recommended option for custom gradient construction
            // (high fidelity, low computational burden, and low memory consumption).
            Gradient gradient = Gradient.getGradientFromColorMatrix(colors, "Viridis (256; no interpolation)", 256, false);
            LineStyle ls = new LineStyle(4.0f, gradient, 0);
            dataSets.add(DataSet.getFor2D("Viridis (256; no interpolation)", data, ls));
        }
        {
            double[][] data = new double[][]
                    {
                            {0.0d, 0.5d},
                            {1.0d, 0.5d},

                    };
            // This setting differs from the previous one in terms of the number of stored colors (the four colors
            // stored are four colors in the input color table that are most equally spaced, under the assumption of
            // a uniform distribution of associated values). Since the interpolation mode is off, reducing the number
            // of colors will result in gradient discretization.
            Gradient gradient = Gradient.getGradientFromColorMatrix(colors, "Viridis (16; no interpolation)", 16, false);
            LineStyle ls = new LineStyle(4.0f, gradient, 0);
            dataSets.add(DataSet.getFor2D("Viridis (16; no interpolation)", data, ls));
        }
        {
            double[][] data = new double[][]
                    {
                            {0.0d, 0.4d},
                            {1.0d, 0.4d},

                    };
            // This setting uses 3 tabularized colors, but the interpolation is enabled. The latter implies that there
            // will be no gradient discretization this time. However, since the number of stored colors is deficient,
            // the color fidelity will be diminished (the resulting gradient will slightly differ from the expected one,
            // i.e., as there would be an infinite number of input colors spanned on the [0-1] value interval).
            Gradient gradient = Gradient.getGradientFromColorMatrix(colors, "Viridis (3; interpolation)", 3, true);
            LineStyle ls = new LineStyle(4.0f, gradient, 0);
            dataSets.add(DataSet.getFor2D("Viridis (3; interpolation)", data, ls));
        }
        {
            double[][] data = new double[][]
                    {
                            {0.0d, 0.3d},
                            {1.0d, 0.3d},

                    };

            // The fourth setting is of the highest fidelity but also imposes the highest computational (and memory)
            // burden (there are many pre-defined color assignments, and the requested colors should be interpolated).
            Gradient gradient = Gradient.getGradientFromColorMatrix(colors, "Viridis (256; interpolation)", 256, true);
            LineStyle ls = new LineStyle(4.0f, gradient, 0);
            dataSets.add(DataSet.getFor2D("Viridis (256; interpolation)", data, ls));
        }

        {
            double[][] data = new double[][]
                    {
                            {0.0d, 0.2d},
                            {1.0d, 0.2d},
                    };

            // The next setting shows how to create a custom gradient from a custom color table (4 colors, interpolation).
            // Note that the table's entries are in the RGBA format (0-1 floats).
            float [][] myColors = new float[][]
                    {
                            {0.0f, 0.0f, 0.0f, 1.0f}, // black
                            {0.0f, 0.0f, 1.0f, 1.0f}, // blue
                            {1.0f, 0.0f, 0.0f, 1.0f}, // red
                            {0.0f, 1.0f, 0.0f, 1.0f}  // green
                    };

            Gradient gradient = Gradient.getGradientFromColorMatrix(myColors, "My gradient #1 (4; interpolation)", 4, true);
            LineStyle ls = new LineStyle(4.0f, gradient, 0);
            dataSets.add(DataSet.getFor2D("My gradient #1 (4; interpolation)", data, ls));
        }

        {
            double[][] data = new double[][]
                    {
                            {0.0d, 0.1d},
                            {1.0d, 0.1d},
                    };

            // The last setting shows how to create a custom gradient from color assignments (4 colors, interpolation).
            // Note that the colors are in the RGBA format (0-1 floats).
            ArrayList<ColorAssignment> cas = new ArrayList<>();
            cas.add(new ColorAssignment(0.0f, new Color(0.0f, 0.0f, 0.0f, 1.0f)));
            cas.add(new ColorAssignment(0.2f, new Color(0.0f, 0.0f, 1.0f, 1.0f)));
            cas.add(new ColorAssignment(0.8f, new Color(1.0f, 0.0f, 0.0f, 1.0f)));
            cas.add(new ColorAssignment(1.0f, new Color(0.0f, 1.0f, 0.0f, 1.0f)));

            Gradient gradient = new Gradient(cas, "My gradient #2 (4; interpolation)",  true);
            LineStyle ls = new LineStyle(4.0f, gradient, 0);
            dataSets.add(DataSet.getFor2D("My gradient #2 (4; interpolation)", data, ls));
        }

        plot.getModel().setDataSets(dataSets, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
