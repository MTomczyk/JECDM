package y2025.ERS.e5_use_case;

import color.Color;
import color.gradient.Gradient;
import dataset.DSFactory3D;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import decisionsupport.ERSFactory;
import decisionsupport.operators.LNormOnSimplex;
import drmanager.DRMPFactory;
import emo.interactive.StandardDSSBuilder;
import emo.interactive.iemod.IEMOD;
import emo.interactive.iemod.IEMODBuilder;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.EAException;
import exception.RunnerException;
import exeption.HistoryException;
import frame.Frame;
import history.PreferenceInformationWrapper;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.PWI;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IterationInterval;
import io.FileUtils;
import io.image.ImageSaver;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.rs.iterationslimit.Constant;
import model.internals.value.scalarizing.LNorm;
import plot.Plot3D;
import plot.Plot3DFactory;
import plot.PlotUtils;
import plotswrapper.GridPlots;
import preference.indirect.PairwiseComparison;
import print.PrintUtils;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.cw.cw1.CW1Bundle;
import random.IRandom;
import random.MersenneTwister64;
import reproduction.operators.crossover.SBX;
import reproduction.operators.mutation.PM;
import reproduction.valuecheck.Absorb;
import runner.IRunner;
import runner.Runner;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import selection.Random;
import system.dm.DecisionMakerSystem;
import utils.Screenshot;
import visualization.updaters.sources.EASource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Performs the use-case simulation.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class UseCaseIEMOD
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        boolean useERS = true;
        boolean updateCamera = true;
        boolean saveScreenshots = true;

        IRandom R = new MersenneTwister64(0);
        AbstractMOOProblemBundle problemBundle = CW1Bundle.getBundle(
                () -> SBX.getConstrained(1.0d, 10.0d, new Absorb()),
                () -> PM.getConstrained(1.0d / 5.0d, 10.0d, new Absorb())
        );
        IEMODBuilder<LNorm> IB = new IEMODBuilder<>(R);
        IB.setSimilarity(new Euclidean());
        IB.setNeighborhoodSize(10);
        IGoal[] goals = GoalsFactory.getLNormsDND(3, 20, Double.POSITIVE_INFINITY, problemBundle._normalizations);

        System.out.println("Population size = " + goals.length);
        LNorm[] initialModels = new LNorm[goals.length];
        for (int i = 0; i < goals.length; i++)
            initialModels[i] = new LNorm(goals[i].getParams()[0],
                    Double.POSITIVE_INFINITY, problemBundle._normalizations);

        IB.setGoals(goals);
        IB.setParentsSelector(new Random());
        IB.setCriteria(problemBundle._criteria);
        IB.setFixedOSBoundsLearningPolicy(problemBundle._normalizations, problemBundle._utopia, problemBundle._nadir);
        IB.setProblemImplementations(problemBundle);

        StandardDSSBuilder<LNorm> DSSB = new StandardDSSBuilder<>();
        IConstructor<LNorm> C;
        if (useERS)
        {
            C = ERSFactory.getDefaultForLNorms(goals.length,
                    new Constant(50000),
                    3, Double.POSITIVE_INFINITY, problemBundle._normalizations,
                    new LNormOnSimplex(Double.POSITIVE_INFINITY, 0.2d, 0.2d / (2.0d * (3 - 1)),
                            problemBundle._normalizations),
                    // not necessary here
                    new Tournament<>(2), initialModels);
        }
        else
        {
            FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(
                    new LNormGenerator(3, Double.POSITIVE_INFINITY, problemBundle._normalizations)
            );
            pFRS._initialModels = initialModels;
            pFRS._feasibleSamplesToGenerate = goals.length;
            pFRS._inconsistencyThreshold = goals.length - 1;
            pFRS._samplingLimit = 1000000000; // 10^9
            C = new FRS<>(pFRS);
        }
        DSSB.setModelConstructor(C);
        DSSB.setDMFeedbackProvider(new ArtificialValueDM<>(new model.definitions.LNorm(
                new LNorm(new double[]{
                        0.21, 0.34, 0.45
                }, Double.POSITIVE_INFINITY, problemBundle._normalizations)
        )));
        IPreferenceModel<LNorm> model = new model.definitions.LNorm();
        DSSB.setPreferenceModel(model);
        int start = 2000;
        int interval = 2000;
        int limit = 5;
        int total = start + interval * limit;
        DSSB.setInteractionRule(new IterationInterval(start, interval, limit));
        DSSB.setReferenceSetConstructor(new PWI(model, new RequiredSpread(0.2d)));
        IB.setStandardDSSBuilder(DSSB);

        Gradient gradient1 = Gradient.getViridisGradient();
        Gradient gradient2 = Gradient.getRedBlueGradient();

        // Create plot 3D
        Plot3D[] plots3D = new Plot3D[2];
        plots3D[0] = Plot3DFactory.getPlot(
                WhiteScheme.getForPlot3D(),
                "M", "A", "I",
                DRMPFactory.getFor3D(problemBundle._displayRanges[0],
                        problemBundle._displayRanges[1],
                        problemBundle._displayRanges[2]
                ),
                3, 3, 3,
                PlotUtils.getDecimalFormat('.', 1),
                PlotUtils.getDecimalFormat('.', 1),
                PlotUtils.getDecimalFormat('.', 1),
                2.2f, 2.0f, scheme ->
                {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                    scheme._sizes.put(SizeFields.AXIS3D_X_TICK_LABEL_OFFSET, 0.1f);
                    scheme._sizes.put(SizeFields.AXIS3D_Z_TICK_LABEL_OFFSET, 0.05f);
                },
                pP -> pP._axesAlignments = new Align[]{
                        Align.FRONT_BOTTOM,
                        Align.LEFT_BOTTOM,
                        Align.FRONT_RIGHT}, null);

        plots3D[1] = Plot3DFactory.getPlot(
                WhiteScheme.getForPlot3D(),
                "w_1", "w_2", "w_3",
                DRMPFactory.getFor3D(1.0d, 1.0d, 1.0d),
                3, 3, 3,
                PlotUtils.getDecimalFormat('.', 1),
                PlotUtils.getDecimalFormat('.', 1),
                PlotUtils.getDecimalFormat('.', 1),
                2.2f, 2.0f, scheme ->
                {
                    scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE);
                    scheme._sizes.put(SizeFields.AXIS3D_X_TICK_LABEL_OFFSET, 0.1f);
                    scheme._sizes.put(SizeFields.AXIS3D_Z_TICK_LABEL_OFFSET, 0.05f);
                },
                pP -> pP._axesAlignments = new Align[]{
                        Align.FRONT_BOTTOM,
                        Align.LEFT_BOTTOM,
                        Align.FRONT_RIGHT}, null);


        GridPlots GP = new GridPlots(plots3D, 1, 2);
        Frame frame = new Frame(GP, 2000, 1000);
        plots3D[0].getModel().notifyDisplayRangesChangedListeners();
        plots3D[1].getModel().notifyDisplayRangesChangedListeners();

        // OK DLA PCS 0, 1, 2
        plots3D[0].getModel().updatePlotRotation(359.62421f, 43.54839f);
        plots3D[0].getModel().updateCameraTranslation(-0.02500f, 0.07500f, 1.72916f);
        plots3D[1].getModel().updatePlotRotation(359.62421f, 43.54839f);
        plots3D[1].getModel().updateCameraTranslation(-0.02500f, 0.07500f, 1.72916f);

        frame.setVisible(true);

        try
        {
            IEMOD iemod = IB.getInstance();
            EASource eaSource = new EASource(iemod);

            IRunner runner = new Runner(iemod, iemod.getExpectedNumberOfSteadyStateRepeats());
            System.out.println(iemod.getExpectedNumberOfSteadyStateRepeats());
            runner.init();

            plots3D[0].getModel().setDataSet(DSFactory3D.getDS("Population", eaSource.createData(),
                    new MarkerStyle(0.015f, gradient1, 2, Marker.SPHERE_HIGH_POLY_3D)));

            int lastPC = -1;
            boolean changed;

            {
                double[][] data = new double[initialModels.length][];
                for (int i = 0; i < data.length; i++) data[i] = initialModels[i].getWeights().clone();
                plots3D[1].getModel().setDataSet(DSFactory3D.getDS("Weights", data,
                        new MarkerStyle(0.025f, gradient2, 2, Marker.SPHERE_HIGH_POLY_3D)));
            }

            for (int g = 1; g < total; g++)
            {
                runner.executeSingleGeneration(g, null);

                // Check interaction data
                DecisionMakerSystem DMS = iemod.getDecisionSupportSystem().getDecisionMakersSystems()[0];
                int pcs = DMS.getHistory().getNoPreferenceExamples();
                {
                    if (updateCamera)
                    {
                        if (pcs == 3)
                        {
                            plots3D[0].getModel().updatePlotRotation(358.12106f, 29.21371f);
                            plots3D[0].getModel().updateCameraTranslation(-0.12500f, 0.07500f, 1.72916f);
                            plots3D[1].getModel().updatePlotRotation(358.30896f, 58.97178f);
                            plots3D[1].getModel().updateCameraTranslation(-0.02500f, 0.07500f, 1.72083f);
                        }
                        else if (pcs == 4)
                        {
                            plots3D[0].getModel().updateCameraRotation(346.84760f, 13.42742f);
                            plots3D[0].getModel().updatePlotRotation(2.06677f, 2.35887f);
                            plots3D[0].getModel().updateCameraTranslation(0.32984f, 0.29368f, 0.80606f);
                        }
                        else if (pcs == 5)
                        {
                            plots3D[1].getModel().updateCameraRotation(354.73904f, 345.66531f);
                            plots3D[1].getModel().updatePlotRotation(343.27765f, 47.35888f);
                            plots3D[1].getModel().updateCameraTranslation(0.11982f, 0.06566f, 0.68140f);
                        }

                    }

                    if ((pcs > 0) && (lastPC != pcs))
                    {
                        changed = true;
                        PreferenceInformationWrapper PIW = DMS.getHistory().getPreferenceInformationCopy().getLast();
                        PairwiseComparison PC = (PairwiseComparison) PIW._preferenceInformation;
                        System.out.println("Preferred");
                        PrintUtils.printVectorOfDoubles(PC.getPreferredAlternative().getPerformanceVector(), 5);
                        System.out.println("Not preferred");
                        PrintUtils.printVectorOfDoubles(PC.getNotPreferredAlternative().getPerformanceVector(), 5);
                        lastPC = pcs;
                    }
                    else changed = false;
                }

                plots3D[0].getModel().setDataSet(DSFactory3D.getDS("Population", eaSource.createData(),
                        new MarkerStyle(0.015f - 0.001f * pcs, gradient1, 2, Marker.SPHERE_HIGH_POLY_3D)));

                {
                    ArrayList<LNorm> im = model.getInternalModels();

                    if ((im != null) && (changed))
                    {
                        double[][] data = new double[im.size()][];
                        for (int i = 0; i < data.length; i++) data[i] = im.get(i).getWeights().clone();
                        plots3D[1].getModel().setDataSet(DSFactory3D.getDS("Weights", data,
                                new MarkerStyle(0.025f - 0.0018f * pcs, gradient2, 2,
                                        Marker.SPHERE_HIGH_POLY_3D)));
                    }
                }

                if ((saveScreenshots)
                        && (
                        (g == start - 1) ||
                                (g == start + interval - 1) ||
                                (g == start + 2 * interval - 1) ||
                                (g == start + 3 * interval - 1) ||
                                (g == start + 4 * interval - 1) ||
                                (g == start + 5 * interval - 1)
                ))
                {
                    String pref = useERS ? "ERS" : "FRS";
                    Thread.sleep(100);
                    Screenshot screenshot = plots3D[0].getModel().requestScreenshotCreation(1000, 1000, false);
                    screenshot._barrier.await();
                    Path path = FileUtils.getPathRelatedToClass(UseCaseIEMOD.class,
                            "Projects", "src", File.separatorChar);
                    ImageSaver.saveImage(screenshot._image, path + File.separator
                            + pref + "_uc_population_" + pcs, "jpg", 1.0f);

                    screenshot = plots3D[1].getModel().requestScreenshotCreation(1000, 1000, false);
                    screenshot._barrier.await();
                    ImageSaver.saveImage(screenshot._image, path + File.separator
                            + pref + "_uc_models_" + pcs, "jpg", 1.0f);
                }
            }

        } catch (EAException | RunnerException | HistoryException | InterruptedException | IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
