package t1_10.t6_ivemo_heatmap.t2_moead;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import drmanager.DisplayRangesManager;
import emo.aposteriori.moead.MOEAD;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.pbi.Euclidean;
import frame.Frame;
import plot.heatmap.Heatmap3D;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import space.normalization.minmax.Gamma;
import space.simplex.DasDennis;
import statistics.Constant;
import statistics.Sum;
import tools.ivemo.heatmap.Heatmap2DProcessor;
import tools.ivemo.heatmap.Heatmap3DProcessor;
import tools.ivemo.heatmap.feature.ConstantValue;

/**
 * This tutorial showcases how to employ {@link Heatmap2DProcessor} for analysis.
 * Case: MOEA/D, 3D, total no. trials in which found
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial2d
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
        int generations = 50;
        int xDix = 100;
        int yDiv = 100;
        int zDiv = 100;
        int trials = 100;
        int cuts = 15; // no cuts for DND method
        int SSR = DasDennis.getNoProblems(3, cuts); // get the number of steady-state repeats
        System.out.println(SSR);

        // Considered bounds for the analysis for both dimensions (in objective/performance space):
        Range xDR = new Range(0.0d, 2.0d);
        Range yDR = new Range(0.0d, 2.0d);
        Range zDR = new Range(0.0d, 2.0d);

        // Create Heatmap2DProcessor:
        Heatmap3DProcessor.Params pHP = new Heatmap3DProcessor.Params();
        // Provides instances of tested EA:
        pHP._eaFactory = () -> {
            // MOEA/D applied to DTLZ2:
            AbstractMOOProblemBundle problem = AbstractMOOProblemBundle.getBundle(Problem.DTLZ2, 3);
            IGoal[] goals = GoalsFactory.getLNormsDND(3, cuts, Double.POSITIVE_INFINITY, problem._normalizations);
            return MOEAD.getMOEAD(0, false, false, R, goals,
                    problem, new Euclidean(), 10);
        };

        pHP._steadyStateRepeats = SSR; // set the no. steady-state repeats
        pHP._notify = true; // will print notifications
        pHP._trials = trials; // set the number of test runs
        pHP._generations = generations; // set the limit for the number of generations
        pHP._featureGetter = new ConstantValue(1); // returns the feature (constant 1)
        pHP._trialStatistics = new Constant(1.0d); // set per-trial statistic (Constant = 1.0d; thus, feature has no effect)
        pHP._finalStatistics = new Sum(); // set final statistics (total no. trials in which found)
        pHP._xAxisDivisions = xDix; // set the discretization level for X-axis
        pHP._xAxisDisplayRange = xDR; // set considered bound on the objective space (X-axis)
        pHP._yAxisDivisions = yDiv; // set the discretization level for Y-axis
        pHP._yAxisDisplayRange = yDR; // set considered bound on the objective space (Y-axis)
        pHP._zAxisDivisions = zDiv; // set the discretization level for Z-axis
        pHP._zAxisDisplayRange = zDR; // set considered bound on the objective space (Z-axis)
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
        pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(Range.get0R(trials));
        pPlot._heatmapDisplayRange.setNormalizer(new Gamma(0.5d));
        pPlot._gradient = Gradient.getCividisGradient();
        pPlot._colorbar = new Colorbar(Gradient.getCividisGradient(), "No. trials in which found",
                new FromDisplayRange(pPlot._heatmapDisplayRange, 5));

        Heatmap3D HM = new Heatmap3D(pPlot);
        //frame.Frame frame = new Frame(HM, 0.5f);
        Frame frame = new Frame(HM, 900, 800);

        HM.getModel().setDataAndPerformProcessing(h3D.getSortedCoords(), h3D.getSortedValues()._sortedValues);
        frame.setVisible(true);
    }
}
