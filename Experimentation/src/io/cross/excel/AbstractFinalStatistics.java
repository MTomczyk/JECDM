package io.cross.excel;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import exception.ScenarioException;
import indicator.IIndicator;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import io.utils.excel.Table;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import scenario.CrossedScenarios;
import scenario.KeyValues;
import scenario.Scenario;
import scenario.Value;

import java.util.Arrays;

/**
 * Abstract extension of {@link io.scenario.excel.AbstractExcelSaver}. It is dedicated to Excel-based savers that
 * summarize the final results. The example and default implementations are {@link FinalStatisticsXLS} and
 * {@link FinalStatisticsXLS}. They store the final results in tables; each one is stored in a separate sheet, and each
 * one is dedicated to a different performance indicator. The dimensionality N of the cross-analysis can be specified.
 * The first N - 1 key-values objects are linked to tables' rows, while the last key-values object is associated with columns.
 *
 * @author MTomczyk
 */
public abstract class AbstractFinalStatistics extends AbstractExcelSaver implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param level            the level of cross-analysis (should be at least 2)
     * @param style            provides some basic customization options
     */
    public AbstractFinalStatistics(String path, String filename, CrossedScenarios crossedScenarios, int level, Style style)
    {
        super(path, filename, crossedScenarios, style, level);
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
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    @Override
    public void notifyProcessingBegins() throws CrossedScenariosException
    {
        super.notifyProcessingBegins();

        String[] selected = new String[_level - 1];
        for (int i = 0; i < _level - 1; i++)
            selected[i] = _crossedScenarios.getComparedKeyValues()[i].getKey().toString();
        instantiateTakenRows(selected);
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
            // create the main area
            _tables[i] = createTable(_excel._sheets[i], x, y, w, h);
            createLeftTopMainBlock(_tables[i], _uIndicators._entities.get(i).getName(), 2 * (_level - 1), 3);

            // create the header
            {
                int bw = 2 * (_level - 1);
                _tables[i]._cell[0][bw].setCellValue(_crossedScenarios.getComparedKeyValues()[_level - 1].getKey().toString());
                if (_excel._doFormatting)
                {
                    _tables[i].applyCellStyleToCells(bw, 0, w - bw, 1, _excel._headerStyle);
                    _tables[i].applyBorder(bw, 0, w - bw, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                    _tables[i].mergeCells(bw, 0, w - bw, 1);
                }
            }

            // create the last value + statistics block
            {
                int bw = 2 * (_level - 1);

                for (int j = 0; j < _crossedScenarios.getComparedKeyValues()[_level - 1].getValues().length; j++)
                {
                    int xx = bw + j * _uStatistics._entities.size();
                    _tables[i]._cell[1][xx].setCellValue(_crossedScenarios.getComparedKeyValues()[_level - 1].getValues()[j].getValue());

                    if (_excel._doFormatting)
                    {
                        _tables[i].applyAlignment(xx, 1, _uStatistics._entities.size(), 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                        _tables[i].applyBorder(xx, 1, _uStatistics._entities.size(), 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                        _tables[i].mergeCells(xx, 1, _uStatistics._entities.size(), 1);
                    }

                    for (int k = 0; k < _uStatistics._entities.size(); k++)
                    {
                        _tables[i]._cell[2][xx + k].setCellValue(_uStatistics._entities.get(k).getName());

                        if (_excel._doFormatting)
                        {
                            _tables[i].applyBorder(xx + k, 2, 1, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                            _tables[i].applyAlignment(xx + k, 2, 1, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                        }
                    }
                }
            }

            KeyValues[] kvs = new KeyValues[_crossedScenarios.getComparedKeyValues().length - 1];
            for (int j = 0; j < _crossedScenarios.getComparedKeyValues().length - 1; j++)
                kvs[j] = _crossedScenarios.getComparedKeyValues()[j];
            createLeftCombinationsBlock(_tables[i], kvs, 0, 3);

            // Create aux fill colors
            int[] cbs = new int[_crossedScenarios.getComparedKeyValues()[_level - 1].getValues().length];
            Arrays.fill(cbs, _uStatistics._entities.size());
            int[] rbs = new int[(h - 3) / (_crossedScenarios.getComparedKeyValues()[_level - 2].getValues().length)];
            Arrays.fill(rbs, _crossedScenarios.getComparedKeyValues()[_level - 2].getValues().length);

            _tables[i].applyChessboard(2 * (_level - 1), 3, cbs, rbs, _excel._style._tableFillChessboardColor);
        }
    }

    /**
     * Method for notifying the savers that the processing begins.
     *
     * @param scenario scenario that is to be processed
     * @param SDC      scenario data container linked to the scenario being currently processed (read-only)
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
     */
    @Override
    public void notifyScenarioProcessingBegins(Scenario scenario, AbstractScenarioDataContainer SDC) throws CrossedScenariosException
    {
        super.notifyScenarioProcessingBegins(scenario, SDC);

        try
        {
            {
                _activeBaseRow = 3;
                for (int i = 0; i < _level - 1; i++)
                {
                    String k = _crossedScenarios.getComparedKeyValues()[i].getKey().toString();
                    Value value = scenario.getKeyValuesMap().get(k);
                    KeyValues kvs = _crossedScenarios.getComparedKeyValues()[i];
                    int idx = kvs.getValueIndexMap().get(value.toString());
                    _activeBaseRow += idx * _takenRows[i];
                }
            }

            {
                String k = _crossedScenarios.getComparedKeyValues()[_level - 1].getKey().toString();
                Value value = scenario.getKeyValuesMap().get(k);
                KeyValues kvs = _crossedScenarios.getComparedKeyValues()[_level - 1];
                int idx = kvs.getValueIndexMap().get(value.toString());
                _activeBaseColumn = 2 * (_level - 1) + idx * _uStatistics._entities.size();
            }

        } catch (Exception e)
        {
            throw new CrossedScenariosException("Error occurred when notifying (reason = " + e.getMessage() + ")", this.getClass(), e, _crossedScenarios);
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

        try
        {
            _activeSheet = _uIndicators._entitiesMap.get(indicator.getName());
        } catch (Exception e)
        {
            throw new CrossedScenariosException("Error occurred when notifying (reason = " + e.getMessage() + ")", this.getClass(), e, _crossedScenarios);
        }
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
     *                     {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        if (statistics.length != _currentStatistics.length)
            throw new ScenarioException("The number of statistics differs from the number of statistic functions reported",
                    null, this.getClass(), _scenario);
        if (generation == _lastGeneration)
        {
            for (int s = 0; s < statistics.length; s++)
            {
                int sIndex = _uStatistics._entitiesMap.get(_currentStatistics[s].getName());
                _tables[_activeSheet]._cell[_activeBaseRow][_activeBaseColumn + sIndex].setCellValue(statistics[s]);
            }
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
     * @throws CrossedScenariosException crossed-scenarios-level exception can be cast and propagated higher
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
        int left = _excel._style._tableMarginX;
        int right = left + getExpectedTableWidth();
        setColumnWidths(left, right);
    }


    /**
     * Returns the expected table height (in rows)
     *
     * @return expected table height (in rows)
     */
    public int getExpectedTableHeight()
    {
        return 3 + _takenRows[0] * _crossedScenarios.getComparedKeyValues()[0].getValues().length;
    }

    /**
     * Returns the expected table width (in columns)
     *
     * @return expected table width (in columns)
     */
    public int getExpectedTableWidth()
    {
        return 2 * (_level - 1) + _uStatistics._entities.size() *
                _crossedScenarios.getComparedKeyValues()[_level - 1].getValues().length;
    }
}
