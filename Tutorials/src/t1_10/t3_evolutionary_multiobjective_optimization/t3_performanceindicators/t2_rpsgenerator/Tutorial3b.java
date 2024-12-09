package t1_10.t3_evolutionary_multiobjective_optimization.t3_performanceindicators.t2_rpsgenerator;

import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import plot.dummy.DummyColorPlot;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import plotswrapper.GridPlots;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.ReferencePointsFactory;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;

import java.util.ArrayList;

/**
 * Tutorial on the {@link ReferencePointsFactory} class (constructs reference Pareto optimal points).
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode", "ConstantValue"})
public class Tutorial3b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int M = 3;
        // Use a sufficiently large number of samples to generate:
        int n = 1000000;

        // Predefined no. cuts for the Das and Dennis' method (20 for 2D, 12 for 3D, and 8 for 4D)
        int[] cuts = new int[]{29, 12, 8};

        // Create problems IDs:
        Problem[] problems = new Problem[]{
                Problem.DTLZ1, Problem.DTLZ2,
                Problem.DTLZ3, Problem.DTLZ4,
                Problem.DTLZ5, Problem.DTLZ6,
                Problem.DTLZ7};

        /*Problem[] problems = new Problem[]{
                Problem.WFG1, Problem.WFG2, Problem.WFG3,
                Problem.WFG4, Problem.WFG5, Problem.WFG6,
                Problem.WFG7, Problem.WFG8, Problem.WFG9,
        };*/

        // Create data sets and bundles:
        ArrayList<IDataSet> dataSets = new ArrayList<>(problems.length);
        ArrayList<AbstractMOOProblemBundle> bundles = new ArrayList<>(problems.length);

        int noGoals = 0;

        // Iterate over all problems:
        for (Problem problem : problems)
        {
            System.out.println("Generating data for " + problem);
            DTLZBundle bundle = DTLZBundle.getBundle(problem, M, 2);
            //WFGBundle bundle = WFGBundle.getBundle(problem, M, M - 1, 2); // the number for position/distance-related variable cannot be arbitrary for WFG
            bundles.add(bundle);
            IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(M, cuts[M - 2], bundle._normalizations);
            //IGoal[] goals = GoalsFactory.getLNormsDND(M, cuts[M - 2], Double.POSITIVE_INFINITY, bundle._normalizations);

            noGoals = goals.length; // will be the same for all problems
            double[][] rps = ReferencePointsFactory.getFilteredReferencePoints(problem, n, M, R, goals);

            if (M == 2) dataSets.add(DataSet.getFor2D("RPs", rps, new MarkerStyle(1.5f, Gradient.getRedBlueGradient(), 0, Marker.CIRCLE)));
            else if (M == 3)
                dataSets.add(DataSet.getFor3D("RPs", rps, new MarkerStyle(0.05f, Gradient.getRedBlueGradient(), 0, Marker.SPHERE_LOW_POLY_3D)));
            else
                dataSets.add(DataSet.getForParallelCoordinatePlot2D("RPs", M, rps, null, new LineStyle(0.5f, Gradient.getRedBlueGradient(), 0)));

        }

        // Let's represent the result in a grid layout (the same number of columns and rows).
        // Let's determine the expected grid width (or height):
        int expPlots = (int) (Math.sqrt(problems.length) + 0.5d);

        // Start constructing the plots (some of them may be excessive):
        AbstractPlot[] plots = new AbstractPlot[expPlots * expPlots];

        for (int i = 0; i < expPlots * expPlots; i++) // do not iterate over the excessive plots (nulls are ignored)
        {
            if (i >= problems.length)
            {
                plots[i] = new DummyColorPlot(color.Color.WHITE); // Create dummy (empty) plot with white background
            }
            else
            {
                if (M == 2)
                {
                    Plot2D.Params pP = new Plot2D.Params();
                    pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(bundles.get(i)._paretoFrontBounds[0],
                            bundles.get(i)._paretoFrontBounds[1]);
                    pP._xAxisTitle = "f1";
                    pP._yAxisTitle = "f2";
                    pP._title = "Points = " + n + "; Goals = " + noGoals + "; Problem = " + problems[i];
                    plots[i] = new Plot2D(pP);
                }
                else if (M == 3)
                {
                    Plot3D.Params pP = new Plot3D.Params();
                    pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(bundles.get(i)._paretoFrontBounds[0],
                            bundles.get(i)._paretoFrontBounds[1], bundles.get(i)._paretoFrontBounds[2]);
                    pP._xAxisTitle = "f1";
                    pP._yAxisTitle = "f2";
                    pP._zAxisTitle = "f3";
                    pP._scheme = WhiteScheme.getForPlot3D();
                    pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.1f);
                    pP._title = "Points = " + n + "; Goals = " + noGoals + "; Problem = " + problems[i];
                    plots[i] = new Plot3D(pP);
                }
                else
                {
                    ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(M);
                    pP._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(M, bundles.get(i)._paretoFrontBounds, new boolean[M], new boolean[M]);
                    pP._axesTitles = ParallelCoordinatePlot2D.getAxesTitlesAsSequence("D", M);
                    pP._title = "Points = " + n + "; Goals = " + noGoals + "; Problem = " + problems[i];
                    pP._scheme = WhiteScheme.getForPCP2D();
                    //  pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.1f);
                    plots[i] = new ParallelCoordinatePlot2D(pP);
                }
            }
        }

        // Create grids plots wrapper:
        GridPlots gridPlots = new GridPlots(plots, expPlots, expPlots);

        //Frame frame = new Frame(plot, 0.5f);
        Frame frame = new Frame(gridPlots, 1400, 1400);

        for (int i = 0; i < problems.length; i++)
            frame.getModel().getPlotsWrapper().getModel().getPlot(i).getModel().setDataSet(dataSets.get(i), true, false);

        frame.setVisible(true);
    }
}
