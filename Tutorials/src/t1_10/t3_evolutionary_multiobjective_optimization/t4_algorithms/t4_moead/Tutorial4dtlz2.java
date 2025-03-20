package t1_10.t3_evolutionary_multiobjective_optimization.t4_algorithms.t4_moead;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.moead.MOEAD;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.ISimilarity;
import emo.utils.decomposition.similarity.pbi.Euclidean;
import exception.RunnerException;
import frame.Frame;
import indicator.IPerformanceIndicator;
import indicator.emo.HV;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import plotswrapper.GridPlots;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import updater.*;
import visualization.Visualization;
import visualization.updaters.sources.EASource;
import visualization.updaters.sources.GenerationIndicator;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This tutorial showcases how to instantiate and run MOEA/D algorithm.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode"})
public class Tutorial4dtlz2
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create RNG:
        IRandom R = new MersenneTwister64(0);
        // The number of objectives (scenarios):
        int[] M = new int[]{2, 3, 4};

        // Maps M -> the number of cuts for the Das and Dennis' method
        HashMap<Integer, Integer> mToCuts = new HashMap<>();
        mToCuts.put(2, 49);
        mToCuts.put(3, 12);
        mToCuts.put(4, 9);

        // Generations limit:
        int G = 400;

        // Problem ID:
        Problem problemID = Problem.DTLZ2;

        // False: OS bounds are predetermined; true = dynamically updated.
        boolean dynamicOSUpdate = false;

        // Maps M -> display range for the Y-axis of the convergence plot
        HashMap<Integer, Range> mToDR = new HashMap<>();
        mToDR.put(2, new Range(0.0d, 0.5d));
        mToDR.put(3, new Range(0.0d, 0.75d));
        mToDR.put(4, new Range(0.0d, 1.1d));

        // Create EAs array:
        EA[] ea = new EA[M.length];

        // Create reference problem bundles:
        DTLZBundle[] problems = new DTLZBundle[M.length];
        for (int i = 0; i < M.length; i++)
        {
            int l = DTLZBundle.getRecommendedNODistanceRelatedParameters(problemID, M[i]);
            problems[i] = DTLZBundle.getBundle(problemID, M[i], l);
        }

        // There should be 1 repeat per each goal (filled later).
        int[] steadyStateRepeats = new int[M.length];

        // Create EAs - iterate over Ms:
        for (int i = 0; i < M.length; i++)
        {
            // Set goals (normalizations = null; they will be set (if fixed ranges are used) by the getMOEAD method).
            IGoal[] goals = GoalsFactory.getLNormsDND(M[i], mToCuts.get(M[i]), Double.POSITIVE_INFINITY, null);
            //IGoal [] goals = GoalsFactory.getPBIsDND(M[i], mToN.get(M[i]), 5.0d, null);
            // Not recommended (ignores convergence):
            //IGoal[] goals = GoalsFactory.getPointLineProjectionsDND(M[i], mToN.get(M[i]), null);

            //  Fill steady-state repeats no.
            steadyStateRepeats[i] = goals.length;

            // Derives weights/reference points/the first double vector obtainable via the getParams() method of IGoal
            // (and calculates the Euclidean distance)
            ISimilarity similarity = new Euclidean();
            int neighborhoodSize = 10;


            // Create EA instance (note that the population size = goals.length):
            // Recommendation: use the nadir incumbent when solving DTLZ1
            ea[i] = MOEAD.getMOEAD(i, dynamicOSUpdate, false, R, goals, problems[i], similarity, neighborhoodSize);
        }

        // Create the visualization module (6 plots; 2 columns x 3 rows; 1 row = scatter plot (populations) and
        // convergence plot (HV over time):
        AbstractPlot[] plots = new AbstractPlot[M.length * 2];

        // Create plots
        for (int i = 0; i < M.length; i++)
        {
            if (M[i] == 2)
            {
                Plot2D.Params pP = new Plot2D.Params();
                pP._title = "Dynamic OS update = " + dynamicOSUpdate + " (M = " + M[i] + ")";
                pP._xAxisTitle = "f1";
                pP._yAxisTitle = "f2";
                pP._pDisplayRangesManager = new DisplayRangesManager.Params(new Range[]{problems[i]._displayRanges[0],
                        problems[i]._displayRanges[1], Range.get0R(G - 1)}, false);
                pP._scheme = new WhiteScheme();
                pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
                plots[2 * i] = new Plot2D(pP);
            }
            else if (M[i] == 3)
            {
                Plot3D.Params pP = new Plot3D.Params();
                pP._title = "Dynamic OS update = " + dynamicOSUpdate + " (M = " + M[i] + ")";
                pP._xAxisTitle = "f1";
                pP._yAxisTitle = "f2";
                pP._zAxisTitle = "f3";
                pP._pDisplayRangesManager = new DisplayRangesManager.Params(new Range[]{problems[i]._displayRanges[0],
                        problems[i]._displayRanges[1], problems[i]._displayRanges[2], Range.get0R(G - 1)}, false);
                pP._scheme = WhiteScheme.getForPlot3D(0.25f);
                pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.08f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 5));
                plots[2 * i] = new Plot3D(pP);
            }
            else
            {
                ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(M[i]);
                pP._title = "Dynamic OS update = " + dynamicOSUpdate + " (M = " + M[i] + ")";
                pP._xAxisTitle = "Objective value";
                pP._yAxisTitle = "Objectives";
                pP._axesTitles = ParallelCoordinatePlot2D.getAxesTitlesAsSequence("f", M[i]);
                // create custom display range
                pP._pDisplayRangesManager = new DisplayRangesManager.Params();
                pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[M[i] + 2]; // M + custom + X-axis
                pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[M[i] + 1]; // X-axis does not require mapping
                for (int dr = 0; dr < M[i]; dr++)
                {
                    pP._pDisplayRangesManager._DR[dr] = new DisplayRangesManager.DisplayRange(problems[i]._displayRanges[dr], false);
                    pP._pDisplayRangesManager._attIdx_to_drIdx[dr] = dr;
                }
                pP._pDisplayRangesManager._DR[M[i]] = new DisplayRangesManager.DisplayRange(Range.get0R(G - 1), false);
                pP._pDisplayRangesManager._attIdx_to_drIdx[M[i]] = M[i];
                pP._pDisplayRangesManager._DR[M[i] + 1] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false);

                pP._scheme = WhiteScheme.getForPCP2D();
                pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[4], 5));

                plots[2 * i] = new ParallelCoordinatePlot2D(pP);
            }

            // Convergence plot:
            Plot2D.Params pP = new Plot2D.Params();
            pP._title = "HV over generations (M = " + M[i] + ")";
            pP._yAxisTitle = "HV";
            pP._xAxisTitle = "Generation";
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D(Range.get0R(G - 1), mToDR.get(M[i]));
            //pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(null, true);
            //pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(null, true);
            plots[2 * i + 1] = new Plot2D(pP);
        }

        // Grid layout (4 = no. processing queues -> parallelization):
        GridPlots gridPlots = new GridPlots(plots, M.length, 3, 4);

        //Frame frame = new Frame(gridPlots, 0.8f);
        Frame frame = new Frame(gridPlots, 950, 1200);

        // Data updater configuration:
        DataUpdater.Params pDU = new DataUpdater.Params(gridPlots);

        // Assume that there are 6 (EA's) x 2 (for the scatter plot and the convergence plot) sources.
        pDU._dataSources = new IDataSource[M.length * 2];
        for (int i = 0; i < M.length; i++)
        {
            double[] RP = new double[M[i]];
            Arrays.fill(RP, 1.1d);
            HV.Params pHV = new HV.Params(M[i], problems[i]._normalizations, RP);
            pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
            IPerformanceIndicator HV = new HV(pHV);

            pDU._dataSources[2 * i] = new EASource(ea[i], true); // true = add timestamp
            pDU._dataSources[2 * i + 1] = new GenerationIndicator(ea[i], HV); // true = add timestamp
        }

        // Create 12 data processors:
        pDU._dataProcessors = new IDataProcessor[M.length * 2];
        for (int i = 0; i < M.length * 2; i++) pDU._dataProcessors[i] = new DataProcessor(true);

        // Do sources->processors bindings
        pDU._sourcesToProcessors = new SourceToProcessors[M.length * 2];
        for (int i = 0; i < M.length * 2; i++) pDU._sourcesToProcessors[i] = new SourceToProcessors(i);

        // Do processors -> plots bindings
        pDU._processorToPlots = new ProcessorToPlots[M.length * 2];

        // For all algorithms:
        for (int i = 0; i < M.length; i++)
        {
            IDataSet dataSet;

            Gradient gradient = Color.getViridisGradient();
            if (M[i] == 2)
                dataSet = DataSet.getFor2D("Population", new MarkerStyle(2.0f, gradient, M[i], Marker.CIRCLE));
            else if (M[i] == 3)
                dataSet = DataSet.getFor3D("Population", new MarkerStyle(0.02f, gradient, M[i], Marker.SPHERE_LOW_POLY_3D));
            else
                dataSet = DataSet.getForParallelCoordinatePlot2D("Population", M[i], new LineStyle(0.2f, gradient, M[i]));
            // Scatter plots
            pDU._processorToPlots[2 * i] = new ProcessorToPlots(2 * i, dataSet);
            // Convergence plot
            dataSet = DataSet.getForConvergencePlot2D("HV (dynamic OS update = " + dynamicOSUpdate + ")", new LineStyle(1.0f, Color.RED));
            pDU._processorToPlots[2 * i + 1] = new ProcessorToPlots(2 * i + 1, dataSet);
        }

        DataUpdater dataUpdater;
        try
        {
            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        Runner.Params pR = new Runner.Params(ea);
        pR._visualization = new Visualization(frame, dataUpdater);
        pR._displayMode = DisplayMode.AT_THE_END;
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;
        pR._steadyStateRepeats = steadyStateRepeats;
        Runner runner = new Runner(pR);

        try
        {
            runner.executeEvolution(G);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }
    }
}
