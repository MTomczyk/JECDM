package y2025.ERS.e3_samplers;

import compatibility.CompatibilityAnalyzer;
import container.Containers;
import container.global.GlobalDataContainer;
import container.global.initializers.DefaultRandomNumberGeneratorInitializer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import decisionsupport.operators.LNormOnSimplex;
import exception.TrialException;
import exeption.PreferenceModelException;
import history.PreferenceInformationWrapper;
import indicator.ExecutionTime;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.FileUtils;
import io.cross.excel.FinalRankerXLSX;
import io.cross.excel.FinalStatisticsXLSX;
import io.scenario.excel.SummarizerXLSX;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.AbstractRejectionSampling;
import model.constructor.value.rs.ers.IterableERS;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.constructor.value.rs.ers.evolutionary.EvolutionaryModelConstructor;
import model.constructor.value.rs.ers.evolutionary.IOffspringConstructor;
import model.constructor.value.rs.ers.evolutionary.Tournament;
import model.constructor.value.rs.frs.IterableFRS;
import model.constructor.value.rs.iterationslimit.Constant;
import model.internals.value.scalarizing.LNorm;
import model.similarity.lnorm.Euclidean;
import random.MersenneTwister32;
import scenario.CrossedSetting;
import statistics.*;
import statistics.tests.ITest;
import statistics.tests.NoTimesNonNegative;
import statistics.tests.TStudent;
import y2025.ERS.common.*;
import y2025.ERS.common.indicators.*;
import y2025.ERS.e1_auxiliary.GeneratePCsData;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Provides the data containers for this experiment.
 *
 * @author MTomczyk
 */
