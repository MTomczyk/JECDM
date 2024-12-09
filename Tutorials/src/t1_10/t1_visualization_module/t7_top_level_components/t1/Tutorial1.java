package t1_10.t1_visualization_module.t7_top_level_components.t1;

import color.gradient.Gradient;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import frame.Frame;
import plot.AbstractPlot;
import plotswrapper.PlotsWrapper;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import t1_10.t1_visualization_module.t7_top_level_components.shared.DataSets;
import t1_10.t1_visualization_module.t7_top_level_components.shared.Plot;
import t1_10.t1_visualization_module.t7_top_level_components.shared.plotwrapper.PlotAndButton;

/**
 * This tutorial shows how to customize the plot wrapper (i.e., {@link plotwrapper.AbstractPlotWrapper}).
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode", "ExtractMethodRecommender"})
public class Tutorial1
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Let's create a 2D plot first.
        AbstractPlot plot = Plot.getPlot2D_ForGaussianDistribution();

        IRandom R = new MersenneTwister64(0);

        // The following line generates a 2D data set (gaussian distribution).
        IDataSet dataSet = DataSets.getDataSetFor2DGaussianDistribution("Dataset",
                new MarkerStyle(1.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE), 0.25f, R);

        // Plot customization:
        // First, a custom plot wrapper object is instantiated (this custom implementation wraps the input plot and
        // adds a button with the specified label).
        PlotAndButton plotAndButton = new PlotAndButton(plot, "Button label");

        // Next, a default plots wrapper (aggregates plot wrappers) is instantiated (wraps the PlotAndButton wrapped
        // created above).
        PlotsWrapper plotsWrapper = new PlotsWrapper(plotAndButton);

        // Finally, a frame object is constructed.
        Frame frame = new Frame(plotsWrapper, 0.5f);

        // Reminder: dataset can only be uploaded after the frame is initialized.
        plot.getModel().setDataSet(dataSet, true);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot.getController().addRightClickPopupMenu(menu);

        frame.setVisible(true);
    }
}
