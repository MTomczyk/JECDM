package y2025.ERS.e1_auxiliary;

import color.gradient.Color;
import color.gradient.ColorPalettes;
import compatibility.CompatibilityAnalyzer;
import criterion.Criteria;
import dataset.DSFactory3D;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.ArrowStyle;
import dataset.painter.style.ArrowStyles;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Arrow;
import dataset.painter.style.enums.Line;
import dataset.painter.style.enums.Marker;
import exception.TrialException;
import model.constructor.value.rs.ers.evolutionary.EvolutionaryModelConstructor;
import model.constructor.value.rs.ers.evolutionary.IOffspringConstructor;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.iterationslimit.Constant;
import decisionsupport.operators.LNormOnSimplex;
import dmcontext.DMContext;
import drmanager.DRMPFactory;
import emo.interactive.iemod.IEMOD;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.goal.definitions.PreferenceValueModel;
import emo.utils.decomposition.moead.MOEADGoalsManager;
import emo.utils.decomposition.similarity.pbi.Euclidean;
import exception.RunnerException;
import exeption.ConstructorException;
import frame.Frame;
import history.PreferenceInformationWrapper;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import io.FileUtils;
import io.image.ImageSaver;
import model.IPreferenceModel;
import model.constructor.Report;
import model.constructor.random.LNormGenerator;
import model.internals.value.scalarizing.LNorm;
import plot.Plot3D;
import plot.Plot3DFactory;
import plotswrapper.GridPlots;
import population.Specimen;
import population.Specimens;
import preference.indirect.PairwiseComparison;
import print.PrintUtils;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.ReferencePointsFactory;
import problem.moo.dtlz.DTLZBundle;
import y2025.ERS.common.EAWrapperIterableSampler;
import model.constructor.value.rs.ers.IterableERS;
import y2025.ERS.common.indicators.ModelClosestNeighborDistance;
import random.IRandom;
import random.MersenneTwister64;
import runner.Runner;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.ColorFields;
import scheme.enums.SizeFields;
import space.normalization.builder.StandardLinearBuilder;
import space.os.ObjectiveSpace;
import space.simplex.DasDennis;
import statistics.Min;
import utils.Screenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Runs the animation discussed in Section 2.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicatedCode", "ConstantValue"})
public class AnimationERS
{
    /**
     * Runs the experiment.
     *
     * @param args not used
     * @throws InterruptedException the exception can be thrown 
     * @throws IOException          the exception can be thrown 
     */
    public static void main(String[] args) throws InterruptedException, IOException
    {
        // This code surpasses the original processing and executes some of the functionalities manually
        IRandom R = new MersenneTwister64(0); // Random number generator
        int M = 3; // no. objectives
        AbstractMOOProblemBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, M); // gets the problem bundle (DTLZ2)
        // create goals (L-norms with alpha = infty):
        IGoal[] goals = GoalsFactory.getLNormsDND(M, 15, Double.POSITIVE_INFINITY, problemBundle._normalizations);
        System.out.println("Population size = " + goals.length);

        boolean generateScreenshots = false;

        // Do not use the automated interactions (limit = 0):
        IRule iRule = new IterationInterval(100, 1, 0);
        IReferenceSetConstructor referenceSetConstructor = new RandomPairs(new RequiredSpread(0.0d)); // does not matter, not used
        IDMFeedbackProvider dmFeedbackProvider = new ArtificialValueDM<>(new model.definitions.LNorm()); // does not matter, not used

        IPreferenceModel<LNorm> model = new model.definitions.LNorm(); // preference model definition
        // ERS sampler
        IterableERS.Params<LNorm> pERS = new IterableERS.Params<>(new LNormGenerator(M, Double.POSITIVE_INFINITY, problemBundle._normalizations));
        pERS._iterationsLimit = new Constant(5000);
        pERS._passModels = true;
        pERS._similarity = new model.similarity.lnorm.Euclidean();
        pERS._feasibleSamplesToGenerate = goals.length;
        pERS._initialModels = new LNorm[goals.length];
        IOffspringConstructor<LNorm> offspringConstructor = new LNormOnSimplex(Double.POSITIVE_INFINITY,
                0.1d, 0.1d);
        pERS._EMC = new EvolutionaryModelConstructor<>(offspringConstructor, new Tournament<>(2));
        for (int i = 0; i < goals.length; i++)
            pERS._initialModels[i] = new LNorm(goals[i].getParams()[0].clone(), goals[i].getParams()[1][0]);
        IterableERS<LNorm> ers = new IterableERS<>(pERS);

