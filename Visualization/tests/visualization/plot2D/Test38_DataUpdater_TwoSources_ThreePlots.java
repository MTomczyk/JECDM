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
public class Test38_DataUpdater_TwoSources_ThreePlots
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
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};
        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Z-xis Test (j)",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
        Plot2D plotA = new Plot2D(pP);

        pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Z-Axis Test (j)";
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
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 2, 1};
        pP._colorbar = new Colorbar(Gradient.getInfernoGradient(), "Y-axis Test (j)",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
        Plot2D plotB = new Plot2D(pP);

        pP = new Plot2D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "Y-axis Test (j)";
        pP._yAxisTitle = "Z-Axis Test (j)";
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
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(new Range(-1.0d, 1.0d), false, false);
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{1, 2, 0};
        pP._colorbar = new Colorbar(Gradient.getBlueRedGradient(), "X-axis Test (j)",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
        Plot2D plotC = new Plot2D(pP);

        GridPlots GP = new GridPlots(new AbstractPlot[]{plotA, plotB, plotC}, 1, 3, 1);
        Frame.Params pF = Frame.Params.getParams(GP, 0.7f, 0.5f);


        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);

        int sets = 1000;
        int points = 1000;
        double[][][] data1 = new double[sets][points][3];
        IRandom R = new MersenneTwister64(1);

        for (int s = 0; s < sets; s++)
        {
            float d = (float) s / (sets - 1);
            d /= 4.0f;
            for (int i = 0; i < points; i++)
            {
                double a = R.nextDouble() * Math.PI * 2.0d;
                double b = R.nextDouble() * Math.PI * 2.0d;
                data1[s][i][0] = d * Math.cos(a) * Math.sin(b);
                data1[s][i][1] = d * Math.sin(a) * Math.sin(a);
                data1[s][i][2] = d * Math.cos(b);
            }
        }
        IDataSource source1 = new CyclicDataSource(data1);

        double[][][] data2 = new double[sets][points][3];

        for (int s = 0; s < sets; s++)
        {
            float d = (float) s / (sets - 1);
            d /= 4.0f;
            for (int i = 0; i < points; i++)
            {
                data2[s][i][0] = d * R.nextGaussian();
                data2[s][i][1] = d * R.nextGaussian();
                data2[s][i][2] = d * R.nextGaussian();
            }
        }
        IDataSource source2 = new CyclicDataSource(data2);


        IDataSet reference11 = DataSet.getFor2D("data set 1", new MarkerStyle(4.0f, Gradient.getViridisGradient(), 0, Marker.SQUARE));
        IDataSet reference12 = DataSet.getFor2D("data set 2", new MarkerStyle(4.0f, Gradient.getViridisGradient(), 0, Marker.SQUARE));
        IDataSet reference21 = DataSet.getFor2D("data set 1", new MarkerStyle(6.0f, Gradient.getInfernoGradient(), 1, Marker.CIRCLE));
        IDataSet reference22 = DataSet.getFor2D("data set 2", new MarkerStyle(6.0f, Gradient.getInfernoGradient(), 1, Marker.CIRCLE));
        IDataSet reference31 = DataSet.getFor2D("data set 1", new MarkerStyle(8.0f, Gradient.getRedBlueGradient(), 2, Marker.TRIANGLE_UP));
        IDataSet reference32 = DataSet.getFor2D("data set 2", new MarkerStyle(8.0f, Gradient.getRedBlueGradient(), 2, Marker.TRIANGLE_UP));


        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]{source1, source2};
        pDU._sourcesToProcessors = new SourceToProcessors[]{
                new SourceToProcessors(0),
                new SourceToProcessors(1)
        };

        pDU._dataProcessors = new IDataProcessor[]{
                new DataProcessor(false),
                new DataProcessor(false)
        };
        pDU._processorToPlots = new ProcessorToPlots[]{
                new ProcessorToPlots(new int[]{0, 1, 2}, new IDataSet[]{reference11, reference21, reference31}),
                new ProcessorToPlots(new int[]{0, 1, 2}, new IDataSet[]{reference12, reference22, reference32})
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
        plotC.getModel().notifyDisplayRangesChangedListeners();

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
