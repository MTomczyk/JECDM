package t1_10.t6_ivemo_heatmap.t1_nsgaii;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import emo.aposteriori.nsgaii.NSGAII;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import space.normalization.minmax.Gamma;
import statistics.Mean;
import statistics.Min;
import tools.ivemo.heatmap.Heatmap2DProcessor;
import tools.ivemo.heatmap.Heatmap3DProcessor;
import tools.ivemo.heatmap.feature.Generation;

/**
 * This tutorial showcases how to employ {@link Heatmap2DProcessor} for analysis.
 * Case: NSGA-II, 3D, average mean generation when found.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial1c
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create random number generator:
        IRandom R = new MersenneTwister64(0);

        // Set some basic parameters:
        int populationSize = 136;
        int generations = 50;
        int xDix = 100;
        int yDiv = 100;
        int zDiv = 100;
        int trials = 100;

        // Considered bounds for the analysis for both dimensions (in objective/performance space):
        Range xDR = new Range(0.0d, 2.0d);
        Range yDR = new Range(0.0d, 2.0d);
        Range zDR = new Range(0.0d, 2.0d);

        // Create Heatmap2DProcessor:
        Heatmap3DProcessor.Params pHP = new Heatmap3DProcessor.Params();
        // Provides instances of tested EA:
        pHP._eaFactory = () -> {
            // NSGA-II applied to DTLZ2:
            AbstractMOOProblemBundle problem = AbstractMOOProblemBundle.getBundle(Problem.DTLZ2, 3);
            return NSGAII.getNSGAII(0, false, populationSize, R, problem);
        };
        pHP._notify = true; // will print notifications
        pHP._trials = trials; // set the number of test runs
        pHP._generations = generations; // set the limit for the number of generations
        pHP._featureGetter = new Generation(); // returns the feature (generation number when captured)
        pHP._trialStatistics = new Min(); // set per-trial statistic (mean = average of the reported generations)
        pHP._finalStatistics = new Mean(); // set final statistics (average of per-trial results)
        pHP._xAxisDivisions = xDix; // set the discretization level for x-axis
        pHP._xAxisDisplayRange = xDR; // set considered bound on the objective space (x-axis)
        pHP._yAxisDivisions = yDiv; // set the discretization level for y-axis
        pHP._yAxisDisplayRange = yDR; // set considered bound on the objective space (y-axis)
        pHP._zAxisDivisions = zDiv; // set the discretization level for z-axis
        pHP._zAxisDisplayRange = zDR; // set considered bound on the objective space (z-axis)
        Heatmap3DProcessor h3D = new Heatmap3DProcessor(pHP);

        // Execute processing:
        h3D.executeProcessing();
        // Generate sorted input data for heatmaps (accessible via h2D.getSortedCoords(), h2D.getSortedValues())
        h3D.generateSortedInputData();

        // Create plot to visualize the data:
        Heatmap3D.Params pPlot = new Heatmap3D.Params();
        pPlot._xDiv = xDix;
        pPlot._yDiv = yDiv;
        pPlot._zDiv = zDiv;
        pPlot._scheme = WhiteScheme.getForPlot3D(0.25f);
        pPlot._xAxisTitle = "f1";
        pPlot._yAxisTitle = "f2";
        pPlot._zAxisTitle = "f3";

        pPlot._verticalGridLinesWithBoxTicks = false;
        pPlot._horizontalGridLinesWithBoxTicks = false;
        pPlot._depthGridLinesWithBoxTicks = false;

        pPlot._xAxisWithBoxTicks = false;
        pPlot._yAxisWithBoxTicks = false;
        pPlot._zAxisWithBoxTicks = false;

        pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(xDR, yDR, zDR);
        pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(Range.get0R(generations));
        pPlot._heatmapDisplayRange.setNormalizer(new Gamma(0.5d));
        pPlot._gradient = Gradient.getViridisGradient();
        pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Avg of min generations",
                new FromDisplayRange(pPlot._heatmapDisplayRange, 5));

        Heatmap3D HM = new Heatmap3D(pPlot);
        //frame.Frame frame = new Frame(HM, 0.5f);
        Frame frame = new Frame(HM, 900, 800);

        HM.getModel().setDataAndPerformProcessing(h3D.getSortedCoords(), h3D.getSortedValues()._sortedValues);
        frame.setVisible(true);
    }
}
