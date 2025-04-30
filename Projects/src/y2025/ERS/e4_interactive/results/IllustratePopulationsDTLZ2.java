package y2025.ERS.e4_interactive.results;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import color.Color;
import color.gradient.ColorPalettes;
import dataset.DSFactory3D;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import decisionsupport.ERSFactory;
import decisionsupport.operators.LNormOnSimplex;
import dmcontext.DMContext;
import drmanager.DRMPFactory;
import emo.interactive.iemod.IEMOD;
import emo.utils.decomposition.goal.GoalsFactory;
import exeption.ReferenceSetsConstructorException;
import frame.Frame;
import indicator.PerformanceIndicator;
import indicator.emo.HV;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSet;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import io.FileUtils;
import io.image.ImageSaver;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.rs.iterationslimit.Constant;
import model.internals.value.scalarizing.LNorm;
import plot.Plot3D;
import plot.Plot3DFactory;
import print.PrintUtils;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import utils.Screenshot;
import y2025.ERS.common.PCsDataContainer;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import scheme.WhiteScheme;
import scheme.enums.ColorFields;
import space.Vector;
import space.simplex.DasDennis;
import visualization.updaters.sources.EASource;
import visualization.utils.ReferenceParetoFront;
import y2025.ERS.e1_auxiliary.GeneratePCsData;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This script generates and saves (screenshots) populations constructed by IEMO/D coupled with ERS or FRS and
 * applied to DTLZ2 with 3 objectives.
 *
 * @author MTomczyk
 */
