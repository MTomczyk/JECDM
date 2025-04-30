package model.constructor.value.rs.ers;

import alternative.Alternative;
import color.gradient.Color;
import compatibility.CompatibilityAnalyzer;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import frame.Frame;
import history.PreferenceInformationWrapper;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.constructor.value.rs.ers.updaters.LNormsQueueSource;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import plot.Plot3D;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import updater.*;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Provides a visualization-based test for {@link ModelsQueue}. The models are sampled randomly.
 * The scenario involves three dimensions.
 *
 * @author MTomczyk
 */
class ModelsQueueVisualizationRandom3DTest
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
        IRandomModel<LNorm> RM = new LNormGenerator(3, Double.POSITIVE_INFINITY);
        int noModels = 100;
        int improvementAttempts = 9900;
        ArrayList<LNorm> models = new ArrayList<>();
        for (int m = 0; m < noModels; m++) models.add(RM.generateModel(R));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.6d, 0.2d, 0.2d}),
                new Alternative("A2", new double[]{0.3d, 0.3d, 0.4d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.4d, 0.3d, 0.3d}),
                new Alternative("A4", new double[]{0.3d, 0.5d, 0.2d}))));

        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, 3,
                CA, new MostSimilarWithTieResolving<>(), new Euclidean());

        Plot3D.Params pP = new Plot3D.Params();
        pP._scheme = new WhiteScheme();
        pP._xAxisTitle = "w1";
        pP._yAxisTitle = "w2";
        pP._zAxisTitle = "w3";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(Range.getNormalRange(),
                Range.getNormalRange(), Range.getNormalRange());
        Plot3D plot3D = new Plot3D(pP);

        Frame frame = new Frame(plot3D);
        frame.setVisible(true);

        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]{new LNormsQueueSource(modelsQueue, true)};
        pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
        pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
        pDU._processorToPlots = new ProcessorToPlots[]{
                new ProcessorToPlots(0, DataSet.getFor3D("Compatible weights", new MarkerStyle(0.02f,
                        Color.BLUE, Marker.SPHERE_LOW_POLY_3D)))
        };

        String msg = null;
        try
        {
            DataUpdater DU = new DataUpdater(pDU);
            long startTime = System.nanoTime();
            modelsQueue.initializeWithBatch(models, feedback);
            //DU.update();
            for (int ia = 0; ia < improvementAttempts; ia++)
            {
                modelsQueue.insertModel(RM.generateModel(R), feedback);
            }
            long stopTime = System.nanoTime();
            double ms = (stopTime - startTime) / 1.0E6;
            System.out.println("Took " + ms + " ms");
            DU.update();

        } catch (Exception e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

}