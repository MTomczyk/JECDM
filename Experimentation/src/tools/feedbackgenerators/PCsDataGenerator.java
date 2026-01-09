package tools.feedbackgenerators;

import alternative.Alternative;
import exception.Exception;
import model.internals.value.AbstractValueInternalModel;
import random.IRandom;
import scenario.Scenario;
import space.Range;
import space.normalization.INormalization;
import space.os.ObjectiveSpace;
import utils.Level;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * This class assists in generating data on reference pairs of solutions (artificial data) to be compared by
 * a decision-maker. It is implemented in line with the procedure described in the article ``Efficient Preference
 * Learning Algorithm for Interactive Evolutionary Multi-Objective Optimization''
 * (<a href="https://doi.org/10.1016/j.swevo.2025.102254">article</a>). Its use is described in Tutorial 10:
 * Evolutionary Rejection Sampling.
 *
 * @author MTomczyk
 */
public final class PCsDataGenerator extends AbstractPCsData
{
    /**
     * Params container.
     */
    public static final class Params extends AbstractPCsData.Params
    {
        /**
         * Keys that can use a common selection of alternatives (can be null if not used). IMPORTANT NOTE: order
         * matters; it must be a subset of provided keys, defined as [some index, last index in keys]. I.e., these
         * should be keys appearing as the last ones in the primary keys input. E.g., if keys = ["A", "B", "C"],
         * this field can be only null, ["C"], ["B", "C"], or ["A", "B", "C"].
         */
        public String[] _keysCommonForAlternatives;

        /**
         * Random number generator.
         */
        public IRandom _R;

        /**
         * Object responsible for providing sets of alternatives.
         */
        public IAlternativesProvider _alternativesProvider;

        /**
         * Object responsible for constructing ranges spanned over an entire alternatives space (can be null
         * if normalizations are to be ignored). Ranges are built along with the alternatives sets.
         */
        public IRangesProvider _rangesProvider;

        /**
         * Object responsible for constructing criteria types associated with criteria/objectives (gain/false flags).
         * Can be null if normalizations are to be ignored. Types are built along with the alternatives sets.
         */
        public ICriteriaTypesProvider _criteriaTypesProvider;

        /**
         * Object responsible for providing the model of an artificial DM.
         */
        public IDMModelProvider _dmModelProvider;

        /**
         * Artificial DM's model writer (parser).
         */
        public IDMModelWriterParser _modelWriter;

        /**
         * Object used to determine alternatives to be compared and to form pairwise comparisons.
         */
        public IReferenceAlternativesSelector _refAlternativesSelector;

        /**
         * Object responsible for parsing alternative into its string representation (to be saved in the file).
         */
        public IAlternativeWriterParser _alternativeWriter;
    }

    /**
     * Keys that can use a common selection of alternatives (can be null if not used). IMPORTANT NOTE: order
     * matters; it must be a subset of provided keys, defined as [some index, last index in keys]. I.e., these
     * should be keys appearing as the last ones in the primary keys input. E.g., if keys = ["A", "B", "C"],
     * this field can be only null, ["C"], ["B", "C"], or ["A", "B", "C"].
     */
    private final String[] _keysCommonForAlternatives;

    /**
     * Random number generator.
     */
    private final IRandom _R;

    /**
     * Object responsible for providing sets of alternatives.
     */
    private final IAlternativesProvider _alternativesProvider;

    /**
     * Object responsible for constructing ranges spanned over an entire alternatives space.
     * Ranges are built along with the alternatives sets.
     */
    private final IRangesProvider _rangesProvider;

    /**
     * Object responsible for constructing criteria types associated with criteria/objectives (gain/false flags).
     * Can be null if normalizations are to be ignored. Types are built along with the alternatives sets.
     */
    private final ICriteriaTypesProvider _criteriaTypesProvider;

    /**
     * Object responsible for providing the model of an artificial DM.
     */
    private final IDMModelProvider _dmModelProvider;

    /**
     * Artificial DM's model writer (parser).
     */
    private final IDMModelWriterParser _modelWriter;

    /**
     * Object used to determine alternatives to be compared and to form pairwise comparisons.
     */
    private final IReferenceAlternativesSelector _refAlternativesSelector;


    /**
     * Object responsible for parsing alternative into its string representation (to be saved in the file).
     */
    private final IAlternativeWriterParser _alternativeWriter;


    /**
     * Parameterized constructor.
     *
     * @param p params container
     * @throws Exception can be thrown
     */
    public PCsDataGenerator(Params p) throws Exception
    {
        super(p);
        validate(p);

        _keysCommonForAlternatives = p._keysCommonForAlternatives;
        _R = p._R;
        _alternativesProvider = p._alternativesProvider;
        _rangesProvider = p._rangesProvider;
        _criteriaTypesProvider = p._criteriaTypesProvider;
        _dmModelProvider = p._dmModelProvider;
        _modelWriter = p._modelWriter;
        _refAlternativesSelector = p._refAlternativesSelector;
        _alternativeWriter = p._alternativeWriter;
    }


