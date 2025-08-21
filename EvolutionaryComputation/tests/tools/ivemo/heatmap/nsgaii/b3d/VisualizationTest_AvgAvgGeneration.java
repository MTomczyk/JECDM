package tools.ivemo.heatmap.nsgaii.b3d;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import criterion.Criteria;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.nsgaii.NSGAIIBundle;
import frame.Frame;
import phase.PhasesBundle;
import plot.heatmap.Heatmap3D;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import selection.Tournament;
import space.Range;
import statistics.Mean;
import thread.swingtimer.reporters.RenderGenerationTimesReporter;
import tools.ivemo.heatmap.AbstractHeatmapProcessor;
import tools.ivemo.heatmap.Heatmap3DProcessor;
import tools.ivemo.heatmap.feature.Generation;

/**
 * Various tests for the {@link AbstractHeatmapProcessor} class.
 * Simple test that involves running NSGA-II on DTLZ2 Benchmarks.
 *
 * @author MTomczyk
 */
class VisualizationTest_AvgAvgGeneration
{
    /**
     * Performs visualization test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {


        // DPI TEST SCALE
        System.setProperty("sun.java2d.uiScale", "1");
        // DPI TEST SCALE

        IRandom R = new MersenneTwister64(0);

        int M = 3;

        int populationSize = 90;
        int offspringSize = 90;

        int generations = 100;

        int xDiv = 50;
        int yDiv = 50;
        int zDiv = 50;

        int trials = 100;

        int decVariables = 20;

        Heatmap3DProcessor.Params pHP = new Heatmap3DProcessor.Params();
        pHP._eaFactory = () -> {
            Problem problem = Problem.DTLZ2;
            DTLZBundle problemBundle = DTLZBundle.getBundle(problem, M, decVariables);
            Criteria criteria = Criteria.constructCriteria("C", M, false);
            NSGAIIBundle.Params pAB = new NSGAIIBundle.Params(criteria);
            pAB._construct = problemBundle._construct;
            pAB._reproduce = problemBundle._reproduce;
            pAB._evaluate = problemBundle._evaluate;
            pAB._initialNormalizations = problemBundle._normalizations;
            pAB._osManager = null;
            Tournament.Params pT = new Tournament.Params();
            pT._size = 2;
            pT._preferenceDirection = false;
            pAB._select = new Tournament(pT);
            NSGAIIBundle algorithmBundle = new NSGAIIBundle(pAB);
            EA.Params pEA = new EA.Params("NSGA-II", criteria);
            pEA._phases = PhasesBundle.getPhasesAssignmentsFromBundle(algorithmBundle._phasesBundle);
            pEA._id = 0;
            pEA._R = R;
            pEA._populationSize = populationSize;
            pEA._offspringSize = offspringSize;
            return new EA(pEA);
        };

        pHP._notify = true;
        pHP._trials = trials;
        pHP._generations = generations;
        pHP._featureGetter = new Generation();
        pHP._trialStatistics = new Mean();
        pHP._finalStatistics = new Mean();
        pHP._xAxisDivisions = xDiv;
        pHP._xAxisDisplayRange = new Range(0.0d, 2.0d);
        pHP._yAxisDivisions = yDiv;
        pHP._yAxisDisplayRange = new Range(0.0d, 2.0d);
        pHP._zAxisDivisions = zDiv;
        pHP._zAxisDisplayRange = new Range(0.0d, 2.0d);

        Heatmap3DProcessor h3D = new Heatmap3DProcessor(pHP);
        h3D.executeProcessing();
        h3D.generateSortedInputData();

        Heatmap3D.Params pPlot = new Heatmap3D.Params();
        pPlot._xDiv = xDiv;
        pPlot._yDiv = yDiv;
        pPlot._zDiv = zDiv;
        pPlot._scheme = new WhiteScheme();
        pPlot._scheme._aligns.put(AlignFields.TITLE, Align.TOP);
        pPlot._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pPlot._title = null;
        pPlot._drawXAxis = true;
        pPlot._xAxisTitle = "X";
        pPlot._drawYAxis = true;
        pPlot._yAxisTitle = "Y";
        pPlot._drawZAxis = true;
        pPlot._zAxisTitle = "Z";

        pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(new Range(0.0d, 2.0d),
                new Range(0.0d, 2.0d), new Range(0.0d, 2.0d));

        pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(new Range(0.0d, generations), false, false);
        pPlot._gradient = Gradient.getViridisGradient();

        pPlot._horizontalGridLinesWithBoxTicks = false;
        pPlot._verticalGridLinesWithBoxTicks = false;
        pPlot._depthGridLinesWithBoxTicks = false;
        pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Expected generation-no when occupied", new FromDisplayRange(pPlot._heatmapDisplayRange, 5));


        Heatmap3D HM = new Heatmap3D(pPlot);

        frame.Frame frame = new Frame(HM, 0.5f);
        frame.getModel().getPlotsWrapper().getController().addReporter(new RenderGenerationTimesReporter(frame.getModel().getGlobalContainer(), 1));

        HM.getModel().setDataAndPerformProcessing(h3D.getSortedCoords(), h3D.getSortedValues()._sortedValues);
        frame.setVisible(true);
    }
}