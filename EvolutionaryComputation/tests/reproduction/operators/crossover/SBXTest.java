package reproduction.operators.crossover;

import color.Color;
import dataset.DSFactory2D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import frame.Frame;
import org.junit.jupiter.api.Test;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot2DFactory;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.valuecheck.Wrap;
import scheme.enums.ColorFields;
import space.Range;
import statistics.distribution.DiscreteDistribution;
import visualization.IVisualization;
import visualization.Visualization;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Several tests (visualizations) for the SBX crossover operator
 *
 * @author MTomczyk
 */
public class SBXTest
{
    /**
     * Runs the test (any exception check).
     */
    @Test
    public void test1()
    {
        String msg = null;
        try
        {

            main(null);
        } catch (RuntimeException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);
    }


    /**
     * Performs visual experiment testing the performance of the SBX with SP operator.
     *
     * @param args not used
     */
    public static void main(String[] args) throws RuntimeException
    {
        double[] distributionIndexes = new double[]{5.0d, 10.0d, 20.0d};
        double[][] values = new double[][]{{0.1d, 0.9d}, {0.2d, 0.8d}, {0.5d, 0.7d}};
        int trials = 10000000;
        int buckets = 100;

        AbstractPlot[] plots = new AbstractPlot[distributionIndexes.length * values.length];
        int plotIdx;
        IRandom R = new MersenneTwister64(0);

        for (int value = 0; value < values.length; value++)
        {
            for (int index = 0; index < distributionIndexes.length; index++)
            {
                String title = "Dist. index = " + String.format("%.1f", distributionIndexes[index]) + " Val 1. = " + String.format("%.1f", values[value][0]) + " Val 2. = " + String.format("%.1f", values[value][1]);
                Plot2D plot2D = Plot2DFactory.getPlot("Value", "Probability", Range.getNormalRange(), Range.get0R(0.25d),
                        11, 5, 1.5f, scheme -> scheme._colors.put(ColorFields.PLOT_BACKGROUND, new Color(250, 250, 250)), pP -> pP._title = title);
                plotIdx = value * distributionIndexes.length + index;
                plots[plotIdx] = plot2D;
            }
        }

        GridPlots gp = new GridPlots(plots, values.length, distributionIndexes.length);
        Frame.Params pF = Frame.Params.getParams(gp, 0.75f, 0.75f);
        pF._title = "SBX distribution test";
        IVisualization V = new Visualization(new Frame(pF));

        for (int value = 0; value < values.length; value++)
        {
            System.out.println(value);
            for (int index = 0; index < distributionIndexes.length; index++)
            {
                System.out.println(distributionIndexes[index]);
                DiscreteDistribution DD = new DiscreteDistribution();
                DD.init(buckets, 0.0d, 1.0d);

                SBX.Params pSBX = new SBX.Params(1.0d, distributionIndexes[index]);
                pSBX._valueCheck = new Wrap();

                SBX sbx = new SBX(pSBX);

                for (int t = 0; t < trials; t++)
                {
                    double[] p1 = new double[]{values[value][0]};
                    double[] p2 = new double[]{values[value][1]};
                    double[] o = sbx.crossover(p1, p2, R)._o;
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
                ArrayList<IDataSet> dataSets = new ArrayList<>();
                dataSets.add(DSFactory2D.getDS("V", new double[][]{{values[value][0], 0.0d}, {values[value][0], 1.0d},
                                {values[value][1], 0.0d}, {values[value][1], 1.0d}},
                        null, new LineStyle(1.0f, color.gradient.Color.BLACK), true));
                dataSets.add(DataSet.getFor2D("Probability distribution", data, new LineStyle(1.0f, color.gradient.Color.RED)));
                plots[plotIdx].getModel().setDataSets(dataSets, true);
            }
        }

        V.display();
    }
}