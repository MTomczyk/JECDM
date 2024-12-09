package t1_10.t1_visualization_module.t4_parallel_coordinate_plot_2d.t4;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * This tutorial focuses on creating and displaying a basic 2D parallel coordinate plot (using custom mapping).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial4
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // The below field is used to parameterize the number of dimensions
        int dims = 5;
        ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(dims);

        pP._xAxisTitle = "Objectives";
        pP._yAxisTitle = "Y-Axis Test (j)";

        pP._axesTitles = ParallelCoordinatePlot2D.getAxesTitlesAsSequence("D", dims);
        for (int i = 0; i < dims; i++) pP._axesTitles[i] = "D" + (i + 1);

        // Use a customized white scheme (make space for colorbar)
        pP._scheme = WhiteScheme.getForPCP2D();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        // This tutorial shows how to employ a custom display range to color the data set based on a custom attribute.
        // The parallel coordinate plot allows the use of custom display ranges, but they must be positioned explicitly
        // between the first M display ranges associated with the parallel coordinate lines and the last display range
        // reserved for the X-axis. For this reason, the display ranges params container is established manually in the
        // following lines. The first "dims" display ranges are assumed to be connected with the Y-dimensions. The next
        // display range will be associated with the custom attribute. The final display range is fixed at the [0, 1]
        // interval and will be connected with the X-axis.
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[dims + 2];
        for (int i = 0; i < dims; i++)
            pP._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), true);
        pP._pDisplayRangesManager._DR[dims] = new DisplayRangesManager.DisplayRange(null, true); // custom
        pP._pDisplayRangesManager._DR[dims + 1] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false); // for X-axis
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[dims + 1];
        for (int i = 0; i < dims + 1; i++) pP._pDisplayRangesManager._attIdx_to_drIdx[i] = i; // the x-axis does not require mapping

        // The colorbar is linked to the custom display range:
        pP._colorbar = new Colorbar(Gradient.getPlasmaGradient(), "COLORBAR", new FromDisplayRange(pP._pDisplayRangesManager._DR[dims], 10));
        pP._disableOverlappingTicks = true;

        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);

        //Frame frame = new Frame(plot, 0.8f, 0.4f);
        Frame frame = new Frame(plot, 1400, 600);

        {
            int lines = 2000;

            double[][] data = new double[lines][dims + 1];
            IRandom R = new MersenneTwister64(1);

            double[] means = new double[dims];
            double[] std = new double[dims];
            for (int i = 0; i < dims; i++)
            {
                means[i] = R.nextDouble();
                std[i] = Math.pow(1.0d - means[i], 2.0d);
            }

            // This time, the data point includes an additional attribute defined as a sum of all preceding ones.
            for (int i = 0; i < lines; i++)
            {
                double sum = 0;
                for (int j = 0; j < dims; j++)
                {
                    data[i][j] = means[j] + R.nextGaussian() * std[j];
                    sum += data[i][j];
                }
                data[i][dims] = sum;
            }

            // Let's bind the line gradient with the custom display range.
            LineStyle ls = new LineStyle(0.25f, Gradient.getInfernoGradient(), dims);
            IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 1", dims, data, null, ls);
            plot.getModel().setDataSet(ds, true);
        }

        NumberFormat numberFormat = new DecimalFormat();
        for (int i = 0; i < dims; i++)
            plot.getComponentsContainer().getAxes()[1 + i].getTicksDataGetter().setNumberFormat(numberFormat);
        plot.getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(numberFormat);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);

    }
}
