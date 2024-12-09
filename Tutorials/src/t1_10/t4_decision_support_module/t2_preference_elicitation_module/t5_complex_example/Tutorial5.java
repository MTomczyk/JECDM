package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t5_complex_example;


import color.gradient.Color;
import color.gradient.Gradient;
import criterion.Criteria;
import dataset.DataSet;
import dataset.painter.style.LineStyle;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import dmcontext.DMContext;
import drmanager.DisplayRangesManager;
import ea.EA;
import emo.aposteriori.moead.MOEAD;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import emo.utils.decomposition.similarity.pbi.Euclidean;
import exception.RunnerException;
import exeption.DecisionMakerSystemException;
import exeption.ModelSystemException;
import exeption.ModuleException;
import frame.Frame;
import inconsistency.RemoveOldest;
import interaction.Status;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.feedbackprovider.dm.artificial.value.PairwiseComparisons;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.rules.IterationInterval;
import model.constructor.Dummy;
import model.internals.value.scalarizing.LNorm;
import plot.Plot3D;
import population.Specimens;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.Runner;
import runner.enums.DisplayMode;
import runner.enums.UpdaterMode;
import scheme.WhiteScheme;
import space.Range;
import space.os.ObjectiveSpace;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.model.ModelSystem;
import system.modules.elicitation.PreferenceElicitationModule;
import system.modules.elicitation.Report;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;
import updater.*;
import visualization.IVisualization;
import visualization.Visualization;
import visualization.updaters.sources.EASource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This tutorial showcases how to use the preference elicitation module along with an evolutionary algorithm (MOEA/D).
 *
 * @author MTomczyk
 */
