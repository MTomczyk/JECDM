package t1_10.t3_evolutionary_multiobjective_optimization.t5_comparative;


import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.moead.MOEAD;
import emo.aposteriori.nsga.NSGA;
import emo.aposteriori.nsgaii.NSGAII;
import emo.aposteriori.nsgaiii.NSGAIII;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.RandomAssignment;
import emo.utils.decomposition.nsgaiii.RandomSpecimen;
import emo.utils.decomposition.similarity.pbi.Euclidean;
import exception.RunnerException;
import frame.Frame;
import indicator.IPerformanceIndicator;
import indicator.emo.GD;
import indicator.emo.HV;
import indicator.emo.IGD;
import plot.AbstractPlot;
import plot.Plot2D;
import plotswrapper.GridPlots;
import problem.Problem;
import problem.moo.ReferencePointsFactory;
import problem.moo.wfg.WFGBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.WhiteScheme;
import space.Range;
import space.distance.IDistance;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.GenerationIndicator;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This tutorial compares NSGA, NSGA-II, NSGA-III, and MOEA/D algorithms.
 *
 * @author MTomczyk
 */

@SuppressWarnings("DuplicatedCode")
public class Tutorial5wfg1
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        System.out.println("Creating basic data...");

        // Create RNG:
        IRandom R = new MersenneTwister64(0);
        // The number of objectives (scenarios):
        int[] M = new int[]{2, 3, 4, 5};

        // Maps M -> the number of cuts for the Das and Dennis' method:
        HashMap<Integer, Integer> mToCuts = new HashMap<>();
        mToCuts.put(2, 49);
        mToCuts.put(3, 12);
        mToCuts.put(4, 9);
        mToCuts.put(5, 7);

        // Create algorithms identifiers:
        String[] algorithms = new String[]{"NSGA", "NSGA-II", "NSGA-III", "MOEA/D"};

        // Create goals (algorithm -> dimensions -> goals); only for NSGA-III and MOEA/D:
        HashMap<String, HashMap<Integer, IGoal[]>> goals = new HashMap<>(2);
        {
            HashMap<Integer, IGoal[]> g = new HashMap<>(M.length);
            goals.put("NSGA-III", g);
            for (Integer m : M)
            {
                int cuts = mToCuts.get(m);
                g.put(m, GoalsFactory.getPointLineProjectionsDND(m, cuts, null));
            }
        }
        {
            HashMap<Integer, IGoal[]> g = new HashMap<>(M.length);
            goals.put("MOEA/D", g);
            for (Integer m : M)
            {
                int cuts = mToCuts.get(m);
                g.put(m, GoalsFactory.getPBIsDND(m, cuts, 5.0d, null));
            }
        }

        // Maps M -> population size:
        HashMap<Integer, Integer> populationSizes = new HashMap<>(M.length);
        for (Integer m : M)
            populationSizes.put(m, goals.get("NSGA-III").get(m).length);

        // Maps algorithm -> M -> number of steady-state repeats:
        HashMap<String, HashMap<Integer, Integer>> steadyStateRepeats = new HashMap<>(algorithms.length);
        {
            HashMap<Integer, Integer> ssr = new HashMap<>();
            for (Integer m : M) ssr.put(m, 1);
            steadyStateRepeats.put("NSGA", ssr);
            steadyStateRepeats.put("NSGA-II", ssr);
            steadyStateRepeats.put("NSGA-III", ssr);
        }
        {
            HashMap<Integer, Integer> ssr = new HashMap<>();
            steadyStateRepeats.put("MOEA/D", ssr);
            for (Integer m : M) ssr.put(m, populationSizes.get(m));

        }

        // Generations limits (M -> generations):
        HashMap<Integer, Integer> mToG = new HashMap<>(M.length);
        mToG.put(2, 700);
        mToG.put(3, 800);
        mToG.put(4, 900);
        mToG.put(5, 1000);

        // Problem ID:
        Problem problemID = Problem.WFG1ALPHA05;

        // False: OS bounds are predetermined; true = dynamically updated:
        boolean dynamicOSUpdate = true;

        // Create reference problem bundles (dimensions vs algorithms):
        WFGBundle[][] problems = new WFGBundle[M.length][algorithms.length];

        for (int i = 0; i < M.length; i++)
        {
            for (int j = 0; j < algorithms.length; j++)
            {
                int k = WFGBundle.getRecommendedNOPositionRelatedParameters(problemID, M[i]);
                int l = WFGBundle.getRecommendedNODistanceRelatedParameters(problemID, M[i]);
                problems[i][j] = WFGBundle.getBundle(problemID, M[i], k, l);
            }
        }

        // Algorithm -> color (for data sets):
        HashMap<String, Color> dsColors = new HashMap<>(algorithms.length);
        dsColors.put("NSGA", Color.GREEN);
        dsColors.put("NSGA-II", Color.BLUE);
        dsColors.put("NSGA-III", Color.RED);
        dsColors.put("MOEA/D", Color.BLACK);

        System.out.println("Creating EAs...");

        // Create EAs array (dimensions vs algorithms):
        EA[][] eas = new EA[M.length][algorithms.length];

        // Create EAs - iterate over Ms:
        for (int i = 0; i < M.length; i++)
        {
            // iterate over algorithms:
            for (int j = 0; j < algorithms.length; j++)
            {
                switch (algorithms[j])
                {
                    case "NSGA" -> eas[i][j] = NSGA.getNSGA(i, dynamicOSUpdate, 0.1d,
                            populationSizes.get(M[i]), R, problems[i][j]);
                    case "NSGA-II" ->
                            eas[i][j] = NSGAII.getNSGAII(i, dynamicOSUpdate, populationSizes.get(M[i]), R, problems[i][j]);
                    case "NSGA-III" ->
                    {
                        IGoal[] gs = goals.get("NSGA-III").get(M[i]);
                        eas[i][j] = NSGAIII.getNSGAIII(i, dynamicOSUpdate, false, R, gs, problems[i][j],
                                new RandomAssignment(), new RandomSpecimen());
                    }
                    case "MOEA/D" ->
                    {
                        IGoal[] gs = goals.get("MOEA/D").get(M[i]);
                        eas[i][j] = MOEAD.getMOEAD(i, dynamicOSUpdate, false, R, gs, problems[i][j],
                                new Euclidean(), 10);
                    }
                }
            }
        }

        System.out.println("Creating performance metrics...");

        // Create the performance metrics:

        // M -> HV, IGD (PLP), IGD (PBI)
        HashMap<Integer, IPerformanceIndicator> mToHV = new HashMap<>(M.length);
        HashMap<Integer, IPerformanceIndicator> mToIGD_PLP = new HashMap<>(M.length);
        HashMap<Integer, IPerformanceIndicator> mToGD_PLP = new HashMap<>(M.length);

        for (int m : M)
        {
            System.out.println("For M = " + m);

            // Create reference bundle (do not use the same as the one passed to EAs for safety):
            int k = WFGBundle.getRecommendedNOPositionRelatedParameters(problemID, m);
            int l = WFGBundle.getRecommendedNODistanceRelatedParameters(problemID, m);
            WFGBundle referenceBundle = WFGBundle.getBundle(problemID, m, k, l);

            int samples = 1000000;
            { // HV
                double[] rp = new double[m];
                Arrays.fill(rp, 1.1d);
                mToHV.put(m, new HV(new HV.Params(m, referenceBundle._normalizations, rp)));
            }
            { // IGD/GD PLP
                IGoal[] gs = GoalsFactory.getPointLineProjectionsDND(m, mToCuts.get(m), referenceBundle._normalizations);
                double[][] rps = ReferencePointsFactory.getFilteredReferencePoints(problemID, samples, m, R, gs);
                IDistance distance = new space.distance.Euclidean(referenceBundle._normalizations);
                mToIGD_PLP.put(m, new IGD(distance, rps));
                mToGD_PLP.put(m, new GD(distance, rps));
            }
        }

        System.out.println("Creating the visualization system...");
        // M x 5 plots (Rows: M = objectives; Columns = performance indicators)

        // Indicators names:
        String[] indicatorsNames = new String[]{"HV", "IGD", "GD"};

        // Aggregated performance indicators: indicator name -> M -> indicator
        HashMap<String, HashMap<Integer, IPerformanceIndicator>> indicators = new HashMap<>(indicatorsNames.length);
        indicators.put(indicatorsNames[0], mToHV);
        indicators.put(indicatorsNames[1], mToIGD_PLP);
        indicators.put(indicatorsNames[2], mToGD_PLP);

        // Maps M -> display range for the Y-axis of the convergence plot (HV):
        HashMap<Integer, Range> mToDR_HV = new HashMap<>();
        mToDR_HV.put(2, new Range(0.25d, 0.9d));
        mToDR_HV.put(3, new Range(0.7d, 1.25d));
        mToDR_HV.put(4, new Range(1.0d, 1.5d));
        mToDR_HV.put(5, new Range(1.1d, 1.625d));

        // Maps M -> display range for the Y-axis of the convergence plot (IGD):
        HashMap<Integer, Range> mToDR_IGD = new HashMap<>();
        mToDR_IGD.put(2, new Range(0.0d, 0.12d));
        mToDR_IGD.put(3, new Range(0.0d, 0.14d));
        mToDR_IGD.put(4, new Range(0.0d, 0.16d));
        mToDR_IGD.put(5, new Range(0.0d, 0.18d));

        // Maps M -> display range for the Y-axis of the convergence plot (GD):
        HashMap<Integer, Range> mToDR_GD = new HashMap<>();
        mToDR_GD.put(2, new Range(0.0d, 0.2d));
        mToDR_GD.put(3, new Range(0.0d, 0.225d));
        mToDR_GD.put(4, new Range(0.0d, 0.25));
        mToDR_GD.put(5, new Range(0.0d, 0.3d));

        // Aggregate predefined y-ranges:
        HashMap<String, HashMap<Integer, Range>> yRanges = new HashMap<>(5);
        yRanges.put(indicatorsNames[0], mToDR_HV);
        yRanges.put(indicatorsNames[1], mToDR_IGD);
        yRanges.put(indicatorsNames[2], mToDR_GD);

        AbstractPlot[] plots = new AbstractPlot[M.length * indicatorsNames.length];
        int idx = 0;
        for (Integer m : M)
        {
            for (String indicatorsName : indicatorsNames)
            {
                Plot2D.Params pP = new Plot2D.Params();
                pP._scheme = new WhiteScheme();
                pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(0, mToG.get(m)), yRanges.get(indicatorsName).get(m));
                pP._title = "Problem = " + problemID + ", M = " + m;
                pP._yAxisTitle = indicatorsName;
                pP._xAxisTitle = "Generation";
                pP._drawLegend = true;
                plots[idx++] = new Plot2D(pP);
            }
        }

        GridPlots gridPlots = new GridPlots(plots, M.length, indicatorsNames.length, 4);
        Frame frame = new Frame(gridPlots, 0.4f, 0.8f);

        // Create data updater:
        DataUpdater.Params pDU = new DataUpdater.Params(gridPlots);
        // Create data sources:
        int total = M.length * algorithms.length * indicatorsNames.length;
        pDU._dataSources = new IDataSource[total];
        idx = 0;
        for (int i = 0; i < M.length; i++) // per row
            for (String indicatorsName : indicatorsNames) // per column
                for (int j = 0; j < algorithms.length; j++) // algorithms
                    pDU._dataSources[idx++] = new GenerationIndicator(eas[i][j], indicators.get(indicatorsName).get(M[i]));

        // Straightforward:
        pDU._dataProcessors = new IDataProcessor[total];
        pDU._sourcesToProcessors = new SourceToProcessors[total];
        for (int i = 0; i < total; i++)
        {
            pDU._dataProcessors[i] = new DataProcessor(true);
            pDU._sourcesToProcessors[i] = new SourceToProcessors(i);
        }

        pDU._processorToPlots = new ProcessorToPlots[total];
        idx = 0;
        for (int i = 0; i < M.length; i++) // per row
            for (int k = 0; k < indicatorsNames.length; k++) // per column
                for (String algorithm : algorithms)
                {
                    IDataSet rDS = DataSet.getForConvergencePlot2D(algorithm, new LineStyle(1.0f, dsColors.get(algorithm)));
                    pDU._processorToPlots[idx++] = new ProcessorToPlots(i * indicatorsNames.length + k, rDS);
                }

        DataUpdater dataUpdater;

        try
        {
            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        IVisualization visualization = new Visualization(frame, dataUpdater);

        System.out.println("Create the runner object...");

        // Create flat arrays
        EA[] rEAs = new EA[M.length * algorithms.length];
        int[] generations = new int[M.length * algorithms.length];
        int[] ssrs = new int[M.length * algorithms.length];
        idx = 0;
        for (int i = 0; i < M.length; i++)
            for (int j = 0; j < algorithms.length; j++)
            {
                rEAs[idx] = eas[i][j];
                generations[idx] = mToG.get(M[i]);
                ssrs[idx] = steadyStateRepeats.get(algorithms[j]).get(M[i]);
                idx++;
            }

        // Create runner:
        Runner.Params pR = new Runner.Params(rEAs, ssrs, visualization);
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;
        IRunner runner = new Runner(pR);

        System.out.println("Running experiments...");

        try
        {
            runner.executeEvolution(generations);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }
    }
}
