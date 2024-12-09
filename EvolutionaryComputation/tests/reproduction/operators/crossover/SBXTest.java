package reproduction.operators.crossover;

import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.valuecheck.Wrap;
import space.Range;
import statistics.distribution.DiscreteDistribution;
import visualization.IVisualization;
import visualization.Visualization;

/**
 * Several tests (visualizations) for the SBX crossover operator
 *
 * @author MTomczyk
 */
class SBXTest
{
    /**
     * Performs visual experiment testing the performance of the SBX with SP operator.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        double[] distributionIndexes = new double[]{1.0d, 5.0d, 10.0d, 20.0d};
        double[][] values = new double[][]{{0.1d, 0.9d}, {0.2d, 0.8d}, {0.5d, 0.7d}};
        int trials = 1000000;
        int buckets = 100;

        AbstractPlot[] plots = new AbstractPlot[distributionIndexes.length * values.length];
        int plotIdx;
        IRandom R = new MersenneTwister64(0);


        for (int value = 0; value < values.length; value++)
        {
            for (int index = 0; index < distributionIndexes.length; index++)
            {
                Plot2D.Params params = new Plot2D.Params();
                params._title = "Dist. index = " + String.format("%.1f", distributionIndexes[index]) + " Val 1. = " + String.format("%.1f", values[value][0]) + " Val 2. = " + String.format("%.1f", values[value][1]);
                params._xAxisTitle = "Value";
                params._yAxisTitle = "Probability";
                params._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), new Range(0.0d, 0.2d));
                plotIdx = value * distributionIndexes.length + index;
                plots[plotIdx] = new Plot2D(params);
            }
        }

        GridPlots gp = new GridPlots(plots, values.length, distributionIndexes.length);
        Frame.Params pF = Frame.Params.getParams(gp, 0.75f, 0.75f);
        pF._title = "PM distribution test";
        IVisualization V = new Visualization(new Frame(pF));

        for (int value = 0; value < values.length; value++)
        {
            for (int index = 0; index < distributionIndexes.length; index++)
            {
                DiscreteDistribution DD = new DiscreteDistribution();
                DD.init(buckets, 0.0d, 1.0d);

                SBX.Params pSBX = new SBX.Params(1.0d, distributionIndexes[index]);
                pSBX._valueCheck = new Wrap();

                SBX sbx = new SBX(pSBX);

                for (int t = 0; t < trials; t++)
                {
                    double[] p1 = new double[]{values[value][0]};
                    double[] p2 = new double[]{values[value][1]};
                    double[] o = sbx.crossover(p1, p2, R);
                    DD.add(o[0]);
                }

                assert DD.getNoSamples() == trials;
                double[] dist = DD.getNormalizedDistribution();

                double[][] data = new double[buckets][];
                for (int j = 0; j < buckets; j++)
                {
                    double x = DD.getBucketsBounds()[j].getLeft() + DD.getBucketsBounds()[j].getInterval() / 2.0d;
                    double y = dist[j];
                    data[j] = new double[]{x, y};
                }

                plotIdx = value * distributionIndexes.length + index;
                IDataSet ds = DataSet.getFor2D("Probability distribution", data, new LineStyle(1.0f, color.gradient.Color.RED));
                plots[plotIdx].getModel().setDataSet(ds, false);
            }
        }

        V.display();
    }
}