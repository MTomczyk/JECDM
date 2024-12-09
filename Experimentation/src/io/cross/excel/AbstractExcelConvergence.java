package io.cross.excel;

import container.scenario.AbstractScenarioDataContainer;
import exception.CrossedScenariosException;
import exception.ScenarioException;
import indicator.IIndicator;
import io.cross.ICrossSaver;
import io.utils.excel.Style;
import io.utils.excel.Table;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import scenario.CrossedScenarios;
import scenario.Scenario;
import scenario.Value;

/**
 * Abstract extension of {@link io.scenario.excel.AbstractExcelSaver}. It is dedicated to Excel-based savers that provide
 * comprehensive convergence plots (results attained throughout all generations). The example and default implementations
 * are {@link ConvergenceXLS} and {@link ConvergenceXLSX}. They store the final results in tables; each one is stored in
 * a separate sheet, and each one is dedicated to a different performance indicator. The dimensionality N of the
 * cross-analysis can be specified. All crossed key-values (cartesian products) are provided as a table header. The
 * results for each generation are provided in subsequent rows.
 *
 * @author MTomczyk
 */
public abstract class AbstractExcelConvergence extends AbstractExcelSaver implements ICrossSaver
{
    /**
     * Parameterized constructor.
     *
     * @param path             full path to the folder where the file should be stored (without a path separator)
     * @param filename         the filename (without the suffix, e.g., extension)
     * @param crossedScenarios crossed scenarios being currently processed
     * @param level            the level of cross-analysis (should be at least 1)
     * @param style            provides some basic customization options
     */
    public AbstractExcelConvergence(String path, String filename, CrossedScenarios crossedScenarios, int level, Style style)
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

        _takenColumns = new int[_level];

        int accu = _uStatistics._entities.size();
        for (int j = _level - 1; j >= 0; j--)
        {
            _takenColumns[j] = accu;
            accu *= _crossedScenarios.getComparedKeyValues()[j].getValues().length;
        }

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
            createLeftTopMainBlock(_tables[i], "Generation", 1, h);

            // create main rows

            int repeats = 1;
            for (int k = 0; k < _level; k++)
            {
                int shiftX = 1;
                for (int r = 0; r < repeats; r++)
                {
                    for (int v = 0; v < _crossedScenarios.getComparedKeyValues()[k].getValues().length; v++)
                    {
                        String label = _crossedScenarios.getComparedKeyValues()[k].getKey().toString() + " = "
                                + _crossedScenarios.getComparedKeyValues()[k].getValues()[v];
                        _tables[i]._cell[k][shiftX].setCellValue(label);

                        if (_excel._doFormatting)
                        {
                            _tables[i].applyCellStyleToCells(shiftX, k, _takenColumns[k], 1, _excel._headerStyle);
                            _tables[i].applyBorder(shiftX, k, _takenColumns[k], 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                            _tables[i].mergeCells(shiftX, k, _takenColumns[k], 1);
                        }
                        shiftX += _takenColumns[k];
                    }
                }
                repeats *= _crossedScenarios.getComparedKeyValues()[k].getValues().length;
            }

            int shiftX = 1;
            for (int r = 0; r < repeats; r++)
            {
                for (int s = 0; s < _uStatistics._entities.size(); s++)
                {
                    _tables[i]._cell[_level][shiftX].setCellValue(_uStatistics._entities.get(s).toString());
                    if (_excel._doFormatting)
                    {
                        _tables[i].applyAlignment(shiftX, _level, 1, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                        _tables[i].applyBorder(shiftX, _level, 1, 1, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                    }
                    shiftX++;
                }
            }
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

        _activeBaseColumn = getBaseColumn(scenario);
        _currentStatistics = SDC.getStatisticFunctions();
    }

    /**
     * Auxiliary method for determining the base column for data filling.
     *
     * @param scenario scenario being currently processed
     * @return column index
     */
    protected int getBaseColumn(Scenario scenario)
    {
        int result = _excel._style._tableMarginX + 1;
        for (int i = 0; i < _crossedScenarios.getComparedKeyValues().length; i++)
        {
            Value value = scenario.getKeyValuesMap().get(_crossedScenarios.getComparedKeyValues()[i].getKey().toString());
            int index = _crossedScenarios.getComparedKeyValues()[i].getValueIndexMap().get(value.toString());
            result += index * _takenColumns[i];
        }
        return result;
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
     *                     {@link AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        _activeBaseRow = getExpectedTableHeight() + generation;

        Row row = _excel._sheets[_activeSheet].getRow(_activeBaseRow);
        if (row == null) row = _excel._sheets[_activeSheet].createRow(_activeBaseRow);

        for (int s = 0; s < statistics.length; s++)
        {
            int sIndex = _uStatistics._entitiesMap.get(_currentStatistics[s].getName());
            Cell cell = row.getCell(_activeBaseColumn + sIndex);
            if (cell == null) cell = row.createCell(_activeBaseColumn + sIndex);
            cell.setCellValue(statistics[s]);
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
     * Method for notifying the savers that the processing end (after all scenarios are processed).
     */
    @Override
    public void notifyProcessingEnds() throws CrossedScenariosException
    {
        // construct generations columns
        int generations = -1;
        for (Integer i : _generationsPerScenario)
            if ((i != null) && (i > generations)) generations = i;

        if (generations > 0)
        {
            _maxGenerations = generations;
            for (int i = 0; i < _excel._sheets.length; i++)
            {
                int x = _excel._style._tableMarginX;
                int y = _excel._style._tableMarginY + getExpectedTableHeight();
                Table table = new Table(x, y, 1, generations, _excel._sheets[i]);

                for (int g = 0; g < generations; g++) table._cell[g][0].setCellValue(g);

                if (_excel._doFormatting)
                {
                    table.applyCellStyleClones(_excel._contentStyle, _excel._workbook);
                    table.applyBackground(0, 0, 1, generations, _excel._style._tableFillForegroundColor);
                    table.applyBorder(0, 0, 1, generations, _excel._style._tableBorderColor, _excel._style._tableBorderStyle);
                }
            }
        }
        else _maxGenerations = null;

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
        return 1 + _level;
    }

    /**
     * Returns the expected table width (in columns)
     *
     * @return expected table width (in columns)
     */
    public int getExpectedTableWidth()
    {
        return 1 + _takenColumns[0] * _crossedScenarios.getComparedKeyValues()[0].getValues().length;
    }
}
