package io.scenario.excel;

import exception.ScenarioException;
import executor.ScenariosSummarizer;
import indicator.IIndicator;
import io.scenario.IScenarioSaver;
import io.utils.excel.Style;
import scenario.Scenario;
import statistics.IStatistic;

/**
 * This abstract implementation of {@link IScenarioSaver} creates scenario-level summary Excel files. First, per-indicator
 * results are stored in separate sheets. Next, each statistic is stored in a different column within one tab, while rows
 * refer to generations.This extension supports 1997-2003 Excel files (xls).
 *
 * @author MTomczyk
 */
public class SummarizerXLS extends AbstractExcelSummarizer implements IScenarioSaver
{
    /**
     * Default constructor.
     */
    public SummarizerXLS()
    {
        this(new Style());
    }


    /**
     * Parameterized constructor.
     *
     * @param style provides some basic customization options
     */
    public SummarizerXLS(Style style)
    {
        this("", "", null, null, null, null, style);
    }

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
    public SummarizerXLS(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators,
                         IStatistic[] statistics, Style style)
    {
        super(path, filename, scenario, trialIDs, indicators, statistics, style);
        _excel._doFormatting = false;
    }

    /**
     * Creates a new instance of the object. Intended to be used by {@link ScenariosSummarizer} to clone the
     * initial object instance one time per each scenario involved (i.e., one clone will be mapped to one scenario).
     *
     * @param path       full path to the folder where the file should be stored (without a path separator)
     * @param filename   the filename (without the suffix, e.g., extension)
     * @param scenario   scenario being currently processed
     * @param trialIDs   trial IDs
     * @param indicators performance indicators employed when assessing the performance of EAs.
     * @param statistics statistic functions used to aggregate the data
     * @return new object instance
     * @throws ScenarioException the scenario-level exception can be cast 
     */
    @Override
    public IScenarioSaver getInstance(String path, String filename, Scenario scenario, int[] trialIDs, IIndicator[] indicators, IStatistic[] statistics) throws ScenarioException
    {
        return new SummarizerXLS(path, filename, scenario, trialIDs, indicators, statistics, _excel._style);
    }

    /**
     * Auxiliary method for instantiating the workbook.
     */
    @Override
    protected void instantiateWorkbook()
    {
        _excel.instantiateWorkbookAsHSSF();
    }

    /**
     * Auxiliary method for instantiating sheets.
     */
    @Override
    protected void instantiateSheets()
    {
        _excel.instantiateSheetsAsHSSF(getSheetsNamesFromIndicators());
    }

    /**
     * Returns the file suffix (includes .xls)
     *
     * @return file suffix
     */
    @Override
    public String getFileSuffix()
    {
        return ".xls";
    }

    /**
     * The implementation flushes the data to the actual file and closes the outputs.
     *
     * @throws ScenarioException scenario-level exception can be thrown
     */
    @Override
    public void close() throws ScenarioException
    {
        super.close();
    }
}
