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
 * Several tests (visualizations) for the PM mutation operator.
 *
 * @author MTomczyk
 */
class PMTest
{
    /**
     * Runs visual experiment testing outcomes of the PM operator.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        double[] distributionIndexes = new double[]{1.0d, 5.0d, 10.0d, 20.0d};
        double[] values = new double[]{0.0d, 0.2d, 0.5d, 0.8d, 1.0d};
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
                params._title =   "Dist. index = " + String.format("%.1f", distributionIndexes[index]) + " Val. = " +  String.format("%.1f", values[value]);
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

                PM.Params pPM = new PM.Params(1.0d, distributionIndexes[index]);
                pPM._valueCheck = new Wrap();
                PM pm = new PM(pPM);

                for (int t = 0; t < trials; t++)
                {
                    double[] v = new double[]{values[value]};
                    pm.mutate(v, R);
                    DD.add(v[0]);
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
                plots[plotIdx].getModel().setDataSet(ds, true);
            }
        }

        V.display();
    }
}