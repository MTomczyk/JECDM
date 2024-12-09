package t1_10.t1_visualization_module.t4_parallel_coordinate_plot_2d.t3;

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
 * This tutorial focuses on creating and displaying a basic 2D parallel coordinate plot (using gradient).
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial3
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

        // Define the vertical axes' labels:
        pP._axesTitles = ParallelCoordinatePlot2D.getAxesTitlesAsSequence("D", dims);

        // Use a customized white scheme (make space for colorbar)
        pP._scheme = WhiteScheme.getForPCP2D();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(dims, Range.getNormalRange(), true);

        // Add a colorbar that is linked to the first dimension (index of 0).
        pP._colorbar = new Colorbar(Gradient.getPlasmaGradient(), "COLORBAR", new FromDisplayRange(pP._pDisplayRangesManager._DR[0], 10));

        // This flag can be set to true to turn off those ticks that would obscure plot readability.
        pP._disableOverlappingTicks = true;

        ParallelCoordinatePlot2D plot = new ParallelCoordinatePlot2D(pP);

        Frame frame = new Frame(plot, 0.8f, 0.4f);
        //Frame frame = new Frame(plot, 1400, 600);

        {
            // Let's create 2000 data points artificially.
            int lines = 2000;

            double[][] data = new double[lines][dims];
            IRandom R = new MersenneTwister64(1);

            // The attribute values will be drawn from the Gaussian distribution.
            // The mean is drawn randomly (for each attribute), while the std is defined as (1 - mean)^2
            double[] means = new double[dims];
            double[] std = new double[dims];
            for (int i = 0; i < dims; i++)
            {
                means[i] = R.nextDouble();
                std[i] = Math.pow(1.0d - means[i], 2.0d);
            }

            // Create data points here.
            for (int i = 0; i < lines; i++)
                for (int j = 0; j < dims; j++)
                    data[i][j] = means[j] + R.nextGaussian() * std[j];

            // Render the data points using a gradient color determined based on the first attribute value.
            LineStyle ls = new LineStyle(0.25f, Gradient.getPlasmaGradient(), 0);
            IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 1", dims, data, null, ls);
            plot.getModel().setDataSet(ds, true);
        }

        // The below lines specify a decimal number formatter to all vertical axes and the colorbar axis. Note that the
        // axes returned by the getAxes() method are stored as follows: the first (zero index) axis is linked to the
        // X-axis, while the remaining n axes represent the vertical ones. Hence, the ``1 + i'' index is used in the
        // loop below to access the vertical axes.
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
