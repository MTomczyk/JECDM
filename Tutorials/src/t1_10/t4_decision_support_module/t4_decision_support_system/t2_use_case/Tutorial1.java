package t1_10.t4_decision_support_module.t4_decision_support_system.t2_use_case;

import color.gradient.Color;
import color.gradient.Gradient;
import component.axis.ticksupdater.FromDisplayRange;
import component.colorbar.Colorbar;
import criterion.Criteria;
import dataset.DataSet;
import dataset.IDataSet;
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
import exeption.DecisionSupportSystemException;
import frame.Frame;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.IValidator;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import model.IPreferenceModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.frs.FRS;
import model.internals.value.scalarizing.LNorm;
import plot.Plot3D;
import population.Specimen;
import population.Specimens;
import problem.Problem;
import problem.moo.dtlz.DTLZBundle;
import random.IRandom;
import random.MersenneTwister64;
import runner.IRunner;
import runner.Runner;
import runner.enums.DisplayMode;
import scheme.WhiteScheme;
import scheme.enums.SizeFields;
import space.Range;
import space.normalization.minmax.Gamma;
import space.os.ObjectiveSpace;
import statistics.Mean;
import system.ds.DSSParamsProvider;
import system.ds.DecisionSupportSystem;
import system.ds.Report;
import updater.*;
import visualization.Visualization;

import java.util.ArrayList;

/**
 * This tutorial showcases how to couple a decision support system with MOEA/D (highly synthetical example).
 *
 * @author MTomczyk
 */
