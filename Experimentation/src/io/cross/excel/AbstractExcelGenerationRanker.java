package io.cross.excel;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import exception.ScenarioException;
import executor.CrossSummarizer;
import indicator.IIndicator;
import io.cross.ICrossSaver;
import io.cross.excel.utils.FinalRankerDataCollector;
import io.cross.excel.utils.RankerUtils;
import io.utils.excel.Style;
import io.utils.excel.Table;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import scenario.*;
import statistics.tests.ITest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract extension of {@link io.scenario.excel.AbstractExcelSaver}. It is dedicated to Excel-based savers that
 * provide comprehensive rank-based summaries (concerning results attained in the last generation, or any specified
 * generation). In this case, it is required to specify a key whose values will be explicitly compared against one
 * another (selected key). The example and default implementations are {@link FinalRankerXLS} and
 * {@link FinalStatisticsXLSX}. They store the final results in tables; each one is stored in a separate sheet, and each
 * one is dedicated to a different performance indicator. The dimensionality N of the cross-analysis can be specified.
 * All crossed key-values (cartesian products) are provided on the left side of the table. Note that the selected kay is
 * always pushed to the right for better visibility. The next columns are devoted to the rank analysis: wins matrix,
 * tries matrix, and ranks. Regarding the wins matrix, the trial results attained for the selected key when using
 * different values are directly compared (one by one, i.e., the first trial result against the first trial result, and
 * so on). It is counted how often one scenario provided better results than the other yielded scenario (the preference
 * direction is provided via the indicator used). Similarly, the procedure counts the number of ties (the tolerance
 * level can be provided). Next, the ranks are calculated. In this case, for the selected key, it is counted how many
 * times each value attained the best results (rank of 1), the second best (rank of 2), and so on, when compared against
 * other values (again, trial results are compared one by one). In the case of a tie, the rank distribution is spread
 * among similar performers. E.g., if two values attain the best results (tie), they receive a 0.5 bonus for ranks 1 and
 * 2 (instead of a bonus of 1 for just one rank). The next best performer is then associated with a rank of 3.
 * Additionally, the average rank is provided (1 is the best). Lastly, statistical tests implementing
 * {@link statistics.tests.ITest} can be provided via this constructor. For each provided test, an additional block of
 * results directly comparing one value against another is supplied with the produced tables.
 *
 * @author MTomczyk
 */
public abstract class AbstractExcelGenerationRanker extends AbstractExcelSaver implements ICrossSaver
{
    /**
     * Key selected for a direct comparison (string representation).
     */
    protected String _selectedKey;

    /**
     * Index of a compared key in {@link CrossedScenarios} that matches the selected one.
     */
    private int _selectedKeyIndex;

    /**
     * Delta (tolerance) used when comparing doubles.
     */
    protected final double _tolerance;

    /**
     * Delta (tolerance) used when comparing doubles (default value).
     */
    protected final static double _defaultTolerance = 0.000000001d;

    /**
     * Auxiliary array storing those keys that were not selected for a direct comparison.
     */
    private String[] _notSelectedKeys;

    /**
     * Joint key names (all keys except the selected + the selected at the end).
     */
    private String[] _jointKeyNames;

    /**
     * Joint key indexes (indexes of all keys except the selected + index of the selected at the end; refer to the
     * original indexing).
     */
    private int[] _jointKeyIndexes;

    /**
     * Auxiliary map that transforms a joint ket in {@link AbstractExcelGenerationRanker#_jointKeyNames} into their
     * in-array indexes.
     */
    private HashMap<String, Integer> _jointKeysIndexMap;

    /**
     * Statistical tests used when comparing the key-values (e.g., algorithms).
     */
    protected ITest[] _tests;

    /**
     * Data collector.
     */
    private FinalRankerDataCollector[] _dataCollectors; // one per each indicator

