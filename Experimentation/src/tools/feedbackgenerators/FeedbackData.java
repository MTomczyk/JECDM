package tools.feedbackgenerators;

import alternative.Alternative;
import model.internals.value.AbstractValueInternalModel;
import preference.PreferenceInformationUtils;
import preference.indirect.PairwiseComparison;
import relation.Relations;
import scenario.Scenario;
import scenario.Scenarios;
import space.Range;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Container for the data on reference alternatives used to simulate, e.g., pairwise comparisons, stored for various
 * scenarios (generated, e.g., by {@link PCsDataGenerator}).
 *
 * @author MTomczyk
 */
public class FeedbackData
{
    /**
     * Simple container for scenario-related data.
     */
    public static class ScenarioData
    {
        /**
         * Parameterized constructor.
         *
         * @param scenario  scenario ID
         * @param ranges    ranges spanned over the considered alternatives space (can be null, if the normalizations should be ignored)
         * @param trialData containers for trial-related data
         */
        public ScenarioData(Scenario scenario, Range[] ranges, TrialData[] trialData)
        {
            _scenario = scenario;
            _ranges = ranges;
            _trialData = trialData;
        }

        /**
         * Scenario ID.
         */
        public final Scenario _scenario;

        /**
         * Ranges spanned over the considered alternatives space (can be null, if the normalizations should be ignored).
         */
        public final Range[] _ranges;

        /**
         * Containers for trial-related data.
         */
        public final TrialData[] _trialData;
    }

    /**
     * Simple container for trial-related data.
     */
    public static class TrialData
    {
        /**
         * Parameterized constructor.
         *
         * @param no              trial no
         * @param dmModel         model of an artificial DM
         * @param interactionData data on interactions (ordered from the first, i.e., oldest, to the last)
         */
        public TrialData(int no, AbstractValueInternalModel dmModel, InteractionData[] interactionData)
        {
            _no = no;
            _dmModel = dmModel;
            _interactionsData = interactionData;
        }

        /**
         * Trial no.
         */
        public final int _no;

        /**
         * Model of an artificial DM.
         */
        public final AbstractValueInternalModel _dmModel;

        /**
         * Data on interactions (ordered from the first, i.e., oldest, to the last).
         */
        public InteractionData[] _interactionsData;
    }

    /**
     * Simple container for interaction-related data.
     */
    public static class InteractionData
    {
        /**
         * Parameterized constructor.
         *
         * @param no           interaction number (0 = the first interaction; 1 = the second; etc.)
         * @param alternatives data on evaluated alternatives
         */
        public InteractionData(int no, Alternative[] alternatives)
        {
            _no = no;
            _alternatives = alternatives;
        }

        /**
         * Data on evaluated alternatives.
         */
        public final Alternative[] _alternatives;

        /**
         * Interaction number (0 = the first interaction; 1 = the second; etc.)
         */
        public final int _no;
    }

    /**
     * Parameterized constructor. Instantiates and initially fills the {@link FeedbackData#_scenariosData} field.
     *
     * @param scenarios contains data on considered scenarios
     */
    public FeedbackData(Scenarios scenarios)
    {
        _scenariosData = new HashMap<>(scenarios.getScenarios().length);
        for (Scenario s : scenarios.getScenarios())
            _scenariosData.put(s.toString(), null);
    }

    /**
     * Top-level container for per-scenarios data. Maps scenario's string representation into its associated data.
     */
    public final HashMap<String, ScenarioData> _scenariosData;

    /**
     * Auxiliary method for adding a scenario-related data to {@link FeedbackData#_scenariosData}.
     *
     * @param scenarioData scenario data to be added
     */
    public void addData(ScenarioData scenarioData)
    {
        if (!_scenariosData.containsKey(scenarioData._scenario.toString())) return;
        _scenariosData.replace(scenarioData._scenario.toString(), scenarioData);
    }

    /**
     * Returns trial data associated with specific scenario and trial ID.
     *
     * @param scenario scenario's string representation.
     * @param trialID  trial ID
     * @return trial data (null, if data for a scenario cannot be derived)
     */
    public TrialData getTrialData(String scenario, int trialID)
    {
        if (!_scenariosData.containsKey(scenario)) return null;
        ScenarioData SD = _scenariosData.get(scenario);
        return SD._trialData[trialID];
    }

    /**
     * a
     * Constructs pairwise comparisons from the feedback data on a scenario and specific trial. The output elements
     * are ordered as imposed by their order in {@link TrialData}. Assumes that {@link InteractionData} consists of
     * two alternatives. The
     *
     * @param scenario               scenario's string representation.
     * @param trialID                trial ID
     * @param skipNotPreferenceCases if true, pairwise comparison indicated as INDIFFERENCE or INCOMPARABLE are skipped and not returned
     * @return pairwise comparisons for a scenario and a trial (null, if data for a scenario cannot be derived)
     */
    public ArrayList<PairwiseComparison> getPairwiseComparisons(String scenario, int trialID, boolean skipNotPreferenceCases)
    {
        if (!_scenariosData.containsKey(scenario)) return null;
        ScenarioData SD = _scenariosData.get(scenario);
        TrialData TD = SD._trialData[trialID];
        ArrayList<PairwiseComparison> PCs = new ArrayList<>(TD._interactionsData.length);
        for (InteractionData ID : TD._interactionsData)
        {
            PairwiseComparison PC = PreferenceInformationUtils.getPairwiseComparison(TD._dmModel,
                    ID._alternatives[0], ID._alternatives[1]);
            if ((!skipNotPreferenceCases) || (PC.getRelation().equals(Relations.PREFERENCE))) PCs.add(PC);
        }
        return PCs;
    }

}
