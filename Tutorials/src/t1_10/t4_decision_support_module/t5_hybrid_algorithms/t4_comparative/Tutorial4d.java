package t1_10.t4_decision_support_module.t5_hybrid_algorithms.t4_comparative;

import color.gradient.Color;
import color.gradient.ColorPalettes;
import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.moead.MOEAD;
import emo.interactive.StandardDSSBuilder;
import emo.interactive.iemod.IEMOD;
import emo.interactive.ktscone.cdemo.CDEMO;
import emo.interactive.ktscone.dcemo.DCEMO;
import emo.interactive.nemo.nemo0.NEMO0Builder;
import emo.interactive.nemo.nemoii.NEMOII;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.lnorm.Euclidean;
import exception.EAException;
import exception.RunnerException;
import frame.Frame;
import indicator.emo.interactive.ValueModelQuality;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IterationInterval;
import model.IPreferenceModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.representative.MDVF;
import model.constructor.value.representative.RepresentativeModel;
import model.constructor.value.rs.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import plot.Plot2D;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import scheme.WhiteScheme;
import scheme.enums.Align;
import scheme.enums.AlignFields;
import scheme.enums.SizeFields;
import selection.Random;
import space.Range;
import space.normalization.minmax.Logarithmic;
import statistics.Mean;
import statistics.Min;
import updater.*;
import visualization.Visualization;
import visualization.updaters.sources.GenerationIndicator;

