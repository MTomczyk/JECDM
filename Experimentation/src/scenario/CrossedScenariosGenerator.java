package scenario;

import combinatorics.Possibilities;
import condition.ScenarioDisablingConditions;
import exception.GlobalException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Auxiliary class that aids in generating scenarios for cross-examination.
 *
 * @author MTomczyk
 */
public class CrossedScenariosGenerator
{
    /**
     * The main method for generating {@link CrossedScenarios} objects from {@link CrossedSetting}.
     *
     * @param scenarios       contains data on all experimental scenarios
     * @param crossedSettings instantiated crossed settings ({@link CrossedSetting#instantiateSetting(Scenarios)} method should be called)
     * @return scenarios for cross-examination
     * @throws GlobalException global exception can be thrown
     */
    public CrossedScenarios[] generateCrossedScenarios(Scenarios scenarios, CrossedSetting[] crossedSettings) throws GlobalException
    {
        LinkedList<CrossedScenarios> crossedScenarios = new LinkedList<>();
        for (CrossedSetting crossedSetting : crossedSettings)
        {
            CrossedScenarios[] cs = getCrossedScenarios(scenarios, crossedSetting);
            for (CrossedScenarios css : cs) if (css != null) crossedScenarios.add(css);
        }

        if (crossedScenarios.isEmpty()) return null;
        return filterOutDuplicates(crossedScenarios);
    }

    /**
     * Auxiliary method that filters out duplicated crossed scenarios from the input ones generated by the main method
     * {@link CrossedScenariosGenerator#generateCrossedScenarios(Scenarios, CrossedSetting[])}.
     *
     * @param crossedScenarios input crossed scenarios
     * @return array of unique crossed scenarios (null, if no such were generated, e.g., due to disabling conditions)
     */
    private CrossedScenarios[] filterOutDuplicates(LinkedList<CrossedScenarios> crossedScenarios)
    {
        CrossedScenarios[] allCS = new CrossedScenarios[crossedScenarios.size()];
        int idx = 0;
        for (CrossedScenarios cs : crossedScenarios) allCS[idx++] = cs;
        boolean[] duplicate = new boolean[allCS.length];
        int u = allCS.length;
        for (int i = 0; i < allCS.length; i++)
        {
            if (duplicate[i]) continue;
            for (int j = i + 1; j < allCS.length; j++)
            {
                if (duplicate[j]) continue;
                if (allCS[i].equals(allCS[j]))
                {
                    u--;
                    duplicate[j] = true;
                }
            }
        }

        CrossedScenarios[] unique = new CrossedScenarios[u];
        idx = 0;
        for (int i = 0; i < allCS.length; i++) if (!duplicate[i]) unique[idx++] = allCS[i];
        return unique;
    }


    /**
     * Supportive method for creating the crossed scenarios (fixing the values for the constant keys).
     *
     * @param scenarios      contains data on all experimental scenarios
     * @param crossedSetting crossed setting used to establish a crossed scenario
     * @return crossed scenario
     * @throws GlobalException global-level exception can be thrown and propagated higher
     */
    private CrossedScenarios[] getCrossedScenarios(Scenarios scenarios, CrossedSetting crossedSetting) throws GlobalException
    {
        Set<String> mapComparedKeys = new HashSet<>(crossedSetting.getComparedKeyValues().length);
        for (KeyValues kv : crossedSetting.getComparedKeyValues()) mapComparedKeys.add(kv.getKey().toString());

        // determine the fixed key-value
        LinkedList<KeyValues> kvToBeFixed = new LinkedList<>();
        for (KeyValues kv : scenarios.getOrderedKeyValues())
            if (!mapComparedKeys.contains(kv.getKey().toString()))
                kvToBeFixed.add(kv);

        KeyValues[] fixed = new KeyValues[kvToBeFixed.size()];
        int idx = 0;
        for (KeyValues kv : kvToBeFixed) fixed[idx++] = kv;

        // determine the cartesian product of fixed values
        int[] valuesToFix = new int[fixed.length];
        for (int i = 0; i < fixed.length; i++) valuesToFix[i] = fixed[i].getValues().length;
        int[][] cp = Possibilities.generateCartesianProduct(valuesToFix);

        CrossedScenarios[] crossedScenarios = new CrossedScenarios[cp.length];

        for (int i = 0; i < cp.length; i++)
        {
            KeyValue[] fixedKeyValues = new KeyValue[fixed.length];
            for (int j = 0; j < fixed.length; j++)
                fixedKeyValues[j] = new KeyValue(fixed[j].getKey(), fixed[j].getValues()[cp[i][j]]);

            CrossedScenarios cs = getCrossedScenarios(scenarios, fixedKeyValues, crossedSetting);
            crossedScenarios[i] = cs;
        }

        return crossedScenarios;
    }


    /**
     * Auxiliary method for creating crossed scenarios given the fixed key-values.
     *
     * @param scenarios      wrapper for all scenarios
     * @param fixedKeyValues fixed key-values
     * @param crossedSetting crossed setting
     * @return crossed scenarios
     * @throws GlobalException global-level exception can be thrown and propagated higher
     */
    private CrossedScenarios getCrossedScenarios(Scenarios scenarios, KeyValue[] fixedKeyValues,
                                                 CrossedSetting crossedSetting) throws GlobalException
    {
        // determine the level
        int level = crossedSetting.getComparedKeyValues().length;
        Scenario[] matching = scenarios.getScenariosThatMatch(fixedKeyValues);

        if (matching.length == 0) return null;
        int valid = matching.length;

        for (int s = 0; s < matching.length; s++)
        {
            if (crossedSetting.getScenarioDisablingConditions() != null)
            {
                for (ScenarioDisablingConditions sdc : crossedSetting.getScenarioDisablingConditions())
                    if (sdc.shouldBeDisabled(matching[s]))
                    {
                        matching[s] = null;
                        valid--;
                        break;
                    }
            }

            if (matching[s] != null) // the values should be in the provided values set
            {
                for (KeyValue kv : matching[s].getKeyValues())
                {
                    KeyValues kvs = crossedSetting.getComparedKeyValuesMap().get(kv.getKey().toString());
                    if (kvs == null) continue;
                    if (kvs.getValueMap().get(kv.getValue().getValue()) == null)
                    {
                        matching[s] = null;
                        valid--;
                        break;
                    }
                }
            }
        }


        if (valid == 0) return null;
        Scenario[] validMatching = new Scenario[valid];
        int idx = 0;
        for (Scenario s : matching)
            if (s != null) validMatching[idx++] = s;

        return new CrossedScenarios(fixedKeyValues, crossedSetting.getComparedKeyValues().clone(),
                validMatching, level, scenarios);
    }

}
