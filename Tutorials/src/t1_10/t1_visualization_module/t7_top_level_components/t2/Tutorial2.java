package t1_10.t1_visualization_module.t7_top_level_components.t2;

import color.gradient.Gradient;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import frame.Frame;
import plot.AbstractPlot;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import t1_10.t1_visualization_module.t7_top_level_components.shared.DataSets;
import t1_10.t1_visualization_module.t7_top_level_components.shared.Plot;
import t1_10.t1_visualization_module.t7_top_level_components.shared.plotswrapper.PlotsAndButton;
import t1_10.t1_visualization_module.t7_top_level_components.shared.plotwrapper.PlotAndButton;

/**
 * This tutorial shows how to customize the plots wrapper (i.e., {@link plotswrapper.AbstractPlotsWrapper}).
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode", "ExtractMethodRecommender"})
public class Tutorial2
{
    /**
     * Runs the application.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // This tutorial uses two plots: left (2D) and right (3D):
        AbstractPlot plotLeft = Plot.getPlot2D_ForGaussianDistribution();
        AbstractPlot plotRight = Plot.getPlot3D_ForGaussianDistribution();

        IRandom R = new MersenneTwister64(0);

        // A dedicated dataset is constructed for each plot (2D and 3D Gaussian distributions).
        IDataSet dataSetLeft = DataSets.getDataSetFor2DGaussianDistribution("Dataset left",
                new MarkerStyle(1.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE), 0.25f, R);
        IDataSet dataSetRight = DataSets.getDataSetFor3DGaussianDistribution("Dataset right",
                new MarkerStyle(0.025f, Gradient.getViridisGradient(), 3, Marker.SPHERE_LOW_POLY_3D), 0.25f, R);

        // The following lines instantiate plots wrappers (plot + button) for each plot.
        PlotAndButton plotAndButtonLeft = new PlotAndButton(plotLeft, "Button label (left)");
        PlotAndButton plotAndButtonRight = new PlotAndButton(plotRight, "Button label (right)");

        // Next, a custom plot wrapped is instantiated. It accordingly places the left and right plots (wrappers) and
        // adds additional button below them.
        PlotsAndButton plotsAndButton = new PlotsAndButton(plotAndButtonLeft, plotAndButtonRight, "Main button");

        // The plots wrapper can also be instantiated explicitly via the params container, which
        // provides means for further customization:
        //PlotsAndButton.Params pPAB = new PlotsAndButton.Params(plotAndButtonLeft, plotAndButtonRight, "Main button");
        //pPAB._noUpdatersQueues = 2;
        //pPAB._animatorFPS = 60;
        //pPAB._interactorFPS = 60;
        //PlotsAndButton plotsAndButton = new PlotsAndButton(pPAB);

        Frame frame = new Frame(plotsAndButton, 0.5f, 0.5f);

        plotLeft.getModel().setDataSet(dataSetLeft, true);
        plotRight.getModel().setDataSet(dataSetRight, true);

        // Each plot should be assigned a unique menu (unique object)
        {
            RightClickPopupMenu menu = new RightClickPopupMenu();
            menu.addItem(new SaveAsImage());
            plotLeft.getController().addRightClickPopupMenu(menu);
        }
        {
            RightClickPopupMenu menu = new RightClickPopupMenu();
            menu.addItem(new SaveAsImage());
            plotRight.getController().addRightClickPopupMenu(menu);
        }

        frame.setVisible(true);
    }
}