    /**
     * Parameterized constructor. Parameterizes the summarizer to present results attained in the final generation.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param selectedKey      key selected for a direct comparison
     * @param tests            statistical tests used when comparing the key-values (e.g., algorithms)
     * @param level            the level of cross-analysis (should be at least 2)
     * @param tolerance        Delta (tolerance) used when comparing doubles)
     * @param style            provides some basic customization options
     */
    public AbstractExcelGenerationRanker(String path,
                                         String filename,
                                         CrossedScenarios crossedScenarios,
                                         String selectedKey,
                                         ITest[] tests,
                                         int level,
                                         double tolerance,
                                         Style style)
    {
        this(path, filename, crossedScenarios, selectedKey, tests, null, level, tolerance, style);
    }

    /**
     * Parameterized constructor.
     *
     * @param path                         full path to the folder where the file should be stored (without a path
     *                                     separator)
     * @param filename                     the filename (without the suffix, e.g., extension)
     * @param crossedScenarios             crossed scenarios being currently processed
     * @param selectedKey                  key selected for a direct comparison
     * @param tests                        statistical tests used when comparing the key-values (e.g., algorithms)
     * @param summarizeResultsInGeneration if not null, the data summary will concern results attained in this specified
     *                                     generation number (instead of the last generation)
     * @param level                        the level of cross-analysis (should be at least 2)
     * @param tolerance                    Delta (tolerance) used when comparing doubles)
     * @param style                        provides some basic customization options
     */
    public AbstractExcelGenerationRanker(String path,
                                         String filename,
                                         CrossedScenarios crossedScenarios,
                                         String selectedKey,
                                         ITest[] tests,
                                         Integer summarizeResultsInGeneration,
                                         int level,
                                         double tolerance,
                                         Style style)
    {
        super(path, filename, crossedScenarios, style, summarizeResultsInGeneration, level);
        _tests = tests;
        _selectedKey = null;
        _tolerance = tolerance;
        if (selectedKey != null) _selectedKey = selectedKey.toUpperCase();
        _fullPath = getFullPath(path, filename);
    }

    /**
     * Auxiliary method for validating the data and instantiating auxiliary fields.
     *
     * @param crossedScenarios crossed scenarios being currently processed
     * @param tests            statistical tests used (elements should not be used and should be unique)
     * @throws CrossedScenariosException the crossed scenarios exception can be thrown 
     */
    protected void validate(CrossedScenarios crossedScenarios, ITest[] tests) throws CrossedScenariosException
    {
        if (tests != null)
        {
            Set<String> names = new HashSet<>();
            for (ITest t : tests)
            {
                if (t == null)
                    throw new CrossedScenariosException("One of the statistical tests is not provided (is null)", null, this.getClass(), crossedScenarios);
                if (names.contains(t.toString()))
                    throw new CrossedScenariosException("Statistical tests = " + t + " is not unique", null, this.getClass(), crossedScenarios);
                names.add(t.toString());
            }
        }
    }

    /**
     * Can be implemented to notify {@link CrossSummarizer} that the saver's instance (creation) should be skipped given
     * the crossed scenarios setting.
     *
     * @param crossedScenarios crossed scenarios
     * @return true, if the instance should be skipped; false otherwise
     */
    @Override
    public boolean shouldBeSkipped(CrossedScenarios crossedScenarios)
    {
        if (_selectedKey == null) return true;
        return crossedScenarios.getComparedKeysMap().get(_selectedKey) == null;
    }


    /**
     * Returns the level (dimensionality) the saver can operate with.
     *
     * @return the dedicated level
     */
    @Override
    public int getDedicatedLevel()
    {
        return _level;
    }

