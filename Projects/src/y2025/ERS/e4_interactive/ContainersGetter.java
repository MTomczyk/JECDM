package y2025.ERS.e4_interactive;

import alternative.AbstractAlternatives;
import alternative.Alternative;
import condition.ScenarioDisablingConditions;
import container.Containers;
import container.global.GlobalDataContainer;
import container.global.initializers.DefaultRandomNumberGeneratorInitializer;
import container.scenario.AbstractScenarioDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.scenario.intializers.INumberOfSteadyStateRepeatsInitializer;
import container.trial.TrialDataContainerFactory;
import decisionsupport.ERSFactory;
import decisionsupport.operators.LNormOnSimplex;
import dmcontext.DMContext;
import ea.IEA;
import emo.interactive.iemod.IEMOD;
import emo.interactive.nemo.nemo0.NEMO0;
import emo.interactive.nemo.nemoii.NEMOII;
import emo.utils.decomposition.goal.GoalsFactory;
import exception.ScenarioException;
import exception.TrialException;
import exeption.ReferenceSetsConstructorException;
import indicator.ExecutionTime;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import indicator.emo.GDConcaveSpherical;
import indicator.emo.HV;
import indicator.emo.interactive.HistorySize;
import indicator.emo.interactive.ReportedInconsistencies;
import indicator.emo.interactive.ValueModelQuality;
import interaction.feedbackprovider.dm.IDMFeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSet;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.refine.Refiner;
import interaction.trigger.rules.IRule;
import interaction.trigger.rules.IterationInterval;
import io.FileUtils;
import io.cross.excel.FinalRankerXLSX;
import io.cross.excel.FinalStatisticsXLSX;
import io.scenario.excel.SummarizerXLSX;
import io.scenario.excel.TrialsResultsXLSX;
import model.IPreferenceModel;
import model.constructor.IConstructor;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.constructor.value.representative.MDVF;
import model.constructor.value.representative.RepresentativeModel;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.frs.FRS;
import model.constructor.value.rs.iterationslimit.Constant;
import model.definitions.LNorm;
import problem.Problem;
import problem.moo.AbstractMOOProblemBundle;
import random.IRandom;
import random.MersenneTwister32;
import random.MersenneTwister64;
import scenario.CrossedSetting;
import space.distance.Euclidean;
import space.normalization.INormalization;
import space.simplex.DasDennis;
import statistics.*;
import statistics.tests.ITest;
import statistics.tests.TStudent;
import statistics.tests.WilcoxonSignedRank;
import tools.prototypes.GDFast;
import utils.GenerationsLimits;
import utils.ReferencePointsOnPareto;
import y2025.ERS.common.PCsDataContainer;
import y2025.ERS.e1_auxiliary.GeneratePCsData;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Provides the data containers for this experiment.
 *
 * @author MTomczyk
 */