/**
 * This tutorial concerns performing simple comparative analysis of the performance of interactive methods.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
public class Tutorial4d
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        int M = 5;

        IRandom R = new MersenneTwister64(0);
        DTLZBundle problem = DTLZBundle.getBundle(Problem.DTLZ2, M, 50);

        IGoal[] iemodGoals = GoalsFactory.getLNormsDND(M, 8, Double.POSITIVE_INFINITY, problem._normalizations);
        IGoal[] moeadGoals = GoalsFactory.getLNormsDND(M, 8, Double.POSITIVE_INFINITY, problem._normalizations);

        int populationSize = iemodGoals.length;
        System.out.println(populationSize);

        int generations = 300;
        int interactionInterval = 30;

        EA[] eas = new EA[6];

        // Common provider can be used here:
        IPreferenceModel<LNorm> dmModel = new model.definitions.LNorm(new LNorm(new double[]{
                0.3d, 0.2d, 0.2d, 0.1d, 0.2d},
                Double.POSITIVE_INFINITY, problem._normalizations));
        IDMFeedbackProvider dmFeedbackProvider = new ArtificialValueDM<>(dmModel);

        // IEMOD
        {
            FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(M, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 1000000;
            pFRS._feasibleSamplesToGenerate = iemodGoals.length; // Important: the number of samples should not be smaller than the population size (no. goals)
            FRS<LNorm> frs = new FRS<>(pFRS);
            eas[0] = IEMOD.getIEMOD(0, false, false, R, iemodGoals, problem,
                    new Euclidean(), 10, new IterationInterval(interactionInterval),
                    new RandomPairs(new RequiredSpread(0.001d)), dmFeedbackProvider, new model.definitions.LNorm(), frs);
        }
        // NEMO0
        {
            // OLD VERSION:
            // Representative model (the procedure is built on FRS)
            /*RepresentativeModel.Params<LNorm> pRM = new RepresentativeModel.Params<>(
                    new LNormGenerator(M, Double.POSITIVE_INFINITY),
                    new MDVF<>()); // most discriminative value function
            pRM._feasibleSamplesToGenerate = 1000000;
            pRM._samplingLimit = populationSize;
            RepresentativeModel<LNorm> representativeModel = new RepresentativeModel<>(pRM);

            eas[1] = NEMO0.getNEMO0(1, populationSize,
                    false, false, R, problem, new IterationInterval(interactionInterval),
                    new RandomPairs(new RequiredSpread(0.001d)), dmFeedbackProvider,
                    new model.definitions.LNorm(), representativeModel);*/

            // NEW VERSION:
            FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(M, Double.POSITIVE_INFINITY));
            pFRS._feasibleSamplesToGenerate = populationSize;
            pFRS._samplingLimit = 1000000;
            model.constructor.value.representative.RepresentativeModel<LNorm>
                    representativeModel = new RepresentativeModel<>(new FRS<>(pFRS), new MDVF<>());

            NEMO0Builder<LNorm> nemo0Builder = new NEMO0Builder<>(R);
            nemo0Builder.setCriteria(Criteria.constructCriteria("C", M, false));
            nemo0Builder.setPopulationSize(populationSize);
            nemo0Builder.setInitialPopulationConstructor(problem._construct);
            nemo0Builder.setSpecimensEvaluator(problem._evaluate);
            nemo0Builder.setParentsReproducer(problem._reproduce);
            nemo0Builder.setParentsSelector(new Random(2));
            nemo0Builder.setStandardDSSBuilder(new StandardDSSBuilder<>());
            nemo0Builder.getDSSBuilder().setModelConstructor(representativeModel);
            nemo0Builder.getDSSBuilder().setPreferenceModel(new model.definitions.LNorm());
            nemo0Builder.getDSSBuilder().setInteractionRule(new IterationInterval(interactionInterval));
            nemo0Builder.getDSSBuilder().setReferenceSetConstructor(new RandomPairs(new RequiredSpread(0.001d)));
            nemo0Builder.getDSSBuilder().setDMFeedbackProvider(dmFeedbackProvider);
            nemo0Builder.setFixedOSBoundsLearningPolicy(problem);
            try
            {
                eas[1] = nemo0Builder.getInstance();
            } catch (EAException e)
            {
                throw new RuntimeException(e);
            }
        }
        // NEMOII
        {
            FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(M, Double.POSITIVE_INFINITY));
            pFRS._samplingLimit = 1000000;
            pFRS._feasibleSamplesToGenerate = populationSize;
            FRS<LNorm> frs = new FRS<>(pFRS);

            eas[2] = NEMOII.getNEMOII(2, populationSize, false, false, R,
                    problem, new IterationInterval(interactionInterval), new RandomPairs(new RequiredSpread(0.001d)),
                    dmFeedbackProvider, new model.definitions.LNorm(), frs);
        }
        // CDEMO
        {
            eas[3] = CDEMO.getCDEMO(3, populationSize,
                    false, false, R, problem, new IterationInterval(interactionInterval),
                    new RandomPairs(new RequiredSpread(0.001d)), dmFeedbackProvider);
        }
        // DCEMO
        {
            eas[4] = DCEMO.getDCEMO(4, populationSize,
                    false, false, R, problem, new IterationInterval(interactionInterval),
                    new RandomPairs(new RequiredSpread(0.001d)), dmFeedbackProvider);
        }
        // MOEA/D
        {
            eas[5] = MOEAD.getMOEAD(5, false, false, R, moeadGoals, problem, new Euclidean(), 10);
        }

        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._scheme._aligns.put(AlignFields.LEGEND, Align.RIGHT_TOP_EXTERNAL);
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.35f);
        pP._xAxisTitle = "Generation";
        pP._yAxisTitle = "DM-perceived quality";
        pP._title = "No. objectives = 5";
        pP._drawLegend = true;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getForConvergencePlot2D(Range.get0R(generations - 1),
                new Range(0.05d, 0.5d)); // left != 0 <- to avoid 0 when working with log scale
        pP._pDisplayRangesManager._DR[1].setNormalizer(new Logarithmic());
        Plot2D plot2D = new Plot2D(pP);

        //Frame frame = new Frame(plot2D, 0.5f);
        Frame frame = new Frame(plot2D, 1200, 800);

        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[eas.length * 2];
        for (int i = 0; i < eas.length; i++)
        {
            pDU._dataSources[2 * i] = new GenerationIndicator(eas[i], new ValueModelQuality<>(dmModel, new Min()));
            pDU._dataSources[2 * i + 1] = new GenerationIndicator(eas[i], new ValueModelQuality<>(dmModel, new Mean()));
        }
        pDU._dataProcessors = new IDataProcessor[eas.length * 2];
        for (int i = 0; i < eas.length * 2; i++)
            pDU._dataProcessors[i] = new DataProcessor(true);
        pDU._sourcesToProcessors = new SourceToProcessors[eas.length * 2];
        for (int i = 0; i < eas.length * 2; i++)
            pDU._sourcesToProcessors[i] = new SourceToProcessors(i);

        pDU._processorToPlots = new ProcessorToPlots[eas.length * 2];
        Color[] colors = new Color[eas.length * 2];
        for (int i = 0; i < eas.length; i++)
        {
            colors[2 * i] = ColorPalettes.getFromDefaultPalette(i);
            colors[2 * i + 1] = ColorPalettes.getFromDefaultPalette(i);
        }


        Marker[] ms = new Marker[]{
                null, Marker.CIRCLE,
                null, Marker.SQUARE,
                null, Marker.TRIANGLE_UP,
                null, Marker.TRIANGLE_DOWN,
                null, Marker.PENTAGON,
                null, Marker.STAR
        };


        String[] names = new String[eas.length * 2];
        for (int i = 0; i < eas.length; i++)
        {
            names[2 * i] = eas[i].getName() + " (BEST)";
            names[2 * i + 1] = eas[i].getName() + " (MEAN)";
        }

        for (int i = 0; i < eas.length * 2; i++)
        {
            MarkerStyle markerStyle = null;
            if (ms[i] != null)
            {
                markerStyle = new MarkerStyle(3.0f, colors[i], ms[i], new LineStyle(0.25f, Color.BLACK));
                markerStyle._paintEvery = generations / 10;
            }
            pDU._processorToPlots[i] = new ProcessorToPlots(0,
                    DataSet.getFor2D(names[i], markerStyle, new LineStyle(0.5f, colors[i])));
        }

        DataUpdater dataUpdater;

        try
        {
            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        Visualization visualization = new Visualization(frame, dataUpdater);

        Runner.Params pR = new Runner.Params(eas);

        pR._steadyStateRepeats = new int[]{iemodGoals.length, 1, 1, 1, 1, moeadGoals.length};
        pR._visualization = visualization;
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        IRunner runner = new Runner(pR);

        try
        {
            runner.executeEvolution(generations);
        } catch (RunnerException e)
        {
            throw new RuntimeException(e);
        }


    }
}
