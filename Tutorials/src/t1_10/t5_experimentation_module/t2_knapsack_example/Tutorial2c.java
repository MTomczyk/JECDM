package t1_10.t5_experimentation_module.t2_knapsack_example;

import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import container.trial.initialziers.DefaultRunnerInitializer;
import executor.CrossSummarizer;
import indicator.FirstSpecimenEvaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import io.cross.excel.*;
import random.IRandom;
import random.MersenneTwister64;
import scenario.CrossedSetting;
import space.IntRange;
import space.Range;
import statistics.*;
import statistics.tests.ITest;
import statistics.tests.TStudent;
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
public class Tutorial2c
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

        // Default folder for crossed results:
        //pGDC._crossedFolderName = "CROSSED_RESULTS";

        pGDC._crossedSettings = new CrossedSetting[2];
        // keys/values will be standardized
        //pGDC._crossedSettings[0] = new CrossedSetting(
        //        new String[]{"algorithm"},
        //        new String[][]{{"PENALTY", "REPAIR"}}); // level = 1, NOTE: level 1 is avoided, it produces
        //        large number of result files
        pGDC._crossedSettings[0] = new CrossedSetting(
                new String[]{"GENERATIONS", "algorithm"},
                new String[][]{{"25", "50", "100"}, {"PENALTY", "REPAIR"}}); // level = 2
        pGDC._crossedSettings[1] = new CrossedSetting(
                new String[]{"GENERATIONS", "PS", "algorithm"},
                new String[][]{{"25", "50", "100"}, {"50", "100", "200"}, {"PENALTY", "REPAIR"}}); // level = 3

        pGDC._referenceCrossSavers = new LinkedList<>();
        // Convergence:
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(1));
        //pGDC._referenceCrossSavers.add(new ConvergenceXLS(1));
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(2));
        //pGDC._referenceCrossSavers.add(new ConvergenceXLS(2));
        pGDC._referenceCrossSavers.add(new ConvergenceXLSX(3));
        //pGDC._referenceCrossSavers.add(new ConvergenceXLS(3));

        // Final statistics:
        //pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(1)); // does not work for lv = 1
        //pGDC._referenceCrossSavers.add(new FinalStatisticsXLS(1)); // does not work for lv = 1
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(2));
        //pGDC._referenceCrossSavers.add(new FinalStatisticsXLS(2));
        pGDC._referenceCrossSavers.add(new FinalStatisticsXLSX(3));
        //pGDC._referenceCrossSavers.add(new FinalStatisticsXLS(3));

        // Final ranker:
        //pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALGORITHM", 1));  // does not work for lv = 1
        //pGDC._referenceCrossSavers.add(new FinalRankerXLS("ALGORITHM", 1)); // does not work for lv = 1
        //pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALGORITHM", 2));
        // Includes T-test (two-sided):
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALGORITHM",
                new ITest[]{TStudent.getPairedTest(true)}, 2));
        //pGDC._referenceCrossSavers.add(new FinalRankerXLS("ALGORITHM",
        //        new ITest[]{TStudent.getPairedTest(true)}, 2));
        pGDC._referenceCrossSavers.add(new FinalRankerXLSX("ALGORITHM",
                new ITest[]{TStudent.getPairedTest(true)}, 3));
        //pGDC._referenceCrossSavers.add(new FinalRankerXLS("ALGORITHM", 3));

        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // SCENARIO DATA CONTAINER =======================================================================================
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();

        pSDCF._numberOfGenerationsInitializer = p -> Integer.parseInt(p._scenario.getKeyValuesMap().get("GENERATIONS").getValue());
        pSDCF._steadyStateRepeats = 1; // will assume a constant value (= default value)

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
        pSDCF._dataLoadingInterval = 1000;

        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TRIAL DATA CONTAINER =======================================================================================
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();

        // Creating EA should be conditional:
        pTDCF._eaInitializer = (R1, p) -> {
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

        // CROSS SUMMARIZER =======================================================================================

        // Create params container (pass global/scenario/trial containers):
        CrossSummarizer.Params pS = new CrossSummarizer.Params();
        pS._GDC = GDC;
        pS._SDCF = SDCF;
        pS._TDCF = TDCF;

        // Create the executor:
        CrossSummarizer scenariosSummarizer = new CrossSummarizer(pS);
        Summary summary = scenariosSummarizer.execute();

        // Print summary (general + without scenarios summaries (false flag)):
        System.out.println(summary.getStringRepresentation(false));
    }
}