public class Tutorial1
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
        Criteria criteria = Criteria.constructCriteria("C", 3, false);

        // Create the MOEA/D (population size = 1326):
        DTLZBundle problem = DTLZBundle.getBundle(Problem.DTLZ2, 3,
                DTLZBundle.getRecommendedNODistanceRelatedParameters(Problem.DTLZ2, 3));
        IGoal[] goals = GoalsFactory.getLNormsDND(3, 50, Double.POSITIVE_INFINITY);
        EA EA = MOEAD.getMOEAD(0, false, false, R, goals, problem, new Euclidean(), 10);

        System.out.println(goals.length);

        // Creates fixed OS:
        ObjectiveSpace os = new ObjectiveSpace(problem._utopia, problem._nadir);

        // Create the DSS params:
        int interactionsEvery = 75;
        IRule rule = new IterationInterval(interactionsEvery);

        // Create the preference model (L-norm):
        IPreferenceModel<LNorm> preferenceModel = new model.definitions.LNorm();

        // Create the reference set constructor:
        IValidator validator = new RequiredSpread(0.001f);
        IReferenceSetConstructor setConstructor = new RandomPairs(validator);

        // Creat the artificial DM:
        IDMFeedbackProvider artificialDM = new ArtificialValueDM<>(new model.definitions.LNorm(new LNorm(
                new double[]{1.0d / 3.0d, 1.0d / 3.0d, 1.0d / 3.0d},
                //new double[]{0.5d, 0.3d, 0.2d},
                Double.POSITIVE_INFINITY, problem._normalizations)));

        // Instantiate FRS:
        FRS.Params<LNorm> pFRS = new FRS.Params<>(new LNormGenerator(3, Double.POSITIVE_INFINITY));
        pFRS._feasibleSamplesToGenerate = 1000;
        pFRS._samplingLimit = 1000000;

        // Create DSS Params (dedicated to one DM, one model, and one DM-based feedback provider):
        DecisionSupportSystem.Params pDSS = DSSParamsProvider.getForSingleDecisionMakerSingleModelArtificialProvider(
                criteria, "DM", rule, setConstructor, artificialDM, preferenceModel, new FRS<>(pFRS));
        DecisionSupportSystem DSS;

        // Create DSS:
        try
        {
            DSS = new DecisionSupportSystem(pDSS);
        } catch (DecisionSupportSystemException e)
        {
            throw new RuntimeException(e);
        }

        // Create the plot
        Plot3D.Params p3D = new Plot3D.Params();
        p3D._scheme = WhiteScheme.getForPlot3D(0.25f);
        p3D._scheme._sizes.put(SizeFields.COLORBAR_SHRINK, 0.5f);
        p3D._scheme._sizes.put(SizeFields.MARGIN_TOP_RELATIVE_SIZE_MULTIPLIER, 0.1f);
        p3D._xAxisTitle = "f1";
        p3D._yAxisTitle = "f2";
        p3D._zAxisTitle = "f3";
        p3D._title = "Interaction interval = " + interactionsEvery + "; " + setConstructor;


        // Display ranges are creatively customized to render (apart from the population):
        // - color the population (markers) using a gradient (the value = mean score attained by an alternative given all compatible model instances)
        // - pairwise comparisons (line + two markers; green = preferred; red = not).
        // Thus, the display ranges are as follows:
        // - [0-2]: dedicated to X, Y, and Z axes;
        // - [3]: related to coloring specimen markers based on their mean relevance score (dynamically updated)
        // - [4]: [0-1] fixed display range (0 = will be mapped into a green color; 1 into red)
        p3D._pDisplayRangesManager = new DisplayRangesManager.Params();
        p3D._pDisplayRangesManager._DR = new DisplayRangesManager.DisplayRange[5];
        for (int i = 0; i < 3; i++)
            p3D._pDisplayRangesManager._DR[i] = new DisplayRangesManager.DisplayRange(os._ranges[i]);
        p3D._pDisplayRangesManager._DR[3] = new DisplayRangesManager.DisplayRange(null, true, true); // for relevance scores
        // To emphasize more relevant alternatives
        p3D._pDisplayRangesManager._DR[3].setNormalizer(new Gamma(0.5d));
        p3D._pDisplayRangesManager._DR[4] = new DisplayRangesManager.DisplayRange(Range.getNormalRange()); // for pairwise comparisons
        p3D._pDisplayRangesManager._attIdx_to_drIdx = new Integer[5];
        for (int i = 0; i < 5; i++) p3D._pDisplayRangesManager._attIdx_to_drIdx[i] = i;

        // Set colorbar (mean relevance score):
        p3D._colorbar = new Colorbar(Gradient.getPlasmaGradientInverse(), "Mean relevance (less is better)",
                new FromDisplayRange(p3D._pDisplayRangesManager._DR[3], 5));

        Plot3D plot3D = new Plot3D(p3D);

        Frame frame = new Frame(plot3D, 0.5f);
        //Frame frame = new Frame(plot3D, 1200, 1000);

        // Create data updater (must be creatively customized):
        DataUpdater.Params pDU = new DataUpdater.Params(frame.getModel().getPlotsWrapper());
        pDU._dataSources = new IDataSource[2];
        // First data source = population relevance. It generates regular X, Y, and Z specimens coordinates. Additionally,
        // the data vectors are extended by the average score attained by an alternative given all internal models (thus, the
        // reference to the preference model is passed to give the access)
        pDU._dataSources[0] = new PopulationRelevance<>(EA, DSS.getDecisionMakersSystems()[0].getModelSystems()[0].getPreferenceModel(), new Mean());
        // History source: data vectors represent all pairwise comparisons. Each pair is represented as two matrix rows
        // (one per each alternative; the first row in a pair is preferred). Further, the regular X, Y, Z coordinates
        // are extended by a non-relevant value (provides the offset) and a 0/1 value (0 = preferred, 1 not).
        // We cannot set 0/1 value as the fourth element, as this attribute is reserved to the mean relevance score.
        pDU._dataSources[1] = new HistorySource(DSS.getDecisionMakersSystems()[0].getHistory());
        pDU._dataProcessors = new IDataProcessor[2];
        // Regular data processor for the first source.
        pDU._dataProcessors[0] = new DataProcessor();
        // The second source uses a custom split processor. It splits the input data matrix so that each pairwise comparison
        // is stored as a different element in the final LinkedList. These elements are also interlaced with nulls
        // to generate independent (broken) lines in final renders.
        pDU._dataProcessors[1] = new SplitProcessor();
        pDU._sourcesToProcessors = new SourceToProcessors[2];
        pDU._sourcesToProcessors[0] = new SourceToProcessors(0);
        pDU._sourcesToProcessors[1] = new SourceToProcessors(1);
        pDU._processorToPlots = new ProcessorToPlots[2];

        // Create the processor->plots mappings.
        pDU._processorToPlots[0] = new ProcessorToPlots(0, DataSet.getFor3D("MOEA/D",
                new MarkerStyle(0.025f, Gradient.getPlasmaGradientInverse(), 3, Marker.SPHERE_LOW_POLY_3D)));
        IDataSet pcDataSet = DataSet.getFor3D("DM: PCs", new MarkerStyle(0.05f, Gradient.getGreenRedGradient(),
                        4, Marker.TETRAHEDRON_FRONT_3D, new LineStyle(0.1f, Color.BLACK, 0.2f), 1.0f),
                new LineStyle(0.2f, Color.GRAY_50, 0.2f));
        // Important note regarding the processing pipeline for "pairwise comparisons." The below line disables its
        // individual contribution when updating display ranges. This has to be done. The generated data point has this
        // non-relevant fourth element that otherwise contributes to DR[3] -- which is associated with the mean relevance
        // scores. Its involvement in calculations could create entirely improper display range bounds.
        pcDataSet.setSkipDisplayRangesUpdateMasks(new boolean[]{true, true, true, true, true});
        pDU._processorToPlots[1] = new ProcessorToPlots(0, pcDataSet);

        DataUpdater dataUpdater;
        try
        {
            dataUpdater = new DataUpdater(pDU);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        Runner.Params pR = new Runner.Params(EA);
        pR._steadyStateRepeats = new int[]{goals.length};
        pR._visualization = new Visualization(frame, dataUpdater);
        pR._displayMode = DisplayMode.FROM_THE_BEGINNING;
        IRunner runner = new Runner(pR);

        int[] limits = new int[]{generations};

        try
        {
            // Notify that the system starts
            DSS.notifySystemStarts();
            runner.init();

            // Create the context:
            DMContext.Params pContext = getDMContextParams(0, R, os, EA.getSpecimensContainer().getPopulation());
            // Execute DSS process (after each generation):
            Report report = DSS.executeProcess(pContext);


            for (int g = 1; g < generations; g++)
            {
                runner.executeSingleGeneration(g, limits);
                pContext = getDMContextParams(g, R, os, EA.getSpecimensContainer().getPopulation());
                report = DSS.executeProcess(pContext);
                System.out.println(g);

                System.out.println("AAAA " + DSS.getDecisionMakersSystems()[0].getHistory().getNoPreferenceExamples());
            }


        } catch (RunnerException | DecisionSupportSystemException e)
        {

            throw new RuntimeException(e);
        }
    }

    /**
     * Auxiliary method for creating the decision-making context.
     *
     * @param generation current generation
     * @param R          random number generator
     * @param os         fixed objective space
     * @param specimens  current population
     * @return decision-making context
     */
    private static DMContext.Params getDMContextParams(int generation,
                                                       IRandom R,
                                                       ObjectiveSpace os,
                                                       ArrayList<Specimen> specimens)
    {
        DMContext.Params pDMC = new DMContext.Params();
        pDMC._currentIteration = generation;
        pDMC._R = R;
        pDMC._currentOS = os;
        pDMC._osChanged = false;
        pDMC._currentAlternativesSuperset = new Specimens(specimens);
        return pDMC;
    }

}
