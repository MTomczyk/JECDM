package tools.ivemo.heatmap.visualization;

import frame.Frame;
import plot.AbstractPlot;
import plot.heatmap.Heatmap2D;
import plot.heatmap.Heatmap3D;
import plot.heatmap.utils.Coords;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.GridPlots;
import plotwrapper.AbstractPlotWrapper;
import scheme.enums.ColorFields;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;

import java.awt.*;

/**
 * Returns plot frame for displaying heatmaps.
 *
 * @author MTomczyk
 */

public class FrameFactory
{
    /**
     * Parameterized constructor.
     *
     * @param FP           frame params
     * @param PP           loaded plot params
     * @param sortedCoords heatmap data entries (coordinates) sorted in ascending order of values (each row per one plot)
     * @return frame object
     * @throws Exception the exception can be thrown (e.g., when the input data is invalid)
     */
    public static Frame getFrame(FrameParams FP, PlotParams[] PP, Coords[][] sortedCoords) throws Exception
    {

        AbstractPlotWrapper[] plotWrappers = null;
        AbstractPlot[] plots;
        Frame frame = null;

        try
        {
            plotWrappers = new AbstractPlotWrapper[PP.length];
            plots = new AbstractPlot[PP.length];

            for (int i = 0; i < PP.length; i++)
            {
                if (PP[i]._dimensions == 2)
                {
                    Heatmap2D h2d = PlotFactory.getHeatmap2D(PP[i]);
                    plots[i] = h2d;
                    plotWrappers[i] = new HeatmapPanel2D(h2d, PP[i]);
                }
                else if (PP[i]._dimensions == 3)
                {
                    Heatmap3D h3d = PlotFactory.getHeatmap3D(PP[i]);
                    plots[i] = h3d;
                    plotWrappers[i] = new HeatmapPanel3D(h3d, PP[i]);
                }
            }

            AbstractPlotsWrapper plotsWrapper = new GridPlots(plotWrappers, 1, plotWrappers.length);
            int actualWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
            int actualHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
            int referenceSize = (int) Math.min(actualWidth * FP._frameSize + 0.5d, actualHeight * FP._frameSize + 0.5d);
            int trueWidth = referenceSize * PP.length;

            frame = new Frame(plotsWrapper, trueWidth, referenceSize);
            frame.setTitle(FP._frameTitle);
            frame.setBackground(PP[0]._scheme.getColors(null, ColorFields.PLOT_BACKGROUND));
            if (FP._printFPS) plotsWrapper.getController().addReporter(new RenderGenerationTimesReporter(
                    frame.getModel().getGlobalContainer(), 1));

            for (int i = 0; i < PP.length; i++)
            {
                double[] sV = new double[sortedCoords[i].length];
                for (int j = 0; j < sortedCoords[i].length; j++) sV[j] = sortedCoords[i][j].getValue();


                if (PP[i]._dimensions == 2)
                    ((Heatmap2D) plots[i]).getModel().setDataAndPerformProcessing(sortedCoords[i], sV);
                else if (PP[i]._dimensions == 3)
                    ((Heatmap3D) plots[i]).getModel().setDataAndPerformProcessing(sortedCoords[i], sV);
            }

            return frame;

        } catch (Exception e)
        {
            if (plotWrappers != null)
            {
                for (AbstractPlotWrapper pw : plotWrappers)
                    if (pw != null) pw.setVisible(false);
            }

            if (frame != null)
            {
                frame.setVisible(false);
                frame.dispose();
            }

            throw e;
        }
    }
}
