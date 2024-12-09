package t1_10.t5_experimentation_module.t2_knapsack_example;

import container.global.GlobalDataContainer;
import container.scenario.ScenarioDataContainerFactory;
import container.trial.TrialDataContainerFactory;
import executor.ExperimentPerformer;
import indicator.FirstSpecimenEvaluation;
import indicator.IIndicator;
import indicator.PerformanceIndicator;
import random.IRandom;
import random.MersenneTwister64;
import space.IntRange;
import space.Range;
import summary.Summary;
import t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea.Data;
import t1_10.t5_experimentation_module.t2_knapsack_example.knapsackea.Utils;

import java.util.HashMap;

/**
 * This tutorial showcases how to design a simple experiment (investigate the performance of an evolutionary
 * algorithm for solving a knapsack problem given different scenario specifications).
 *
 * @author MTomczyk
 */
@SuppressWarnings("CommentedOutCode")
public class Tutorial2a
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Random number generator (common to all objects):
        IRandom R = new MersenneTwister64(0);
        // No. items considered
        int[] items = new int[]{50, 100, 200};
        // Map that will store data objects and will be shared across all scenarios
        HashMap<Integer, Data> dataMap = new HashMap<>(items.length);
        for (Integer it : items)
            dataMap.put(it, Data.getInstance(it, R, new Range(5.0d, 10.0d), new IntRange(2, 5)));
        // Items (values) viewed as string (will be required when defining scenarios)
        String [] sItems = new String[items.length];
        for (int i = 0; i < items.length; i++) sItems[i] = String.valueOf(items[i]);


        // GLOBAL DATA CONTAINER =======================================================================================

        // Create the params container:
        GlobalDataContainer.Params pGDC = new GlobalDataContainer.Params();

        // Prepare random number generator constructor (it is recommended to leave it as it is - use the default constructor).
        // pGDC._RNGI = new DefaultRandomNumberGeneratorInitializer();

        // Specify the main path for the folder where the results will be stored (IMPORTANT: adjust to your needs):
        pGDC._mainPath = "E:\\knapsack_test";
        // No. trials to be performed for each scenario (IDs = 0, 1,...,99)
        pGDC._noTrials = 100;
        // Split processing into threads (Important note: use value that is similar to your no. physical cores,
        // and gives no. trials % no. threads == 0)
        pGDC._noThreads = 20;

        // Defined scenario keys (labels/definitions); note that the key/values will be standardized (check the JavaDoc);
        // here, they will be transformed into upper cases:
        pGDC._scenarioKeys = new String[]{
                "Algorithm", // 0
                "Items",  // 1
                "Capacity", // 2
                "Generations", // 3
                "pS"}; // 4 (population size)

        // Define scenario values (1:1 mapping with keys):
        pGDC._scenarioValues = new String[][]{
                {"Penalty", "Repair"}, // 0
                sItems, // 1
                {"50", "100", "200"}, // 2
                {"25", "50", "100"}, // 3
                {"50", "100", "200"} // 4
        };

        // The keys and values specified above will produce in total 2 * 2 * 2 * 4 * 4 = 128 scenarios:
        // [["ALGORITHM":"PENALTY"], ["ITEMS":"50"], ["CAPACITY":"50"], ["GENERATIONS":"25"], ["PS":"50"]],
        // [["ALGORITHM":"PENALTY"], ["ITEMS":"50"], ["CAPACITY":"50"], ["GENERATIONS":"25"], ["PS":"100"]],
        // ...

        // Scenario disabling conditions, however, can be specified to deactivate some scenarios from the above Cartesian
        // product. A scenario will be turned off if it is recommended by at least one condition. A condition comprises
        // pairs of key values, and it favors turning off a scenario if its key values match the scenario's. Not all key
        // values must be used in a condition. The below condition would disable all scenarios with:
        // - "ITEMS":"10", "CAPACITY":"50",
        //pGDC._scenarioDisablingConditions = new ScenarioDisablingConditions[1];
        // The key-values will be correctly matched due to standardization.
        //pGDC._scenarioDisablingConditions[0] = new ScenarioDisablingConditions(new String[]{"itEMS", "CapaCITY"},
        //new String[]{"10", "50"});

        // Associative arrays that can be used to store some custom data can be stored in the following way
        // (the maps for a given level are accessible from the same level and all levels below; thus, the map below
        // will be accessible at the global, scenario, and trial level). Let's store the pre-instantiated
        // data objects (are considered read only -- but they are not immutable), so that they will be already accessible:
        pGDC._mapObject = new HashMap<>();
        pGDC._mapObject.put("DATA", dataMap); // there is also map for double, integer, and string

        // Monitor thread = reports the progress on executing a scenario. The report will be printed into the console
        // after each provided ms (reporting interval).
        pGDC._useMonitorThread = true; // turn on (true by default)
        pGDC._monitorReportingInterval = 1000; // set to 1000 ms

        // Create object instance (we will not use it in this example; the container is utilized by the ExperimentExecutor class):
        GlobalDataContainer GDC = new GlobalDataContainer(pGDC);

        // GDC.storeDouble("DOUBLE_FOR_GLOBAL", 5.0d); // auxiliary data can also be stored in this way

        // SCENARIO DATA CONTAINER =======================================================================================
        ScenarioDataContainerFactory.Params pSDCF = new ScenarioDataContainerFactory.Params();

        // Dummy map accessible at scenario and trial level:
        pSDCF._mapInteger = new HashMap<>();
        pSDCF._mapInteger.put("INT_FOR_SCENARIO", 10);

        // Using the below line will force all scenarios to consider the limit for the number of generations = 100.
        // pSDCF._generations = 100;
        // However, the module lets you change the parameters conditionally if the (condition = scenario's key/values).
        // For this purpose, you can implement a dedicated interface consisting of one method (lambda method can
        // be used; if there is no desired dedicated method, you can extend the default factory class to customize it).
        // For instance, the below line will set the number of generations as provided via the scenario's key value.
        // Note that the input is the scenario's params container filled by the executor and passed to the factory in
        // order to create the scenario data container instance (dedicated to a specific scenario setting):
        pSDCF._numberOfGenerationsInitializer = p -> {
            // p = scenario's params container filled by the experiment executor
            // _scenario: scenario object (provides data on the scenario being processed; particularly on key-values)
            // keyValuesMap = allows retrieving current scenario value for a specified key (note that using keyValues
            // is faster, but may be prone to errors due to the fact that GDC may alter the orders in which keys are stored).
            // getValue() = Value object; next getValue gives its string representation
            return Integer.parseInt(p._scenario.getKeyValuesMap().get("GENERATIONS").getValue());
            // Shortcut can also be used for "GENERATIONS":
            //return p._scenario.getGenerations();
        };

        // Similar pattern exists for the expected number of steady state repeats:
        pSDCF._steadyStateRepeats = 1; // will assume a constant value (= default value)
        // pSDCF._numberOfSteadyStateRepeatsInitializer = null;// you can implement this initializer to customize the indications

        // Let's use two indicators (unconditionally). Indicator should return a performance value for an algorithm
        // in specific iteration (and trial run). The first indicator will return the 0-th performance value
        // associated with the first population member (assumed to be the best one; 0-th element = value). The second
        // indicator concerns 1-th element (size).
        pSDCF._indicators = new IIndicator[2];
        pSDCF._indicators[0] = new PerformanceIndicator(new FirstSpecimenEvaluation(0, false)); // 0-th element; less is preferred = false
        pSDCF._indicators[1] = new PerformanceIndicator(new FirstSpecimenEvaluation(1, true)); // 1-th element; less is preferred = true
        // pSDCF._indicatorsInitializer = null; performance indicators can also be defined conditionally

        // The field below can be adjusted to balance memory consumption and hard drive input operations (default value
        // = Integer.MAX_VALUE). Generally, the EA's assessment (per trial) is stored in a matrix (rows = generations X
        // columns = indicators). For the default setting, the assessment is completed when the algorithm finishes
        // processing and only after the data is moved from the memory into binary files. However, the field below
        // steers the maximum number of rows in the results matrix. During the algorithm execution, if the allowed
        // capacity is overflowed, the data is flushed to the files, and the matrix is cleared. Overall, the smaller
        // the interval, the lesser the memory consumption, but the disc input operations are more frequently executed.
         pSDCF._dataStoringInterval = 1000; // more than for any scenario (so does not affect)

        // Create the factory:
        ScenarioDataContainerFactory SDCF = new ScenarioDataContainerFactory(pSDCF);

        // TRIAL DATA CONTAINER =======================================================================================
        TrialDataContainerFactory.Params pTDCF = new TrialDataContainerFactory.Params();

        // Dummy map accessible at trial level:
        pTDCF._mapString = new HashMap<>();
        pTDCF._mapString.put("STR_FOR_TRIAL", "string value");

        // Creating EA should be conditional:
        pTDCF._eaInitializer = (R1, p) -> {
            // Retrieve parameterization:
            // You need to use p._SDC.getScenario() -- access via scenario data container
            // upper case due to standardization
            int it = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("ITEMS").getValue());
            int capacity = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("CAPACITY").getValue());
            int populationSize = Integer.parseInt(p._SDC.getScenario().getKeyValuesMap().get("PS").getValue());
            boolean repair = p._SDC.getScenario().getKeyValuesMap().get("ALGORITHM").getValue().equals("REPAIR");

            // Data map is accessible from here, so the below line would work:
            // Data data = dataMap.get(10);
            // This line shows how to retrieve the map from GDC:

            @SuppressWarnings("unchecked") // unchecked cast due to type erasure
            HashMap<Integer, Data> dm = (HashMap<Integer, Data>) p._SDC.getGDC().getObject("DATA");
            Data data = dm.get(it);

            // You can access the values stored in maps:
            //System.out.println(p._SDC.getInteger("INT_FOR_SCENARIO")); // returns 10 // scenario level
            //System.out.println(p._mapString.get("STR_FOR_TRIAL")); // returns "string value" // trial level

            return Utils.getKnapsackEA(populationSize, data, capacity, repair, R1);
        };

        // Default runner initializer is recommended. It:
        // - Disables visualization
        // - Uses the number of steady-state repeats as imposed the scenario data container
        // The default initializer is already set, so the line below has no effect:
        /// pTDCF._runnerInitializer = new DefaultRunnerInitializer();

        // Create the factory:
        TrialDataContainerFactory TDCF = new TrialDataContainerFactory(pTDCF);

        // EXPERIMENT EXECUTOR =======================================================================================

        // Create params container (pass global/scenario/trial containers):
        ExperimentPerformer.Params pEE = new ExperimentPerformer.Params();
        pEE._GDC = GDC;
        pEE._SDCF = SDCF;
        pEE._TDCF = TDCF;
        //pEE._notify = false; //use can disable notifications

        // Create the executor:
        ExperimentPerformer EE = new ExperimentPerformer(pEE);

        // Execute the experiment (retrieve summary):
        Summary summary = EE.execute();
        //EE.execute(null); // you can also pass arguments (e.g., via the console).

        // Print summary (general + without scenarios summaries (false flag)):
        System.out.println(summary.getStringRepresentation(false));
    }
}
