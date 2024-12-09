package t1_10.t5_experimentation_module.t2_knapsack_example;

import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import container.trial.initialziers.DefaultRunnerInitializer;
import executor.ScenariosSummarizer;
import indicator.FirstSpecimenEvaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.scenario.SummarizerCSV;
import io.scenario.excel.SummarizerXLSX;
import random.IRandom;
import random.MersenneTwister64;
import space.IntRange;
import space.Range;
import statistics.*;
import summary.Summary;
import t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea.Data;
import t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea.Utils;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This tutorial showcases how to create a global data container
 *
 * @author MTomczyk
 */
public class Tutorial2b
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
        int[] items = new int[]{50, 100, 200};
        HashMap<Integer, Data> dataMap = new HashMap<>(items.length);
        for (Integer it : items)
            dataMap.put(it, Data.getInstance(it, R, new Range(5.0d, 10.0d), new IntRange(2, 5)));


        // GLOBAL DATA CONTAINER =======================================================================================
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();
        pGDC._mainPath = "E:\\knapsack_test";
        pGDC._noTrials = 100;
        pGDC._noThreads = 20;
        pGDC._scenarioKeys = new String[]{"ALGORITHM", "ITEMS", "CAPACITY", "GENERATIONS", "PS"};
        pGDC._scenarioValues = new String[][]{
                {"Penalty", "Repair"}, // 0
                new String[items.length], // 1 (will be supplied next; see lines below)
                {"50", "100", "200"}, // 2
                {"25", "50", "100"}, // 3
                {"50", "100", "200"} // 4
        };
        for (int i = 0; i < items.length; i++) pGDC._scenarioValues[1][i] = String.valueOf(items[i]);
        pGDC._monitorReportingInterval = 1000;

        pGDC._mapObject = new HashMap<>();
        pGDC._mapObject.put("DATA", dataMap);

        // The ``savers'' are responsible for creating per-scenario result files. The object used below will provide the
        // aggregated results (obtained through statistic functions) in an Excel file, where the columns will represent
        // different statistics, while rows will be associated with generations. Further, results obtained according
        // to different indicators will be presented in different tabs.
        pGDC._referenceScenarioSavers = new LinkedList<>();
        pGDC._referenceScenarioSavers.add(new SummarizerXLSX());
        pGDC._referenceScenarioSavers.add(new SummarizerCSV(';'));

        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // SCENARIO DATA CONTAINER =======================================================================================
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();

        pSDCF._numberOfGenerationsInitializer = p -> Integer.parseInt(p._scenario.getKeyValuesMap().get("GENERATIONS").getValue());
        pSDCF._steadyStateRepeats = 1;

        pSDCF._indicators = new IIndicator[2];
        pSDCF._indicators[0] = new PerformanceIndicator(new FirstSpecimenEvaluation(0, false));
        pSDCF._indicators[1] = new PerformanceIndicator(new FirstSpecimenEvaluation(1, true));

        // Statistic functions used to summarize trial outcomes:
        pSDCF._statistics = new IStatistic[4];
        pSDCF._statistics[0] = new Min();
        pSDCF._statistics[1] = new Mean();
        pSDCF._statistics[2] = new Max();
        pSDCF._statistics[3] = new StandardDeviation();
        pSDCF._statisticFunctionsInitializer = null;

        // Data loading interval:
        pSDCF._dataLoadingInterval = 1000;

        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TRIAL DATA CONTAINER =======================================================================================
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();

        // Creating EA should be conditional:
        pTDCF._eaInitializer = (R1, p) -> {
            // upper cases due to standardization
            int items1 = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("ITEMS").getValue());
            int capacity = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("CAPACITY").getValue());
            int populationSize = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("PS").getValue());
            boolean repair = p._SDC.getScenario().getKeyValuesMap().get("ALGORITHM").getValue().equals("REPAIR");
            @SuppressWarnings("unchecked") // unchecked cast due to type erasure
            HashMap<Integer, Data> dataMap1 = (HashMap<Integer, Data>) p._SDC.getGDC().getObject("DATA");
            Data data = dataMap1.get(items1);
            return Utils.getKnapsackEA(populationSize, data, capacity, repair, R1);
        };

        pTDCF._runnerInitializer = new DefaultRunnerInitializer();
        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);

        // SCENARIOS SUMMARIZER EXECUTOR =======================================================================================

        // Create params container (pass global/scenario/trial containers):
        ScenariosSummarizer.Params pS = new ScenariosSummarizer.Params();
        pS._GDC = GDC;
        pS._SDCF = SDCF;
        pS._TDCF = TDCF;

        // Create the executor:
        ScenariosSummarizer scenariosSummarizer = new ScenariosSummarizer(pS);
        Summary summary = scenariosSummarizer.execute();

        // Print summary (general + without scenarios summaries (false flag)):
        System.out.println(summary.getStringRepresentation(false));
    }
}
