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
import plot.Plot2D;
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
 * The scenario involves two dimensions.
 *
 * @author MTomczyk
 */
class ModelsQueueVisualizationRandom2DTest
{
    /**
     * Runs the test.
     * @param args not used
     */
    public static void main(String [] args)
    {
        IRandom R = new MersenneTwister64(0);
        CompatibilityAnalyzer CA = new CompatibilityAnalyzer();
        IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);
        int noModels = 30;
        int improvementAttempts = 10000000;
        ArrayList<LNorm> models = new ArrayList<>();
        for (int m = 0; m < noModels; m++) models.add(RM.generateModel(R));

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.5d, 0.5d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.5d, 0.5d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        ModelsQueue<LNorm> modelsQueue = new ModelsQueue<>(noModels, 5,
                CA, new MostSimilarWithTieResolving<>(), new Euclidean());

        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._xAxisTitle = "w1";
        pP._yAxisTitle = "w2";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        Plot2D plot2D = new Plot2D(pP);

        Frame frame = new Frame(plot2D);
        frame.setVisible(true);

        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[]{new LNormsQueueSource(modelsQueue, true)};
        pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
        pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
        pDU._processorToPlots = new ProcessorToPlots[]{
                new ProcessorToPlots(0, DataSet.getFor2D("Compatible weights", new MarkerStyle(1.0f,
                        Color.BLUE, Marker.SQUARE)))
        };

        String msg = null;
        try
        {
            DataUpdater DU = new DataUpdater(pDU);
            modelsQueue.initializeWithBatch(models, feedback);
            DU.update();
            for (int ia = 0; ia < improvementAttempts; ia++)
            {
                modelsQueue.insertModel(RM.generateModel(R), feedback);
                DU.update();
            }

        } catch (Exception e)
        {
            msg = e.toString();
        }
        assertNull(msg);
    }

}