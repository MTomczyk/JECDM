package t1_10.t5_experimentation_module.t4_interactive_emo_experiment;

import container.Containers;
import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.AbstractTrialDataContainer;
import container.trial.TrialDataContainerFactory;
import container.trial.initialziers.IEAInitializer;
import ea.EA;
import emo.interactive.iemod.IEMOD;
import emo.interactive.ktscone.cdemo.CDEMO;
import emo.interactive.ktscone.dcemo.DCEMO;
import emo.interactive.nemo.nemo0.NEMO0;
import emo.interactive.nemo.nemoii.NEMOII;
import emo.utils.decomposition.goal.GoalsFactory;
import emo.utils.decomposition.goal.IGoal;
import exception.TrialException;
import indicator.ExecutionTime;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import indicator.emo.GD;
import indicator.emo.interactive.HistorySize;
import indicator.emo.interactive.ReportedInconsistencies;
import indicator.emo.interactive.ValueModelQuality;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import io.cross.excel.ConvergenceXLSX;
import io.cross.excel.FinalRankerXLSX;
import io.cross.excel.FinalStatisticsXLSX;
import io.scenario.excel.SummarizerXLSX;
import model.IPreferenceModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.representative.MDVF;
import model.constructor.value.rs.representative.RepresentativeModel;
import model.definitions.LNorm;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import problem.moo.ReferencePointsFactory;
import random.IRandom;
import random.MersenneTwister64;
import scenario.CrossedSetting;
import space.distance.Euclidean;
import space.normalization.INormalization;
import space.simplex.DasDennis;
import statistics.*;
import statistics.tests.ITest;
import statistics.tests.WilcoxonSignedRank;
import utils.weights.WeightsGetter;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Provides some utility methods for the tutorial.
 *
 * @author MTomczyk
 */
@SuppressWarnings({"DuplicateBranchesInSwitch", "DuplicatedCode", "ExtractMethodRecommender"})
public class Utils
{
    /**
     * Creates global data container, scenario data container factory, and trial data container factory,
     * and returns them via {@link Containers}.
     *
     * @return created containers
     */
    public static Containers getContainers()
    {
        // Map containing data on the number of cuts for DND method given a number of objectives
        HashMap<Integer, Integer> cutsMap = Utils.getDNDCutsMap();
        System.out.println("Cuts map = " + cutsMap);

        // Map containing data on the population size given a number of objectives
        HashMap<Integer, Integer> psMap = Utils.getPSMap(cutsMap);
        System.out.println("Population size map = " + psMap);

        System.out.println("Creating reference points...");
        HashMap<Problem, HashMap<Integer, double[][]>> referencePoints = Utils.getRPs(psMap, new MersenneTwister64(0));
        System.out.println("Reference points are created...");

        // GLOBAL DATA CONTAINER =======================================================================================
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._mainPath = "E:\\iemo_test";
        pGDC._noTrials = 100;
        pGDC._noThreads = 20;
        pGDC._useMonitorThread = true; // turn on (true by default)
        pGDC._monitorReportingInterval = 20000;

        pGDC._scenarioKeys = new String[]{
                "ALGORITHM", // 0
                "PROBLEM",  // 1
                "OBJECTIVES", // 2
        };

        // Define scenario values (1:1 mapping with keys):
        pGDC._scenarioValues = new String[][]{

                {
                        "CDEMO", "DCEMO", "NEMO0", "NEMOII", "IEMOD"
                }, // 0 (note that special symbols, e.g., / in IEMOD, are not allowed)
                {
                        "DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "WFG1ALPHA02", "WFG2", "WFG3", "WFG4"
                },
                {
                        "2", "3", "4", "5"
                }, // 2

        };

        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerXLSX());

        pGDC._crossedSettings = new CrossedSetting[1];
        pGDC._crossedSettings[0] = new CrossedSetting(new String[]{"PROBLEM", "OBJECTIVES", "ALGORITHM"},
                new String[][]{
                        pGDC._scenarioValues[1].clone(),
                        pGDC._scenarioValues[2].clone(),
                        pGDC._scenarioValues[0].clone()
                });

