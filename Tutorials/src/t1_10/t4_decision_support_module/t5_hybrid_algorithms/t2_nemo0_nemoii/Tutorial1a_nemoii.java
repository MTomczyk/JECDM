package t1_10.t4_decision_support_module.t5_hybrid_algorithms.t2_nemo0_nemoii;

import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import dataset.DataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import drmanager.DisplayRangesManager;
import emo.interactive.nemo.nemoii.NEMOII;
import exception.RunnerException;
import frame.Frame;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import model.IPreferenceModel;
import model.constructor.random.LNormGenerator;
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
import scheme.enums.SizeFields;
import system.ds.DecisionSupportSystem;
import updater.*;
import visualization.Visualization;
import visualization.updaters.sources.EASource;

/**
 * This tutorial showcases the NEMO-II (L-norm) algorithm ({@link NEMOII}).
 *
 * @author MTomczyk
 */
public class Tutorial1a_nemoii
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        int populationSize = 100;
        int generations = 500;
        int M = 2;

        IRandom R = new MersenneTwister64(0);
        DTLZBundle problem = DTLZBundle.getBundle(Problem.DTLZ2, M, 50);

        // 0 generation is skipped (init does not involve DSS, use (1, 5) params if want to trigger interactions ASAP (i.e., from generation 1)
        IRule interactionRule = new IterationInterval(50);
        IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();
        IReferenceSetConstructor referenceSetConstructor = new RandomPairs(new RequiredSpread(0.001d));
        IDMFeedbackProvider dmFeedbackProvider = new ArtificialValueDM<>(new model.definitions.LNorm(new LNorm(
                new double[]{0.5d, 0.5d},
                //new double[]{0.25d, 0.75d},
                Double.POSITIVE_INFINITY, problem._normalizations)));

        FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(M, Double.POSITIVE_INFINITY));
        pFRS._samplingLimit = 1000000;
        pFRS._feasibleSamplesToGenerate = 1000;
        FRS<LNorm> frs = new FRS<>(pFRS);

        NEMOII nemoii = NEMOII.getNEMOII(0, populationSize,
                false, false, R, problem, interactionRule, referenceSetConstructor,
                dmFeedbackProvider, preferenceModel, frs);

        Plot2D.Params pP = new Plot2D.Params();
        pP._scheme = new WhiteScheme();
        pP._scheme._sizes.put(SizeFields.MARGIN_RIGHT_RELATIVE_SIZE_MULTIPLIER, 0.25f);
        pP._xAxisTitle = "f1";
        pP._yAxisTitle = "f2";
        pP._drawLegend = true;
        pP._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(problem._displayRanges[0],
                problem._displayRanges[1], null);
        pP._pDisplayRangesManager._DR[2] = new DisplayRangesManager.DisplayRange(null, true, true);

        pP._colorbar = new Colorbar(Gradient.getViridisGradient(), "Generation",
                new FromDisplayRange(pP._pDisplayRangesManager._DR[2], 5));
        Plot2D plot2D = new Plot2D(pP);

        //Frame frame = new Frame(plot2D, 0.5f);
        Frame frame = new Frame(plot2D, 900, 800);

        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[1];
        pDU._dataSources[0] = new EASource(nemoii, true);
        pDU._dataProcessors = new IDataProcessor[1];
        pDU._dataProcessors[0] = new DataProcessor(true);
        pDU._sourcesToProcessors = new SourceToProcessors[1];
        pDU._sourcesToProcessors[0] = new SourceToProcessors(0);
        pDU._processorToPlots = new ProcessorToPlots[1];
        pDU._processorToPlots[0] = new ProcessorToPlots(0, DataSet.getFor2D("NEMO-II",
                new MarkerStyle(2.0f, Gradient.getViridisGradient(), 2, Marker.CIRCLE)));

        DataUpdater dataUpdater;

        try
        {
            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        Visualization visualization = new Visualization(frame, dataUpdater);

        Runner.Params pR = new Runner.Params(nemoii);
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

        // Print history:
        DecisionSupportSystem decisionSupportSystem = nemoii.getDecisionSupportSystem();
        System.out.println(decisionSupportSystem.getDecisionMakersSystems()[0].getHistory().getFullStringRepresentation());

    }

}
