package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.t6;

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
import exception.RunnerException;
import frame.Frame;
import plot.AbstractPlot;
import plot.Plot2D;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import space.Range;
import statistics.IStatistic;
import statistics.Max;
import statistics.Mean;
import statistics.Min;
import t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common.Utils;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.EASource;
import visualization.updaters.sources.GenerationAndStatistics;

/**
 * This tutorial focuses on creating an evolutionary method for solving the knapsack problem and running the evolution
 * using the {@link IRunner} implementation. The visualization mode concerns three plots:
 * - scatter plot for the method with the repair mode on;
 * - scatter plot for the method with the repair mode off;
 * - convergence plot revealing the performance of both methods.
 *
 * @author MTomczyk
 */


public class Tutorial6
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int populationSize = 1000;
        int knapsackCapacity = 50;
        int generations = 50;

        EA[] knapsackEAs = new EA[]
                {
                        Utils.getKnapsackEA(populationSize, R, knapsackCapacity, true),
                        Utils.getKnapsackEA(populationSize, R, knapsackCapacity, false)
                };

        // Create the plots:
        AbstractPlot[] plots = new AbstractPlot[3];

        // Scatter plots
        Gradient[] gradients = new Gradient[]{Gradient.getViridisGradient(), Gradient.getViridisGradient()};
        for (int i = 0; i < 2; i++)
        {
            // Create the plot first (the third display range is associated with the generation):
            Plot2D.Params pPlot = new Plot2D.Params();
            pPlot._pDisplayRangesManager = new DisplayRangesManager.Params();
            pPlot._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[3];
            pPlot._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(new Range(20.0d, 120.0d), false);
            pPlot._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(20.0d, 90.0d), false);
            pPlot._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true);
            pPlot._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2};
            pPlot._xAxisTitle = "Value";
            pPlot._yAxisTitle = "Size";
            pPlot._drawLegend = true;
            pPlot._colorbar = new Colorbar(gradients[i], "Generation",
                    new FromDisplayRange(pPlot._pDisplayRangesManager._DR[2], 5));
            pPlot._scheme = new WhiteScheme();
            pPlot._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
            plots[i] = new Plot2D(pPlot);
        }

        // Convergence plot:
        {
            Plot2D.Params pPlot = new Plot2D.Params();
            pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();
            pPlot._xAxisTitle = "Generation";
            pPlot._yAxisTitle = "Value";
            pPlot._drawLegend = true;
            pPlot._scheme = new WhiteScheme();
            pPlot._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
            plots[2] = new Plot2D(pPlot);
        }

        // Create the plots wrapper (grid-layout; 1 row and 3 columns):
        AbstractPlotsWrapper plotsWrapper = new GridPlots(plots, 1, 3);

        // Create the frame:
        Frame frame = new Frame(plotsWrapper, 0.8f, 0.5f);
        //Frame frame = new Frame(plotsWrapper, 1800, 600);

        // Create the sources:
        IDataSource sourceEA0Scatter = new EASource(knapsackEAs[0], true);
        IDataSource sourceEA1Scatter = new EASource(knapsackEAs[1], true);
        IDataSource sourceEA0Convergence = new GenerationAndStatistics(knapsackEAs[0], new IStatistic[]{new Mean(), new Max(), new Min()}, 0);
        IDataSource sourceEA1Convergence = new GenerationAndStatistics(knapsackEAs[1], new IStatistic[]{new Mean(), new Max(), new Min()}, 0);

        // Create data processors:
        IDataProcessor dataProcessorEA0Scatter = new DataProcessor(true);
        IDataProcessor dataProcessorEA1Scatter = new DataProcessor(true);
        IDataProcessor dataProcessorEA0Convergence = new DataProcessor(true);
        IDataProcessor dataProcessorEA1Convergence = new DataProcessor(true);

        // Create reference data sets:
        IDataSet referenceEA0Scatter = DataSet.getFor2D(knapsackEAs[0].getName(), new MarkerStyle(2.0f,
                gradients[0], 2, Marker.CIRCLE));
        IDataSet referenceEA1Scatter = DataSet.getFor2D(knapsackEAs[1].getName(), new MarkerStyle(2.0f,
                gradients[1], 2, Marker.CIRCLE));
        IDataSet referenceEA0Convergence = DataSet.getForConvergencePlot2D(knapsackEAs[0].getName(),
                new LineStyle(0.5f, new Color(0.0f, 0.8f, 0.0f)),
                new color.Color(0.0f, 1.0f, 0.0f, 0.2f));
        IDataSet referenceEA1Convergence = DataSet.getForConvergencePlot2D(knapsackEAs[1].getName(),
                new LineStyle(0.5f, new Color(0.8f, 0.0f, 0.0f)),
                new color.Color(1.0f, 0.0f, 0.0f, 0.2f));

        // Create data updater:
        DataUpdater dataUpdater;
        try
        {
            DataUpdater.Params pDU = new DataUpdater.Params(plotsWrapper);
            pDU._dataSources = new IDataSource[]{
                    sourceEA0Scatter, sourceEA1Scatter, sourceEA0Convergence, sourceEA1Convergence
            };
            pDU._dataProcessors = new IDataProcessor[]{
                    dataProcessorEA0Scatter, dataProcessorEA1Scatter, dataProcessorEA0Convergence, dataProcessorEA1Convergence
            };
            pDU._sourcesToProcessors = new SourceToProcessors[]{
                    new SourceToProcessors(0),
                    new SourceToProcessors(1),
                    new SourceToProcessors(2),
                    new SourceToProcessors(3)
            };
            pDU._processorToPlots = new ProcessorToPlots[]{
                    new ProcessorToPlots(0, referenceEA0Scatter),
                    new ProcessorToPlots(1, referenceEA1Scatter),
                    new ProcessorToPlots(2, referenceEA0Convergence),
                    new ProcessorToPlots(2, referenceEA1Convergence)
            };

            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Create the visualization object
        IVisualization visualization = new Visualization(frame, dataUpdater);

        Runner.Params pR = new Runner.Params(knapsackEAs);

        // Set the visualization mode:
        pR._visualization = visualization;
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;

        IRunner runner = new Runner(pR);
        // Use one method to execute the evolution:
        try
        {
            runner.executeEvolution(generations);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

    }

}
