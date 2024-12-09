package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.t2;

import color.gradient.Color;
import color.gradient.Gradient;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
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
import scheme.enums.SizeFields;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.ContEASource;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Kernel;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Utils;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;

import java.util.LinkedList;

/**
 * This tutorial focuses on creating an evolutionary method for solving the 2-variable continuous problem.
 *
 * @author MTomczyk
 */
public class Tutorial2
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
        int populationSize = 1000;
        int generations = 20;
        EA contEA = Utils.getContEA(populationSize, kernels, R);

        Plot3D plot3D = Utils.createDedicatedScatterPlot();
        Frame frame = new Frame(plot3D, 1200, 1000);
        //Frame frame = new Frame(plot3D, 0.5f);

        RightClickPopupMenu menu = new RightClickPopupMenu();
        menu.addItem(new SaveAsImage());
        plot3D.getController().addRightClickPopupMenu(menu);

        AbstractScheme scheme = WhiteScheme.getForPlot3D(0.25f);
        scheme._sizes.put(SizeFields.COLORBAR_SHRINK, 0.5f);
        plot3D.updateScheme(scheme);

        double[][] data = Utils.getFitnessLandscape(200, kernels);
        LinkedList<double[][]> listData = new LinkedList<>();
        listData.add(data);

        IDataSet refDSEA = DataSet.getFor3D("EA", new MarkerStyle(0.02f, Color.BLACK, Marker.SPHERE_LOW_POLY_3D));
        IDataSet refDSFitness = DataSet.getFor3D("Fitness landscape", new MarkerStyle(0.01f, Gradient.getViridisGradient(), 1, Marker.SPHERE_LOW_POLY_3D));
        plot3D.getModel().setDataSet(refDSFitness.wrapAround(listData), true);

        // Set the visualization mode
        // Two data sources
        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]
                {
                        new CyclicDataSource(data), // represents the fitness landscape
                        new ContEASource(contEA, false, false), // EA's source
                        //new ContEASource(contEA, true, true),
                };

        pDU._dataProcessors = new IDataProcessor[]
                {
                        new DataProcessor(),  // standard processor
                        new DataProcessor()  // standard processor
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