    /**
     * Auxiliary method for checking if a new set of alternatives must be constructed.
     *
     * @param previousScenario previously processed scenario (null, if no scenario has been processed yet)
     * @param currentScenario  scenario being processed currently
     * @return true, if a new set of alternatives must be constructed; false otherwise
     */
    private boolean isConstructingNewAlternativesRequired(Scenario previousScenario, Scenario currentScenario)
    {
        if (previousScenario == null) return true;
        if ((_keysCommonForAlternatives == null) || (_keysCommonForAlternatives.length == 0)) return true;
        for (int i = 0; i < _keys.length - _keysCommonForAlternatives.length; i++)
            if (!previousScenario.getKeyValues()[i].getValue().equals(currentScenario.getKeyValues()[i].getValue()))
                return true;
        return false;
    }

    /**
     * This method executes the processing.
     *
     * @throws Exception the exception can be thrown
     */
    public void process() throws Exception
    {
        if (_notify) _log.log("Processing started", Level.Global, 0);
        if (_notify) _log.log("Attempting to create the file = " + _filePath, Level.Global, 0);

        ArrayList<ArrayList<Alternative>> alternatives = null;
        Range[] ranges = null;
        boolean[] criteriaTypes = null;
        INormalization[] normalizations = null;

        try (BufferedWriter w = new BufferedWriter(new FileWriter(_filePath)))
        {
            Scenario _previousScenario = null;

            for (Scenario scenario : _scenarios.getScenarios())
            {
                if ((_scenarioDisablingConditions != null) && (_scenarioDisablingConditions.shouldBeDisabled(scenario)))
                {
                    if (_notify) _log.log("Skipping processing scenario = " + scenario, Level.Global, 0);
                    continue;
                }

                w.write(scenario.toString());
                w.write(System.lineSeparator());

                if (_notify) _log.log("Processing scenario = " + scenario + " started", Level.Global, 0);

                int noInteractions = _noInteractionsProvider.getNoInteractions(scenario);

                if (isConstructingNewAlternativesRequired(_previousScenario, scenario))
                {
                    if (_notify) _log.log("New set(s) of alternatives are to be constructed", Level.Global, _indent);
                    alternatives = _alternativesProvider.getAlternatives(scenario, noInteractions, _R);

                    if (_rangesProvider != null) ranges = _rangesProvider.getRanges(scenario);
                    if (_criteriaTypesProvider != null) criteriaTypes = _criteriaTypesProvider.getTypes(scenario);

                    if ((ranges != null) && (criteriaTypes != null))
                    {
                        ObjectiveSpace os = new ObjectiveSpace(ranges, criteriaTypes);
                        normalizations = _normalizationsBuilder.getNormalizations(os);
                    }
                    else normalizations = null; // must be restarted
                }

                if (alternatives == null)
                    throw new Exception("Alternatives sets are not constructed (the matrix is null)", null, this.getClass());


                w.write("Ranges entries = \"" + (ranges == null ? 0 : ranges.length) + "\"");
                w.write(System.lineSeparator());
                if (ranges != null)
                    for (int r = 0; r < ranges.length; r++)
                    {
                        w.write("Range ");
                        w.write(Integer.toString(r));
                        w.write(" = \"");
                        w.write(Double.toString(ranges[r].getLeft()));
                        w.write(" ");
                        w.write(Double.toString(ranges[r].getRight()));
                        w.write("\"");
                        w.write(System.lineSeparator());
                    }

                w.write("Criteria types entries = \"" + (criteriaTypes == null ? 0 : criteriaTypes.length) + "\"");
                w.write(System.lineSeparator());
                if (criteriaTypes != null)
                    for (int r = 0; r < criteriaTypes.length; r++)
                    {
                        w.write("Criterion ");
                        w.write(Integer.toString(r));
                        w.write(" = \"");
                        w.write(Boolean.toString(criteriaTypes[r]));
                        w.write("\"");
                        w.write(System.lineSeparator());
                    }

                if (_notify) _log.log("Processing trials started", Level.Global, _indent);
                for (int t = 0; t < _trials; t++)
                {
                    if (_notify) _log.log("Processing trial = " + t, Level.Global, _dIndent);

                    w.write("T = \"" + t + "\"");
                    w.write(System.lineSeparator());

                    AbstractValueInternalModel model = _dmModelProvider.getModel(scenario, t, normalizations, _R);
                    if (model == null)
                        throw new Exception("Artificial DM model is not constructed (is null)", null, this.getClass());

                    w.write(_modelWriter.parseToString(model));
                    w.write(System.lineSeparator());

                    if (_notify)
                        _log.log("Evaluating and sorting alternatives according to the DM's model", Level.Global, _dIndent);
                    for (int i = 0; i < noInteractions; i++)
                    {
                        ArrayList<Alternative> layer = alternatives.get(i);
                        for (Alternative a : layer) a.setAuxScore(model.evaluate(a));
                        if (model.isLessPreferred()) layer.sort(Comparator.comparingDouble(Alternative::getAuxScore));
                        else layer.sort((o1, o2) -> -Double.compare(o1.getAuxScore(), o2.getAuxScore()));
                    }

                    if (_notify) _log.log("Constructing pairwise comparisons", Level.Global, _dIndent);
                    int[][] I = _refAlternativesSelector.getAlternativesIndices(scenario, t, _R, alternatives);
                    if (I == null)
                        throw new Exception("Reference alternatives cannot be constructed (indices matrix is null)", null, this.getClass());
                    for (int pc = 0; pc < noInteractions; pc++)
                    {
                        w.write("Interaction = \"" + pc + "\"");
                        w.write(System.lineSeparator());
                        Alternative A1 = alternatives.get(pc).get(I[pc][0]);
                        Alternative A2 = alternatives.get(pc).get(I[pc][1]);
                        int comp = Double.compare(A1.getAuxScore(), A2.getAuxScore());
                        if (model.isLessPreferred()) comp *= -1;
                        {
                            w.write("Alternative = \"" + A1.getName() + "\"");
                            w.write(System.lineSeparator());
                            if (comp == 1) w.write("State = \"" + PreferenceStates.PREFERRED + "\"");
                            else if (comp == 0) w.write("State = \"" + PreferenceStates.EQUAL + "\"");
                            else w.write("State = \"" + PreferenceStates.NOT_PREFERRED + "\"");
                            w.write(System.lineSeparator());
                            w.write(_alternativeWriter.parseToString(A1));
                            w.write(System.lineSeparator());
                        }
                        {
                            w.write("Alternative = \"" + A2.getName() + "\"");
                            w.write(System.lineSeparator());
                            if (comp == 1) w.write("State = \"" + PreferenceStates.NOT_PREFERRED + "\"");
                            else if (comp == 0) w.write("State = \"" + PreferenceStates.EQUAL + "\"");
                            else w.write("State = \"" + PreferenceStates.PREFERRED + "\"");
                            w.write(System.lineSeparator());
                            w.write(_alternativeWriter.parseToString(A2));
                            w.write(System.lineSeparator());
                        }
                    }
                }
                if (_notify) _log.log("Processing trials ended", Level.Global, _indent);

                _previousScenario = scenario;
                if (_notify) _log.log("Processing scenario = " + scenario + " ended", Level.Global, 0);
            }
            if (_notify) _log.log("Processing ended", Level.Global, 0);

        } catch (IOException e)
        {
            throw new Exception("Error occurred when processing the results file (message = " +
                    e.getMessage() + ")", null, this.getClass());
        }
    }