public class IllustratePopulationsDTLZ2
{
    /**
     * Runs the script.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        try
        {
            IRandom R = new MersenneTwister64(0);

            Path path = FileUtils.getPathRelatedToClass(GeneratePCsData.class, "Projects", "src", File.separatorChar);
            String fp = path.toString() + File.separatorChar + "pcs.txt"; // Load pairwise comparisons data
            PCsDataContainer PCs = new PCsDataContainer(fp, 4, 3, 100, 10);
            boolean useERS = false; // Changes the sampler used
            int trial = 24; // selected trial (24) (48)

            int interactions = 10;
            int gensPerInteraction = 100;

            System.out.print("DM's weights: ");
            double[] dmW = PCs._PCs[2][1]._trialPCs[trial]._dmW.clone();
            PrintUtils.printVectorOfDoubles(dmW, 2);

            // Derive optimum for the selected Chebyshev function
            double[][] opt = new double[][]{{1.0d / dmW[0], 1.0d / dmW[1], 1.0d / dmW[2]}};
            Vector.normalize(opt[0]);

            // Create plot 3D
            Plot3D plot3D = Plot3DFactory.getPlot(
                    WhiteScheme.getForPlot3D(),
                    "w1", "w2", "w3",
                    DRMPFactory.getFor3D(1.0d, 1.0d, 1.0d),
                    5, 5, 5,
                    "0.00", "0.00", "0.00",
                    2.0f, 2.0f, scheme ->
                            scheme._colors.put(ColorFields.PLOT_BACKGROUND, Color.WHITE),
                    null, null);

            int plotSize = 1000;
            Frame frame = new Frame(plot3D, plotSize, plotSize);
            plot3D.getModel().notifyDisplayRangesChangedListeners();

            // create reference data sets
            ArrayList<IDataSet> dataSets = new ArrayList<>();
            dataSets.add(DSFactory3D.getDS("Optimum", opt, new MarkerStyle(0.035f,
                    color.gradient.Color.BLACK, Marker.SPHERE_HIGH_POLY_3D)));
            dataSets.add(ReferenceParetoFront.getConcaveSpherical3DPF("Pareto front", 1.0f, 30,
                    new MarkerStyle(0.01f, color.gradient.Color.GRAY_50, Marker.SPHERE_HIGH_POLY_3D)));
            dataSets.add(DSFactory3D.getReferenceDS("Population", new MarkerStyle(0.005f,
                    ColorPalettes.getFromDefaultPalette(0), Marker.SPHERE_HIGH_POLY_3D)));
            plot3D.getModel().setDataSets(dataSets);

            plot3D.getModel().updateCameraTranslation(-0.03750f, 0.00000f, 1.76250f);
            plot3D.getModel().updateCameraRotation(0.00000f, 0.00000f);
            plot3D.getModel().updatePlotRotation(357.37225f, 44.54361f);

            // Create IEMO/D
            DTLZBundle problemBundle = DTLZBundle.getBundle(Problem.DTLZ2, 3);
            int populationSize = DasDennis.getNoProblems(3, 13);

            // Construct initial models:
            IConstructor<LNorm> constructor;
            IRandomModel<LNorm> RM = new LNormGenerator(3, Double.POSITIVE_INFINITY, problemBundle._normalizations);
            model.internals.value.scalarizing.LNorm[] initialModels = new model.internals.value.scalarizing.LNorm[populationSize];
            ArrayList<double[]> initialWeights = DasDennis.getWeightVectors(3, 13);
            for (int i = 0; i < initialWeights.size(); i++)
                initialModels[i] = new model.internals.value.scalarizing.LNorm(initialWeights.get(i),
                        Double.POSITIVE_INFINITY, problemBundle._normalizations);

            if (!useERS)
            {
                FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(RM);
                pFRS._initialModels = initialModels;
                pFRS._feasibleSamplesToGenerate = populationSize;
                pFRS._inconsistencyThreshold = populationSize - 1;
                pFRS._samplingLimit = 1000000000; // 10^9
                constructor = new FRS<>(pFRS);
            } else
            {
                constructor = ERSFactory.getDefaultForLNorms(populationSize,
                        new Constant(50000), 3, Double.POSITIVE_INFINITY, problemBundle._normalizations,
                        new LNormOnSimplex(Double.POSITIVE_INFINITY, 0.2d, 0.2d), new Tournament<>(2),
                        initialModels);
            }


            IRule rule = new IterationInterval(300, gensPerInteraction, interactions);
            IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();
            IReferenceSetConstructor rsc = new IReferenceSetConstructor()
            {
                private int _counter = 0;

                @Override
                public int getExpectedSize(AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
                {
                    return 2;
                }

                @Override
                public LinkedList<ReferenceSet> constructReferenceSets(DMContext dmContext, AbstractAlternatives<?> filteredAlternatives) throws ReferenceSetsConstructorException
                {
                    PCsDataContainer.TrialPCs trialPCs = PCs._PCs[2][1]._trialPCs[trial];
                    LinkedList<ReferenceSet> referenceSets = new LinkedList<>();
                    double[] e1 = trialPCs._referenceEvaluations[_counter][0].clone();
                    double[] e2 = trialPCs._referenceEvaluations[_counter][1].clone();

                    // do rescaling:
                    for (int m = 0; m < 3; m++)
                    {
                        double lb = problemBundle._paretoFrontBounds[m].getLeft();
                        double rb = problemBundle._paretoFrontBounds[m].getRight();
                        e1[m] = lb + e1[m] * (rb - lb);
                        e2[m] = lb + e2[m] * (rb - lb);
                    }

                    referenceSets.add(new ReferenceSet(new Alternative("A1_" + _counter, e1),
                            new Alternative("A2_" + _counter, e2)));
                    if (_counter < 10) _counter++; // for safety
                    return referenceSets;
                }
            };

            IDMFeedbackProvider iDM = new ArtificialValueDM<>(new model.definitions.LNorm(new model.internals.value.scalarizing.LNorm(
                    PCs._PCs[2][1]._trialPCs[trial]._dmW.clone(),
                    Double.POSITIVE_INFINITY, problemBundle._normalizations)));

            IEMOD iemod = IEMOD.getIEMOD(0, false, false,
                    R, GoalsFactory.getLNormsDND(3, 13, Double.POSITIVE_INFINITY, problemBundle._normalizations),
                    problemBundle, new emo.utils.decomposition.similarity.lnorm.Euclidean(), 10,
                    rule, rsc, iDM, preferenceModel, constructor);

            // Instantiate HV
            HV.Params pHV = new HV.Params(3, problemBundle._normalizations, new double[]{1.1d, 1.1d, 1.1d});
            pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
            pHV._presorting = true;
            pHV._deriveUniqueSpecimens = true;
            pHV._toleranceDuplicates = 1.0E-6;
            PerformanceIndicator hv = new PerformanceIndicator(new HV(pHV));

            IRunner runner = new Runner(new Runner.Params(iemod, populationSize));

            EASource eaSource = new EASource(iemod);

            frame.setVisible(true);

            // Projection-related data
            float[] ms = new float[]{0.035f, 0.0325f, 0.03f, 0.02f, 0.02f,
                    0.015f, 0.005f, 0.005f, 0.005f, 0.005f, 0.005f};
            float[] dms = new float[]{0.1f, 0.1f, 0.1f, 0.05f, 0.04f,
                    0.04f, 0.03f, 0.03f, 0.02f, 0.02f, 0.01f};

            runner.init();
            plot3D.getModel().updateSelectedDataSet(DSFactory3D.getDS("Population", eaSource.createData(),
                    new MarkerStyle(ms[0], ColorPalettes.getFromDefaultPalette(0), Marker.SPHERE_HIGH_POLY_3D)));
            plot3D.getModel().updateSelectedDataSet(DSFactory3D.getDS("Optimum", opt,
                    new MarkerStyle(dms[0], color.gradient.Color.BLACK, Marker.SPHERE_HIGH_POLY_3D)));

            int phase = 0;
            boolean snapshot;

            for (int g = 1; g < 300 + gensPerInteraction * interactions; g++)
            {
                snapshot = g == 299;
                if (g < 300) phase = 0;
                else
                {
                    for (int i = 1; i < interactions + 1; i++)
                    {
                        if (g <= 300 + (gensPerInteraction * i) - 1)
                        {
                            phase = i;
                            if (g == 300 + (gensPerInteraction * i) - 1) snapshot = true;
                            break;
                        }
                    }
                }

                if ((phase == 3) || (phase == 4))
                {
                    plot3D.getModel().updateCameraTranslation(-0.02173f, -0.00012f, 1.13473f);
                    plot3D.getModel().updatePlotRotation(357.37225f, 44.54361f);
                    plot3D.getModel().updateCameraRotation(335.27576f, 353.59756f);
                } else if (phase == 5)
                {
                    plot3D.getModel().updateCameraTranslation(0.12091f, -0.13884f, 0.71682f);
                    plot3D.getModel().updatePlotRotation(319.16205f, 37.22654f);
                    plot3D.getModel().updateCameraRotation(330.78046f, 0.54878f);
                } else if (phase == 6)
                {
                    plot3D.getModel().updateCameraTranslation(0.10379f, -0.20208f, 0.19788f);
                    plot3D.getModel().updatePlotRotation(357.37225f, 44.54361f);
                    plot3D.getModel().updateCameraRotation(329.09470f, 357.25610f);
                } else if (phase > 6)
                {
                    plot3D.getModel().updateCameraTranslation(0.10815f, -0.21378f, 0.12451f);
                    plot3D.getModel().updatePlotRotation(357.37225f, 44.54361f);
                    plot3D.getModel().updateCameraRotation(313.54840f, 354.51218f);
                }

                runner.executeSingleGeneration(g, null);
                plot3D.getModel().updateSelectedDataSet(DSFactory3D.getDS("Population", eaSource.createData(),
                        new MarkerStyle(ms[phase], ColorPalettes.getFromDefaultPalette(0), Marker.SPHERE_HIGH_POLY_3D)));
                plot3D.getModel().updateSelectedDataSet(DSFactory3D.getDS("Optimum", opt,
                        new MarkerStyle(dms[phase], color.gradient.Color.BLACK, Marker.SPHERE_HIGH_POLY_3D)));

                if (snapshot) // generate screenshot
                {
                    System.out.println("Current generation = " + g + " phase = " + phase);
                    System.out.println("HV = " + hv.evaluate(iemod));
                    Thread.sleep(100);
                    Screenshot screenshot = plot3D.getModel().requestScreenshotCreation(plotSize, plotSize, false);
                    screenshot._barrier.await();
                    String file = FileUtils.getPathRelatedToClass(IllustratePopulationsDTLZ2.class, "Projects", "src", File.separatorChar).toString();
                    file += File.separatorChar + "IEMOD_" + (useERS ? "ERS" : "FRS") + "_" + phase;
                    ImageSaver.saveImage(screenshot._image, file, "jpg", 1.0f);
                    Thread.sleep(100);
                }
            }

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