public class ContainersGetter
{
    /**
     * Tester.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        try
        {
            getContainers();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Main method for creating the containers.
     *
     * @return containers
     * @throws Exception the exception can be thrown
     */
    @SuppressWarnings("DataFlowIssue")
    public static Containers getContainers() throws Exception
    {
        System.out.println("Creating pre-defined data....");
        // Create pre-defined data

        IRandom R = new MersenneTwister64(0);

        int interactionsLimit = 10; // the limit for the number of interactions
        int generationsPerPC = 100; // the number of generations per interaction

        Path path = FileUtils.getPathRelatedToClass(GeneratePCsData.class, "Projects", "src", File.separatorChar);
        String fp = path.toString() + File.separatorChar + "pcs.txt";
        // For JAR:
        //String fp = "pcs.txt";
        PCsDataContainer PCs = new PCsDataContainer(fp, 4, 3, 100, interactionsLimit);

        // Define no. cuts for DND method (objectives -> cuts).
        HashMap<Integer, Integer> dndCuts = new HashMap<>();
        dndCuts.put(2, 49);
        dndCuts.put(3, 13);
        dndCuts.put(4, 8);
        dndCuts.put(5, 6);
        System.out.print("DND cuts = ");
        System.out.println(dndCuts);

        // Define population size based on the no. cuts (objectives -> population size):
        HashMap<Integer, Integer> populationSizes = new HashMap<>();
        populationSizes.put(2, DasDennis.getNoProblems(2, 49));
        populationSizes.put(3, DasDennis.getNoProblems(3, 13));
        populationSizes.put(4, DasDennis.getNoProblems(4, 8));
        populationSizes.put(5, DasDennis.getNoProblems(5, 6));
        System.out.print("Population sizes = ");
        System.out.println(populationSizes);

        // Reference points container:
        ReferencePointsOnPareto RPs = ReferencePointsOnPareto.getDefault(new int[]{100, 1000, 10000, 100000}, 2, 6, R);

        // Problems with concave PFs
        String[] cps = new String[]{"DTLZ2", "DTLZ3", "DTLZ4", "WFG4", "WFG5", "WFG6", "WFG7", "WFG8", "WFG9"};
        Set<String> concaveFronts = new HashSet<>(Arrays.asList(cps));

        // GDC params container
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();

        pGDC._mainPath = "D:" + File.separator + "experiments" + File.separator + "ERS" + File.separator + "e4_interactive"; // my path
        pGDC._noTrials = 100; // no trials
        pGDC._noThreads = 15;
        pGDC._useMonitorThread = true; // monitor what is going on
        pGDC._monitorReportingInterval = 20000; // monitor every 10 s
        pGDC._scenarioKeys = new String[]{ // define scenario keys
                "SAMPLER", // the sampler used
                "PROBLEM",  // the number of objectives
                "OBJECTIVES",  // no. compatible samples to generate
        };

        pGDC._scenarioValues = new String[][]{
                {
                        "FRS", "ERS",
                        "FRS_NEMO0", "ERS_NEMO0",
                        "FRS_NEMOII", "ERS_NEMOII",
                },
                {
                        "DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7",
                        "WFG1ALPHA02", "WFG2", "WFG3", "WFG4", "WFG5", "WFG6", "WFG7", "WFG8", "WFG9",
                        "ZDT1", "ZDT2", "ZDT3", "ZDT4", "ZDT5", "ZDT6",
                },
                {
                        "2", "3", "4", "5"
                },
        };

        pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[]
                {
                        new ScenarioDisablingConditions("SAMPLER", "FRS"),
                        new ScenarioDisablingConditions("SAMPLER", "ERS"),

                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT1", "3"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT1", "4"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT1", "5"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT2", "3"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT2", "4"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT2", "5"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT3", "3"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT3", "4"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT3", "5"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT4", "3"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT4", "4"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT4", "5"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT5", "3"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT5", "4"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT5", "5"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT6", "3"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT6", "4"}),
                        new ScenarioDisablingConditions(new String[]{"PROBLEM", "OBJECTIVES"}, new String[]{"ZDT6", "5"}),
                };

        // Define the generations limits:
        GenerationsLimits GL = GenerationsLimits.getInstance(new GenerationsLimits.ProblemLimit[]{
                new GenerationsLimits.ProblemLimit("DTLZ1|DTLZ[5-7]|WFG[2-9]", 500, 100),
                new GenerationsLimits.ProblemLimit("DTLZ2", 250, 50),
                new GenerationsLimits.ProblemLimit("DTLZ3", 1250, 250),
                new GenerationsLimits.ProblemLimit("DTLZ4", 300, 100),
                new GenerationsLimits.ProblemLimit("WFG1ALPHA02", 1200, 200),
                new GenerationsLimits.ProblemLimit("ZDT[1-6]", 1000, 100),
        });

        pGDC._crossedSettings = new CrossedSetting[2];
        pGDC._crossedSettings[0] = new CrossedSetting(new String[]{
                "PROBLEM", "SAMPLER", "OBJECTIVES",},
                new String[][]{pGDC._scenarioValues[1], pGDC._scenarioValues[0], pGDC._scenarioValues[2]});
        pGDC._crossedSettings[1] = new CrossedSetting(new String[]{
                "OBJECTIVES", "PROBLEM", "SAMPLER",},
                new String[][]{pGDC._scenarioValues[2], pGDC._scenarioValues[1], pGDC._scenarioValues[0]});

        pGDC._referenceCrossSavers = new LinkedList<>();
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(3));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("SAMPLER", new ITest[]{
                TStudent.getPairedTest(true),
                new WilcoxonSignedRank()
        }, 3, 1.0E-5));

        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerXLSX());
        pGDC._referenceScenarioSavers.add(new TrialsResultsXLSX());
        pGDC._extraAllowedCharacters = new Character[]{'_', '.', '-'}; // add extra allowed characters for processing

        pGDC._RNGI = new DefaultRandomNumberGeneratorInitializer(MersenneTwister32::new);

        // Create global data container
        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // SDCF params container
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();
        pSDCF._statistics = new IStatistic[]{new MinIgnoreNegatives(), new MeanIgnoreNegatives(),
                new MaxIgnoreNegatives(), new StandardDeviationIgnoreNegatives()};

        // Define the generations limits:
        pSDCF._numberOfGenerationsInitializer = p -> {
            String problem = p._scenario.getProblem();
            int M = p._scenario.getObjectives();
            return GL.getLimit(problem, M) + (interactionsLimit * generationsPerPC);
        };

        // For IEMO/D (and MOEA/D): the number of steady-state repeats equals the population size
        pSDCF._numberOfSteadyStateRepeatsInitializer = new INumberOfSteadyStateRepeatsInitializer()
        {
            @Override
            public int instantiateSteadyStateRepeats(AbstractScenarioDataContainer.Params p) throws ScenarioException
            {
                if (p._scenario.getKeyValuesMap().get("SAMPLER").getValue().length() == 3)
                    return populationSizes.get(p._scenario.getObjectives());
                return 1;
            }
        };
        pSDCF._dataStoringInterval = 100;
        pSDCF._dataLoadingInterval = 100;

        // Define the performance indicators:
        pSDCF._indicatorsInitializer = p -> {

            IIndicator[] indicators = new IIndicator[7]; // 7 performance indicators
            Problem problem = AbstractMOOProblemBundle.getProblemFromString(p._scenario.getProblem()); // get problem ID
            int M = p._scenario.getObjectives(); // get the number of objectives

            // Get problem normalizations:
            INormalization[] normalizations = AbstractMOOProblemBundle.getNormalizations(problem, M); // derive proper normalizations

            // Best relevance score:
            indicators[0] = new PerformanceIndicator("VMQ_MIN", new ValueModelQuality<>(
                    new LNorm(new model.internals.value.scalarizing.LNorm(null, Double.POSITIVE_INFINITY, normalizations)),
                    new Min()), (scenario, trialID) -> new PerformanceIndicator("VMQ_MIN", new ValueModelQuality<>(
                    new LNorm(new model.internals.value.scalarizing.LNorm(
                            PCs._PCs[2][M - 2]._trialPCs[trialID]._dmW.clone(),
                            Double.POSITIVE_INFINITY, normalizations)), new Min())));

            // Average relevance score:
            indicators[1] = new PerformanceIndicator("VMQ_MEAN", new ValueModelQuality<>(
                    new LNorm(new model.internals.value.scalarizing.LNorm(null, Double.POSITIVE_INFINITY, normalizations)),
                    new Min()), (scenario, trialID) -> new PerformanceIndicator("VMQ_MEAN", new ValueModelQuality<>(
                    new LNorm(new model.internals.value.scalarizing.LNorm(
                            PCs._PCs[2][M - 2]._trialPCs[trialID]._dmW.clone(),
                            Double.POSITIVE_INFINITY, normalizations)), new Mean())));
            // HV:
            double[] rp = new double[M];
            Arrays.fill(rp, 1.1d);
            HV.Params pHV = new HV.Params(M, normalizations, rp);
            pHV._policyForNonDominating = HV.PolicyForNonDominating.IGNORE;
            pHV._presorting = true;
            pHV._deriveUniqueSpecimens = true;
            pHV._toleranceDuplicates = 1.0E-6;
            indicators[2] = new PerformanceIndicator(new HV(pHV));

            // Closest neighborhood distances:
            if (concaveFronts.contains(problem.toString())) // if simple concave, use exact calculations
                indicators[3] = new PerformanceIndicator(new GDConcaveSpherical(new Euclidean(normalizations), M));
            else
            {
                int max = 100;
                double[][] rps = RPs.getReferencePointsOnPF(problem.toString(), M);
                if (rps == null)
                    indicators[3] = new PerformanceIndicator(new GDFast(new double[][]{}, normalizations, 2, max));
                else
                {
                    if (rps.length <= 100) max = 50;
                    indicators[3] = new PerformanceIndicator(new GDFast(rps, normalizations, 2, max));
                }
            }
            indicators[4] = new PerformanceIndicator(new ExecutionTime());
            indicators[5] = new PerformanceIndicator(new HistorySize());
            indicators[6] = new PerformanceIndicator(new ReportedInconsistencies());

            return indicators;
        };

        // Create scenario data container factory
        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TDCF params container
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R1, p) -> {
            // Problem bundle should be available:
            if (p._problemBundle == null)
                throw new TrialException("Problem is unavailable", null, ContainersGetter.class, p._SDC.getScenario(), p._trialID);
            if (!(p._problemBundle instanceof AbstractMOOProblemBundle problemBundle))
                throw new TrialException("Problem is not MOO", null, ContainersGetter.class, p._SDC.getScenario(), p._trialID);

            String name = p._SDC.getScenario().getKeyValuesMap().get("SAMPLER").getValue();
            int M = p._SDC.getScenario().getObjectives();
            int PS = populationSizes.get(M);

            // Construct initial models:
            IConstructor<model.internals.value.scalarizing.LNorm> constructor;
            IRandomModel<model.internals.value.scalarizing.LNorm> RM = new LNormGenerator(M, Double.POSITIVE_INFINITY,
                    problemBundle._normalizations);
            model.internals.value.scalarizing.LNorm[] initialModels = new model.internals.value.scalarizing.LNorm[PS];
            ArrayList<double[]> initialWeights = DasDennis.getWeightVectors(M, dndCuts.get(M));
            for (int i = 0; i < initialWeights.size(); i++)
                initialModels[i] = new model.internals.value.scalarizing.LNorm(initialWeights.get(i),
                        Double.POSITIVE_INFINITY, problemBundle._normalizations);

            String sampler = name.substring(0, 3);
            boolean iemod = false;
            boolean nemo0 = false;
            boolean nemoii = false;
            if (name.length() == 3) iemod = true;
            else
            {
                if (name.substring(4).equals("NEMO0")) nemo0 = true;
                else if (name.substring(4).equals("NEMOII")) nemoii = true;
            }

            if (sampler.equals("FRS"))
            {
                FRS.Params<model.internals.value.scalarizing.LNorm> pFRS = new FRS.Params<>(RM);
                pFRS._initialModels = initialModels;
                pFRS._feasibleSamplesToGenerate = PS;
                pFRS._inconsistencyThreshold = PS - 1;
                pFRS._samplingLimit = 1000000000; // 10^9
                constructor = new FRS<>(pFRS);
            }
            else
            {
                constructor = ERSFactory.getDefaultForLNorms(PS,
                        new Constant(50000),
                        M, Double.POSITIVE_INFINITY, problemBundle._normalizations,
                        new LNormOnSimplex(Double.POSITIVE_INFINITY, 0.2d, 0.2d / (2.0d * (M - 1))),
                        new Tournament<>(2), initialModels);
            }

            int startingGen = GL.getLimit(p._SDC.getScenario().getProblem(), M);
            IRule rule = new IterationInterval(startingGen, generationsPerPC, interactionsLimit);
            IPreferenceModel<model.internals.value.scalarizing.LNorm> preferenceModel = new LNorm();
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
                    PCsDataContainer.TrialPCs trialPCs = PCs._PCs[2][M - 2]._trialPCs[p._trialID];
                    LinkedList<ReferenceSet> referenceSets = new LinkedList<>();
                    double[] e1 = trialPCs._referenceEvaluations[_counter][0].clone();
                    double[] e2 = trialPCs._referenceEvaluations[_counter][1].clone();

                    // do rescaling:
                    for (int m = 0; m < M; m++)
                    {
                        double lb = problemBundle._paretoFrontBounds[m].getLeft();
                        double rb = problemBundle._paretoFrontBounds[m].getRight();

                        e1[m] = lb + e1[m] * (rb - lb);
                        e2[m] = lb + e2[m] * (rb - lb);
                    }

                    referenceSets.add(new ReferenceSet(new Alternative("A1_" + _counter, e1),
                            new Alternative("A2_" + _counter, e2)));
                    if (_counter < interactionsLimit) _counter++; // for safety
                    return referenceSets;
                }
            };

            IDMFeedbackProvider iDM = new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(
                    PCs._PCs[2][M - 2]._trialPCs[p._trialID]._dmW.clone(),
                    Double.POSITIVE_INFINITY, problemBundle._normalizations)));

            IEA ea = null;
            if (iemod)
            {
                ea = IEMOD.getIEMOD(0, false, false,
                        R1, GoalsFactory.getLNormsDND(M, dndCuts.get(M), Double.POSITIVE_INFINITY, problemBundle._normalizations),
                        problemBundle, new emo.utils.decomposition.similarity.lnorm.Euclidean(), 10,
                        rule, rsc, iDM, preferenceModel, constructor, null,
                        p1 -> { // disable filters (do not matter, the reference pairs are not taken from the population)
                            Refiner.Params pR = new Refiner.Params();
                            pR._terminationFilters = new LinkedList<>();
                            pR._reductionFilters = new LinkedList<>();
                            p1._refiner = new Refiner(pR);
                        });
            }
            else if (nemo0)
            {
                ea = NEMO0.getNEMO0(0, populationSizes.get(M), false, false,
                        R1, problemBundle, new selection.Tournament(2),
                        problemBundle._construct, problemBundle._evaluate, problemBundle._reproduce,
                        rule, rsc, iDM, preferenceModel,
                        new RepresentativeModel<>(constructor, new MDVF<>()),
                        null,
                        p1 -> { // disable filters (do not matter, the reference pairs are not taken from the population)
                            Refiner.Params pR = new Refiner.Params();
                            pR._terminationFilters = new LinkedList<>();
                            pR._reductionFilters = new LinkedList<>();
                            p1._refiner = new Refiner(pR);
                        },
                        null, null);
            }
            else if (nemoii)
            {
                ea = NEMOII.getNEMOII(0, populationSizes.get(M), false, false,
                        R1, problemBundle, new selection.Tournament(2),
                        problemBundle._construct, problemBundle._evaluate,
                        problemBundle._reproduce, rule, rsc, iDM, preferenceModel, constructor,
                        null,
                        p1 -> { // disable filters (do not matter, the reference pairs are not taken from the population)
                            Refiner.Params pR = new Refiner.Params();
                            pR._terminationFilters = new LinkedList<>();
                            pR._reductionFilters = new LinkedList<>();
                            p1._refiner = new Refiner(pR);
                        }, null, null);
            }

            return ea;
        };

        // Create trial data container factory
        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);

        return new Containers(GDC, SDCF, TDCF);
    }
}