    /**
     * Auxiliary method for validating params container's data.
     *
     * @param p params container
     * @throws Exception exception can be thrown
     */
    private void validate(Params p) throws Exception
    {
        Set<String> uniqueKeys = new HashSet<>(p._keys.length);
        for (String k : p._keys)
        {
            if (uniqueKeys.contains(k)) throw new Exception("The key = " + k + " is not unique",
                    null, this.getClass());
            uniqueKeys.add(k);
        }

        if (p._keysCommonForAlternatives != null)
        { // keys common for alternatives
            for (String k : p._keysCommonForAlternatives)
                if (k == null)
                    throw new Exception("One of the provided keys that are common to built alternatives is null",
                            null, this.getClass());
            for (String k : p._keysCommonForAlternatives)
                if (!uniqueKeys.contains(k))
                    throw new Exception("One of the provided keys that are common to built alternatives is does not exist in the primary keys array (requested key = " + k + ")",
                            null, this.getClass());
            Set<String> unique = new HashSet<>(p._keysCommonForAlternatives.length);
            for (String k : p._keysCommonForAlternatives)
            {
                if (unique.contains(k))
                    throw new Exception("The key = " + k + " that is common to built alternatives is not unique",
                            null, this.getClass());
                unique.add(k);
            }
            int offset = p._keys.length - p._keysCommonForAlternatives.length;
            for (int i = 0; i < p._keysCommonForAlternatives.length; i++)
            {
                if (!p._keysCommonForAlternatives[i].equals(p._keys[offset + i]))
                    throw new Exception("Key (common to built alternatives) does not match a key at corresponding index = "
                            + (offset + i) + " (" + p._keysCommonForAlternatives[i] + " vs " + p._keys[offset + i] + ")", null, this.getClass());
            }
        }
        if (p._R == null) throw new Exception("The random number generator is null", null, this.getClass());
        if (p._alternativesProvider == null)
            throw new Exception("The alternatives provider is null", null, this.getClass());
        if (p._dmModelProvider == null)
            throw new Exception("The artificial DM provider is null", null, this.getClass());
        if (p._modelWriter == null)
            throw new Exception("The artificial DM's model writer is null", null, this.getClass());
        if (p._refAlternativesSelector == null)
            throw new Exception("The reference alternatives selector is null", null, this.getClass());
        if (p._alternativeWriter == null)
            throw new Exception("The alternative writer is null", null, this.getClass());
        if (((p._rangesProvider == null) && (p._criteriaTypesProvider != null)) ||
                ((p._rangesProvider != null) && (p._criteriaTypesProvider == null)))
            throw new Exception("The ranges provider is set but the criteria provider is not (or vice versa)", null, this.getClass());
    }

}
