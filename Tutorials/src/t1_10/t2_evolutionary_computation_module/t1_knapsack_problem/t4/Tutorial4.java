package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.t4;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
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
import t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.common.Utils;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.EASource;

/**
 * This tutorial focuses on creating an evolutionary method for solving the knapsack problem and running the evolution
 * using the {@link IRunner} implementation. Additionally, it shows how to couple a scatter plot with the runner.
 *
 * @author MTomczyk
 */


public class Tutorial4
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

        // Creates knapsack capacities array
        int[] knapsackCapacities = new int[200];
        for (int i = 0; i < knapsackCapacities.length; i++)
            knapsackCapacities[i] = i;

        int generations = 100;
        boolean repair = true;

        // Create knapsack EAs
        EA[] knapsackEAs = new EA[knapsackCapacities.length];
        for (int i = 0; i < knapsackCapacities.length; i++)
            knapsackEAs[i] = Utils.getKnapsackEA(populationSize, R, knapsackCapacities[i], repair);

        Plot2D.Params pPlot = new Plot2D.Params();
        pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D();
        pPlot._xAxisTitle = "Value";
        pPlot._yAxisTitle = "Size";
        pPlot._drawLegend = false;
        Plot2D plot2D = new Plot2D(pPlot);

        Frame frame = new Frame(plot2D, 0.5f);
        //Frame frame = new Frame(plot2D, 600, 600);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot2D.getController().addRightClickPopupMenu(menu);

        // Create a straightforward source->processor->plot processing for the data updater
        IDataSource[] sourceEAs = new IDataSource[knapsackCapacities.length];
        for (int i = 0; i < knapsackCapacities.length; i++)
            sourceEAs[i] = new EASource(knapsackEAs[i], false);

        IDataProcessor[] dataProcessors = new IDataProcessor[knapsackCapacities.length];
        for (int i = 0; i < knapsackCapacities.length; i++)
            dataProcessors[i] = new DataProcessor();

        IDataSet[] referenceEAs = new IDataSet[knapsackCapacities.length];

        for (int i = 0; i < knapsackCapacities.length; i++)
        {
            MarkerStyle ms = new MarkerStyle(2.0f, new Color((float) i / (float) (knapsackCapacities.length - 1), 0.0f, 0.0f), Marker.CIRCLE);
            referenceEAs[i] = DataSet.getFor2D("Capacity = ", ms);
        }

        // Create the data updater:
        DataUpdater dataUpdater;
        try
        {

            DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
            pDU._dataSources = sourceEAs;
            pDU._dataProcessors = dataProcessors;
            pDU._sourcesToProcessors = new SourceToProcessors[knapsackCapacities.length];
            for (int i = 0; i < knapsackCapacities.length; i++) pDU._sourcesToProcessors[i] = new SourceToProcessors(i);
            pDU._processorToPlots = new ProcessorToPlots[knapsackCapacities.length];
            for (int i = 0; i < knapsackCapacities.length; i++)
                pDU._processorToPlots[i] = new ProcessorToPlots(0, referenceEAs[i]);
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
        // Execute evolution
        try
        {
            runner.executeEvolution(generations);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }
    }


}
