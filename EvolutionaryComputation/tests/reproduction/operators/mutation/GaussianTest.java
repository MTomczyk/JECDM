package reproduction.operators.mutation;

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
 * Several tests (visualizations) for the Gaussian mutation operator.
 *
 * @author MTomczyk
 */
class GaussianTest
{
    /**
     * Performs visual experiment testing outcomes of the gaussian mutation operator.
     * @param args not used
     */
    public static void main(String[] args)
    {
        double [] stds = new double[]{0.1d, 0.2d, 0.5d};
        double [] values = new double[]{0.2d, 0.5d, 0.8d};
        int trials = 100000;
        int buckets = 100;

        AbstractPlot [] plots = new AbstractPlot[stds.length * values.length];
        int plotIdx;
        IRandom R = new MersenneTwister64(0);

        for (int value = 0; value < values.length; value++)
        {
            for (int std = 0; std < stds.length; std++)
            {
                Plot2D.Params params = new Plot2D.Params();
                params._title =   "Val. = " + String.format("%.1f", values[value]) + " Std. = " + String.format("%.1f", stds[std]);
                params._xAxisTitle = "Value";
                params._yAxisTitle = "Probability";
                params._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), new Range(0.0d, 0.2d));
                plotIdx = value * stds.length + std;
                plots[plotIdx] = new Plot2D(params);
            }
        }

        GridPlots gp = new GridPlots(plots, values.length, stds.length);
        Frame.Params pF = Frame.Params.getParams(gp, 0.75f, 0.75f);
        pF._title = "Gaussian distribution test";
        IVisualization V = new Visualization(new Frame(pF));

        for (int value = 0; value < values.length; value++)
        {
            for (int std = 0; std < stds.length; std++)
            {
                double[][] data = new double[buckets][];
                for (int j = 0; j < buckets; j++)
                {
                    DiscreteDistribution DD = new DiscreteDistribution();
                    DD.init(buckets, 0.0d, 1.0d);

                    Gaussian.Params pG = new Gaussian.Params(1.0d, stds[std]);
                    pG._valueCheck = new Wrap();
                    Gaussian G = new Gaussian(pG);

                    for (int t = 0; t < trials; t++)
                    {
                        double[] v = new double[]{values[value]};
                        G.mutate(v, R);
                        DD.add(v[0]);
                    }

                    assert DD.getNoSamples() == trials;
                    double[] dist = DD.getNormalizedDistribution();

                    double x = DD.getBucketsBounds()[j].getLeft() + DD.getBucketsBounds()[j].getInterval() / 2.0d;
                    double y = dist[j];
                    data[j] = new double[]{x, y};
                }

                plotIdx = value * stds.length + std;
                IDataSet ds = DataSet.getFor2D("Probability distribution", data, new LineStyle(1.0f, color.gradient.Color.RED));
                plots[plotIdx].getModel().setDataSet(ds, true);
            }
        }


        V.display();
    }
}