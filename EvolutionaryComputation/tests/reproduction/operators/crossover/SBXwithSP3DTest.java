package reproduction.operators.crossover;


import color.gradient.Color;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import org.junit.jupiter.api.Test;
import plot.Plot3D;
import print.PrintUtils;
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
class SBXwithSP3DTest
{
    /**
     * Test 1.
     */
    @Test
    void SBXwithSP3DTest1()
    {
        double distributionIndex = 10.0d;
        double[][] values = new double[][]{{0.1d, 0.1d, 0.1d}, {0.9d, 0.9d, 0.9d}};
        int trials = 1000000;

        IRandom R = new MersenneTwister64(0);

        SBXwithSP.Params pSBX = new SBXwithSP.Params(1.0d, distributionIndex);
        pSBX._valueCheck = new Wrap();

        SBXwithSP sbx = new SBXwithSP(pSBX);

        int[] dist = new int[8];
        int wrong = 0;
        for (int t = 0; t < trials; t++)
        {
            double[] p1 = values[0].clone();
            double[] p2 = values[1].clone();
            double[] o = sbx.crossover(p1, p2, R);
            if ((o[0] < 0.0) || (o[0] > 1.0d) || (o[1] < 0.0d) || (o[1] > 1.0d) || (o[2] < 0.0d) || (o[2] > 1.0d))
                wrong++;
            else if ((o[0] <= 0.5d) && (o[1] <= 0.5d) && (o[2] <= 0.5d)) dist[0]++;
            else if ((o[0] <= 0.5d) && (o[1] <= 0.5d) && (o[2] > 0.5d)) dist[1]++;
            else if ((o[0] <= 0.5d) && (o[1] > 0.5d) && (o[2] <= 0.5d)) dist[2]++;
            else if ((o[0] <= 0.5d) && (o[1] > 0.5d) && (o[2] > 0.5d)) dist[3]++;
            else if ((o[0] > 0.5d) && (o[1] <= 0.5d) && (o[2] <= 0.5d)) dist[4]++;
            else if ((o[0] > 0.5d) && (o[1] <= 0.5d) && (o[2] > 0.5d)) dist[5]++;
            else if ((o[0] > 0.5d) && (o[1] > 0.5d) && (o[2] <= 0.5d)) dist[6]++;
            else if ((o[0] > 0.5d) && (o[1] > 0.5d) && (o[2] > 0.5d)) dist[7]++;
        }

        int sum = dist[0] + dist[1] + dist[2] + dist[3] + dist[4] + dist[5] + dist[6] + dist[7];
        assertEquals(trials, sum);
        assertEquals(0, dist[2]);
        assertEquals(0, dist[5]);
        assertEquals(0, wrong);
        assertTrue(Math.abs((double) dist[0] / (double) dist[1] - 2.0d) < 0.2d);
        assertTrue(Math.abs((double) dist[0] / (double) dist[3] - 2.0d) < 0.2d);
        assertTrue(Math.abs((double) dist[0] / (double) dist[4] - 2.0d) < 0.2d);
        assertTrue(Math.abs((double) dist[0] / (double) dist[7] - 1.0d) < 0.2d);
        assertTrue(Math.abs((double) dist[7] / (double) dist[4] - 2.0d) < 0.2d);
        assertTrue(Math.abs((double) dist[7] / (double) dist[6] - 2.0d) < 0.2d);
    }

    /**
     * Performs visual experiment testing the SBX with SP operator.
     *
     * @param args not used.
     */
    public static void main(String[] args)
    {
        double distributionIndex = 10.0d;
        double[][] values = new double[][]{{0.1d, 0.1d, 0.1d}, {0.9d, 0.9d, 0.9d}};
        int trials = 1000000;

        IRandom R = new MersenneTwister64(0);

        SBXwithSP.Params pSBX = new SBXwithSP.Params(1.0d, distributionIndex);
        pSBX._valueCheck = new Wrap();

        SBXwithSP sbx = new SBXwithSP(pSBX);

        double[][] data = new double[trials][];

        int[] dist = new int[8];
        int wrong = 0;
        for (int t = 0; t < trials; t++)
        {
            double[] p1 = values[0].clone();
            double[] p2 = values[1].clone();
            double[] o = sbx.crossover(p1, p2, R);
            if ((o[0] < 0.0) || (o[0] > 1.0d) || (o[1] < 0.0d) || (o[1] > 1.0d) || (o[2] < 0.0d) || (o[2] > 1.0d))
                wrong++;
            else if ((o[0] <= 0.5d) && (o[1] <= 0.5d) && (o[2] <= 0.5d)) dist[0]++;
            else if ((o[0] <= 0.5d) && (o[1] <= 0.5d) && (o[2] > 0.5d)) dist[1]++;
            else if ((o[0] <= 0.5d) && (o[1] > 0.5d) && (o[2] <= 0.5d)) dist[2]++;
            else if ((o[0] <= 0.5d) && (o[1] > 0.5d) && (o[2] > 0.5d)) dist[3]++;
            else if ((o[0] > 0.5d) && (o[1] <= 0.5d) && (o[2] <= 0.5d)) dist[4]++;
            else if ((o[0] > 0.5d) && (o[1] <= 0.5d) && (o[2] > 0.5d)) dist[5]++;
            else if ((o[0] > 0.5d) && (o[1] > 0.5d) && (o[2] <= 0.5d)) dist[6]++;
            else if ((o[0] > 0.5d) && (o[1] > 0.5d) && (o[2] > 0.5d)) dist[7]++;
            data[t] = o;
        }

        System.out.println("Distribution test:");
        int sum = dist[0] + dist[1] + dist[2] + dist[3] + dist[4] + dist[5] + dist[6] + dist[7];
        System.out.println(dist[0] + " " + dist[1] + " " + dist[2] + " " + dist[3] + " " + dist[4] + " " + dist[5] + " " + dist[6] + " " + dist[7] + " : " + sum);
        assert sum == trials;

        assert dist[2] == 0;
        assert dist[5] == 0;
        assert Math.abs((double) dist[0] / (double) dist[1] - 2.0d) < 0.2d;
        assert Math.abs((double) dist[0] / (double) dist[3] - 2.0d) < 0.2d;
        assert Math.abs((double) dist[0] / (double) dist[4] - 2.0d) < 0.2d;
        assert Math.abs((double) dist[0] / (double) dist[7] - 1.0d) < 0.2d;
        assert Math.abs((double) dist[7] / (double) dist[4] - 2.0d) < 0.2d;
        assert Math.abs((double) dist[7] / (double) dist[6] - 2.0d) < 0.2d;

        System.out.println("Errors = " + wrong);

        Plot3D.Params params = new Plot3D.Params();
        params._title = "Offspring distribution";
        params._xAxisTitle = "x";
        params._yAxisTitle = "y";
        params._zAxisTitle = "z";
        params._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(),
                Range.getNormalRange(), Range.getNormalRange());
        Plot3D plot = new Plot3D(params);
        IVisualization V = new Visualization(plot, 0.5f, 0.5f);

        PrintUtils.printVectorOfDoubles(data[0], 3);

        DataSet dataSet = DataSet.getFor3D("data set", data, new MarkerStyle(1.0f, Color.RED, Marker.POINT_3D));
        plot.getModel().setDataSet(dataSet);

        V.init();
        V.display();
    }
}