package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.t5;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import drmanager.DisplayRangesManager;
import ea.EA;
import exception.RunnerException;
import frame.Frame;
import plot.Plot2D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;
import statistics.IStatistic;
import statistics.Max;
import statistics.Mean;
import statistics.Min;
import t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common.Utils;
import updater.DataProcessor;
import updater.DataUpdater;
import updater.IDataProcessor;
import updater.IDataSource;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.GenerationAndStatistics;

/**
 * This tutorial focuses on creating an evolutionary method for solving the knapsack problem and running the evolution
 * using the {@link IRunner} implementation. Additionally, it shows how to couple a convergence plot with the runner.
 *
 * @author MTomczyk
 */


public class Tutorial5
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
        int populationSize = 100;
        int knapsackCapacity = 50;
        int generations = 50;
        boolean repair = true;

        EA knapsackEA = Utils.getKnapsackEA(populationSize, R, knapsackCapacity, repair);

        // Create the plot first (convergence plot):
        Plot2D.Params pPlot = new Plot2D.Params();
        pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();
        pPlot._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(new Range(25.0d, 150.0d), false);
        pPlot._xAxisTitle = "Generation";
        pPlot._yAxisTitle = "Value";
        pPlot._drawLegend = true;
        pPlot._scheme = new WhiteScheme();
        pPlot._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
        Plot2D plot2D = new Plot2D(pPlot);

        // Create the frame:
        Frame frame = new Frame(plot2D, 0.5f);
        //Frame frame = new Frame(plot2D, 600, 600);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot2D.getController().addRightClickPopupMenu(menu);

        // Create the EA-related source, processor, and reference data set (style):
        // The source produces a single data point from the current population: [generation, mean value, min value, and max value],
        // which is compatible with the input data for the convergence plot.
        IDataSource source = new GenerationAndStatistics(knapsackEA, new IStatistic[]{new Mean(), new Max(), new Min()}, 0);
        // Cumulative = true to capture all points generated throughout all generations.
        IDataProcessor dataProcessor = new DataProcessor(true);

        IDataSet referenceDS = DataSet.getForConvergencePlot2D(knapsackEA.getName(),  new LineStyle(0.5f, new Color(0.0f, 0.8f, 0.0f)), new color.Color(0.0f, 1.0f, 0.0f, 0.2f));
        //IDataSet referenceDS = DataSet.getForConvergencePlot2D(knapsackEA.getName(), new LineStyle(0.5f, new Color(0.8f, 0.0f, 0.0f)), new color.Color(1.0f, 0.0f, 0.0f, 0.2f));
        // Create data updater:
        DataUpdater dataUpdater;
        try
        {
            dataUpdater = DataUpdater.getSimpleDataUpdater(frame.getModel().getPlotsWrapper(), source, dataProcessor, referenceDS);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Create the visualization object
        IVisualization visualization = new Visualization(frame, dataUpdater);

        Runner.Params pR = new Runner.Params(knapsackEA);

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
