package emo.various;

import color.gradient.Color;
import criterion.Criteria;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.Utils;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import exception.RunnerException;
import frame.Frame;
import indicator.IPerformanceIndicator;
import indicator.emo.IGD;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import plot.parallelcoordinate.ParallelCoordinatePlot2D;
import plotswrapper.GridPlots;
import problem.Problem;
import problem.moo.ReferencePointsFactory;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import space.distance.Euclidean;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.EASource;
import visualization.updaters.sources.GenerationIndicator;

/**
 * Solving a test problem using NSGA, NSGA-II, NSGA-III, and MOEA/D.
 * The visualization module is run to present the results (scatter plot + convergence plot).
 * The number of objectives can be controlled.
 *
 * @author MTomczyk
 */

public class Algorithms_2D3D4D_IGD_Indicator
{
    /**
     * Runs evolutionary algorithm.
     *
     * @param args (not used)
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);

        int populationSize2D = 30;
        int offspringSize2D = 30;

        IGoal[] f2DNSGAIII = GoalsFactory.getPointLineProjectionsDND(2, populationSize2D - 1);
        IGoal[] f3DNSGAIII = GoalsFactory.getPointLineProjectionsDND(3, 12);
        IGoal[] f4DNSGAIII = GoalsFactory.getPointLineProjectionsDND(4, 9);

        IGoal[] f2DMOEAD = GoalsFactory.getPBIsDND(2, populationSize2D - 1, 3.0d);
        IGoal[] f3DMOEAD = GoalsFactory.getPBIsDND(3, 12, 3.0d);
        IGoal[] f4DMOEAD = GoalsFactory.getPBIsDND(4, 9, 3.0d);

        int populationSize3D = f3DNSGAIII.length;
        int offspringSize3D = f3DNSGAIII.length;
        int populationSize4D = f4DNSGAIII.length;
        int offspringSize4D = f4DNSGAIII.length;

        int [] generations = new int[]{100, 100, 100, 100, 200, 200, 200, 200, 300, 300, 300, 300};

        // create problem bundle
        Problem problem = Problem.DTLZ2;
        DTLZBundle problemBundle2D = DTLZBundle.getBundle(problem, 2, 5);
        DTLZBundle problemBundle3D = DTLZBundle.getBundle(problem, 3, 5);
        DTLZBundle problemBundle4D = DTLZBundle.getBundle(problem, 4, 5);

        // Prepare performance indicator.
        double[][] rps2D = ReferencePointsFactory.getRandomReferencePoints(problem, 1000, 2, R);
        double[][] rps3D = ReferencePointsFactory.getRandomReferencePoints(problem, 1000, 3, R);
        double[][] rps4D = ReferencePointsFactory.getRandomReferencePoints(problem, 1000, 4, R);
        IPerformanceIndicator indicator2D = new IGD(new Euclidean(problemBundle2D._normalizations), rps2D);
        IPerformanceIndicator indicator3D = new IGD(new Euclidean(problemBundle3D._normalizations), rps3D);
        IPerformanceIndicator indicator4D = new IGD(new Euclidean(problemBundle4D._normalizations), rps4D);

        boolean dynamicObjectiveRanges = false;
        Criteria criteria2D = Criteria.constructCriteria("C", 2, false);
        Criteria criteria3D = Criteria.constructCriteria("C", 3, false);
        Criteria criteria4D = Criteria.constructCriteria("C", 4, false);


        // prepare algorithms
        EA nsga2D = Utils.getNSGA(0, criteria2D, problemBundle2D, dynamicObjectiveRanges, R, populationSize2D, offspringSize2D, 0.2d);
        EA nsgaii2D = Utils.getNSGAII(1, criteria2D, problemBundle2D, dynamicObjectiveRanges, R, populationSize2D, offspringSize2D);
        EA nsgaiii2D = Utils.getNSGAIII(2, criteria2D, problemBundle2D, dynamicObjectiveRanges, R, f2DNSGAIII);
        EA moead2D = Utils.getMOEAD(3, criteria2D, problemBundle2D, dynamicObjectiveRanges, R, f2DMOEAD, new emo.utils.decomposition.similarity.pbi.Euclidean());

        EA nsga3D = Utils.getNSGA(4, criteria3D, problemBundle3D, dynamicObjectiveRanges, R, populationSize3D, offspringSize3D, 0.2d);
        EA nsgaii3D = Utils.getNSGAII(5, criteria3D, problemBundle3D, dynamicObjectiveRanges, R, populationSize3D, offspringSize3D);
        EA nsgaiii3D = Utils.getNSGAIII(6, criteria3D, problemBundle3D, dynamicObjectiveRanges, R, f3DNSGAIII);
        EA moead3D = Utils.getMOEAD(7, criteria3D, problemBundle3D, dynamicObjectiveRanges, R, f3DMOEAD, new emo.utils.decomposition.similarity.pbi.Euclidean());

        EA nsga4D = Utils.getNSGA(8, criteria4D, problemBundle4D, dynamicObjectiveRanges, R, populationSize4D, offspringSize4D, 0.2d);
        EA nsgaii4D = Utils.getNSGAII(9, criteria4D, problemBundle4D, dynamicObjectiveRanges, R, populationSize4D, offspringSize4D);
        EA nsgaiii4D = Utils.getNSGAIII(10, criteria4D, problemBundle4D, dynamicObjectiveRanges, R, f4DNSGAIII);
        EA moead4D = Utils.getMOEAD(11, criteria4D, problemBundle4D, dynamicObjectiveRanges, R, f4DMOEAD, new emo.utils.decomposition.similarity.pbi.Euclidean());


        // create visualization module
        Plot2D plot2DScatter;
        {
            Plot2D.Params params = new Plot2D.Params();
            params._xAxisTitle = "f1";
            params._yAxisTitle = "f2";
            params._drawLegend = true;
            params._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(problemBundle2D._displayRanges[0],
                    problemBundle2D._displayRanges[1]);
            plot2DScatter = new Plot2D(params);
        }
        Plot2D plot2DConvergence;
        {
            Plot2D.Params params = new Plot2D.Params();
            params._xAxisTitle = "Generation";
            params._yAxisTitle = "IGD (2D case)";
            params._drawLegend = true;
            params._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();
            plot2DConvergence = new Plot2D(params);
        }
        Plot3D plot3DScatter;
        {
            Plot3D.Params params = new Plot3D.Params();
            params._xAxisTitle = "f1";
            params._yAxisTitle = "f2";
            params._zAxisTitle = "f3";
            params._drawLegend = true;
            params._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(problemBundle3D._displayRanges[0],
                    problemBundle3D._displayRanges[1], problemBundle3D._displayRanges[2]);
            plot3DScatter = new Plot3D(params);
        }
        Plot2D plot3DConvergence;
        {
            Plot2D.Params params = new Plot2D.Params();
            params._xAxisTitle = "Generation";
            params._yAxisTitle = "IGD (3D case)";
            params._drawLegend = true;
            params._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();
            plot3DConvergence = new Plot2D(params);
        }
        Plot2D plot4DScatter;
        {
            ParallelCoordinatePlot2D.Params params = new ParallelCoordinatePlot2D.Params(4);
            params._axesTitles = new String[]{"f1", "f2", "f3", "f4"};
            params._xAxisTitle = "Objectives";
            params._drawLegend = true;
            params._pDisplayRangesManager = DisplayRangesManager.Params.getForParallelCoordinatePlot2D(
                    4, problemBundle4D._displayRanges, new boolean[4], new boolean[4]);
            plot4DScatter = new ParallelCoordinatePlot2D(params);
        }
        Plot2D plot4DConvergence;
        {
            Plot2D.Params params = new Plot2D.Params();
            params._xAxisTitle = "Generation";
            params._yAxisTitle = "IGD (4D case)";
            params._drawLegend = true;
            params._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();
            plot4DConvergence = new Plot2D(params);
        }

        AbstractPlot[] plots = new AbstractPlot[]{plot2DScatter, plot2DConvergence,
                plot3DScatter, plot3DConvergence, plot4DScatter, plot4DConvergence};
        GridPlots.Params pG = new GridPlots.Params(plots, 3, 2);
        pG._noUpdatersQueues = 8;
        GridPlots gridPlots = new GridPlots(pG);
        Frame frame = new Frame(gridPlots, 0.5f, 0.5f);

        // create updaters
        DataSet dsNSGA_plot2DScatter = DataSet.getFor2D("NSGA", new MarkerStyle(2.0f, color.gradient.Color.RED, Marker.CIRCLE));
        DataSet dsNSGAII_plot2DScatter = DataSet.getFor2D("NSGA-II", new MarkerStyle(2.0f, Color.BLUE, Marker.CIRCLE));
        DataSet dsNSGAIII_plot2DScatter = DataSet.getFor2D("NSGA-III", new MarkerStyle(2.0f, Color.GREEN, Marker.CIRCLE));
        DataSet dsMOEAD_plot2DScatter = DataSet.getFor2D("MOEA/D", new MarkerStyle(2.0f, Color.CYAN, Marker.CIRCLE));

        DataSet dsNSGA_plot2DConvergence = DataSet.getForConvergencePlot2D("NSGA", (double[][]) null, new LineStyle(1.0f, Color.RED), color.Color.RED);
        DataSet dsNSGAII_plot2DConvergence = DataSet.getForConvergencePlot2D("NSGA-II", (double[][])null, new LineStyle(1.0f, Color.BLUE), color.Color.BLUE);
        DataSet dsNSGAIII_plot2DConvergence = DataSet.getForConvergencePlot2D("NSGA-III", (double[][])null, new LineStyle(1.0f, Color.GREEN), color.Color.GREEN);
        DataSet dsMOEAD_plot2DConvergence = DataSet.getForConvergencePlot2D("MOEA/D", (double[][])null, new LineStyle(1.0f, Color.CYAN), color.Color.CYAN);

        DataSet dsNSGA_plot3DScatter = DataSet.getFor3D("NSGA", new MarkerStyle(0.02f, color.gradient.Color.RED, Marker.SPHERE_LOW_POLY_3D));
        DataSet dsNSGAII_plot3DScatter = DataSet.getFor3D("NSGA-II", new MarkerStyle(0.02f, Color.BLUE, Marker.SPHERE_LOW_POLY_3D));
        DataSet dsNSGAIII_plot3DScatter = DataSet.getFor3D("NSGA-III", new MarkerStyle(0.02f, Color.GREEN, Marker.SPHERE_LOW_POLY_3D));
        DataSet dsMOEAD_plot3DScatter = DataSet.getFor3D("MOEA/D", new MarkerStyle(0.02f, Color.CYAN, Marker.SPHERE_LOW_POLY_3D));

        DataSet dsNSGA_plot3DConvergence = DataSet.getForConvergencePlot2D("NSGA", (double[][])null, new LineStyle(1.0f, Color.RED), color.Color.RED);
        DataSet dsNSGAII_plot3DConvergence = DataSet.getForConvergencePlot2D("NSGA-II", (double[][])null, new LineStyle(1.0f, Color.BLUE), color.Color.BLUE);
        DataSet dsNSGAIII_plot3DConvergence = DataSet.getForConvergencePlot2D("NSGA-III", (double[][])null, new LineStyle(1.0f, Color.GREEN), color.Color.GREEN);
        DataSet dsMOEAD_plot3DConvergence = DataSet.getForConvergencePlot2D("MOEA/D", (double[][])null, new LineStyle(1.0f, Color.CYAN), color.Color.CYAN);

        DataSet dsNSGA_plot4DScatter = DataSet.getForParallelCoordinatePlot2D("NSGA", 4, new LineStyle(1.0f, Color.RED));
        DataSet dsNSGAII_plot4DScatter = DataSet.getForParallelCoordinatePlot2D("NSGA-II", 4, new LineStyle(1.0f, Color.BLUE));
        DataSet dsNSGAIII_plot4DScatter = DataSet.getForParallelCoordinatePlot2D("NSGA-III", 4, new LineStyle(1.0f, Color.GREEN));
        DataSet dsMOEAD_plot4DScatter = DataSet.getForParallelCoordinatePlot2D("MOEA/D", 4, new LineStyle(1.0f, Color.CYAN));

        DataSet dsNSGA_plot4DConvergence = DataSet.getForConvergencePlot2D("NSGA", (double[][])null, new LineStyle(1.0f, Color.RED), color.Color.RED);
        DataSet dsNSGAII_plot4DConvergence = DataSet.getForConvergencePlot2D("NSGA-II", (double[][])null, new LineStyle(1.0f, Color.BLUE), color.Color.BLUE);
        DataSet dsNSGAIII_plot4DConvergence = DataSet.getForConvergencePlot2D("NSGA-III", (double[][])null, new LineStyle(1.0f, Color.GREEN), color.Color.GREEN);
        DataSet dsMOEAD_plot4DConvergence = DataSet.getForConvergencePlot2D("MOEA/D", (double[][])null, new LineStyle(1.0f, Color.CYAN), color.Color.CYAN);

        IDataSource sourceNSGA_plot2DScatter = new EASource(nsga2D, false);
        IDataSource sourceNSGAII_plot2DScatter = new EASource(nsgaii2D, false);
        IDataSource sourceNSGAIII_plot2DScatter = new EASource(nsgaiii2D, false);
        IDataSource sourceMOEAD_plot2DScatter = new EASource(moead2D, false);

        IDataSource sourceNSGA_plot2DConvergence = new GenerationIndicator(nsga2D, indicator2D);
        IDataSource sourceNSGAII_plot2DConvergence = new GenerationIndicator(nsgaii2D, indicator2D);
        IDataSource sourceNSGAIII_plot2DConvergence = new GenerationIndicator(nsgaiii2D, indicator2D);
        IDataSource sourceMOEAD_plot2DConvergence = new GenerationIndicator(moead2D, indicator2D);

        IDataSource sourceNSGA_plot3DScatter = new EASource(nsga3D, false);
        IDataSource sourceNSGAII_plot3DScatter = new EASource(nsgaii3D, false);
        IDataSource sourceNSGAIII_plot3DScatter = new EASource(nsgaiii3D, false);
        IDataSource sourceMOEAD_plot3DScatter = new EASource(moead3D, false);

        IDataSource sourceNSGA_plot3DConvergence = new GenerationIndicator(nsga3D, indicator3D);
        IDataSource sourceNSGAII_plot3DConvergence = new GenerationIndicator(nsgaii3D, indicator3D);
        IDataSource sourceNSGAIII_plot3DConvergence = new GenerationIndicator(nsgaiii3D, indicator3D);
        IDataSource sourceMOEAD_plot3DConvergence = new GenerationIndicator(moead3D, indicator3D);

        IDataSource sourceNSGA_plot4DScatter = new EASource(nsga4D, false);
        IDataSource sourceNSGAII_plot4DScatter = new EASource(nsgaii4D, false);
        IDataSource sourceNSGAIII_plot4DScatter = new EASource(nsgaiii4D, false);
        IDataSource sourceMOEAD_plot4DScatter = new EASource(moead4D, false);

        IDataSource sourceNSGA_plot4DConvergence = new GenerationIndicator(nsga4D, indicator4D);
        IDataSource sourceNSGAII_plot4DConvergence = new GenerationIndicator(nsgaii4D, indicator4D);
        IDataSource sourceNSGAIII_plot4DConvergence = new GenerationIndicator(nsgaiii4D, indicator4D);
        IDataSource sourceMOEAD_plot4DConvergence = new GenerationIndicator(moead4D, indicator4D);


        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]{
                sourceNSGA_plot2DScatter,
                sourceNSGAII_plot2DScatter,
                sourceNSGAIII_plot2DScatter,
                sourceMOEAD_plot2DScatter,

                sourceNSGA_plot2DConvergence,
                sourceNSGAII_plot2DConvergence,
                sourceNSGAIII_plot2DConvergence,
                sourceMOEAD_plot2DConvergence,

                sourceNSGA_plot3DScatter,
                sourceNSGAII_plot3DScatter,
                sourceNSGAIII_plot3DScatter,
                sourceMOEAD_plot3DScatter,

                sourceNSGA_plot3DConvergence,
                sourceNSGAII_plot3DConvergence,
                sourceNSGAIII_plot3DConvergence,
                sourceMOEAD_plot3DConvergence,

                sourceNSGA_plot4DScatter,
                sourceNSGAII_plot4DScatter,
                sourceNSGAIII_plot4DScatter,
                sourceMOEAD_plot4DScatter,

                sourceNSGA_plot4DConvergence,
                sourceNSGAII_plot4DConvergence,
                sourceNSGAIII_plot4DConvergence,
                sourceMOEAD_plot4DConvergence
        };

        pDU._dataProcessors = new IDataProcessor[]{
                new DataProcessor(), new DataProcessor(), new DataProcessor(), new DataProcessor(),
                new DataProcessor(true), new DataProcessor(true), new DataProcessor(true), new DataProcessor(true),
                new DataProcessor(), new DataProcessor(), new DataProcessor(), new DataProcessor(),
                new DataProcessor(true), new DataProcessor(true), new DataProcessor(true), new DataProcessor(true),
                new DataProcessor(), new DataProcessor(), new DataProcessor(), new DataProcessor(),
                new DataProcessor(true), new DataProcessor(true), new DataProcessor(true), new DataProcessor(true)};

        pDU._sourcesToProcessors = new SourceToProcessors[24];
        for (int i = 0; i < 24; i++) pDU._sourcesToProcessors[i] = new SourceToProcessors(i);

        pDU._processorToPlots = new ProcessorToPlots[]{
                new ProcessorToPlots(new int[]{0}, new IDataSet[]{dsNSGA_plot2DScatter}),
                new ProcessorToPlots(new int[]{0}, new IDataSet[]{dsNSGAII_plot2DScatter}),
                new ProcessorToPlots(new int[]{0}, new IDataSet[]{dsNSGAIII_plot2DScatter}),
                new ProcessorToPlots(new int[]{0}, new IDataSet[]{dsMOEAD_plot2DScatter}),

                new ProcessorToPlots(new int[]{1}, new IDataSet[]{dsNSGA_plot2DConvergence}),
                new ProcessorToPlots(new int[]{1}, new IDataSet[]{dsNSGAII_plot2DConvergence}),
                new ProcessorToPlots(new int[]{1}, new IDataSet[]{dsNSGAIII_plot2DConvergence}),
                new ProcessorToPlots(new int[]{1}, new IDataSet[]{dsMOEAD_plot2DConvergence}),

                new ProcessorToPlots(new int[]{2}, new IDataSet[]{dsNSGA_plot3DScatter}),
                new ProcessorToPlots(new int[]{2}, new IDataSet[]{dsNSGAII_plot3DScatter}),
                new ProcessorToPlots(new int[]{2}, new IDataSet[]{dsNSGAIII_plot3DScatter}),
                new ProcessorToPlots(new int[]{2}, new IDataSet[]{dsMOEAD_plot3DScatter}),

                new ProcessorToPlots(new int[]{3}, new IDataSet[]{dsNSGA_plot3DConvergence}),
                new ProcessorToPlots(new int[]{3}, new IDataSet[]{dsNSGAII_plot3DConvergence}),
                new ProcessorToPlots(new int[]{3}, new IDataSet[]{dsNSGAIII_plot3DConvergence}),
                new ProcessorToPlots(new int[]{3}, new IDataSet[]{dsMOEAD_plot3DConvergence}),

                new ProcessorToPlots(new int[]{4}, new IDataSet[]{dsNSGA_plot4DScatter}),
                new ProcessorToPlots(new int[]{4}, new IDataSet[]{dsNSGAII_plot4DScatter}),
                new ProcessorToPlots(new int[]{4}, new IDataSet[]{dsNSGAIII_plot4DScatter}),
                new ProcessorToPlots(new int[]{4}, new IDataSet[]{dsMOEAD_plot4DScatter}),

                new ProcessorToPlots(new int[]{5}, new IDataSet[]{dsNSGA_plot4DConvergence}),
                new ProcessorToPlots(new int[]{5}, new IDataSet[]{dsNSGAII_plot4DConvergence}),
                new ProcessorToPlots(new int[]{5}, new IDataSet[]{dsNSGAIII_plot4DConvergence}),
                new ProcessorToPlots(new int[]{5}, new IDataSet[]{dsMOEAD_plot4DConvergence})
        };


        DataUpdater updater;
        try
        {
            updater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        IVisualization visualization = new Visualization(frame, updater);

        // create runner object
        Runner.Params pR = new Runner.Params(new EA[]{
                nsga2D, nsgaii2D, nsgaiii2D, moead2D,
                nsga3D, nsgaii3D, nsgaiii3D, moead3D,
                nsga4D, nsgaii4D, nsgaiii4D, moead4D},
                new int[]{
                        1, 1, 1, f2DMOEAD.length,
                        1, 1, 1, f3DMOEAD.length,
                        1, 1, 1, f4DMOEAD.length
                },
                visualization);
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;

        IRunner runner = new Runner(pR);

        // run the evolution
         try
        {
            runner.executeEvolution(generations);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }
    }
}