    /**
     * Method for notifying the savers that the processing begins (prior to executing any scenario).
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast 
     */
    @Override
    public void notifyProcessingBegins() throws CrossedScenariosException
    {
        super.notifyProcessingBegins();

        _jointKeyIndexes = new int[_level];
        _selectedKeyIndex = _crossedScenarios.getComparedKeysMap().get(_selectedKey);
        _notSelectedKeys = new String[_level - 1];
        int idx = 0;
        for (int i = 0; i < _level; i++)
        {
            if (i != _selectedKeyIndex)
            {
                _notSelectedKeys[idx] = _crossedScenarios.getComparedKeyValues()[i].getKey().toString();
                _jointKeyIndexes[idx] = i;
                idx++;
            }
            else _jointKeyIndexes[_level - 1] = i;
        }

        _jointKeyNames = new String[_level];
        System.arraycopy(_notSelectedKeys, 0, _jointKeyNames, 0, _notSelectedKeys.length);
        _jointKeyNames[_level - 1] = _selectedKey;

        _jointKeysIndexMap = new HashMap<>(_jointKeyNames.length);
        for (int i = 0; i < _jointKeyNames.length; i++)
            _jointKeysIndexMap.put(_jointKeyNames[i], i);

        _dataCollectors = new FinalRankerDataCollector[_uIndicators._entities.size()];

        instantiateTakenRows(_jointKeyNames);
        instantiateAuxBaseColumns();
        instantiateSheets();
        instantiateTables();
    }


