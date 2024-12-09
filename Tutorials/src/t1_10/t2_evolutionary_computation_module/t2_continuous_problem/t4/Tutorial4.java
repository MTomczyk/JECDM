package t1_10.t2_evolutionary_computation_module.t2_continuous_problem.t4;

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
import plot.Plot3D;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.GridPlots;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.AbstractScheme;
import scheme.WhiteScheme;
import scheme.enums.Align;
import space.Range;
import statistics.IStatistic;
import statistics.Max;
import statistics.Mean;
import statistics.Min;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.ContEASource;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Kernel;
import t1_10.t2_evolutionary_computation_module.t2_continuous_problem.common.Utils;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.GenerationAndStatistics;

import java.util.LinkedList;

/**
 * This tutorial focuses on creating an evolutionary method for solving the 2-variable continuous problem (fitness
 * landscape + populations over time + scatter plot).
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
        Kernel[] kernels = Utils.getDefaultKernels();
        IRandom R = new MersenneTwister64(0);
        int populationSize = 100;
        int generations = 50;
        EA contEA = Utils.getContEA(populationSize, kernels, R);

        // --- Construct plots
        AbstractPlot[] plots = new AbstractPlot[2];
        {
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
            plots[0] = new Plot3D(pP);
        }

        {
            Plot2D.Params pP = new Plot2D.Params();
            pP._xAxisTitle = "Generation";
            pP._yAxisTitle = "Fitness";
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D();
            plots[1] = new Plot2D(pP);
        }

        AbstractPlotsWrapper wrapper = new GridPlots(plots, 1, 2);
        //Frame frame = new Frame(wrapper, 0.8f, 0.5f);
        Frame frame = new Frame(wrapper, 1400, 600);

        AbstractScheme scheme = WhiteScheme.getForPlot3D(0.25f);
        plots[0].updateScheme(scheme);


        double[][] data = Utils.getFitnessLandscape(200, kernels);
        LinkedList<double[][]> listData = new LinkedList<>();
        listData.add(data);

        // Reference data sets for Plot 3D
        IDataSet refDSEA = DataSet.getFor3D("EA", new MarkerStyle(0.02f, Gradient.getInfernoGradient(), 3, Marker.SPHERE_LOW_POLY_3D));
        IDataSet refDSFitness = DataSet.getFor3D("Fitness landscape", new MarkerStyle(0.01f, Gradient.getViridisGradient(), 1, Marker.SPHERE_LOW_POLY_3D));
        plots[0].getModel().setDataSet(refDSFitness.wrapAround(listData), true);

        // Reference data set for the convergence plot 3D
        IDataSet refFitness = DataSet.getForConvergencePlot2D("Fitness",
                new LineStyle(1.0f, new Color(0.0f, 0.8f, 0.0f, 1.0f)),
                new color.Color(0.0f, 1.0f, 0.0f, 0.5f));

        // Set the visualization mode
        // Three data sources (2 for Plot3D and 1 for the convergence plot)
        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]
                {
                        new CyclicDataSource(data), // represents the fitness landscape
                        new ContEASource(contEA, true, false), // EA's source
                        new GenerationAndStatistics(contEA, new IStatistic[]{new Mean(), new Max(), new Min()}, 0)
                };

        // Three data processors
        pDU._dataProcessors = new IDataProcessor[]
                {
                        new DataProcessor(), // standard processor
                        new DataProcessor(true), // processor in the cumulative mode (to illustrate all populations)
                        new DataProcessor(true) // processor in the cumulative mode (to illustrate all populations)
                };

        // Three sources->processors mappings (1:1)
        pDU._sourcesToProcessors = new SourceToProcessors[]{
                new SourceToProcessors(0),
                new SourceToProcessors(1),
                new SourceToProcessors(2)
        };

        // Three processors->plot mappings (0:0), (1:0), and (2:1) <- for the convergence plot)
        pDU._processorToPlots = new ProcessorToPlots[]{
                new ProcessorToPlots(0, refDSFitness),
                new ProcessorToPlots(0, refDSEA),
                new ProcessorToPlots(1, refFitness)
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
