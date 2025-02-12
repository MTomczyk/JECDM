package t1_10.t3_evolutionary_multiobjective_optimization.t4_algorithms.t3_nsgaiii;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import component.drawingarea.DrawingArea3D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.nsgaiii.NSGAIII;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.nsgaiii.IAssignmentResolveTie;
import emo.utils.decomposition.nsgaiii.ISpecimenResolveTie;
import emo.utils.decomposition.nsgaiii.RandomAssignment;
import emo.utils.decomposition.nsgaiii.RandomSpecimen;
import exception.RunnerException;
import frame.Frame;
import indicator.IPerformanceIndicator;
import indicator.emo.HV;
import plot.AbstractPlot;
import plot.Plot2D;
import plot.Plot3D;
import plotswrapper.GridPlots;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
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

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This tutorial showcases how to instantiate and run NSGA-III algorithm.
 * Note that this is a revised version of {@link Tutorial3wfg1}.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode"})
public class Tutorial3dtlz2Alternative
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
        // The number of objectives (scenarios):
        int[] M = new int[]{2, 3};

        // Maps M -> the number of cuts for the Das and Dennis' method
        HashMap<Integer, Integer> mToCuts = new HashMap<>();
        mToCuts.put(2, 29);
        mToCuts.put(3, 30);

        int G = 500;

        Problem problemID = Problem.DTLZ2;
        boolean dynamicOSUpdate = false;

        HashMap<Integer, Range> mToDR = new HashMap<>();
        mToDR.put(2, new Range(0.0d, 0.5d));
        mToDR.put(3, new Range(0.0d, 1.0d));

        EA[] ea = new EA[M.length];

        DTLZBundle[] problems = new DTLZBundle[M.length];
        for (int i = 0; i < M.length; i++)
        {
            int k = DTLZBundle.getRecommendedNODistanceRelatedParameters(problemID, M[i]);
            problems[i] = DTLZBundle.getBundle(problemID, M[i], k);
        }

        for (int i = 0; i < M.length; i++)
        {
            IGoal[] goals = GoalsFactory.getPBIsDND(M[i], mToCuts.get(M[i]), 5.0d, null);
            IAssignmentResolveTie assignmentResolveTie = new RandomAssignment();
            ISpecimenResolveTie specimenResolveTie = new RandomSpecimen();
            ea[i] = NSGAIII.getNSGAIII(i, dynamicOSUpdate, false, R, goals, problems[i], assignmentResolveTie, specimenResolveTie);
        }


        AbstractPlot[] plots = new AbstractPlot[M.length * 2];

        for (int i = 0; i < M.length; i++)
        {
            if (M[i] == 2)
            {
                Plot2D.Params pP = new Plot2D.Params();
                pP._title = "Populations over generations (M = " + M[i] + ")";
                pP._xAxisTitle = "f1";
                pP._yAxisTitle = "f2";
                pP._pDisplayRangesManager = new DisplayRangesManager.Params(new Range[]{problems[i]._displayRanges[0],
                        problems[i]._displayRanges[1], Range.get0R(G - 1)}, false);
                pP._scheme = new WhiteScheme();
                // Adjust scheme
                pP._scheme.setAllFontsTo("Times New Roman");
                pP._scheme.rescale(1.25f, SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
                plots[2 * i] = new Plot2D(pP);
                // Adjust formatters:
                plots[2 * i].getComponentsContainer().getAxes()[0].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
                plots[2 * i].getComponentsContainer().getAxes()[1].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
                plots[2 * i].getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat("0"));
            }
            else
            {
                Plot3D.Params pP = new Plot3D.Params();
                pP._title = "Populations over generations (M = " + M[i] + ")";
                pP._xAxisTitle = "f1";
                pP._yAxisTitle = "f2";
                pP._zAxisTitle = "f3";
                pP._pDisplayRangesManager = new DisplayRangesManager.Params(new Range[]{problems[i]._displayRanges[0],
                        problems[i]._displayRanges[1], problems[i]._displayRanges[2], Range.get0R(G - 1)}, false);
                pP._scheme = WhiteScheme.getForPlot3D(0.25f);
                pP._scheme.setAllFontsTo("Times New Roman");
                pP._scheme.rescale(1.5f, SizeFields.AXIS3D_X_TICK_LABEL_FONT_SIZE_SCALE);
                pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Y_TICK_LABEL_FONT_SIZE_SCALE);
                pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Z_TICK_LABEL_FONT_SIZE_SCALE);
                pP._scheme.rescale(1.5f, SizeFields.AXIS3D_X_TITLE_FONT_SIZE_SCALE);
                pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Y_TITLE_FONT_SIZE_SCALE);
                pP._scheme.rescale(1.5f, SizeFields.AXIS3D_Z_TITLE_FONT_SIZE_SCALE);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_COLORBAR_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._scheme.rescale(1.25f, SizeFields.AXIS_COLORBAR_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
                pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                        new FromDisplayRange(pP._pDisplayRangesManager._DR[3], 5));
                plots[2 * i] = new Plot3D(pP);
                plots[2 * i].getComponentsContainer().getColorbar().getAxis().getTicksDataGetter().setNumberFormat(new DecimalFormat("0"));
                ((DrawingArea3D) plots[2 * i].getComponentsContainer().getDrawingArea()).getAxes()[0].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
                ((DrawingArea3D) plots[2 * i].getComponentsContainer().getDrawingArea()).getAxes()[1].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
                ((DrawingArea3D) plots[2 * i].getComponentsContainer().getDrawingArea()).getAxes()[2].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
            }

            Plot2D.Params pP = new Plot2D.Params();
            pP._title = "HV over generations (M = " + M[i] + ")";
            pP._yAxisTitle = "HV";
            pP._xAxisTitle = "Generation";
            pP._scheme = WhiteScheme.getForPCP2D();
            // Adjust scheme
            pP._scheme.setAllFontsTo("Times New Roman");
            pP._scheme.rescale(1.25f, SizeFields.AXIS_X_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme.rescale(1.25f, SizeFields.AXIS_Y_TICK_LABEL_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme.rescale(1.25f, SizeFields.AXIS_X_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme.rescale(1.25f, SizeFields.AXIS_Y_TITLE_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._scheme.rescale(1.25f, SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER);
            pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D(Range.get0R(G - 1), mToDR.get(M[i]));
            plots[2 * i + 1] = new Plot2D(pP);
            plots[2 * i + 1].getComponentsContainer().getAxes()[0].getTicksDataGetter().setNumberFormat(new DecimalFormat("0"));
            plots[2 * i + 1].getComponentsContainer().getAxes()[1].getTicksDataGetter().setNumberFormat(new DecimalFormat("0.00"));
        }


        GridPlots gridPlots = new GridPlots(plots, M.length, 2, 4);

        Frame frame = new Frame(gridPlots, 1400, 1200);
        DataUpdater.Params pDU = new DataUpdater.Params(gridPlots);

        pDU._dataSources = new IDataSource[M.length * 2];
        for (int i = 0; i < M.length; i++)
        {
            double[] RP = new double[M[i]];
            Arrays.fill(RP, 1.1d);
            HV.Params pHV = new HV.Params(M[i], problems[i]._normalizations, RP);
            pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
            IPerformanceIndicator HV = new HV(pHV);

            pDU._dataSources[2 * i] = new EASource(ea[i], true); // true = add timestamp
            pDU._dataSources[2 * i + 1] = new GenerationIndicator(ea[i], HV); // true = add timestamp
        }

        pDU._dataProcessors = new IDataProcessor[M.length * 2];
        for (int i = 0; i < M.length * 2; i++) pDU._dataProcessors[i] = new DataProcessor(true);
        pDU._sourcesToProcessors = new SourceToProcessors[M.length * 2];
        for (int i = 0; i < M.length * 2; i++) pDU._sourcesToProcessors[i] = new SourceToProcessors(i);
        pDU._processorToPlots = new ProcessorToPlots[M.length * 2];

        for (int i = 0; i < M.length; i++)
        {
            IDataSet dataSet;

            Gradient gradient = Color.getViridisGradient();
            if (M[i] == 2)
                dataSet = DataSet.getFor2D("Population", new MarkerStyle(2.0f, gradient, M[i], Marker.CIRCLE));
            else
                dataSet = DataSet.getFor3D("Population", new MarkerStyle(0.02f, gradient, M[i], Marker.SPHERE_LOW_POLY_3D));

            pDU._processorToPlots[2 * i] = new ProcessorToPlots(2 * i, dataSet);
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
