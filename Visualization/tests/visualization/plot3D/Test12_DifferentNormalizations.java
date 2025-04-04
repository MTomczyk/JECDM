package visualization.plot3D;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.axis.ticksupdater.ITicksDataGetter;
import component.drawingarea.DrawingArea3D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.Plot3D;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import space.normalization.minmax.Gamma;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays 1 plot ({@link Plot3D}) on a frame.
 *
 * @author MTomczyk
 */
public class Test12_DifferentNormalizations
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        Plot3D.Params pP = new Plot3D.Params();
        // pP._debugMode = true;
        pP._title = "Test (j)";
        pP._xAxisTitle = "X-axis Test (j)";
        pP._yAxisTitle = "Y-Axis Test (j)";
        pP._zAxisTitle = "Z-Axis Test (j)";

        pP._drawXAxis = true;
        pP._drawYAxis = true;
        pP._drawZAxis = true;
        pP._drawLegend = false;
        pP._useAlphaChannel = false;
        pP._scheme = new WhiteScheme();

        pP._xAxisTitle = "X-axis";
        pP._yAxisTitle = "Y-axis";
        pP._zAxisTitle = "Z-axis";

        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(
                new Range(-2.0f, 2.0f),
                new Range(-2.0f, 2.0f),
                new Range(-2.0f, 2.0f));
        pP._pDisplayRangesManager._DR[0].setNormalizerAndPreserveMinMax(new Gamma(0.5f));
        pP._pDisplayRangesManager._DR[1].setNormalizerAndPreserveMinMax(new Gamma(2.0f));
        pP._pDisplayRangesManager._DR[2].setNormalizerAndPreserveMinMax(new Gamma(3.0f));

        Plot3D plot = new Plot3D(pP);

        ITicksDataGetter xTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[0],5);
        xTDG.setForcedUnnormalizedLocations(new float[]{-2.0f, -1.0f, 0.0f, 1.0f, 2.0f});
        ITicksDataGetter yTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[1],5);
        yTDG.setForcedUnnormalizedLocations(new float[]{-2.0f, -1.0f, 0.0f, 1.0f, 2.0f});
        ITicksDataGetter zTDG = new FromDisplayRange(pP._pDisplayRangesManager._DR[2],5);
        zTDG.setForcedUnnormalizedLocations(new float[]{-2.0f, -1.0f, 0.0f, 1.0f, 2.0f});
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setGridlinesTicksDataGetters(xTDG, yTDG, zTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForXAxes(xTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForYAxes(yTDG);
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setTicksDataGetterForZAxes(zTDG);

        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setLabelsForXAxes(new String[]{"A", "B", "C"});
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setLabelsForYAxes(new String[]{"1", "2", "3", "4"});
        ((DrawingArea3D) plot.getComponentsContainer().getDrawingArea()).setLabelsForZAxes(new String[]{"ABC", "XYZ", "test1", "test2"});

        Frame.Params pF = Frame.Params.getParams(plot, 0.5f);

        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "TEST TITLE";

        Frame frame = new Frame(pF);
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer()));

        IRandom R = new MersenneTwister64(0);
        int objects = 1000000;

        double[][] d = new double[objects][3];
        for (int i = 0; i < objects; i++)
        {
            d[i][0] = R.nextGaussian();
            d[i][1] = R.nextGaussian();
            d[i][2] = R.nextGaussian();
        }

        ArrayList<IDataSet> dss = new ArrayList<>();
        MarkerStyle ms = new MarkerStyle(0.1f, Gradient.getViridisGradient(), 0, Marker.POINT_3D);
        //MarkerStyle ms = new MarkerStyle(0.01f, Color.RED, Marker.CUBE);

        dss.add(DataSet.getFor3D("ds1", d, ms, null));
        plot.getModel().setDataSets(dss, true);

        plot.getModel().notifyDisplayRangesChangedListeners();
        frame.setVisible(true);

    }
}
