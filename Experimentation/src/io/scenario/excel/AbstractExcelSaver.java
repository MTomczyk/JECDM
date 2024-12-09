package io.scenario.excel;

import exception.ScenarioException;
import indicator.IIndicator;
import io.scenario.AbstractScenarioSaver;
import io.scenario.IScenarioSaver;
import io.utils.excel.Excel;
import io.utils.excel.Style;
import scenario.Scenario;
import statistics.IStatistic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * This abstract implementation of {@link IScenarioSaver} creates scenario-level Excel files.
 *
 * @author MTomczyk
 */
public abstract class AbstractExcelSaver extends AbstractScenarioSaver implements IScenarioSaver
{
    /**
     * Container for excel-related objects.
     */
    protected Excel _excel = null;

    /**
     * Stores the indicators passed via the constructor.
     */
    protected IIndicator[] _indicators;

    /**
     * Auxiliary map that translates indicator's name into index.
     */
    protected HashMap<String, Integer> _indicatorMap;

    /**
     * Stores the index of a sheet being active.
     */
    protected int _activeSheet;


    /**
     * Parameterized constructor.
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   currently processed scenario
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     * @param style      provides some basic customization options
     */
    public AbstractExcelSaver(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators,
                              IStatistic[] statistics, Style style)
    {
        super(path, filename, scenario, trialIDs, indicators, statistics);
        _indicators = indicators;
        if (_indicators != null)
        {
            _indicatorMap = new HashMap<>();
            for (int i = 0; i < indicators.length; i++) _indicatorMap.put(indicators[i].toString(), i);
        }
        instantiateExcel(style);
    }

    /**
     * Auxiliary method for instantiating the Excel object.
     *
     * @param style provides basic customization options
     */
    protected void instantiateExcel(Style style)
    {
        _excel = new Excel(style);
    }

    /**
     * The implementation creates the workbook object
     *
     * @throws ScenarioException scenario-level exception can be thrown (e.g., then the requested path is invalid)
     */
    @Override
    public void create() throws ScenarioException
    {
        File file = getFileAtScenarioLevel();

        try
        {
            _fileOutputStream = new FileOutputStream(file, false);
        } catch (FileNotFoundException e)
        {
            throw new ScenarioException(e.toString(), this.getClass(), e, _scenario);
        }

        instantiateWorkbook();
        instantiateSheets();
        instantiateStyles();
    }

    /**
     * Auxiliary method for instantiating the workbook. To be overwritten.
     */
    protected void instantiateWorkbook()
    {

    }

    /**
     * Auxiliary method for instantiating sheets. To be overwritten.
     */
    protected void instantiateSheets()
    {

    }

    /**
     * Instantiates style-related objects.
     */
    protected void instantiateStyles()
    {
        _excel.instantiateHeaderStyle();
        _excel.instantiateContentStyle();
    }

    /**
     * The implementation flushes the data to the actual file and closes the outputs.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void close() throws ScenarioException
    {
        if (_excel._workbook != null)
        {
            try
            {
                _excel.close(_fileOutputStream);
            } catch (IOException e)
            {
                throw new ScenarioException("Unable to close the workbook (reason = " + e.getMessage() + ")", this.getClass(), e, _scenario);
            }
        }
    }

    /**
     * Writes the statistics values.
     *
     * @param trialResults raw trial results
     * @param statistics   statistics calculated from trial results (1:1 mapping with statistic objects stored in {@link container.scenario.AbstractScenarioDataContainer})
     * @param generation   current generation number
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void pushData(double[] trialResults, double[] statistics, int generation) throws ScenarioException
    {
        throw new ScenarioException("The \"push data\" method is not implemented", this.getClass(), _scenario);
    }

    /**
     * This extension creates a new sheet using the workbook object.
     *
     * @param indicator   indicator
     * @param generations number of generations
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingBegins(IIndicator indicator, int generations) throws ScenarioException
    {
        super.notifyIndicatorProcessingBegins(indicator, generations);
        _activeSheet = _indicatorMap.get(indicator.toString());
    }

    /**
     * A method for notifying the saver that the data processing linked to the currently involved indicator ends.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void notifyIndicatorProcessingEnds() throws ScenarioException
    {
        createConvergencePlots();

        if (_excel._style._columnWidth != null)
        {
            for (int i = 0; i < 1 + _statistics.length; i++)
                _excel._sheets[_activeSheet].setColumnWidth(i, _excel._style._columnWidth * 256);
        }

        super.notifyIndicatorProcessingEnds();
        _activeSheet = -1;
    }

    /**
     * Auxiliary method for creating convergence plots.
     */
    protected void createConvergencePlots()
    {
        // to be overwritten
    }

    /**
     * Auxiliary method for constructing sheets' names from indicator's names.
     *
     * @return sheets' names
     */
    protected String[] getSheetsNamesFromIndicators()
    {
        String[] names = new String[_indicators.length];
        for (int i = 0; i < _indicators.length; i++) names[i] = _indicators[i].getName();
        return names;
    }


}
