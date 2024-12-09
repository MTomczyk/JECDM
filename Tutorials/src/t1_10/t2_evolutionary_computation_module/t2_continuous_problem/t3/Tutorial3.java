package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.t3;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import ea.EA;
import exception.RunnerException;
import frame.Frame;
import plot.Plot3D;
import popupmenu.RightClickPopupMenu;
import popupmenu.item.SaveAsImage;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.SizeFields;
import space.Range;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.ContEASource;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Kernel;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Utils;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;

/**
 * This tutorial focuses on creating an evolutionary method for solving the 2-variable continuous problem (fitness
 * landscape + populations constructed oer time).
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
        Kernel[] kernels = Utils.getDefaultKernels();
        IRandom R = new MersenneTwister64(0);
        int populationSize = 2000;
        int generations = 20;
        EA contEA = Utils.getContEA(populationSize, kernels, R);

        Plot3D.Params pP = new Plot3D.Params();
        pP._xAxisTitle = "x1";
        pP._yAxisTitle = "Fitness";
        pP._zAxisTitle = "x2";
        pP._pDisplayRangesManager = new DisplayRangesManager.Params();
        pP._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        pP._pDisplayRangesManager._DR[0] = new DisplayRangesManager.DisplayRange(Range.getNormalRange()); // x
        pP._pDisplayRangesManager._DR[1] = new DisplayRangesManager.DisplayRange(null, true); // y
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(Range.getNormalRange()); // z
        pP._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true); // generation
        pP._pDisplayRangesManager._attIdx_to_drIdx = new Integer[]{0, 1, 2, 3};
        pP._colorbar = new Colorbar(Gradient.getInfernoGradient(), "Generation", new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 5));
        pP._axesAlignments = new Align[]{Align.FRONT_BOTTOM, Align.LEFT_BOTTOM, Align.BACK_RIGHT};
        Plot3D plot3D = new Plot3D(pP);

        //Frame frame = new Frame(plot3D, 1200, 1000);
        Frame frame = new Frame(plot3D, 0.5f);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot3D.getController().addRightClickPopupMenu(menu);

        AbstractScheme scheme = WhiteScheme.getForPlot3D(0.25f);
        scheme._sizes.put(SizeFields.COLORBAR_SHRINK, 0.5f);
        plot3D.updateScheme(scheme);

        double[][] data = Utils.getFitnessLandscape(200, kernels);

        // Create reference data sets
        IDataSet refDSEA = DataSet.getFor3D("EA", new MarkerStyle(0.02f, Gradient.getInfernoGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
        IDataSet refDSFitness = DataSet.getFor3D("Fitness landscape", new MarkerStyle(0.01f, Gradient.getViridisGradient(), 1, Marker.SPHERE_LOW_POLY_3D));

        // Set the visualization mode
        // Two data sources
        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]
                {
                        new CyclicDataSource(data), // represents the fitness landscape
                        new ContEASource(contEA, true, false), // EA's source
                        //new ContEASource(contEA, true, true),
                };

        // Two data processors
        pDU._dataProcessors = new IDataProcessor[]
                {
                        new DataProcessor(), // standard processor
                        new DataProcessor(true) // processor in the cumulative mode (to illustrate all populations)
                };

        // Two sources->processors mappings (i:i)
        pDU._sourcesToProcessors = new SourceToProcessors[]{
                new SourceToProcessors(0),
                new SourceToProcessors(1)
        };

        // Two processors->plot mappings (i:0)
        pDU._processorToPlots = new ProcessorToPlots[]{
                new ProcessorToPlots(0, refDSFitness),
                new ProcessorToPlots(0, refDSEA)
        };

        // Construct a regular data updater.
        DataUpdater dataUpdater;
        try
        {
            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Construct the visualization tool.
        IVisualization visualization = new Visualization(frame, dataUpdater);

        // Construct a suitably parameterized runner.
        Runner.Params pR = new Runner.Params(contEA);
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
