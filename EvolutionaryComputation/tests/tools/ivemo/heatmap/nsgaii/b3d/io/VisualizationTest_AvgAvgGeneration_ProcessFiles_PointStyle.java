package tools.ivemo.heatmap.nsgaii.b3d.io;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import criterion.Criteria;
import dataset.painter.style.BucketStyle;
import dataset.painter.style.enums.Bucket;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.nsgaii.NSGAIIBundle;
import frame.Frame;
import phase.PhasesBundle;
import plot.heatmap.Heatmap3D;
import plot.heatmap.utils.Coords;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import selection.Tournament;
import space.Range;
import statistics.Mean;
import tools.ivemo.heatmap.AbstractHeatmapProcessor;
import tools.ivemo.heatmap.Heatmap3DProcessor;
import tools.ivemo.heatmap.feature.Generation;
import tools.ivemo.heatmap.io.ISave;
import tools.ivemo.heatmap.io.load.LoadBinary;
import tools.ivemo.heatmap.io.load.LoadInitFile;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;
import tools.ivemo.heatmap.io.save.SaveBinary;
import tools.ivemo.heatmap.io.save.SaveInitFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the {@link AbstractHeatmapProcessor} class.
 * Simple test that involves running NSGA-II on DTLZ2 Benchmarks.
 *
 * @author MTomczyk
 */
