package visualization.plot2D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import updater.*;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot2D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test37_DataUpdater_TwoPlots_Cumulative
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot2D.Params pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = false;
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "distance",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
        Plot2D plotA = new Plot2D(pP);

        pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Distance";
        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawLegend = false;
        pP._clipDrawingArea = true;
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.3f);
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(null, true, false);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 2, 1};
        pP._colorbar = new Colorbar(Gradient.getInfernoGradient(), "Y-axis Test (j)",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
        Plot2D plotB = new Plot2D(pP);

        GridPlots GP = new GridPlots(new AbstractPlot[]{plotA, plotB}, 1, 2, 2);
        Frame.Params pF = Frame.Params.getParams(GP, 0.7f, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        int sets = 1000;
        int points = 10;
        double[][][] data = new double[sets][points][3];
        IRandom R = new MersenneTwister64(1);

        for (int s = 0; s < sets; s++)
        {
            float d = (float) s / (sets - 1);
            d /= 4.0f;
            for (int i = 0; i < points; i++)
            {
                data[s][i][0] = R.nextGaussian() * d;
                data[s][i][1] = R.nextGaussian() * d;
                data[s][i][2] = Math.sqrt(data[s][i][0] * data[s][i][0] + data[s][i][1] * data[s][i][1]);
            }
        }

        IDataSource source = new CyclicDataSource(data);
        IDataSet reference1 = DataSet.getFor2D("data set 1", new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.SQUARE));
        IDataSet reference2 = DataSet.getFor2D("data set 2", new MarkerStyle(2.0f, Gradient.getInfernoGradient(), 1, Marker.SQUARE));

        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]{source};
        pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
        pDU._dataProcessors = new IDataProcessor[]{new DataProcessor(true)};
        pDU._processorToPlots = new ProcessorToPlots[]{
          new ProcessorToPlots(new int[]{0, 1}, new IDataSet[]{reference1, reference2})
        };

        DataUpdater DU;
        try
        {
            DU = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }


        frame.setVisible(true);
        plotA.getModel().notifyDisplayRangesChangedListeners();
        plotB.getModel().notifyDisplayRangesChangedListeners();

        for (int i = 0; i < 1000; i++)
        {
            if (frame.isTerminating()) break;
            DU.update();
            try
            {
                Thread.sleep(5);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

    }
}
