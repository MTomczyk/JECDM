package io.cross.excel.utils;

import scenario.KeyValues;
import scenario.Scenario;
import scenario.Value;

import java.util.HashMap;

/**
 * Auxiliary class aiding {@link io.cross.excel.FinalRankerXLS} and {@link io.cross.excel.FinalStatisticsXLSX} when
 * calculating final ranks.
 *
 * @author MTomczyk
 */
public class FinalRankerDataCollector
{

    /**
     * Collects raw data (trial results) for a scenario.
     */
    public static class RawData
    {
        /**
         * Reference scenario.
         */
        public final Scenario _scenario;

        /**
         * Scenario index.
         */
        public final int _scenarioIndex;

        /**
         * Trial results.
         */
        public final double[] _trialResults;

        /**
         * Parameterized constructor.
         *
         * @param scenario      reference scenario
         * @param scenarioIndex scenarioIndex
         * @param trialResults  trial results
         */
        public RawData(Scenario scenario, int scenarioIndex, double[] trialResults)
        {
            _scenario = scenario;
            _scenarioIndex = scenarioIndex;
            _trialResults = trialResults;
        }
    }

    /**
     * Processing result.
     */
    public static class Result
    {
        /**
         * Sub-scenario definition (excludes the selected key).
         */
        public final Scenario _subScenario;

        /**
         * Stored trial results.
         */
        public final double[][] _trialResults;

        /**
         * Parameterized constructor.
         *
         * @param subScenario      sub-scenario definition (excludes the selected key)
         * @param noSelectedValues no of values for the selected key
         */
        public Result(Scenario subScenario, int noSelectedValues)
        {
            _subScenario = subScenario;
            _trialResults = new double[noSelectedValues][];
        }
    }

    /**
     * Field that stores the results.
     * Map, where the key is the string representation of a sub-scenario, while values are linked instance of {@link Result}.
     */
    private HashMap<String, Result> _results;

    /**
     * Collects raw trial data per each scenario
     */
    private final RawData[] _collectedPerTrialData;

    /**
     * Parameterized constructor.
     *
     * @param sortedReferenceScenarios the number of sorted reference scenarios
     */
    public FinalRankerDataCollector(int sortedReferenceScenarios)
    {
        _collectedPerTrialData = new RawData[sortedReferenceScenarios];
    }

    /**
     * Method for storing the data.
     *
     * @param scenario      reference scenario
     * @param scenarioIndex scenarioIndex
     * @param trialResults  trial results (should be considered read only)
     */
    public void pushData(Scenario scenario, int scenarioIndex, double[] trialResults)
    {
        _collectedPerTrialData[scenarioIndex] = new RawData(scenario, scenarioIndex, trialResults);
    }

    /**
     * This method is intended to be called after all per-scenario data are collected. It pairs the trial-results using
     * the selected key.
     *
     * @param selectedKey       key selected for matching
     * @param selectedKeyIndex  index of the selected key
     * @param sortedScenarios   sortedScenarios
     * @param comparedKeyValues compared key values
     */
    public void finalize(String selectedKey, int selectedKeyIndex, Scenario[] sortedScenarios,
                         KeyValues[] comparedKeyValues)
    {
        _results = new HashMap<>(_collectedPerTrialData.length);
        for (int scID = 0; scID < sortedScenarios.length; scID++)
        {
            if (_collectedPerTrialData[scID] == null) continue;
            Scenario scenario = sortedScenarios[scID];
            Scenario sub = sortedScenarios[scID].deriveSubScenario(selectedKey);
            String k = "";
            if (sub != null) k = sub.toString();
            if (!_results.containsKey(k))
                _results.put(k, new Result(sub, comparedKeyValues[selectedKeyIndex].getValues().length));
            Value v = scenario.getKeyValuesMap().get(selectedKey);
            int valueIndex = comparedKeyValues[selectedKeyIndex].getValueIndexMap().get(v.toString());
            _results.get(k)._trialResults[valueIndex] = _collectedPerTrialData[scID]._trialResults;
        }
    }

    /**
     * Getter for the field that stores the results.
     * (map, where the key is the string representation of a sub-scenario, while values are linked instance of {@link Result}).
     *
     * @return the map
     */
    public HashMap<String, Result> getResults()
    {
        return _results;
    }

}