        pGDC._referenceCrossSavers = new LinkedList<>();
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(3));
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(3));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALGORITHM", new ITest[]{new WilcoxonSignedRank()}, 3));

        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // SCENARIO DATA CONTAINER =======================================================================================
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();

        // Conditional number of generations
        pSDCF._numberOfGenerationsInitializer = p -> {
            int mul = p._scenario.getObjectives() - 1;
            String problem = p._scenario.getProblem();
            switch (problem)
            {
                case "DTLZ1" ->
                {
                    return 400 + 100 * mul;
                }
                case "DTLZ2", "DTLZ4" ->
                {
                    return 200 + 50 * mul;
                }
                case "DTLZ3" ->
                {
                    return 1000 + 250 * mul;
                }
                case "WFG1ALPHA02" ->
                {
                    return 1000 + 200 * mul;
                }
                default ->
                {
                    return 400 + 100 * mul;
                }
            }
        };

        // Conditional number of SSR (different for IEMOD/D, SSR = PS)
        pSDCF._numberOfSteadyStateRepeatsInitializer = p -> {
            if (p._scenario.getAlgorithm().equals("IEMOD"))
            {
                return psMap.get(p._scenario.getObjectives());
            }
            return 1;
        };

        // We will use six performance indicators:
        // 1-2) Indicators 1-2 are oracles that assess how relevant the constructed solution is given the decision maker's
        // preferences. For this reason, the code employs the ValueModelQuality indicator. It is coupled with the same
        // model that will be coupled with algorithms to simulate the decision maker's answers (using a different model
        // would not make sense). Specifically, we will employ the weighted Chebyshev function for the preference model.
        // IMPORTANT NOTE: Consider a single scenario. Naturally, the decision maker's model is the same for all test
        // runs. However, each trial will involve a different -- pre-defined weight vector. It is a common performance
        // when investigating the performance of interactive methods. This way, their ability to converge towards
        // different preferred regions in the PF can be tested. The weights are accessible via
        // WeightsGetter.getPredefinedVector. The IIntializer for the PerformanceIndicator is created to set the weight
        // vector conditionally on the trial number (see the code below). As for the first indicator (1), it is tuned to
        // report the best (minimal) relevance score attained by specimens in a population. In turn, (2) calculates
        // the average. NOTE that the custom names for the indicators are used to avoid using forbidden characters: VMQ_MIN (MEAN).
        // 3) GD is used to quantify how close the specimens are to PF
        // 4) ExecutionTime is employed (measures execution time in ms).
        // 5) HistorySize is used (provides the size of the history of preference elicitation).
        // 6) ReportedInconsistencies (informs about a number of attempts to construct internal models that failed;
        // thus, consistency reintroduction procedure started
        pSDCF._indicatorsInitializer = p -> {
            IIndicator[] indicators = new IIndicator[6]; // create indicators
            Problem problem = AbstractMOOProblemBundle.getProblemFromString(p._scenario.getProblem()); // get problem ID
            int M = p._scenario.getObjectives(); // get the number of objectives
            INormalization[] normalizations = AbstractMOOProblemBundle.getNormalizations(problem, M); // derive proper normalizations
            double[][] rps = referencePoints.get(problem).get(M); // get the pre-calculated reference points
            // Best relevance score:
            indicators[0] = new PerformanceIndicator("VMQ_MIN", new ValueModelQuality<>(new LNorm(new model.internals.value.scalarizing.LNorm(null, Double.POSITIVE_INFINITY, normalizations)),
                    new Min()), (scenario, trialID) -> {
                double[] w = WeightsGetter.getPredefinedVector(M, trialID);
                return new PerformanceIndicator("VMQ_MIN", new ValueModelQuality<>(new LNorm(new model.internals.value.scalarizing.LNorm(w,
                        Double.POSITIVE_INFINITY, normalizations)), new Min()));
            });
            // Mean relevance score:
            indicators[1] = new PerformanceIndicator("VMQ_MEAN", new ValueModelQuality<>(new LNorm(new model.internals.value.scalarizing.LNorm(null, Double.POSITIVE_INFINITY, normalizations)),
                    new Mean()), (scenario, trialID) -> {
                double[] w = WeightsGetter.getPredefinedVector(M, trialID);
                return new PerformanceIndicator("VMQ_MEAN", new ValueModelQuality<>(new LNorm(new model.internals.value.scalarizing.LNorm(w,
                        Double.POSITIVE_INFINITY, normalizations)), new Mean()));
            });
            // GD and ExecutionTime
            indicators[2] = new PerformanceIndicator(new GD(new Euclidean(normalizations), rps));
            indicators[3] = new PerformanceIndicator(new ExecutionTime());
            indicators[4] = new PerformanceIndicator(new HistorySize());
            indicators[5] = new PerformanceIndicator(new ReportedInconsistencies());
            return indicators;
        };

        pSDCF._statistics = new IStatistic[4];
        pSDCF._statistics[0] = new Min();
        pSDCF._statistics[1] = new Mean();
        pSDCF._statistics[2] = new Max();
        pSDCF._statistics[3] = new StandardDeviation();

        pSDCF._dataStoringInterval = 500;

        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TRIAL DATA CONTAINER =======================================================================================
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();

        // Create EA initializer:
        pTDCF._eaInitializer = new IEAInitializer()
        {
            @Override
            public EA instantiateEA(IRandom R, AbstractTrialDataContainer.Params p) throws TrialException
            {
                // Problem bundle should be available:
                if (p._problemBundle == null)
                    throw new TrialException("Problem is unavailable", this.getClass(), p._SDC.getScenario(), p._trialID);
                if (!(p._problemBundle instanceof AbstractMOOProblemBundle problemBundle))
                    throw new TrialException("Problem is not MOO", this.getClass(), p._SDC.getScenario(), p._trialID);

                String algorithm = p._SDC.getScenario().getAlgorithm();
                int objectives = p._SDC.getObjectives();
                int PS = psMap.get(objectives);
                int cuts = cutsMap.get(objectives);

                // 10 interactions equally distributed, starting from the first generation:
                IRule rule = new IterationInterval(1, p._SDC.getGenerations() / 11, 10);

                // Create the reference sets constructor (random pairs, alternatives cannot be closer:
                // -- on each criterion -- than 0.001 in the normalized space:
                IReferenceSetConstructor referenceSetConstructor = new RandomPairs(new RequiredSpread(0.01d));
                // Get a pre-defined weight vector (trial-dependent):
                double[] weights = WeightsGetter.getPredefinedVector(objectives, p._trialID);
                // Create the internal model (for the artificial DM):
                model.internals.value.scalarizing.LNorm internal = new model.internals.value.scalarizing.LNorm(weights,
                        Double.POSITIVE_INFINITY, problemBundle._normalizations);
                // Create the preference mode:
                IPreferenceModel<model.internals.value.scalarizing.LNorm> artificialDMModel = new LNorm(internal);
                // Create the artificial DM:
                IDMFeedbackProvider artificialDM = new ArtificialValueDM<>(artificialDMModel);

                switch (algorithm)
                {
                    case "CDEMO" ->
                    {
                        return CDEMO.getCDEMO(0, PS, true, true, R, problemBundle,
                                rule, referenceSetConstructor, artificialDM);
                    }
                    case "DCEMO" ->
                    {
                        return DCEMO.getDCEMO(0, PS, true, true, R, problemBundle,
                                rule, referenceSetConstructor, artificialDM);
                    }
                    case "NEMO0" ->
                    {
                        IPreferenceModel<model.internals.value.scalarizing.LNorm> model = new LNorm();
                        RepresentativeModel.Params<model.internals.value.scalarizing.LNorm> pRM = new RepresentativeModel.Params<>(
                                new LNormGenerator(objectives, Double.POSITIVE_INFINITY), new MDVF<>());
                        pRM._feasibleSamplesToGenerate = PS; // the more, the better the representative function
                        pRM._inconsistencyThreshold = 0;
                        pRM._samplingLimit = 10000000;
                        return NEMO0.getNEMO0(0, PS, true, true, R, problemBundle,
                                rule, referenceSetConstructor, artificialDM, model, new RepresentativeModel<>(pRM));
                    }
                    case "NEMOII" ->
                    {
                        IPreferenceModel<model.internals.value.scalarizing.LNorm> model = new LNorm();
                        FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(
                                new LNormGenerator(objectives, Double.POSITIVE_INFINITY));
                        pFRS._feasibleSamplesToGenerate = PS; // the more, the better the approximation of the PO relation
                        pFRS._inconsistencyThreshold = PS - 1;
                        pFRS._samplingLimit = 10000000;
                        return NEMOII.getNEMOII(0, PS, true, true, R, problemBundle,
                                rule, referenceSetConstructor, artificialDM, model, new FRS<>(pFRS));
                    }
                    case "IEMOD" ->
                    {
                        IGoal[] goals = GoalsFactory.getLNormsDND(objectives, cuts, Double.POSITIVE_INFINITY);
                        IPreferenceModel<model.internals.value.scalarizing.LNorm> model = new LNorm();
                        FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(
                                new LNormGenerator(objectives, Double.POSITIVE_INFINITY));
                        pFRS._feasibleSamplesToGenerate = PS; // should be at least PS (PS is enough)
                        pFRS._inconsistencyThreshold = PS - 1;
                        pFRS._samplingLimit = 10000000;

                        return IEMOD.getIEMOD(0, true, true, R, goals,
                                problemBundle, new emo.utils.decomposition.similarity.lnorm.Euclidean(), 10,
                                rule, referenceSetConstructor, artificialDM, model, new FRS<>(pFRS));
                    }
                    default ->
                    {
                        return null;
                    }
                }
            }
        };

        // Create the factory:
        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);

        return new Containers(GDC, SDCF, TDCF);
    }


    /**
     * Provides a map containing data on the number of cuts for the DND method given a number of objectives.
     *
     * @return map  (key = objectives; value = no. cuts)
     */
    public static HashMap<Integer, Integer> getDNDCutsMap()
    {
        HashMap<Integer, Integer> cutsMap = new HashMap<>(5);
        cutsMap.put(2, 29);
        cutsMap.put(3, 10);
        cutsMap.put(4, 7);
        cutsMap.put(5, 6);
        return cutsMap;
    }

    /**
     * Provides a map containing data on the number population size method given the input number of cuts for the DND method.
     *
     * @param cutsMap map obtained via {@link Utils#getDNDCutsMap()}
     * @return map (key = objectives; value = ps)
     */
    public static HashMap<Integer, Integer> getPSMap(HashMap<Integer, Integer> cutsMap)
    {
        HashMap<Integer, Integer> psMap = new HashMap<>(5);
        for (int i : new int[]{2, 3, 4, 5})
        {
            int cuts = cutsMap.get(i);
            int ps = DasDennis.getWeightVectors(i, cuts).size();
            psMap.put(i, ps);
        }
        return psMap;
    }

    /**
     * Creates the reference points (read-only data set). The points are stored in a 2-level associative array.
     * The first key is the problem ID, while the second is the number of objectives. The reference points are random
     * Pareto optimal solutions to the problem. The number is set to the respective population size * 4  (arbitrary
     * decision; one should adjust to one's own needs).
     *
     * @param psMap ma obtained via {@link Utils#getPSMap(HashMap)}
     * @param R     random number generator
     * @return reference points (given a problem and the number of objectives)
     */
    public static HashMap<Problem, HashMap<Integer, double[][]>> getRPs(HashMap<Integer, Integer> psMap,
                                                                        IRandom R)
    {
        // Important note: some of the problems have the same PF. Thus, the whole result could be simplified.
        HashMap<Problem, HashMap<Integer, double[][]>> rpsMap = new HashMap<>(5 * 8);
        for (Problem problem : new Problem[]{Problem.DTLZ1, Problem.DTLZ2, Problem.DTLZ3, Problem.DTLZ4,
                Problem.WFG1ALPHA02, Problem.WFG2, Problem.WFG3, Problem.WFG4})
        {
            HashMap<Integer, double[][]> rps = new HashMap<>();
            for (Integer o : new Integer[]{2, 3, 4, 5})
            {
                int ps = psMap.get(o);
                double[][] pfPoints = ReferencePointsFactory.getRandomReferencePoints(problem, ps * 2, o, R);
                rps.put(o, pfPoints);
            }
            rpsMap.put(problem, rps);
        }
        return rpsMap;
    }
}
