package t1_10.t3_evolutionary_multiobjective_optimization.t4_algorithms.t1_nsga;

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
import emo.aposteriori.nsga.NSGA;
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
import problem.moo.wfg.WFGBundle;
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
 * This tutorial showcases how to instantiate and run NSGA algorithm.
 *
 * @author MTomczyk
 */


@SuppressWarnings({"ConstantValue", "DuplicatedCode"})
public class Tutorial1wfg1
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
        // The number of objectives:
        int M = 4;
        // The population size:

        // Maps M -> population size
        HashMap<Integer, Integer> mToN = new HashMap<>();
        mToN.put(2, 50);
        mToN.put(3, 100);
        mToN.put(4, 200);
        int N = mToN.get(M);

        // Generations limit:
        int G = 1000;

        // Problem ID:
        Problem problemID = Problem.WFG1ALPHA05;
        // The number of decision variables for DTLZ
        // Threshold levels for NSGA:
        double[] thresholds = new double[]{0.0d, 0.1d, 2.0d};

        // Maps M -> display range for the Y-axis of the convergence plot
        HashMap<Integer, Range> mToDR = new HashMap<>();
        mToDR.put(2, new Range(0.0d, 1.0d));
        mToDR.put(3, new Range(0.0d, 1.25d));
        mToDR.put(4, new Range(0.0d, 1.5d));

        // Display range for the Y-axis of the convergence plot
        Range hvDR = mToDR.get(M);

        // False: OS bounds are predetermined; true = dynamically updated.
        boolean dynamicOSUpdate = false;

        // Create EAs array:
        EA[] ea = new EA[thresholds.length];

        // Get problem bundle:
        int k = WFGBundle.getRecommendedNOPositionRelatedParameters(problemID, M);
        int l = WFGBundle.getRecommendedNODistanceRelatedParameters(problemID, M);
        WFGBundle problem = WFGBundle.getBundle(problemID, M, k, l);

        // Create EAs - iterate over thresholds:
        for (int i = 0; i < thresholds.length; i++)
        {
            // Create EA instance:
            ea[i] = NSGA.getNSGA(i, dynamicOSUpdate, thresholds[i], N, R, problem);
        }

        // Create the visualization module (6 plots; 2 columns x 3 rows; 1 row = scatter plot (populations) and
        // convergence plot (HV over time):
        AbstractPlot[] plots = new AbstractPlot[thresholds.length * 2];

        // Create the HV indicator (use the problem's reference normalizations). NOTE that the HV below is parameterized
        // to filter out duplicates (in objective space) and derive non-dominated specimens before calculating HV.
        // Methods that do not ensure that their populations consist only of unique non-dominated specimens (e.g., NSGA)
        // are recommended to be coupled with these modes as a precaution. This way, the computational burden may be
        // significantly reduced.
        double[] RP = new double[M];
        Arrays.fill(RP, 1.1d);
        HV.Params pHV = new HV.Params(M, problem._normalizations, RP);
        pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
        IPerformanceIndicator HV = new HV(pHV);

        // Create plots
        for (int i = 0; i < thresholds.length; i++)
        {
            if (M == 2)
            {
                Plot2D.Params pP = new Plot2D.Params();
                pP._title = "Dynamic OS update = " + dynamicOSUpdate + " (th = " + String.format("%.2f", thresholds[i]) + ")";
                pP._xAxisTitle = "f1";
                pP._yAxisTitle = "f2";
                pP._pDisplayRangesManager = new DisplayRangesManager.Params(new Range[]{problem._displayRanges[0],
                        problem._displayRanges[1], Range.get0R(G - 1)}, false);
                pP._scheme = new WhiteScheme();
                pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
                plots[2 * i] = new Plot2D(pP);
            }
            else if (M == 3)
            {
                Plot3D.Params pP = new Plot3D.Params();
                pP._title = "Dynamic OS update = " + dynamicOSUpdate + " (th = " + String.format("%.2f", thresholds[i]) + ")";
                pP._xAxisTitle = "f1";
                pP._yAxisTitle = "f2";
                pP._zAxisTitle = "f3";
                pP._pDisplayRangesManager = new DisplayRangesManager.Params(new Range[]{problem._displayRanges[0],
                        problem._displayRanges[1], problem._displayRanges[2], Range.get0R(G - 1)}, false);
                pP._scheme = WhiteScheme.getForPlot3D(0.25f);
                pP._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.08f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 5));
                plots[2 * i] = new Plot3D(pP);
            }
            else
            {
                ParallelCoordinatePlot2D.Params pP = new ParallelCoordinatePlot2D.Params(M);
                pP._title = "Dynamic OS update = " + dynamicOSUpdate + " (th = " + String.format("%.2f", thresholds[i]) + ")";
                pP._xAxisTitle = "Objective value";
                pP._yAxisTitle = "Objectives";
                pP._axesTitles = ParallelCoordinatePlot2D.getAxesTitlesAsSequence("f", M);
                // create custom display range
                pP._pDisplayRangesManager = new DisplayRangesManager.Params();
                pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[M + 2]; // M + custom + X-axis
                pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[M + 1]; // X-axis does not require mapping
                for (int dr = 0; dr < M; dr++)
                {
                    pP._pDisplayRangesManager._DR[dr] = new DisplayRangesManager.DisplayRange(problem._displayRanges[dr], false);
                    pP._pDisplayRangesManager._attIdx_to_drIdx[dr] = dr;
                }
                pP._pDisplayRangesManager._DR[M] = new DisplayRangesManager.DisplayRange(Range.get0R(G - 1), false);
                pP._pDisplayRangesManager._attIdx_to_drIdx[M] = M;
                pP._pDisplayRangesManager._DR[M + 1] = new DisplayRangesManager.DisplayRange(Range.getNormalRange(), false);

                pP._scheme = WhiteScheme.getForPCP2D();
                pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[4], 5));

                plots[2 * i] = new ParallelCoordinatePlot2D(pP);
            }

            // Convergence plot:
            Plot2D.Params pP = new Plot2D.Params();
            pP._title = "HV over generations (th = " + String.format("%.2f", thresholds[i]) + ")";
            pP._yAxisTitle = "HV";
            pP._xAxisTitle = "Generation";
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D(Range.get0R(G - 1), hvDR);
            plots[2 * i + 1] = new Plot2D(pP);
        }

        // Grid layout (4 = no. processing queues -> parallelization):
        GridPlots gridPlots = new GridPlots(plots, thresholds.length, 3, 4);

        //Frame frame = new Frame(gridPlots, 0.8f);
        Frame frame = new Frame(gridPlots, 950, 1200);

        // Data updater configuration:
        DataUpdater.Params pDU = new DataUpdater.Params(gridPlots);

        // Assume that there are 6 (EA's) x 2 (for the scatter plot and the convergence plot) sources.
        pDU._dataSources = new IDataSource[thresholds.length * 2];
        for (int i = 0; i < thresholds.length; i++)
        {
            pDU._dataSources[2 * i] = new EASource(ea[i], true); // true = add timestamp
            pDU._dataSources[2 * i + 1] = new GenerationIndicator(ea[i], HV); // true = add timestamp
        }

        // Create 12 data processors:
        pDU._dataProcessors = new IDataProcessor[thresholds.length * 2];
        for (int i = 0; i < thresholds.length * 2; i++) pDU._dataProcessors[i] = new DataProcessor(true);

        // Do sources->processors bindings
        pDU._sourcesToProcessors = new SourceToProcessors[thresholds.length * 2];
        for (int i = 0; i < thresholds.length * 2; i++) pDU._sourcesToProcessors[i] = new SourceToProcessors(i);

        // Do processors -> plots bindings
        pDU._processorToPlots = new ProcessorToPlots[thresholds.length * 2];

        // For all algorithms:
        for (int i = 0; i < thresholds.length; i++)
        {
            IDataSet dataSet;

            Gradient gradient = Color.getViridisGradient();
            if (M == 2)
                dataSet = DataSet.getFor2D("Population", new MarkerStyle(2.0f, gradient, M, Marker.CIRCLE));
            else if (M == 3)
                dataSet = DataSet.getFor3D("Population", new MarkerStyle(0.02f, gradient, M, Marker.SPHERE_LOW_POLY_3D));
            else
                dataSet = DataSet.getForParallelCoordinatePlot2D("Population", M, new LineStyle(0.2f, gradient, M));
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
