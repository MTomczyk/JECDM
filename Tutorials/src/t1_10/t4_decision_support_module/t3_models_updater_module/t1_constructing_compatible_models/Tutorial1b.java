package t1_10.t4_decision_support_module.t3_models_updater_module.t1_constructing_compatible_models;

import alternative.Alternative;
import alternative.Alternatives;
import color.gradient.Color;
import color.gradient.Gradient;
import criterion.Criteria;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import dmcontext.DMContext;
import drmanager.DisplayRangesManager;
import exeption.ConstructorException;
import exeption.HistoryException;
import frame.Frame;
import history.History;
import history.PreferenceInformationWrapper;
import model.constructor.Report;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import plot.AbstractPlot;
import plot.Plot3D;
import plotswrapper.GridPlots;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import random.IRandom;
import random.MersenneTwister64;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import space.Range;
import space.os.ObjectiveSpace;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This tutorial showcases the FRS procedure ({@link FRS}).
 *
 * @author MTomczyk
 */
public class Tutorial1b
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Establish two cost-type criteria.
        Criteria criteria = Criteria.constructCriteria("C", 3, false);
        // Set the known objective space: [0.0, 2.0] x [0.0, 4.0] x [0.0, 8.0]
        ObjectiveSpace os = new ObjectiveSpace(new Range[]{Range.get0R(2.0d), Range.get0R(4.0d),
                Range.get0R(8.0d)}, new boolean[3]);
        // Starting time
        LocalDateTime startingTime = LocalDateTime.now();
        // Random number generator
        IRandom R = new MersenneTwister64(0);

        // Create random model generator (creates one random model instance):
        IRandomModel<LNorm> generator = new LNormGenerator(3, Double.POSITIVE_INFINITY);
        // Create FRS (params container):
        FRS.Params<LNorm> pFRS = new FRS.Params<>(generator);
        pFRS._feasibleSamplesToGenerate = 1000; // the procedure is requested to generate this number of feasible samples
        pFRS._samplingLimit = 1000000; // the limit for the number of samples to generate and examine
        pFRS._inconsistencyThreshold = 0; // if the method constructed less/equal this number of samples, the overall result is considered inconsistent
        // pFRS._normalizationBuilder = new StandardLinearBuilder(); already supplied
        FRS<LNorm> frs = new FRS<>(pFRS); // create object instance

        // Create the history object:
        History history = new History("History (tutorial)");

        // Performance vectors for iterations and two alternatives:
        double[][][] aData = new double[][][]{
                {{1.25d, 2.0d, 4.0d}, {0.0d, 1.0d, 7.0d}},
                {{1.25d, 2.0d, 4.0d}, {1.75d, 3.5d, 2.0d}}
        };

        // Create two artificial feedbacks:
        IPreferenceInformation[] feedback = new IPreferenceInformation[2];
        feedback[0] = PairwiseComparison.getPreference(new Alternative("A0", aData[0][0]),
                new Alternative("A1", aData[0][1]));
        feedback[1] = PairwiseComparison.getPreference(new Alternative("A2", aData[1][0]),
                new Alternative("A3", aData[1][1]));

        // We will store the weight vectors of constructed models to display them later
        double[][][] weights = new double[2][][];

        try
        {
            // Simulate the iterations:
            for (int i = 0; i < feedback.length; i++)
            {
                // Register subsequent pairwise comparisons: the method returns the wrapped input (most recently provided feedback):
                LinkedList<PreferenceInformationWrapper> mostRecentFeedback = history.registerPreferenceInformation(feedback[i], i);
                System.out.println("Iteration = " + i + " =======================");
                System.out.println(history.getFullStringRepresentation());

                // Create the decision-making context:
                DMContext context = Common.getContext(i, new Alternatives(new ArrayList<>()), startingTime, criteria, os, R);
                // System-related calls:
                frs.registerDecisionMakingContext(context);
                frs.notifyAboutMostRecentPreferenceInformation(mostRecentFeedback);
                frs.notifyModelsConstructionBegins();

                // Create feasible samples:
                Report<LNorm> report = frs.constructModels(history.getPreferenceInformationCopy());
                // Print the report:
                System.out.println(report);
                System.out.println();

                // Collect the weight vectors:
                weights[i] = new double[report._models.size()][3];
                for (int j = 0; j < report._models.size(); j++)
                    System.arraycopy(report._models.get(j).getLNorm().getWeights(), 0, weights[i][j], 0, 3);

                frs.notifyModelsConstructionEnds();
                frs.unregisterDecisionMakingContext();
            }
        } catch (HistoryException | ConstructorException e)
        {
            throw new RuntimeException(e);
        }

        // Create the plot
        AbstractPlot[] plots = new AbstractPlot[3];
        String[] titles = new String[]{"Objective space", "Compatible weights (after 1st iteration)", "Compatible weights (after 2nd iteration)"};
        String[] xTitles = new String[]{"f1", "w1", "w2"};
        String[] yTitles = new String[]{"f2", "w1", "w2"};
        String[] zTitles = new String[]{"f3", "w1", "w2"};
        Range[][] ranges = new Range[][]{
                {Range.get0R(2.0d), Range.get0R(4.0d), Range.get0R(8.0d), Range.getNormalRange()},
                {Range.getNormalRange(), Range.getNormalRange(), Range.getNormalRange()},
                {Range.getNormalRange(), Range.getNormalRange(), Range.getNormalRange()}
        };

        for (int i = 0; i < 3; i++)
        {
            Plot3D.Params pP = new Plot3D.Params();
            pP._scheme = WhiteScheme.getForPlot3D();
            pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP);
            pP._drawLegend = true;
            pP._title = titles[i];
            pP._xAxisTitle = xTitles[i];
            pP._yAxisTitle = yTitles[i];
            pP._zAxisTitle = zTitles[i];
            if (i == 0)
                pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor4D(ranges[i][0], ranges[i][1], ranges[i][2], ranges[i][3]);
            else
                pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(ranges[i][0], ranges[i][1], ranges[i][2]);
            plots[i] = new Plot3D(pP);
        }

        GridPlots gridPlots = new GridPlots(plots, 1, 3);
        Frame frame = new Frame(gridPlots, 0.6f, 0.4f);
        //Frame frame = new Frame(gridPlots, 1600, 600);

        // Update data sets:
        {
            ArrayList<IDataSet> dataSets = new ArrayList<>(3);
            dataSets.add(DataSet.getFor3D("First comparison", new double[][]{{aData[0][0][0], aData[0][0][1],
                            aData[0][0][2], 0.0d}, {aData[0][1][0], aData[0][1][1], aData[0][1][2], 1.0d}},
                    new MarkerStyle(0.05f, Gradient.getGreenRedGradient(), 3, Marker.SPHERE_LOW_POLY_3D,
                            new LineStyle(0.2f, Color.BLACK, 0.1f), 1.0f), new LineStyle(0.1f, Color.BLACK, 0.1f)));

            dataSets.add(DataSet.getFor3D("Second comparison", new double[][]{{aData[1][0][0], aData[1][0][1],
                    aData[1][0][2], 0.0d}, {aData[1][1][0], aData[1][1][1], aData[1][1][2], 1.0d}}, new MarkerStyle(0.05f,
                    Gradient.getGreenRedGradient(), 3, Marker.TETRAHEDRON_UP_3D, new LineStyle(0.2f, Color.BLACK, 0.1f), 1.0f),
                    new LineStyle(0.1f, Color.BLACK, 0.1f)));

            gridPlots.getModel().getPlot(0).getModel().setDataSets(dataSets, true);
        }
        {
            IDataSet ds = DataSet.getFor3D("Compatible weights", weights[0], new MarkerStyle(0.025f, Gradient.getInfernoGradient(), 2, Marker.SPHERE_LOW_POLY_3D));
            gridPlots.getModel().getPlot(1).getModel().setDataSet(ds, true);
        }
        {
            IDataSet ds = DataSet.getFor3D("Compatible weights", weights[1], new MarkerStyle(0.025f, Gradient.getInfernoGradient(), 2, Marker.SPHERE_LOW_POLY_3D));
            gridPlots.getModel().getPlot(2).getModel().setDataSet(ds, true);
        }

        frame.setVisible(true);
    }

}
