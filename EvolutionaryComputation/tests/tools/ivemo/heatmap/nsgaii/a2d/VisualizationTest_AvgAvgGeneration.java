package tools.ivemo.heatmap.nsgaii.a2d;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import criterion.Criteria;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.nsgaii.NSGAIIBundle;
import frame.Frame;
import phase.PhasesBundle;
import plot.heatmap.Heatmap2D;
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
import tools.ivemo.heatmap.AbstractHeatmapProcessor;
import tools.ivemo.heatmap.Heatmap2DProcessor;
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

        int populationSize = 30;
        int offspringSize = 30;
        int generations = 100;
        int xDix = 100;
        int yDiv = 100;
        int trials = 100;
        int decVariables = 20;

        Heatmap2DProcessor.Params pHP = new Heatmap2DProcessor.Params();
        pHP._eaFactory = () -> {
            Problem problem = Problem.DTLZ2;
            DTLZBundle problemBundle = DTLZBundle.getBundle(problem, 2, decVariables);
            Criteria criteria = Criteria.constructCriteria("C", 2, false);
            NSGAIIBundle.Params pAB = new NSGAIIBundle.Params(criteria);
            pAB._construct = problemBundle._construct;
            pAB._reproduce = problemBundle._reproduce;
            pAB._evaluate = problemBundle._evaluate;
            pAB._initialNormalizations = problemBundle._normalizations;
            pAB._osManager = null;
            Tournament.Params pT = new Tournament.Params();
            pT._size = 2;
            pT._noOffspring = offspringSize;
            pT._preferenceDirection = false;
            pAB._select = new Tournament(pT);
            NSGAIIBundle algorithmBundle = new NSGAIIBundle(pAB);
            EA.Params pEA = new EA.Params("NSGA-II", criteria);
            PhasesBundle.copyPhasesFromBundleToEA(pEA, algorithmBundle._phasesBundle);
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
        pHP._xAxisDivisions = xDix;
        pHP._xAxisDisplayRange = new Range(0.0d, 2.0d);
        pHP._yAxisDivisions = yDiv;
        pHP._yAxisDisplayRange = new Range(0.0d, 2.0d);
        Heatmap2DProcessor h2D = new Heatmap2DProcessor(pHP);

        h2D.executeProcessing();
        h2D.generateSortedInputData();

        Heatmap2D.Params pPlot = new Heatmap2D.Params();
        pPlot._xDiv = xDix;
        pPlot._yDiv = yDiv;
        pPlot._scheme = new WhiteScheme();
        pPlot._scheme._aligns.put(AlignFields.TITLE, Align.TOP);
        pPlot._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pPlot._title = null;
        pPlot._drawXAxis = true;
        pPlot._xAxisTitle = "X";
        pPlot._drawYAxis = true;
        pPlot._yAxisTitle = "Y";
        pPlot._drawMainGridlines = false;
        pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(new Range(0.0d, 2.0d), new Range(0.0d,  2.0d));
        pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(new Range(0.0d, generations), false, false);
        pPlot._gradient = Gradient.getViridisGradient();
        pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Avg of mean generations", new FromDisplayRange(pPlot._heatmapDisplayRange, 5));
        pPlot._xAxisWithBoxTicks = false;
        pPlot._yAxisWithBoxTicks = false;

        Heatmap2D HM = new Heatmap2D(pPlot);
        frame.Frame frame = new Frame(HM, 0.5f);

        HM.getModel().setDataAndPerformProcessing(h2D.getSortedCoords(), h2D.getSortedValues()._sortedValues);
        frame.setVisible(true);
    }
}