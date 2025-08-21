package reproduction.operators.crossover;


import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import org.junit.jupiter.api.Test;
import plot.Plot2D;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.valuecheck.Wrap;
import space.Range;
import visualization.IVisualization;
import visualization.Visualization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Several tests (visualizations) for the SBX (with SP) crossover operator
 *
 * @author MTomczyk
 */
class SBXwithSP2DTest
{
    /**
     * Test 1.
     */
    @Test
    void SBXwithSP2DTest1()
    {
        double distributionIndex = 10.0d;
        double[][] values = new double[][]{{0.1d, 0.1d}, {0.9d, 0.9d}};
        int trials = 10000;

        IRandom R = new MersenneTwister64(0);

        SBXwithSP.Params pSBX = new SBXwithSP.Params(1.0d, distributionIndex);
        pSBX._valueCheck = new Wrap();

        SBXwithSP sbx = new SBXwithSP(pSBX);

        int[] dist = new int[4];
        int wrong = 0;
        for (int t = 0; t < trials; t++)
        {
            double[] p1 = values[0].clone();
            double[] p2 = values[1].clone();
            double[] o = sbx.crossover(p1, p2, R)._o;
            if ((o[0] < 0.0) || (o[0] > 1.0d) || (o[1] < 0.0d) || (o[1] > 1.0d)) wrong++;
            else if ((o[0] <= 0.5d) && (o[1] <= 0.5d)) dist[0]++;
            else if ((o[0] <= 0.5d) && (o[1] > 0.5d)) dist[1]++;
            else if ((o[0] > 0.5d) && (o[1] > 0.5d)) dist[2]++;
            else if ((o[0] > 0.5d) && (o[1] <= 0.5d)) dist[3]++;
        }

        int sum = dist[0] + dist[1] + dist[2] + dist[3];
        assertEquals(0, wrong);
        assertEquals(trials, sum);
        assertTrue(Math.abs((double) dist[0] / (double) dist[1] - 2.0d) < 0.2d);
        assertTrue(Math.abs((double) dist[2] / (double) dist[3] - 2.0d) < 0.2d);
    }

    /**
     * Performs visual experiment testing the SBX with SP operator.
     *
     * @param args not used.
     */
    public static void main(String[] args)
    {
        double distributionIndex = 10.0d;
        double[][] values = new double[][]{{0.1d, 0.1d}, {0.9d, 0.9d}};
        int trials = 10000;

        IRandom R = new MersenneTwister64(0);

        SBXwithSP.Params pSBX = new SBXwithSP.Params(1.0d, distributionIndex);
        pSBX._valueCheck = new Wrap();

        SBXwithSP sbx = new SBXwithSP(pSBX);

        double[][] data = new double[trials][];

        int[] dist = new int[4];
        int wrong = 0;
        for (int t = 0; t < trials; t++)
        {
            double[] p1 = values[0].clone();
            double[] p2 = values[1].clone();
            double[] o = sbx.crossover(p1, p2, R)._o;
            if ((o[0] < 0.0) || (o[0] > 1.0d) || (o[1] < 0.0d) || (o[1] > 1.0d)) wrong++;
            else if ((o[0] <= 0.5d) && (o[1] <= 0.5d)) dist[0]++;
            else if ((o[0] <= 0.5d) && (o[1] > 0.5d)) dist[1]++;
            else if ((o[0] > 0.5d) && (o[1] > 0.5d)) dist[2]++;
            else if ((o[0] > 0.5d) && (o[1] <= 0.5d)) dist[3]++;
            data[t] = o;
        }

        System.out.println("Distribution test:");
        int sum = dist[0] + dist[1] + dist[2] + dist[3];
        System.out.println(dist[0] + " " + dist[1] + " " + dist[2] + " " + dist[3] + " : " + sum);
        assert sum == trials;
        assert Math.abs((double) dist[0] / (double) dist[1] - 2.0d) < 0.2d;
        assert Math.abs((double) dist[2] / (double) dist[3] - 2.0d) < 0.2d;
        System.out.println("Errors = " + wrong);


        Plot2D.Params params = new Plot2D.Params();
        params._title =   "Offspring distribution";
        params._xAxisTitle = "x";
        params._yAxisTitle = "y";
        params._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        Plot2D plot = new Plot2D(params);
        IVisualization V = new Visualization(plot, 0.5f, 0.5f);

        DataSet dataSet = DataSet.getFor2D("offspring", data, new MarkerStyle(1.0f, color.gradient.Color.RED, Marker.CIRCLE));
        plot.getModel().setDataSet(dataSet);

        V.display();
    }
}