public class Tutorial5
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        IRandom R = new MersenneTwister64(0);
        int generations = 500;
        LocalDateTime systemStartTime = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 3, false);

        // Create the MOEA/D:
        DTLZBundle problem = DTLZBundle.getBundle(Problem.DTLZ2, 3,
                DTLZBundle.getRecommendedNODistanceRelatedParameters(Problem.DTLZ2, 3));
        IGoal[] goals = GoalsFactory.getLNormsDND(3, 30, Double.POSITIVE_INFINITY);
        EA EA = MOEAD.getMOEAD(0, false, false, R, goals, problem, new Euclidean(), 10);

        // Creates fixed OS:
        ObjectiveSpace os = new ObjectiveSpace(problem._utopia, problem._nadir);

        // Create DMs;
        DM[] DMs = new DM[]{new DM(0, "DM1"), new DM(1, "DM2")};

        // Create the preference elicitation module:
        PreferenceElicitationModule.Params pPEM = new PreferenceElicitationModule.Params();
        pPEM._interactionTrigger = new InteractionTrigger(new IterationInterval(100)); // interact every 50 generations
        pPEM._refiner = Refiner.getDefault(); // get default refiner
        pPEM._DMs = DMs;
        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._dmConstructors = new HashMap<>(2);

        for (int i = 0; i < 2; i++)
        {
            LinkedList<IReferenceSetConstructor> constructors = new LinkedList<>();
            constructors.add(new RandomPairs(new RequiredSpread(0.01f))); // change to check the impact (0.01, 0.2, 0.5, 1.0)
            pRSC._dmConstructors.put(DMs[i].getName(), constructors);
        }

        pPEM._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        ArtificialValueDM<LNorm> artificialValueDM1 = new ArtificialValueDM<>(
                new model.definitions.LNorm(new LNorm(new double[]{0.7d, 0.2d, 0.1d},
                        Double.POSITIVE_INFINITY, problem._normalizations)),
                new PairwiseComparisons<>(true));

        ArtificialValueDM<LNorm> artificialValueDM2 = new ArtificialValueDM<>(
                new model.definitions.LNorm(new LNorm(new double[]{0.2d, 0.3d, 0.4d},
                        Double.POSITIVE_INFINITY, problem._normalizations)),
                new PairwiseComparisons<>(true));

        pPEM._feedbackProvider = FeedbackProvider.getForTwoDMs(DMs[0].getName(), artificialValueDM1, DMs[1].getName(), artificialValueDM2);

        // The decision maker system will be explained in further tutorials:
        pPEM._DMSs = new DecisionMakerSystem[2];
        for (int i = 0; i < 2; i++)
        {
            try
            {
                // The following lines are not important now (we must use them to avoid system exceptions)
                DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
                pDMS._DM = DMs[i];
                pDMS._modelSystems = new ModelSystem<?>[1];
                ModelSystem.Params<LNorm> pMS = new ModelSystem.Params<>();
                pMS._preferenceModel = new model.definitions.LNorm();
                pMS._modelConstructor = new Dummy<>();
                pMS._inconsistencyHandler = new RemoveOldest<>();
                pDMS._modelSystems[0] = new ModelSystem<>(pMS);
                pPEM._DMSs[i] = new DecisionMakerSystem(pDMS);
            } catch (DecisionMakerSystemException | ModelSystemException e)
            {
                throw new RuntimeException(e);
            }
        }
        PreferenceElicitationModule PEM = new PreferenceElicitationModule(pPEM);

        try
        {
            PEM.validate();
        } catch (ModuleException e)
        {
            throw new RuntimeException(e);
        }

        // Create Plot 3D (fourth display range is used to color preferred/not preferred alternatives in pairwise comparisons)
        Plot3D.Params p3D = new Plot3D.Params();
        p3D._xAxisTitle = "f1";
        p3D._yAxisTitle = "f2";
        p3D._zAxisTitle = "f3";
        p3D._pDisplayRangesManager = new DisplayRangesManager.Params();
        p3D._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[4];
        for (int i = 0; i < 3; i++)
            p3D._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(problem._displayRanges[i]);
        p3D._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(Range.getNormalRange());
        p3D._pDisplayRangesManager._attIdx_to_drIdx = new Integer[4];
        for (int i = 0; i < 4; i++) p3D._pDisplayRangesManager._attIdx_to_drIdx[i] = i;
        p3D._drawLegend = true;
        p3D._scheme = WhiteScheme.getForPlot3D();
        Plot3D plot3D = new Plot3D(p3D);

        Frame frame = new Frame(plot3D, 0.5f);

        // Create data updater:
        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[3];
        pDU._dataSources[0] = new EASource(EA);
        // See the HistorySource implementation in the same package (bind the source with the DMs' histories of preference elicitation):
        pDU._dataSources[1] = new HistorySource(pPEM._DMSs[0].getHistory());
        pDU._dataSources[2] = new HistorySource(pPEM._DMSs[1].getHistory());
        pDU._dataProcessors = new IDataProcessor[3];
        pDU._dataProcessors[0] = new DataProcessor();
        pDU._dataProcessors[1] = new DataProcessor(true, true); // check the interlace nulls flag
        pDU._dataProcessors[2] = new DataProcessor(true, true);
        pDU._sourcesToProcessors = new SourceToProcessors[3];
        pDU._sourcesToProcessors[0] = new SourceToProcessors(0);
        pDU._sourcesToProcessors[1] = new SourceToProcessors(1);
        pDU._sourcesToProcessors[2] = new SourceToProcessors(2);
        pDU._processorToPlots = new ProcessorToPlots[3];
        pDU._processorToPlots[0] = new ProcessorToPlots(0, DataSet.getFor3D("MOEA/D",
                new MarkerStyle(0.02f, Gradient.getViridisGradient(), 2, Marker.SPHERE_LOW_POLY_3D, 1.0f)));
        // Define how the preference information should look like:
        pDU._processorToPlots[1] = new ProcessorToPlots(0, DataSet.getFor3D("DM1: PCs",
                new MarkerStyle(0.05f, Gradient.getGreenRedGradient(), 3, Marker.TETRAHEDRON_FRONT_3D,
                        new LineStyle(0.1f, Color.BLACK, 0.2f), 1.0f),
                new LineStyle(0.2f, Color.GRAY_50, 0.2f)));
        pDU._processorToPlots[2] = new ProcessorToPlots(0, DataSet.getFor3D("DM2: PCs",
                new MarkerStyle(0.025f, Gradient.getBlueRedGradient(), 3, Marker.CUBE_3D,
                        new LineStyle(0.1f, Color.BLACK, 0.2f), 1.0f),
                new LineStyle(0.2f, Color.GRAY_50, 0.2f)));

        // Create the visualization tool:
        IVisualization visualization;
        try
        {
            visualization = new Visualization(frame, new DataUpdater(pDU));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        // Create the runner:
        Runner.Params pR = new Runner.Params(EA, visualization);
        pR._steadyStateRepeats = new int[]{goals.length}; // important for MOEA/D
        pR._updaterMode = UpdaterMode.AFTER_GENERATION;
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        Runner runner = new Runner(pR);

        // Execute the simulations:
        try
        {
            runner.init();
            runPreferenceElicitationModule(PEM, EA, 0, systemStartTime, criteria, os, R);
            for (int i = 1; i < generations; i++)
            {
                runner.executeSingleGeneration(i, null);
                runPreferenceElicitationModule(PEM, EA, i, systemStartTime, criteria, os, R);
            }

        } catch (RunnerException | ModuleException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Print the final history =================================================");
        for (int i = 0; i < 2; i++)
        {
            // Derive the decision maker system from params container:
            System.out.println(pPEM._DMSs[i].getHistory().getFullStringRepresentation());
        }

    }

    /**
     * Auxiliary method for running the preference elicitation module.
     *
     * @param PEM             preference elicitation module
     * @param ea              evolutionary algorithm
     * @param generation      current generation
     * @param systemStartTime system start time
     * @param criteria        criteria
     * @param os              objective space
     * @param R               random number generator
     * @throws ModuleException exception can be thrown and propagated higher
     */
    private static void runPreferenceElicitationModule(PreferenceElicitationModule PEM,
                                                       EA ea,
                                                       int generation,
                                                       LocalDateTime systemStartTime,
                                                       Criteria criteria,
                                                       ObjectiveSpace os,
                                                       IRandom R) throws ModuleException
    {
        // Create the context
        DMContext context = Common.getContext(generation,
                new Specimens(ea.getSpecimensContainer().getPopulation()),
                systemStartTime, criteria, os, R);

        // Use the preference elicitation module:
        Report report = PEM.executeProcess(context);
        if (report._interactionStatus == Status.PROCESS_ENDED_SUCCESSFULLY)
        {
            System.out.println("Report on preference elicitation (feedback provider; generation = " + generation + ")");
            report._feedbackProviderResult.printStringRepresentation();
        }

    }
}
