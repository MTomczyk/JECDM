package model.constructor.value.rs.ers;

import alternative.Alternative;
import color.gradient.Color;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import dmcontext.DMContext;
import drmanager.DisplayRangesManager;
import exeption.ConstructorException;
import frame.Frame;
import history.PreferenceInformationWrapper;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.constructor.value.rs.iterationslimit.Constant;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import plot.Plot2D;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import space.Range;
import space.normalization.builder.StandardLinearBuilder;

import java.util.LinkedList;


/**
 * Provides a visualization-based test for {@link ModelsQueue}. The models are sampled randomly.
 * The scenario involves two dimensions.
 *
 * @author MTomczyk
 */
class ERSRandom2DTest
{
    /**
     * Runs the test.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        IRandomModel<LNorm> RM = new LNormGenerator(2, Double.POSITIVE_INFINITY);

        LinkedList<PreferenceInformationWrapper> feedback = new LinkedList<>();
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A1", new double[]{0.5d, 0.5d}),
                new Alternative("A2", new double[]{0.0d, 1.0d}))));
        feedback.add(PreferenceInformationWrapper.getTestInstance(PairwiseComparison.getPreference(
                new Alternative("A3", new double[]{0.5d, 0.5d}),
                new Alternative("A4", new double[]{1.0d, 0.0d}))));

        ERS.Params<LNorm> pERS = new ERS.Params<>(RM);
        pERS._iterationsLimit = new Constant(10000);
        pERS._comparator = new MostSimilarWithTieResolving<>();
        pERS._kMostSimilarNeighbors = 5;
        pERS._similarity = new Euclidean();
        pERS._feasibleSamplesToGenerate = 30;
        pERS._inconsistencyThreshold = 0;
        ERS<LNorm> ERS = new ERS<>(pERS);

        DMContext dmContext = new DMContext(null, null, null, null, false, 0,
                new StandardLinearBuilder(), R);
        model.constructor.Report<LNorm> report;
        try
        {
            ERS.registerDecisionMakingContext(dmContext);
            report = ERS.instantiateReport();
            long startTime = System.nanoTime();
            ERS.mainConstructModels(report, feedback);
            long stopTime = System.nanoTime();
            double ms = (stopTime - startTime) / 1.0E6;
            System.out.println("Took " + ms + " ms");

            ERS.unregisterDecisionMakingContext();
            report.printStringRepresentation();

        } catch (ConstructorException e)
        {
            throw new RuntimeException(e);
        }

        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._xAxisTitle = "w1";
        pP._yAxisTitle = "w2";
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(Range.getNormalRange(), Range.getNormalRange());
        Plot2D plot2D = new Plot2D(pP);


        double[][] data = new double[report._models.size()][];
        for (int i = 0; i < data.length; i++) data[i] = report._models.get(i).getWeights();
        // compatibility is not checked here
        DataSet ds = DataSet.getFor2D("Compatible weights", data, new MarkerStyle(1.0f, Color.BLUE, Marker.SQUARE));
        Frame frame = new Frame(plot2D);

        plot2D.getModel().setDataSet(ds, true);
        frame.setVisible(true);
    }

}