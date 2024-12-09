package t1_10.t2_evolutionary_computation_module.t1_knapsack_problem.t3;

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
import plot.Plot2D;
import population.Specimen;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
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


public class Tutorial3
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
        boolean repair = false;

        EA knapsackEA = Utils.getKnapsackEA(populationSize, R, knapsackCapacity, repair);

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
        pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                new FromDisplayRange(pPlot._pDisplayRangesManager._DR[2], 5));
        pPlot._scheme = new WhiteScheme();
        pPlot._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        Plot2D plot2D = new Plot2D(pPlot);

        // Create the frame:
        //Frame frame = new Frame(plot2D, 0.5f);
        Frame frame = new Frame(plot2D, 600, 600);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot2D.getController().addRightClickPopupMenu(menu);

        // Create the EA-related source, processor, and reference data set (style):
        IDataSource sourceEA = new EASource(knapsackEA, true);
        IDataProcessor dataProcessorEA = new DataProcessor(true);
        MarkerStyle ms = new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE);
        IDataSet referenceEA = DataSet.getFor2D(knapsackEA.getName(), ms);

        // Create the dummy data source (line defined by two points):
        IDataSource sourceLine = new CyclicDataSource(new double[][]{{0.0d, knapsackCapacity}, {150.0d, knapsackCapacity}});
        IDataProcessor dataProcessorLine = new DataProcessor();
        IDataSet referenceLine = DataSet.getFor2D("Capacity", new LineStyle(0.25f, Color.BLACK));


        // Create data updater:
        DataUpdater dataUpdater;
        try
        {
            DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
            // Two sources:
            pDU._dataSources = new IDataSource[]{sourceEA, sourceLine};
            // Two processors
            pDU._dataProcessors = new IDataProcessor[]{dataProcessorEA, dataProcessorLine};
            // Sources->Processors are linked 1:1
            pDU._sourcesToProcessors = new SourceToProcessors[]{
                    new SourceToProcessors(0),
                    new SourceToProcessors(1)
            };
            // Both processors point to the same plot (each uses a dedicated reference data set style):
            pDU._processorToPlots = new ProcessorToPlots[]
                    {
                            new ProcessorToPlots(0, referenceEA),
                            new ProcessorToPlots(0, referenceLine)
                    };

            dataUpdater = new DataUpdater(pDU);
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

        int printLimit = 10;

        try
        {
            System.out.println("Initialization (generation = 0):");
            runner.init();

            //noinspection DataFlowIssue
            for (int i = 0; i < Math.min(printLimit, populationSize); i++)
            {
                Specimen s = knapsackEA.getSpecimensContainer().getPopulation().get(i);
                Utils.printSpecimenData(s, true);
            }

        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

        for (int g = 1; g < generations; g++)
        {
            System.out.println("Generation = " + g + ":");
            try
            {
                runner.executeSingleGeneration(g, null);

                //noinspection DataFlowIssue
                for (int i = 0; i < Math.min(printLimit, populationSize); i++)
                {
                    Specimen s = knapsackEA.getSpecimensContainer().getPopulation().get(i);
                    Utils.printSpecimenData(s, true);
                }

            } catch (RunnerException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


}