class VisualizationTest_AvgAvgGeneration_ProcessFiles_PointStyle
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
        int populationSize = 60;
        int offspringSize = 60;
        int generations = 100;
        int xDiv = 20;
        int yDiv = 20;
        int zDiv = 20;
        int trials = 20;
        int decVariables = 20;
        int dimensions = 3;
        Range xDR = new Range(0.0d, 2.0d);
        Range yDR = new Range(0.0d, 2.0d);
        Range zDR = new Range(0.0d, 2.0d);

        // RUN THE EXPERIMENT

        Heatmap3DProcessor.Params pHP = new Heatmap3DProcessor.Params();
        pHP._title = "NSGA-II test heatmap";
        pHP._eaFactory = () -> {
            Problem problem = Problem.DTLZ2;
            DTLZBundle problemBundle = DTLZBundle.getBundle(problem, dimensions, decVariables);
            Criteria criteria = Criteria.constructCriteria("C", dimensions, false);
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
            PhasesBundle.copyPhasesFromBundleToEA(algorithmBundle._phasesBundle, pEA);
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
        pHP._xAxisDisplayRange = xDR;
        pHP._yAxisDivisions = yDiv;
        pHP._yAxisDisplayRange = yDR;
        pHP._zAxisDivisions = zDiv;
        pHP._zAxisDisplayRange = zDR;
        Heatmap3DProcessor h3D = new Heatmap3DProcessor(pHP);

        h3D.executeProcessing();
        h3D.generateSortedInputData();
        Coords[] preCoords = h3D.getSortedCoords();

        // HEATMAP IS GENERATED
        // PRE-DISPLAY
        {
            // MAKE VISUALIZATION
            Heatmap3D.Params pPlot = new Heatmap3D.Params();
            pPlot._xDiv = xDiv;
            pPlot._yDiv = yDiv;
            pPlot._zDiv = zDiv;
            pPlot._scheme = new WhiteScheme();
            pPlot._scheme._aligns.put(AlignFields.TITLE, Align.TOP);
            pPlot._title = "PRE-GENERATED";
            pPlot._drawXAxis = true;
            pPlot._xAxisTitle = "X";
            pPlot._drawYAxis = true;
            pPlot._yAxisTitle = "Y";
            pPlot._drawZAxis = true;
            pPlot._zAxisTitle = "Z";
            pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(xDR, yDR, zDR);
            pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(new Range(0.0d, generations), false, false);
            pPlot._gradient = Gradient.getViridisGradient();
            pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Avg of mean generations", new FromDisplayRange(pPlot._heatmapDisplayRange, 5));
            pPlot._xAxisWithBoxTicks = false;
            pPlot._yAxisWithBoxTicks = false;
            pPlot._zAxisWithBoxTicks = false;
            pPlot._horizontalGridLinesWithBoxTicks = false;
            pPlot._verticalGridLinesWithBoxTicks = false;
            pPlot._depthGridLinesWithBoxTicks = false;
            pPlot._bucketStyle = new BucketStyle(Bucket.POINT_3D, 2.0f, null);


            Heatmap3D HM = new Heatmap3D(pPlot);
            Frame frame = new Frame(HM, 0.5f);
            HM.getModel().setDataAndPerformProcessing(preCoords, h3D.getSortedValues()._sortedValues);
            frame.setVisible(true);
        }

        // SAVING STEP
        FrameParams FP = new FrameParams();
        FP._frameSize = 0.5f;
        FP._frameTitle = "TEST TITLE";

        PlotParams[] PP = new PlotParams[]{new PlotParams()};
        PP[0]._fileName = "nsgaii_test_results";
        PP[0]._title = "TEST PLOT TITLE";
        PP[0]._dimensions = dimensions;
        PP[0]._scheme = new WhiteScheme();
        PP[0]._gradient = Gradient.getViridisGradient();
        PP[0]._heatmapDisplayRange = new Range(0.0d, generations);
        PP[0]._bucketStyle = Bucket.POINT_3D;
        PP[0]._pointSize = 2.0f;
        PP[0]._xAxisTitle = "f1";
        PP[0]._yAxisTitle = "f2";
        PP[0]._zAxisTitle = "f2";
        PP[0]._xAxisDivisions = xDiv;
        PP[0]._yAxisDivisions = yDiv;
        PP[0]._zAxisDivisions = zDiv;
        PP[0]._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(xDR, yDR, zDR);

        ISave save = new SaveInitFile(FP, PP);
        try
        {
            System.out.println("Saving init file...");
            save.save("./");
        } catch (Exception e)
        {
            System.out.println("Error during saving: " + e.getMessage());
        }

        System.out.println("No sorted elements...");
        System.out.println("Coords = " + preCoords.length);
        System.out.println("Sorted values = " + h3D.getSortedValues()._sortedValues.length);

        save = new SaveBinary(PP[0], h3D, true);

        try
        {
            System.out.println("Saving binary data file...");
            save.save("./");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        LoadInitFile loadInit = new LoadInitFile();
        try
        {
            System.out.println("Loading init file...");
            loadInit.load("./");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        PP = loadInit._PP;

        LoadBinary loadBinary = new LoadBinary(PP, true);

        try
        {
            loadBinary.load("./");
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        Coords[] loadedCoords = loadBinary._loadedHeatmapData[0];

        // files can be deleted after loading into memory
        File initFile = new File("./init.xml");
        if (!initFile.delete())
            System.out.println("Failed to delete init.xml file");

        File hmFile = new File("./" + PP[0]._fileName + ".hm");
        if (!hmFile.delete())
            System.out.println("Failed to delete " + PP[0]._fileName + ".hm file");


        assertEquals(loadedCoords.length, preCoords.length);
        for (int i = 0; i < loadedCoords.length; i++)
        {
            assertEquals(preCoords[i]._x, loadedCoords[i]._x);
            assertEquals(preCoords[i]._y, loadedCoords[i]._y);
            assertEquals(preCoords[i]._z, loadedCoords[i]._z);
            assertEquals(preCoords[i].getValue(), loadedCoords[i].getValue(), 0.00000001d);
        }

        double [] sortedValues = new double[loadedCoords.length];
        for (int i = 0; i < loadedCoords.length; i++) sortedValues[i] = loadedCoords[i].getValue();

        // DISPLAY FROM LOADED DATA
        {
            // MAKE VISUALIZATION
            Heatmap3D.Params pPlot = new Heatmap3D.Params();
            pPlot._xDiv = xDiv;
            pPlot._yDiv = yDiv;
            pPlot._zDiv = zDiv;
            pPlot._scheme = new WhiteScheme();
            pPlot._scheme._aligns.put(AlignFields.TITLE, Align.TOP);
            pPlot._title = "LOADED";
            pPlot._drawXAxis = true;
            pPlot._xAxisTitle = "X";
            pPlot._drawYAxis = true;
            pPlot._yAxisTitle = "Y";
            pPlot._drawZAxis = true;
            pPlot._zAxisTitle = "Z";
            pPlot._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(xDR, yDR, zDR);
            pPlot._heatmapDisplayRange = new DisplayRangesManager.DisplayRange(new Range(0.0d, generations), false, false);
            pPlot._gradient = Gradient.getViridisGradient();
            pPlot._colorbar = new Colorbar(Gradient.getViridisGradient(), "Avg of mean generations", new FromDisplayRange(pPlot._heatmapDisplayRange, 5));
            pPlot._xAxisWithBoxTicks = false;
            pPlot._yAxisWithBoxTicks = false;
            pPlot._zAxisWithBoxTicks = false;
            pPlot._horizontalGridLinesWithBoxTicks = false;
            pPlot._verticalGridLinesWithBoxTicks = false;
            pPlot._depthGridLinesWithBoxTicks = false;
            pPlot._bucketStyle = new BucketStyle(PP[0]._bucketStyle, PP[0]._pointSize, null);


            Heatmap3D HM = new Heatmap3D(pPlot);
            Frame frame = new Frame(HM, 0.5f);

            HM.getModel().setDataAndPerformProcessing(loadedCoords, sortedValues);
            frame.setVisible(true);
        }


    }
}