@SuppressWarnings("DuplicatedCode")
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
     * @throws Exception exception can be thrown and propagated higher
     */
    public static Containers getContainers() throws Exception
    {
        System.out.println("Creating pre-defined data....");
        // Create pre-defined data
        double[] alphas = new double[]{1.0d, 5.0d, Double.POSITIVE_INFINITY}; // DM's alpha settings
        int[] objectives = new int[]{2, 3, 4, 5}; // the numbers of objectives considered
        int[] pcs = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // the numbers of pairwise comparisons considered (must increase monotonically)
        int iterationsPerGenerations = 100;
        int generations = 10000;

        Path path = FileUtils.getPathRelatedToClass(GeneratePCsData.class, "Projects", "src", File.separatorChar);
        String fp = path.toString() + File.separatorChar + "pcs.txt";
        // For JAR:
        //String fp = "pcs.txt";
        PCsDataContainer PCs = new PCsDataContainer(fp, 4, 3, 100, 10);

        HashMap<Double, Integer> mapAlphaIndex = Common.getMapAlphaIndex();
        HashMap<Integer, Integer> mapObjectivesIndex = Common.getMapObjectivesIndex();

        // GDC params container
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();

        pGDC._mainPath = "D:" + File.separator + "experiments" + File.separator + "ERS" + File.separator + "e3_samplers"; // my path
        pGDC._noTrials = 100; // no trials
        pGDC._noThreads = 15;
        pGDC._useMonitorThread = true; // monitor what is going on
        pGDC._monitorReportingInterval = 30000; // 10000; // monitor every 30 s
        pGDC._scenarioKeys = new String[]{ // define scenario keys
                "SAMPLER", // the sampler used
                "DM", // DM's model used
                "M",  // the number of objectives
                "N",  // no. compatible samples to generate
                "PCS", // the number of pairwise comparisons
        };

        pGDC._scenarioValues = new String[][]{
                {
                        "FRS",
                        "ERS_2_2", // different mutation/crossover powers

                },
                null, // copied from the arrays (see below)
                null,  // copied from the arrays (see below)
                {"50", "100", "150", "200"},
                null // copied from the arrays (see below)
        };

        pGDC._scenarioValues[1] = new String[alphas.length];
        for (int i = 0; i < alphas.length; i++) pGDC._scenarioValues[1][i] = String.valueOf(alphas[i]);
        pGDC._scenarioValues[2] = new String[objectives.length];
        for (int i = 0; i < objectives.length; i++) pGDC._scenarioValues[2][i] = String.valueOf(objectives[i]);
        pGDC._scenarioValues[4] = new String[pcs.length];
        for (int i = 0; i < pcs.length; i++) pGDC._scenarioValues[4][i] = String.valueOf(pcs[i]);

        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerXLSX());
        pGDC._extraAllowedCharacters = new Character[]{'_', '.', '-'}; // add extra allowed characters for processing

        pGDC._crossedSettings = new CrossedSetting[1];
        pGDC._crossedSettings[0] = new CrossedSetting(new String[]{
                "SAMPLER", "N", "M", "PCS",},
                new String[][]{pGDC._scenarioValues[0],
                        pGDC._scenarioValues[3], pGDC._scenarioValues[2], pGDC._scenarioValues[4]});

        pGDC._referenceCrossSavers = new LinkedList<>();
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(4));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("SAMPLER", new ITest[]{
                TStudent.getPairedTest(true),
        }, 4, 1.0E-5));

        pGDC._RNGI = new DefaultRandomNumberGeneratorInitializer(MersenneTwister32::new);

        // Create global data container
        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // SDCF params container
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();
        pSDCF._statistics = new IStatistic[]
                {
                        new MinIgnoreNegatives(),
                        new MeanIgnoreNegatives(),
                        new MaxIgnoreNegatives(),
                        new StandardDeviationIgnoreNegatives(),
                        new NoTimesNonNegative()
                };

        pSDCF._generations = generations; // constant = one sampler run
        pSDCF._dataStoringInterval = 100; // store every 100 generations
        pSDCF._dataLoadingInterval = 100; // store every 100 generations

        pSDCF._indicators = new IIndicator[7];
        pSDCF._indicators[0] = new PerformanceIndicator(new ExecutionTime()); // measure execution time
        pSDCF._indicators[1] = new SuccessRate(); // measure success rate
        pSDCF._indicators[2] = new CompatibleModels(); // measure compatible models
        pSDCF._indicators[3] = new ModelClosestNeighborDistance(new Min(), false); // measure min of closest neighbor distances
        pSDCF._indicators[4] = new ModelClosestNeighborDistance(new StandardDeviation(), true); // measure std of closest neighbor distances
        pSDCF._indicators[5] = new RequiredCompatibleModelsFoundInTime(); // measure time needed to find required no. compatible models
        pSDCF._indicators[6] = new RequiredCompatibleModelsFoundInGeneration();  // measure generation needed to find required no. compatible models

        // Create scenario data container factory
        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TDCF params container
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();
        pTDCF._eaInitializer = (R1, p) -> {

            String name = p._SDC.getScenario().getKeyValuesMap().get("SAMPLER").getValue();
            String sDM = p._SDC.getScenario().getKeyValuesMap().get("DM").getValue();
            String sM = p._SDC.getScenario().getKeyValuesMap().get("M").getValue();
            String sN = p._SDC.getScenario().getKeyValuesMap().get("N").getValue();
            String sPCS = p._SDC.getScenario().getKeyValuesMap().get("PCS").getValue();

            double alpha;
            // need to check manually as the keys are automatically transformed into upper case
            if (sDM.equals("INFINITY")) alpha = Double.POSITIVE_INFINITY;
            else alpha = Double.parseDouble(sDM);

            int M = Integer.parseInt(sM);
            int N = Integer.parseInt(sN);
            int PCS = Integer.parseInt(sPCS);
            PCsDataContainer.TrialPCs trialPCs = PCs._PCs[mapAlphaIndex.get(alpha)][
                    mapObjectivesIndex.get(M)]._trialPCs[p._trialID];

            LinkedList<PreferenceInformationWrapper> pi;

            try
            {
                pi = Common.getPreferenceInformation(trialPCs, PCS);
            } catch (PreferenceModelException e)
            {
                throw new TrialException("Could not create preference information (" + e.getMessage() + ")", null,
                        (Class<?>) null, p._SDC.getScenario(), p._trialID);
            }

            LNorm[] initialModels = Common.getInitialRandomModels(N, M, alpha, R1);

            int samplingLimit = p._SDC.getGenerations();

            AbstractRejectionSampling<LNorm> sampler;

            if (name.equals("FRS"))
            {
                IterableFRS.Params<LNorm> pFRS = new IterableFRS.Params<>(new LNormGenerator(M, alpha));
                pFRS._samplingLimit = samplingLimit;
                pFRS._feasibleSamplesToGenerate = N;
                pFRS._inconsistencyThreshold = 0;
                pFRS._compatibilityAnalyzer = new CompatibilityAnalyzer();
                pFRS._validateAlreadyExistingSamplesFirst = true;
                pFRS._initialModels = initialModels;
                sampler = new IterableFRS<>(pFRS);
            }
            else
            {
                String[] s = name.split("_");
                double crossoverStd = Double.parseDouble(s[1]) / 10.0d;
                double mutationStd = Double.parseDouble(s[2]) / 10.0d;

                IterableERS.Params<LNorm> pERS = new IterableERS.Params<>(new LNormGenerator(M, alpha));
                pERS._similarity = new Euclidean();
                pERS._feasibleSamplesToGenerate = N;
                pERS._kMostSimilarNeighbors = 3;
                pERS._iterationsLimit = new Constant(samplingLimit);
                pERS._comparator = new MostSimilarWithTieResolving<>();
                pERS._inconsistencyThreshold = 0;
                pERS._compatibilityAnalyzer = new CompatibilityAnalyzer();
                IOffspringConstructor<LNorm> offspringConstructor = new LNormOnSimplex(alpha, crossoverStd, mutationStd);
                pERS._EMC = new EvolutionaryModelConstructor<>(offspringConstructor, new Tournament<>(2));
                pERS._initialModels = initialModels;
                sampler = new IterableERS<>(pERS);
            }

            return new EAWrapperIterableSampler<>(name, sampler, pi, R1, iterationsPerGenerations);
        };

        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);

        return new Containers(GDC, SDCF, TDCF);
    }
}
