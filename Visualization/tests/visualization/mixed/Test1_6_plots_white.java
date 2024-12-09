package visualization.mixed;

import color.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import plot.dummy.DummyColorPlot;
import plot.heatmap.Heatmap2D;
import plot.heatmap.Heatmap3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import statistics.distribution.bucket.BucketCoordsTransform;
import statistics.distribution.bucket.transform.LinearlyThresholded;

import java.util.ArrayList;

/**
 * Tests basic architecture-related functionalities of classes responsible for plots visualization.
 * This test displays several plots.
 *
 * @author MTomczyk
 */
public class Test1_6_plots_white
{
    /**
     * Runs the visualization.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {

        AbstractPlot [] plots = new AbstractPlot[6];
        // plot 1
        {
            Plot2D.Params pP = new Plot2D.Params();
            pP._title = "Test (j)";
            pP._xAxisTitle = "X-axis Test (j)";
            pP._yAxisTitle = "Y-Axis Test (j)";
            pP._drawLegend = false;
            pP._scheme = new WhiteScheme();
            pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(-1.0d, 1.0d),
                    new Range(-1.0d, 1.0d));
            plots[0] = new Plot2D(pP);
        }
        // plot 2
        {
            int dims = 20;
            ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(dims);
            // pP._debugMode = true;
            pP._title = "Test (j)";
            pP._xAxisTitle = "Objectives";
            pP._yAxisTitle = "Y-Axis Test (j)";
            pP._axesTitles = new String[dims];
            for (int i = 0; i < dims; i++) pP._axesTitles[i] = "D" + (i + 1);
            pP._scheme = new WhiteScheme();
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(dims, new Range(-3.0d, 4.0d), false, false);
            plots[1] = new ParallelCoordinatePlot2D(pP);
        }
        // plot 3
        {
            Heatmap2D.Params pP = new Heatmap2D.Params();
            // pP._debugMode = true;
            pP._title = "Test heatmap";
            pP._xAxisTitle = "X - dimension";
            pP._yAxisTitle = "Y - dimension";
            pP._scheme = new WhiteScheme();
            pP._scheme._aligns.put(AlignFields.LEGEND, Align.LEFT_TOP);
            pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);

            Range RX = Range.getNormalRange();
            Range RY = Range.getNormalRange();
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(RX, RY);
            // play with parameters and check
            int xDiv = 100;
            int yDiv = 100;
            pP._xDiv = xDiv;
            pP._yDiv = yDiv;
            pP._verticalGridLinesWithBoxTicks = true;
            pP._horizontalGridLinesWithBoxTicks = true;
            pP._xAxisWithBoxTicks = false;
            pP._yAxisWithBoxTicks = false;
            pP._drawMainGridlines = false;
            pP._gradient = Gradient.getViridisGradient();
            pP._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(null, true, false);
            pP._colorbar = new Colorbar(pP._gradient, true, "Frequency",
                    new FromDisplayRange(pP._heatmapDisplayRange, 5));

            plots[2] = new Heatmap2D(pP);
        }
        // plot 4
        {
            Plot3D.Params pP = new Plot3D.Params();
            // pP._debugMode = true;
            pP._title = "Test (j)";
            pP._drawLegend = false;
            pP._xAxisTitle = "X-axis";
            pP._yAxisTitle = "Y-axis";
            pP._zAxisTitle = "Z-axis";
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D();
            plots[3] = new Plot3D(pP);
        }
        // plot 5
        {
            plots[4] = new DummyColorPlot(Color.WHITE);
        }
        // plot 6
        {
            Heatmap3D.Params pP = new Heatmap3D.Params();
            // pP._debugMode = true;
            pP._title = "Test heatmap";
            pP._xAxisTitle = "X - dimension";
            pP._yAxisTitle = "Y - dimension";
            pP._zAxisTitle = "Z - dimension";
            pP._scheme = new WhiteScheme();
            Range RX = Range.getNormalRange();
            Range RY = Range.getNormalRange();
            Range RZ = Range.getNormalRange();
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(RX, RY, RZ);
            pP._gradient = Gradient.getViridisGradient();
            // play with parameters and check
            pP._drawXAxis = true;
            pP._drawYAxis = true;
            pP._drawZAxis = true;
            // play with parameters and check
            int xDiv = 100;
            int yDiv = 100;
            int zDiv = 100;
            pP._xDiv = xDiv;
            pP._yDiv = yDiv;
            pP._zDiv = zDiv;
            pP._horizontalGridLinesWithBoxTicks = false;
            pP._verticalGridLinesWithBoxTicks = false;
            pP._depthGridLinesWithBoxTicks = false;
            plots[5] = new Heatmap3D(pP);
        }


        AbstractPlotsWrapper wrapper = new GridPlots(plots, 2, 3);
        Frame.Params pF = Frame.Params.getParams(wrapper, 0.6f);
        pF._debugMode = (args != null) && (args.length > 0) && (args[0].equals("T"));
        pF._title = "6 plots";
        Frame frame = new Frame(pF);

        // plot 1
        {
            ArrayList<IDataSet> dss = new ArrayList<>();
            {
                int points = 100000;
                double[][] data = new double[points][2];
                IRandom R = new MersenneTwister64(1);
                for (int i = 0; i < points; i++)
                {
                    data[i][0] = R.nextGaussian() * 0.35f;
                    data[i][1] = R.nextGaussian() * 0.35f;
                }
                MarkerStyle ms = new MarkerStyle(0.2f, Gradient.getViridisGradient(100, false), 0, Marker.SQUARE);
                IDataSet ds = DataSet.getFor2D("data", data, ms, null);
                dss.add(ds);
            }
            plots[0].getModel().setDataSets(dss, true, false);
        }
        // plot 2
        {
            int dims = 20;
            int lines = 1000;
            double[][] data = new double[lines][dims];
            IRandom R = new MersenneTwister64(1);
            double[] means = new double[dims];
            double[] std = new double[dims];
            for (int i = 0; i < dims; i++)
            {
                means[i] = R.nextDouble();
                std[i] = 1.0d - means[i];
            }

            for (int i = 0; i < lines; i++)
                for (int j = 0; j < dims; j++)
                    data[i][j] = means[j] + R.nextGaussian() * std[j];

            LineStyle ls = new LineStyle(0.25f, Gradient.getPlasmaGradient(), 1);
            IDataSet ds = DataSet.getForParallelCoordinatePlot2D("DS 1", dims, data, null, ls);
            plots[1].getModel().setDataSet(ds, true, false);
        }
        // plot 3
        {
            int xDiv = 100;
            int yDiv = 100;
            Range RX = Range.getNormalRange();
            Range RY = Range.getNormalRange();
            double[][] data = new double[yDiv][xDiv];
            BucketCoordsTransform B = new BucketCoordsTransform(2, new int[]{xDiv, yDiv},
                    new Range[]{RY, RX}, new LinearlyThresholded());

            IRandom R = new MersenneTwister64(0);
            int trials = 100000;
            for (int i = 0; i < trials; i++)
            {
                float x = (float) (0.25f + R.nextGaussian() * 0.2d);
                float y = (float) (0.75f + R.nextGaussian() * 0.2d);
                int[] c = B.getBucketCoords(new double[]{x, y});
                if (c == null) continue;
                data[c[1]][c[0]]++;
            }
            ((Heatmap2D) plots[2]).getModel().setDataAndPerformProcessing(data, true);
        }
        // plot 4
        {
            IRandom R = new MersenneTwister64(0);
            int objects = 100000;

            double[][] d = new double[objects][3];
            for (int i = 0; i < objects; i++)
            {
                d[i][0] = R.nextGaussian();
                d[i][1] = R.nextGaussian();
                d[i][2] = R.nextGaussian();
            }

            ArrayList<IDataSet> dss = new ArrayList<>();
            LineStyle edge = new LineStyle(1.0f, Gradient.getPlasmaGradient(10, false), 1);
            MarkerStyle ms = new MarkerStyle(0.01f, Gradient.getViridisGradient(10, false), 0, Marker.CUBE_3D, edge);
            dss.add(DataSet.getFor3D("ds1", d, ms, null));
            plots[3].getModel().setDataSets(dss, true, false);
        }
        // plots 5
        {
            Range RX = Range.getNormalRange();
            Range RY = Range.getNormalRange();
            Range RZ = Range.getNormalRange();
            int xDiv = 100;
            int yDiv = 100;
            int zDiv = 100;

            double[][][] data = new double[zDiv][yDiv][xDiv];
            BucketCoordsTransform B = new BucketCoordsTransform(3, new int[]{xDiv, yDiv, zDiv},
                    new Range[]{RX, RY, RZ}, new LinearlyThresholded());

            IRandom R = new MersenneTwister64(0);
            int trials = 1000000;
            for (int i = 0; i < trials; i++)
            {
                float x = (float) (0.5f + R.nextGaussian() * 0.2d);
                float y = (float) (0.5f + R.nextGaussian() * 0.2d);
                float z = (float) (0.5f + R.nextGaussian() * 0.2d);
                int[] c = B.getBucketCoords(new double[]{x, y, z});
                if (c != null) data[c[2]][c[1]][c[0]]++;
            }

            ((Heatmap3D) plots[5]).getModel().setDataAndPerformProcessing(data);
        }

        frame.setVisible(true);

    }
}