        // Create the EA:
        IEMOD iemod = IEMOD.getIEMOD(0, false, false, R, goals,
                problemBundle, new Euclidean(), 10,
                iRule, referenceSetConstructor, dmFeedbackProvider, model, ers);

        System.out.println("Generating reference Pareto");

        double[][] dtlz2pareto = ReferencePointsFactory.getFilteredReferencePoints(Problem.DTLZ2, 100000, 3, R,
                GoalsFactory.getPointLineProjectionsDND(3, 25, problemBundle._normalizations));
        //getLNormsDND(3, 25, Double.POSITIVE_INFINITY, problemBundle._normalizations)
        System.out.println("Reference Pareto generated");

        // Create the plots:
        Plot3D[] plots = new Plot3D[2];

        {
            plots[0] = Plot3DFactory.getPlot(WhiteScheme.getForPlot3D(),
                    "f1", "f2", "f3",
                    DRMPFactory.getFor3D(1.0d, 1.0d, 1.0d),
                    5, 5, 5,
                    "0.00", "0.00", "0.00",
                    1.5f, 2.0f,
                    scheme -> {
                        scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.075f);
                        scheme._colors.put(ColorFields.PLOT_BACKGROUND, color.Color.WHITE);
                        scheme._sizes.put(SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.035f);
                        scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.031f);
                    },
                    pP1 -> {
                        pP1._title = "Objective space";
                        pP1._drawLegend = true;
                        pP1._axesAlignments = new Align[]{Align.FRONT_BOTTOM, Align.LEFT_BOTTOM, Align.BACK_LEFT};
                    }, null);
        }
        {
            plots[1] = Plot3DFactory.getPlot(WhiteScheme.getForPlot3D(),
                    "w1", "w2", "w3",
                    DRMPFactory.getFor3D(1.0d, 1.0d, 1.0d),
                    5, 5, 5,
                    "0.00", "0.00", "0.00",
                    1.5f, 2.0f,
                    scheme -> {
                        scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.075f);
                        scheme._colors.put(ColorFields.PLOT_BACKGROUND, color.Color.WHITE);
                        scheme._sizes.put(SizeFields.TITLE_FONT_SIZE_RELATIVE_MULTIPLIER, 0.035f);
                        scheme._sizes.put(SizeFields.LEGEND_ENTRY_FONT_SIZE_RELATIVE_MULTIPLIER, 0.031f);
                    },
                    pP1 -> {
                        pP1._title = "Model weight space";
                        pP1._drawLegend = true;
                        pP1._axesAlignments = new Align[]{Align.FRONT_BOTTOM, Align.LEFT_BOTTOM, Align.BACK_LEFT};
                    }, null);
        }

        GridPlots.Params pGP = new GridPlots.Params(plots, 1, 2);
        GridPlots GP = new GridPlots(pGP);

        int plotSize = 1500;
        Frame frame = new Frame(GP, 2 * plotSize, plotSize);

        plots[0].getModel().notifyDisplayRangesChangedListeners();
        plots[1].getModel().notifyDisplayRangesChangedListeners();

        // Default camera:
        plots[0].getController().getInteractListener().getTranslation()[2] = 1.8f;
        plots[0].getController().getInteractListener().getObjectRotation()[1] = 40.0f;
        plots[0].getController().getInteractListener().getObjectRotation()[0] = -10.0f;
        plots[1].getController().getInteractListener().getTranslation()[2] = 1.8f;
        plots[1].getController().getInteractListener().getObjectRotation()[1] = 40.0f;
        plots[1].getController().getInteractListener().getObjectRotation()[0] = -10.0f;

        /*plots[0].getController().getInteractListener().getTranslation()[0] = -0.19544f;
        plots[0].getController().getInteractListener().getTranslation()[1] = -0.00077f;
        plots[0].getController().getInteractListener().getTranslation()[2] = 0.57378f;
        plots[0].getController().getInteractListener().getObjectRotation()[0] = 343.10107f;
        plots[0].getController().getInteractListener().getObjectRotation()[1] = 46.86098f;
        plots[0].getController().getInteractListener().getCameraRotation()[0] = 349.75607f;
        plots[0].getController().getInteractListener().getCameraRotation()[1] = 354.75336f;*/

        /*plots[1].getController().getInteractListener().getTranslation()[0] = 0.09456f;
        plots[1].getController().getInteractListener().getTranslation()[1] = 0.14862f;
        plots[1].getController().getInteractListener().getTranslation()[2] = 0.61699f;
        plots[1].getController().getInteractListener().getObjectRotation()[0] = 339.72125f;
        plots[1].getController().getInteractListener().getObjectRotation()[1] = 38.74440f;
        plots[1].getController().getInteractListener().getCameraRotation()[0] = 355.40070f;
        plots[1].getController().getInteractListener().getCameraRotation()[1] = 354.75336f;*/

        frame.setVisible(true);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        // Prepare Indicator
        ModelClosestNeighborDistance indicator = new ModelClosestNeighborDistance(new Min(), false);

        // Prepare initial data
        ArrayList<double[]> ref = DasDennis.getWeightVectors(3, 30);
        assert ref != null;
        double[][] simplexRPS = new double[ref.size()][];
        for (int i = 0; i < ref.size(); i++) simplexRPS[i] = ref.get(i);
        {
            plots[0].getModel().setDataSet(Common.getParetoDS(dtlz2pareto), true, true);
            IDataSet simplexDS = DataSet.getFor3D("Simplex", simplexRPS,
                    new MarkerStyle(0.01f, Color.GRAY_50, Marker.SPHERE_HIGH_POLY_3D));
            simplexDS.setDisplayableOnLegend(false);
            plots[1].getModel().setDataSet(simplexDS, true, true);
        }

        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        // Create the Runner:
        Runner.Params pR = new Runner.Params(iemod, goals.length);
        Runner runner = new Runner(pR);

        int generationsPhase1 = 500;

        try
        {

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            runner.init();
            {
                ArrayList<IDataSet> dss = new ArrayList<>(3);
                dss.add(Common.getParetoDS(dtlz2pareto));
                dss.add(Common.getMethodDS(iemod, 0.05f));
                plots[0].getModel().setDataSets(dss, true, true);
            }
            {
                plots[1].getModel().setDataSet(Common.getModelsDS(goals, 0.05f), true, true);
            }


            for (int g = 1; g < generationsPhase1; g++)
            {
                for (int r = 0; r < goals.length; r++)
                {
                    runner.executeSingleSteadyStateRepeat(iemod, g, r);
                    plots[0].getModel().updateSelectedDataSet(Common.getMethodDS(iemod, 0.05f), true, true);
                }

                try
                {
                    Thread.sleep(2);
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }

        // Update pairwise comparisons
        LNorm dmLNorm = new LNorm(new double[]{0.3d, 0.2d, 0.5d}, Double.POSITIVE_INFINITY, problemBundle._normalizations);
        LinkedList<PreferenceInformationWrapper> preferenceInformation = new LinkedList<>();
        {
            double[][] dataPCs;

            //ArrayList<Specimen> population = iemod.getSpecimensContainer().getPopulation();
            LinkedList<PairwiseComparison> PCs = new LinkedList<>();

            double[][] evals = new double[][]
                    {
                            {0.688781342, 0.688766924, 0.22897089},
                            {0.46209384, 0.555258427, 0.693756862},
                            {0.8054805, 0.483141402, 0.344947394},
                            {0.916437197, 0.261736454, 0.3050608},

                            {0.4388424, 0.879535413, 0.187125738},
                            {0.575001267, 0.767440896, 0.286947616},
                    };


            for (int i = 0; i < 3; i++)
            {
                Specimen S1 = new Specimen(2 * i, evals[2 * i].clone());
                Specimen S2 = new Specimen(2 * i + 1, evals[2 * i + 1].clone());
                int comp = Double.compare(dmLNorm.evaluate(S1.getAlternative()), dmLNorm.evaluate(S2.getAlternative()));
                if (comp < 0) PCs.add(PairwiseComparison.getPreference(S1.getAlternative(), S2.getAlternative()));
                else if (comp > 0) PCs.add(PairwiseComparison.getPreference(S2.getAlternative(), S1.getAlternative()));
            }

            dataPCs = new double[PCs.size() * 2][];
            int idx = 0;
            for (PairwiseComparison PC : PCs)
            {
                System.out.print("Preferred = ");
                PrintUtils.printVectorOfDoubles(PC.getPreferredAlternative().getPerformanceVector(), 8);
                System.out.print("Not preferred = ");
                PrintUtils.printVectorOfDoubles(PC.getNotPreferredAlternative().getPerformanceVector(), 8);
                dataPCs[idx++] = PC.getNotPreferredAlternative().getPerformanceVector().clone();
                dataPCs[idx++] = PC.getPreferredAlternative().getPerformanceVector().clone();
                preferenceInformation.add(PreferenceInformationWrapper.getTestInstance(PC));
            }

            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }

            if (!PCs.isEmpty())
            {
                ArrayList<IDataSet> dataSets = new ArrayList<>(2);
                dataSets.add(Common.getParetoDS(dtlz2pareto));
                dataSets.add(Common.getMethodDS(iemod, 0.05f));
                dataSets.add(DSFactory3D.getDS("Pairwise comparisons", dataPCs,
                        new LineStyle(0.015f, ColorPalettes.getFromDefaultPalette(3), 0.15f, Line.POLY_OCTO),
                        new ArrowStyles(new ArrowStyle(0.07f, 0.02f,
                                ColorPalettes.getFromDefaultPalette(3), 0.5f, 0.3f, Arrow.TRIANGULAR_3D)),
                        false, true));
                plots[0].getModel().setDataSets(dataSets, true, true);
            }
        }

        // generate screenshots
        if (generateScreenshots)
        {
            for (int i = 0; i < 2; i++)
            {
                Thread.sleep(1000);
                Screenshot screenshot = plots[i].getModel().requestScreenshotCreation(plotSize * 2, plotSize * 2, false);
                screenshot._barrier.await();
                Path path = FileUtils.getPathRelatedToClass(AnimationFRS.class, "Projects", "src", File.separatorChar);
                String fp = path.toString() + File.separatorChar + "ers_screenshot_1_" + i;
                ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);
                Thread.sleep(1000);
            }
        }
        // Run ERS
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._R = R;
        pDMC._currentIteration = generationsPhase1;
        pDMC._currentOS = new ObjectiveSpace(problemBundle._paretoFrontBounds, problemBundle._optimizationDirections);
        pDMC._osChanged = false;
        pDMC._normalizationBuilder = new StandardLinearBuilder();
        pDMC._currentAlternativesSuperset = new Specimens(iemod.getSpecimensContainer().getPopulation());
        DMContext dmContext = new DMContext(pDMC, Criteria.constructCriteria("C", 3, false), LocalDateTime.now());
        Report<LNorm> report = new Report<>(dmContext);

        try
        {
            Thread.sleep(5000);

            ArrayList<IDataSet> dataSets = new ArrayList<>(2);
            IDataSet simplexDS = DataSet.getFor3D("Simplex", simplexRPS,
                    new MarkerStyle(0.01f, Color.GRAY_50, Marker.SPHERE_HIGH_POLY_3D));
            simplexDS.setDisplayableOnLegend(false);

            IDataSet boundary = Common.getBoundaryDS();
            dataSets.add(simplexDS);
            dataSets.add(boundary);

            plots[1].getModel().setDataSets(dataSets, true, true);
            Thread.sleep(1000);
            ers.registerDecisionMakingContext(dmContext);

            boolean printFlag = false;
            boolean skipSleep = false;


            if (!ers.initializeStep(report, preferenceInformation))
            {
                Thread.sleep(1000);

                boolean screenshotTaken = false;
                CompatibilityAnalyzer CA = new CompatibilityAnalyzer();

                int limit = Math.max(0, pERS._iterationsLimit.getIterations(dmContext, preferenceInformation, report, goals.length));
                for (int i = 0; i < limit; i++)
                {
                    LNorm sampled = ers.executeStep(report, preferenceInformation);
                    if (i == limit - 1)
                    {
                        dataSets = new ArrayList<>(3);
                        IDataSet ds1 = Common.getModelsDS(report, 0.015f);
                        IDataSet ds2 = Common.getBoundaryDS();
                        //  dataSets.add(simplexDS);
                        dataSets.add(ds1);
                        dataSets.add(ds2);
                        LinkedList<double[][]> wrap = new LinkedList<>();
                        wrap.add(simplexRPS);
                        dataSets.add(simplexDS.wrapAround(wrap));
                        plots[1].getModel().setDataSets(dataSets, true, true);
                        break;
                    }
                    else
                    {
                        dataSets = new ArrayList<>(3);
                        IDataSet ds1 = Common.getModelsDS(report, 0.015f);
                        IDataSet ds2 = Common.getSampledModelDS(sampled);
                        IDataSet ds3 = Common.getBoundaryDS();
                        //  dataSets.add(simplexDS);
                        dataSets.add(ds1);
                        dataSets.add(ds2);
                        dataSets.add(ds3);
                        LinkedList<double[][]> wrap = new LinkedList<>();
                        wrap.add(simplexRPS);
                        dataSets.add(simplexDS.wrapAround(wrap));
                        plots[1].getModel().setDataSets(dataSets, true, true);

                    }

                    if ((generateScreenshots) && (!screenshotTaken) && (i > 1000) &&
                            (Double.compare(CA.calculateTheMostDiscriminativeCompatibilityWithValueModel(preferenceInformation, sampled), 0.0d) > 0))
                    {
                        screenshotTaken = true;
                        Thread.sleep(1000);
                        Screenshot screenshot = plots[1].getModel().requestScreenshotCreation(plotSize * 2, plotSize * 2,
                                false, new color.Color(255, 255, 255));
                        screenshot._barrier.await();
                        Path path = FileUtils.getPathRelatedToClass(AnimationFRS.class, "Projects", "src", File.separatorChar);
                        String fp = path.toString() + File.separatorChar + "ers_screenshot_2_1";
                        ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);
                        Thread.sleep(1000);

                    }

                    if ((!printFlag) && (ers.getModelsQueue().areAllSortedModelsCompatible()))
                    {
                        printFlag = true;
                        System.out.println("All models become compatible in iteration = " + i);
                    }


                    if (skipSleep) Thread.sleep(5);
                    skipSleep = !skipSleep;
                }
            }

            ers.finalizeStep(report, preferenceInformation);

            ers.unregisterDecisionMakingContext();
        } catch (ConstructorException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        // Derive models
        IGoal[] newGoals = new IGoal[report._models.size()];
        for (int i = 0; i < report._models.size(); i++)
            newGoals[i] = new PreferenceValueModel(report._models.get(i));

        try
        {
            Thread.sleep(10000);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }


        // Update iemod ---------------
        MOEADGoalsManager goalsManager = iemod.getGoalsManager();
        goalsManager.getFamilies()[0].replaceGoals(newGoals);
        goalsManager.establishNeighborhood();
        goalsManager.makeBestAssignments(iemod.getSpecimensContainer());
        goalsManager.updatePopulationAsImposedByAssignments(iemod.getSpecimensContainer());

        int generationsPhase2 = generationsPhase1 + 500;
        try
        {
            for (int g = generationsPhase1; g < generationsPhase2; g++)
            {
                for (int r = 0; r < goals.length; r++)
                {
                    runner.executeSingleSteadyStateRepeat(iemod, g, r);
                    plots[0].getModel().updateSelectedDataSet(Common.getMethodDS(iemod, 0.025f), true, true);
                }
                //runner.executeSingleGeneration(g, null);
                //plots[0].getModel().updateSelectedDataSet(Common.getMethodDS(iemod), true, true);

                try
                {
                    Thread.sleep(2);
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }

            // generate screenshots
            if (generateScreenshots)
            {
                // Set the cameras first
                plots[0].getModel().updateCameraTranslation(-0.19544f, -0.00077f, 0.57378f);
                plots[0].getModel().updatePlotRotation(343.10107f, 46.86098f);
                plots[0].getModel().updateCameraRotation(349.75607f, 354.75336f);

                plots[1].getModel().updateCameraTranslation(0.09456f, 0.14862f, 0.61699f);
                plots[1].getModel().updatePlotRotation(339.72125f, 38.74440f);
                plots[1].getModel().updateCameraRotation(355.40070f, 354.75336f);


                for (int i = 0; i < 2; i++)
                {
                    Thread.sleep(1000);
                    Screenshot screenshot = plots[i].getModel().requestScreenshotCreation(plotSize * 2, plotSize * 2,
                            false, new color.Color(255, 255, 255));
                    screenshot._barrier.await();
                    Path path = FileUtils.getPathRelatedToClass(AnimationFRS.class, "Projects", "src", File.separatorChar);
                    String fp = path.toString() + File.separatorChar + "ers_screenshot_3_" + i;
                    ImageSaver.saveImage(screenshot._image, fp, "jpg", 1.0f);
                    Thread.sleep(1000);
                }
            }

            EAWrapperIterableSampler<LNorm> wrapper = new EAWrapperIterableSampler<>("Dummy",
                    ers, null, null, 0);
            wrapper.setReport(report);
            double v = indicator.evaluate(wrapper); // bypass
            System.out.println("Indicator value = " + v);

        } catch (RunnerException | TrialException e)
        {
            throw new RuntimeException(e);
        }

    }
}