    /**
     * Auxiliary method for instantiating tables.
     */
    protected void instantiateTables()
    {
        _tables = new Table[_excel._sheets.length];
        int x = _excel._style._tableMarginX;
        int y = _excel._style._tableMarginY;
        int w = getExpectedTableWidth();
        int h = getExpectedTableHeight();

        for (int i = 0; i < _tables.length; i++)
        {
            _tables[i] = createTable(_excel._sheets[i], x, y, w, h);

            // create the main block
            int shift = 0;
            int dx = 2 * (_level);

            createLeftTopMainBlock(_tables[i], _uIndicators._entities.get(i).getName(), dx, 2);


            shift += dx;
            Value[] skV = _crossedScenarios.getComparedKeyValues()[_selectedKeyIndex].getValues();
            dx = skV.length;

            createLabelValuesRows(_tables[i], shift, 0, "Wins matrix", skV);
            shift += dx;
            createLabelValuesRows(_tables[i], shift, 0, "Ties matrix", skV);

            shift += dx;
            dx = skV.length + 1;
            // ranks
            {
                _tables[i]._cell[0][shift].setCellValue("Ranks");
                if (_excel._doFormatting)
                {
                    _tables[i].applyCellStyleToCells(shift, 0, dx, 1, _excel._headerStyle);
                    _tables[i].applyBorder(shift, 0, dx, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                    _tables[i].mergeCells(shift, 0, dx, 1);
                }

                for (int k = 0; k < skV.length; k++) _tables[i]._cell[1][shift + k].setCellValue(k + 1);
                _tables[i]._cell[1][shift + skV.length].setCellValue("Average");

                if (_excel._doFormatting)
                {
                    _tables[i].applyBorder(shift, 1, skV.length + 1, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                    _tables[i].applyAlignment(shift, 1, skV.length + 1, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                }
            }

            // TESTS
            if (_tests != null)
            {
                shift += dx;
                dx = skV.length;
                for (ITest test : _tests)
                {
                    createLabelValuesRows(_tables[i], shift, 0, test.toString(), skV);
                    shift += dx;
                }
            }

            // color segments
            int[] cbs;
            if (_tests == null) cbs = new int[]{skV.length, skV.length, skV.length + 1};
            else
            {
                cbs = new int[3 + _tests.length];
                cbs[0] = skV.length;
                cbs[1] = skV.length;
                cbs[2] = skV.length + 1;
                for (int t = 0; t < _tests.length; t++) cbs[3 + t] = skV.length;
            }
            int[] rbs = new int[(h - 2) / (skV.length)];
            Arrays.fill(rbs, skV.length);

            CellStyle newStyle = _excel._workbook.createCellStyle();
            newStyle.cloneStyleFrom(_tables[i]._cell[2][2 * (_level)].getCellStyle());
            _tables[i].applyChessboard(2 * (_level), 2, cbs, rbs, newStyle, _excel._style._tableFillChessboardColor);

            KeyValues[] kvs = new KeyValues[_jointKeyIndexes.length];
            for (int j = 0; j < _jointKeyIndexes.length; j++)
                kvs[j] = _crossedScenarios.getComparedKeyValues()[_jointKeyIndexes[j]];
            createLeftCombinationsBlock(_tables[i], kvs, 0, 2);
        }
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data begins.
     *
     * @param indicator indicator
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingBegins(IIndicator indicator) throws CrossedScenariosException
    {
        super.notifyIndicatorProcessingBegins(indicator);
        int index = _uIndicators._entitiesMap.get(indicator.toString());
        if (_dataCollectors[index] == null) _dataCollectors[index] =
                new FinalRankerDataCollector(_crossedScenarios.getReferenceScenariosSorted().length);
    }

    /**
     * The main method for pushing data to be stored. The data provided is assumed to come from all trials concerning
     * one generation and one indicator (the generation number is not provided; it is assumed that all generations per
     * indicator are provided sequentially). IMPORTANT NOTE: The data supplied is supposed to be organized. The double
     * vectors from different indicators cannot be interlaced. As mentioned above, these vectors will also come in the
     * ascending order of generations, and each will completely cover the results obtained within a generation.
     *
     * @param trialResults trial results
     * @param statistics   statistics calculated from trial results (1:1 mapping with statistic objects stored in
     *                     {@link AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        if (((_summarizeResultsInGeneration != null) && (generation == _summarizeResultsInGeneration))
                || ((_summarizeResultsInGeneration == null) && (generation == _lastGeneration)))
        {
            _dataCollectors[_currentIndicatorIndex].pushData(_currentScenario,
                    _currentScenarioIndex, trialResults.clone());
        }
    }

    /**
     * A method for notifying the saver that the processing of indicator-related data ends.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingEnds() throws CrossedScenariosException
    {

    }


    /**
     * Method for notifying the savers that the processing ends.
     *
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast 
     */
    @Override
    public void notifyScenarioProcessingEnds() throws CrossedScenariosException
    {

    }

    /**
     * Method for notifying the savers that the processing begins (after all scenarios are processed).
     */
    @Override
    public void notifyProcessingEnds() throws CrossedScenariosException
    {
        setColumnWidths(_excel._style._tableMarginX, _excel._style._tableMarginX + getExpectedTableWidth());

        for (FinalRankerDataCollector dc : _dataCollectors)
        {
            if (dc == null) continue;
            dc.finalize(_selectedKey, _selectedKeyIndex, _crossedScenarios.getReferenceScenariosSorted(),
                    _crossedScenarios.getComparedKeyValues());
        }

        // iterate over all sub scenarios
        for (int i = 0; i < _uIndicators._entities.size(); i++)
        {
            if (_dataCollectors[i] == null) continue;
            HashMap<String, FinalRankerDataCollector.Result> R = _dataCollectors[i].getResults();
            // process involved sub scenarios (unique)

            Set<String> processed = new HashSet<>(_crossedScenarios.getReferenceScenariosSorted().length);
            for (int scID = 0; scID < _crossedScenarios.getReferenceScenariosSorted().length; scID++)
            {
                Scenario sub = _crossedScenarios.getReferenceScenariosSorted()[scID].deriveSubScenario(_selectedKey);
                String k = "";
                if (sub != null) k = sub.toString();

                FinalRankerDataCollector.Result rSC = R.get(k); // skip if no results
                if (rSC == null) continue;

                if (processed.contains(k)) // use only for the first time (one sub scenario defines (when added values) multiple general scenarios)
                    continue;

                processed.add(k);

                int baseRow = 2;
                if (sub == null) continue;
                for (KeyValue kv : sub.getKeyValues())
                {
                    Integer jkIndex = _jointKeysIndexMap.get(kv.getKey().toString());
                    if (jkIndex == null) continue;
                    int oIndex = _jointKeyIndexes[jkIndex];
                    int vIndex = _crossedScenarios.getComparedKeyValues()[oIndex].getValueIndexMap().get(kv.getValue().toString());
                    baseRow += _takenRows[jkIndex] * vIndex;
                }

                doRanking(rSC, baseRow, _tables[i], i);
            }
        }
    }

    /**
     * Auxiliary method for filling the ranking data
     *
     * @param rSC            results for the sub-scenario
     * @param baseRow        base row
     * @param table          table to be filled
     * @param indicatorIndex index of an indicator (unified) being currently processed
     */
    private void doRanking(FinalRankerDataCollector.Result rSC, int baseRow, Table table, int indicatorIndex)
    {
        doRankingWM(rSC, baseRow, _auxBaseColumnsMap.get("WM"), table, indicatorIndex);
        doRankingTM(rSC, baseRow, _auxBaseColumnsMap.get("TM"), table);
        doRanks(rSC, baseRow, _auxBaseColumnsMap.get("R"), table, indicatorIndex);
        if (_tests != null)
            for (ITest test : _tests)
                doTest(rSC, baseRow, _auxBaseColumnsMap.get(test.toString()), table, test);

    }

    /**
     * Auxiliary method for filling the ranking data (winning matrix)
     *
     * @param rSC            results for the sub-scenario
     * @param baseRow        base row
     * @param baseColumn     baseColumn
     * @param table          table to be filled
     * @param indicatorIndex index of an indicator (unified) being currently processed
     */
    private void doRankingWM(FinalRankerDataCollector.Result rSC, int baseRow, int baseColumn,
                             Table table, int indicatorIndex)
    {
        for (int i = 0; i < rSC._trialResults.length; i++)
        {
            if (rSC._trialResults[i] == null) continue;
            for (int j = 0; j < rSC._trialResults.length; j++)
            {
                if (i == j) continue;
                if (rSC._trialResults[j] == null) continue;
                double[] v1 = rSC._trialResults[i];
                double[] v2 = rSC._trialResults[j];
                boolean less = _uIndicators._entities.get(indicatorIndex).isLessBetter();
                int wins = RankerUtils.countWins(v1, v2, less, _tolerance);
                table._cell[baseRow + i][baseColumn + j].setCellValue(wins);
            }
        }
    }

    /**
     * Auxiliary method for filling the ranking data (ties matrix)
     *
     * @param rSC        results for the sub-scenario
     * @param baseRow    base row
     * @param baseColumn baseColumn
     * @param table      table to be filled
     */
    private void doRankingTM(FinalRankerDataCollector.Result rSC, int baseRow, int baseColumn, Table table)
    {
        for (int i = 0; i < rSC._trialResults.length; i++)
        {
            if (rSC._trialResults[i] == null) continue;
            for (int j = 0; j < rSC._trialResults.length; j++)
            {
                if (i == j) continue;
                if (rSC._trialResults[j] == null) continue;
                double[] v1 = rSC._trialResults[i];
                double[] v2 = rSC._trialResults[j];
                int ties = RankerUtils.countTies(v1, v2, _tolerance);
                table._cell[baseRow + i][baseColumn + j].setCellValue(ties);
            }
        }
    }

    /**
     * Auxiliary method for filling the ranking data (ranks)
     *
     * @param rSC            results for the sub-scenario
     * @param baseRow        base row
     * @param baseColumn     baseColumn
     * @param table          table to be filled
     * @param indicatorIndex index of an indicator (unified) being currently processed
     */
    private void doRanks(FinalRankerDataCollector.Result rSC, int baseRow, int baseColumn,
                         Table table, int indicatorIndex)
    {
        double[][] ranks = RankerUtils.calculateAttainedRanks(rSC._trialResults,
                _uIndicators._entities.get(indicatorIndex).isLessBetter(), _tolerance);

        for (int i = 0; i < rSC._trialResults.length; i++)
        {
            if (rSC._trialResults[i] == null) continue;
            if (ranks[i] == null) continue;

            double average = 0.0d;
            double sum = 0.0d;
            if (ranks[i].length == 1)
            {
                table._cell[baseRow + i][baseColumn].setCellValue(ranks[i][0]);
                table._cell[baseRow + i][baseColumn + rSC._trialResults.length].setCellValue(1);
            }
            else
            {
                for (int j = 0; j < ranks[i].length; j++)
                {
                    table._cell[baseRow + i][baseColumn + j].setCellValue(ranks[i][j]);
                    average += (j + 1) * ranks[i][j];
                    sum += ranks[i][j];
                }
                if (Double.compare(sum, 0.0d) != 0)
                    table._cell[baseRow + i][baseColumn + rSC._trialResults.length].setCellValue(average / sum);
            }
        }
    }

    /**
     * Auxiliary method for filling the ranking data (test)
     *
     * @param rSC        results for the sub-scenario
     * @param baseRow    base row
     * @param baseColumn baseColumn
     * @param table      table to be filled
     * @param test       test to be used
     */
    private void doTest(FinalRankerDataCollector.Result rSC, int baseRow, int baseColumn,
                        Table table, ITest test)
    {
        if (rSC._trialResults == null) return;
        for (int i = 0; i < rSC._trialResults.length; i++)
        {
            if (rSC._trialResults[i] == null) continue;
            for (int j = 0; j < rSC._trialResults.length; j++)
            {
                if (i == j) continue;
                if (rSC._trialResults[j] == null) continue;
                try
                {
                    double t = test.getPValue(rSC._trialResults[i], rSC._trialResults[j]);
                    table._cell[baseRow + i][baseColumn + j].setCellValue(t);
                } catch (Exception e)
                {
                    table._cell[baseRow + i][baseColumn + j].setCellValue("Error (" + e.getMessage() + ")");
                }
            }
        }
    }

    /**
     * Returns the expected table height (in rows)
     *
     * @return expected table height (in rows)
     */
    public int getExpectedTableHeight()
    {
        int r1 = 2;
        int r2 = _takenRows[0] * _crossedScenarios.getComparedKeyValues()[_jointKeyIndexes[0]].getValues().length;
        return r1 + r2;
    }

    /**
     * Returns the expected table width (in columns)
     *
     * @return expected table width (in columns)
     */
    public int getExpectedTableWidth()
    {
        int r1 = _level * 2;
        int r2 = _crossedScenarios.getComparedKeyValues()[_selectedKeyIndex].getValues().length;
        int r3 = _crossedScenarios.getComparedKeyValues()[_selectedKeyIndex].getValues().length;
        int r4 = r2 + 1;
        int r5 = 0;
        if (_tests != null) r5 = r2 * _tests.length;
        return r1 + r2 + r3 + r4 + r5;
    }

    /**
     * Instantiates the auxiliary columns map.
     */
    public void instantiateAuxBaseColumns()
    {
        int tests = 0;
        if (_tests != null) tests = _tests.length;
        _auxBaseColumnsMap = new HashMap<>(3 + tests);
        int vs = _crossedScenarios.getComparedKeyValues()[_selectedKeyIndex].getValues().length;
        _auxBaseColumnsMap.put("WM", _level * 2); // winning matrix
        _auxBaseColumnsMap.put("TM", _level * 2 + vs); // ties matrix
        _auxBaseColumnsMap.put("R", _level * 2 + vs * 2); // ranks
        int base = vs * 3 + 1;
        if (_tests != null)
            for (ITest t : _tests)
            {
                _auxBaseColumnsMap.put(t.getName(), _level * 2 + base); // ranks
                base += vs;
            }
    }

    /**
     * Auxiliary method for retrieving a middle part of file suffix.
     *
     * @return middle part of file suffix
     */
    protected String getFileSuffixMiddlePart()
    {
        StringBuilder suff = new StringBuilder();
        suff.append(_selectedKey);
        suff.append("_");
        if (_tests != null) for (ITest t : _tests)
            if (t != null) suff.append(t.getName()).append("_");
            else suff.append("_");
        return suff.toString();
    }
}
